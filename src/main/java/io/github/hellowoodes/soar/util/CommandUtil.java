/*
 * Copyright 2018 HelloWoodes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package io.github.hellowoodes.soar.util;

import com.intellij.openapi.application.ApplicationManager;
import io.github.hellowoodes.soar.config.SoarSettings;
import io.github.hellowoodes.soar.constant.SoarAction;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

import static io.github.hellowoodes.soar.constant.Constant.*;

/**
 * Command line util
 *
 * @author HelloWoodes
 * @date 2018/11/6 22:29
 */
@Slf4j
public class CommandUtil {

    /**
     * Execute command
     *
     * @param commandList Command list
     * @return Command execute result
     * @throws Exception Throw exception when failed
     */
    public static String executeCommand(List<String> commandList) throws Exception {
        log.info("CommandList: {} ", commandList);
        String[] command = commandList.toArray(new String[0]);

        Process process = Runtime.getRuntime().exec(command);

        Future<String> successFuture = ApplicationManager.getApplication().executeOnPooledThread(() -> {
            StringBuilder resultContent = new StringBuilder();
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    resultContent.append(line).append(LINE_BREAK);
                }
            }
            log.info("Success content: {}", resultContent);
            return resultContent.toString();
        });

        Future<String> errorFuture = ApplicationManager.getApplication().executeOnPooledThread(() -> {
            StringBuilder resultContent = new StringBuilder();
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    resultContent.append(line).append(LINE_BREAK);
                }
            }
            log.info("Error content: {}", resultContent);
            return resultContent.toString();
        });

        process.waitFor();
        log.info("Precess waitFor");
        process.destroy();
        log.info("Precess destroy");

        String result = Optional.ofNullable(successFuture.get()).orElse(errorFuture.get());
        log.info("Result: {}", result);
        return result;
    }

    /**
     * Get command list by different action
     *
     * @param sqlContent SQL for action
     * @param action     Action type
     * @return Command list for the action
     */
    public static List<String> getCommandList(@Nullable String sqlContent, @NotNull SoarAction action) {
        SoarSettings settings = getSoarSetting();
        List<String> commandList = new ArrayList<>();
        commandList.add(settings.getSoarLocation());
        if (action.getQuery()) {
            getActionCommand(sqlContent, action, settings, commandList);
        } else {
            getCheckVersionCommand(commandList);
        }
        return commandList;
    }

    /**
     * Get check version command list
     *
     * @param commandList Check version command list
     */
    private static void getCheckVersionCommand(List<String> commandList) {
        commandList.add(VERSION_KEY);
    }

    /**
     * Get command for action
     *
     * @param sqlContent  SQL for action
     * @param action      Action type
     * @param settings    Command parameter entity
     * @param commandList The command list for execute
     */
    private static void getActionCommand(String sqlContent, SoarAction action, SoarSettings settings, List<String> commandList) {
        if (settings.isManualConfig()) {
            getManualConfig(settings, commandList);
        } else {
            getFileConfig(settings, commandList);
        }
        getQueryContentCommand(sqlContent, commandList, action);
    }

    /**
     * Get query command for action
     *
     * @param sqlContent  SQL for action
     * @param commandList The command list entity
     * @param action      Action type
     */
    private static void getQueryContentCommand(String sqlContent, List<String> commandList, SoarAction action) {
        // Report type
        addCommandIfValid(commandList, REPORT_TYPE_KEY, action.getReportType().getType());
        // SQL Content
        commandList.add(QUERY_KEY);
        commandList.add(sqlContent);
    }


    /**
     * Get file config command parameter list
     *
     * @param settings    Command parameter entity
     * @param commandList The command list for execute
     */
    private static void getFileConfig(SoarSettings settings, List<String> commandList) {
        // File Config
        addCommandIfValid(commandList, CONFIG_FILE_LOCATION_KEY, settings.getFileConfigYamlFilePath());
        addCommandIfValid(commandList, BLACKLIST_FILE_LOCATION_KEY, settings.getFileConfigBlackListLFilePath());
    }

    /**
     * Get manual config parameter list
     *
     * @param settings    Command parameter entity
     * @param commandList The command list for execute
     */
    private static void getManualConfig(SoarSettings settings, List<String> commandList) {
        // Manual Config
        addCommandIfValid(commandList, ONLINE_DSN_KEY, settings.getOnlineDSN());
        addCommandIfValid(commandList, TEST_DSN_KEY, settings.getTestDSN());

        addCommandIfValid(commandList, DROP_TEMP_TABLE_KEY, settings.isClearTempTable());
        addCommandIfValid(commandList, SAMPLING_KEY, settings.isAllowSampling());
        addCommandIfValid(commandList, ALLOW_ONLINE_AS_TEST_KEY, settings.isAllowRepeatDBConfig());
        addCommandIfValid(commandList, QUERY_TIMEOUT_KEY, settings.getQueryTimeout());
        addCommandIfValid(commandList, CONNECTION_TIMEOUT_KEY, settings.getConnectionTimeout());
    }

    /**
     * Add command to list when valid
     *
     * @param commandList The command list
     * @param configKey   The command key
     * @param configValue The command value
     */
    private static void addCommandIfValid(List<String> commandList, String configKey, Object configValue) {
        Optional<String> optional = Optional
                .ofNullable(configValue)
                .map(String::valueOf)
                .filter(s -> StringUtils.isNoneBlank(s.trim()))
                .map(String::valueOf);

        optional.ifPresent(v -> commandList.add(configKey.concat(EQUAL_LABEL).concat(v)));
    }

    /**
     * Get setting entity
     *
     * @return Setting entity
     */
    private static SoarSettings getSoarSetting() {
        return SoarSettings.getInstance();
    }
}
