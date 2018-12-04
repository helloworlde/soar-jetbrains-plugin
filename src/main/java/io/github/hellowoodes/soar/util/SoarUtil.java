package io.github.hellowoodes.soar.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Future;

import static io.github.hellowoodes.soar.constant.Constant.*;

/**
 * @author hehuimin
 * @date 2018-11-29 12:44
 */
@Slf4j
public class SoarUtil {

    /**
     * Convert string content as content with HTML tag
     *
     * @param content String content
     * @return Content with HTML tag
     */
    public static String convertResultAsHtml(String content) {
        String[] resultParagraphs = content.split(LINE_BREAK);
        StringBuilder resultBuilder = new StringBuilder();

        resultBuilder.append(HTML_TAG_PREFIX);
        Arrays.stream(resultParagraphs)
                .forEachOrdered(paragraph -> resultBuilder.append(String.format(HTML_PARAGRAPH_TEMPLATE, paragraph)));
        resultBuilder.append(HTML_TAG_SUFFIX);

        return resultBuilder.toString();
    }

    /**
     * Get current user home
     *
     * @return User home
     */
    private static String getUserHome() {
        return System.getProperty(SYSTEM_PROPERTIES_USER_HOME);
    }

    /**
     * Get OS type
     *
     * @return OS type
     */
    private static String getOsType() {
        String osName = System.getProperty(SYSTEM_PROPERTIES_OS_NAME).toLowerCase();
        String result;
        if (StringUtils.contains(osName, MAC)) {
            result = DARWIN;
        } else if (StringUtils.contains(osName, WINDOWS)) {
            result = WINDOWS;
        } else {
            result = LINUX;
        }
        return result;
    }

    /**
     * Get latest Soar download urls
     *
     * @return Url collection
     * @throws IOException Throw exception when failed
     */
    private static Map<String, String> getLatestSoarDownloadUrls() throws Exception {
        Map<String, String> resultMap = new HashMap<>();

        HttpClient httpClient = new HttpClient();
        HttpMethod method = new GetMethod(SOAR_RELEASE_URL);
        httpClient.executeMethod(method);
        String resultContent = new String(method.getResponseBody());

        JSONArray jsonObject = JSON.parseArray(resultContent);
        // Get the latest one
        JSONObject latestRelease = jsonObject.getJSONObject(0);
        JSONArray assertArr = latestRelease.getJSONArray(ASSETS_KEY);

        for (int i = 0; i < assertArr.size(); i++) {
            JSONObject currentAssert = assertArr.getJSONObject(i);
            String name = currentAssert.getString(NAME_KEY);
            name = StringUtils.remove(name, SOAR_PREFIX_KEY);
            name = StringUtils.remove(name, AMD_SUFFIX_KEY);

            resultMap.put(name, currentAssert.getString(BROWSER_DOWNLOAD_URL_KEY));
        }

        return resultMap;
    }

    /**
     * Render Soar install remind content
     *
     * @return Soar install remind content
     * @throws Exception Throw exception when failed
     */
    public static String getSoarInstallContent() throws Exception {
        String content = INSTALL_COMMAND_TEMPLATE;

        Future<String> userHomeFuture = ApplicationManager.getApplication().executeOnPooledThread(SoarUtil::getUserHome);
        String userHome = userHomeFuture.get();

        String soarDownloadUrl = getSoarDownloadUrl();
        content = String.format(content, soarDownloadUrl, userHome, userHome);

        return content;
    }

    /**
     * Get Soar latest release download url
     *
     * @return Soar latest release download url
     * @throws Exception Throw exception when failed
     */
    private static String getSoarDownloadUrl() throws Exception {
        Future<String> osTypeFuture = ApplicationManager.getApplication().executeOnPooledThread(SoarUtil::getOsType);
        Future<Map<String, String>> urlMap = ApplicationManager.getApplication().executeOnPooledThread(SoarUtil::getLatestSoarDownloadUrls);

        String osType = osTypeFuture.get();

        return Optional.ofNullable(urlMap.get())
                .map(map -> map.get(osType))
                .orElse(DEFAULT_SOAR_DOWNLOAD_URL);
    }

    /**
     * Initial and file configuration file path
     *
     * @param component     The file path configuration component
     * @param relativePath  Configuration file relative path
     * @param configContent Configuration file content
     */
    public static void initialConfigFilePath(TextFieldWithBrowseButton component, String relativePath, String configContent) {
        try {
            String userHome = getUserHome();
            File configFile = new File(userHome.concat(relativePath));
            if (configContent != null) {
                FileUtil.createIfDoesntExist(configFile);
                FileUtil.writeToFile(configFile, configContent);
            }
            component.setText(configFile.getAbsolutePath());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Trim not necessary content of Soar analysis result
     *
     * @param originResultContent The Soar analysis result
     * @return The content only have needed tag
     */
    public static String trimResultUselessContent(String originResultContent) {
        String result = "";
        if (originResultContent.contains(SOAR_RESULT_BODY_TAG)) {
            result = originResultContent.split(SOAR_RESULT_BODY_TAG_REGEX)[1];
            result = result.replaceAll("☠", BLANK_STRING)
                    .replaceAll("️", BLANK_STRING);
        }
        return result;
    }
}
