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

import io.github.hellowoodes.soar.config.SoarSettings;
import io.github.hellowoodes.soar.constant.ExplainFormat;
import io.github.hellowoodes.soar.constant.ExplainSQLReportType;
import io.github.hellowoodes.soar.constant.ExplainType;
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
import java.util.Objects;
import java.util.Optional;

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
        String[] command = commandList.toArray(new String[0]);
        StringBuilder result = new StringBuilder();

        Process process = Runtime.getRuntime().exec(command);
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
             BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8))) {
            process.waitFor();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line).append("\n");
            }
            while ((line = errorReader.readLine()) != null) {
                result.append(line).append("\n");
            }
        }

        process.destroy();
        return result.toString();
    }

    /**
     * Get command list by different action
     *
     * @param sqlContent SQL for action
     * @param action     Action type
     * @return Command list for the action
     * @throws Exception Throw exception when failed
     */
    public static List<String> getCommandList(@Nullable String sqlContent, @NotNull SoarAction action) throws Exception {
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
        commandList.add(REPORT_TYPE_KEY);
        commandList.add(action.getReportType().getType());

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
        commandList.add(CONFIG_FILE_LOCATION_KEY);
        commandList.add(getSettingParam(settings.getFileConfigYamlFilePath(), "Please set config yaml path"));

        commandList.add(BLACKLIST_FILE_LOCATION_KEY);
        commandList.add(getSettingParam(settings.getFileConfigBlackListLFilePath(), "Please set blacklist file path"));
    }

    /**
     * Get manual config parameter list
     *
     * @param settings    Command parameter entity
     * @param commandList The command list for execute
     */
    private static void getManualConfig(SoarSettings settings, List<String> commandList) {
        // Manual Config
        commandList.add(ONLINE_DSN_KEY);
        commandList.add(getSettingParam(settings.getOnlineDSN(), "Online Database config invalid"));

        commandList.add(TEST_DSN_KEY);
        commandList.add(getSettingParam(settings.getTestDSN(), "Test Database config invalid"));

        commandList.add(EXPLAIN_TYPE_KEY);
        commandList.add(ExplainType.TRADITIONAL.getType());

        commandList.add(EXPLAIN_FORMAT_KEY);
        commandList.add(ExplainFormat.JSON.getType());

        commandList.add(EXPLAIN_SQL_REPORT_TYPE_KEY);
        commandList.add(ExplainSQLReportType.SAMPLE.getType());

        commandList.addAll(addCommandIfValid(DROP_TEMP_TABLE_KEY, settings.isClearTempTable()));
        commandList.addAll(addCommandIfValid(SAMPLING_KEY, settings.isAllowSampling()));
        commandList.addAll(addCommandIfValid(ALLOW_ONLINE_AS_TEST_KEY, settings.isAllowRepeatDBConfig()));
        commandList.addAll(addCommandIfValid(QUERY_TIMEOUT_KEY, settings.getQueryTimeout()));
        commandList.addAll(addCommandIfValid(CONNECTION_TIMEOUT_KEY, settings.getConnectionTimeout()));
    }

    /**
     * Add command to list when valid
     *
     * @param configKey   The command key
     * @param configValue The command value
     * @return The command list for execute
     */
    private static List<String> addCommandIfValid(String configKey, Object configValue) {
        List<String> commandList = new ArrayList<>();

        Optional<String> optional = Optional
                .ofNullable(configValue)
                .filter(Objects::nonNull)
                .map(String::valueOf)
                .filter(s -> StringUtils.isNoneBlank(s.trim()))
                .map(String::valueOf);

        if (optional.isPresent()) {
            commandList.add(configKey);
            commandList.add(optional.get());
        }

        return commandList;
    }

    /**
     * Get setting parameter
     *
     * @param paramName    Setting parameter name
     * @param errorMessage Error message for can't get the key
     * @return The command key if valid
     */
    private static String getSettingParam(String paramName, String errorMessage) {
        Optional<String> optional = Optional.of(paramName);
        return optional
                .filter(StringUtils::isNoneBlank)
                .map(String::trim)
                .orElseThrow(() -> new IllegalArgumentException(errorMessage));
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
