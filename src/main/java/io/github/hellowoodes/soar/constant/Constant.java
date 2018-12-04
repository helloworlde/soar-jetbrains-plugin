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

package io.github.hellowoodes.soar.constant;

import com.intellij.util.ui.JBDimension;
import com.intellij.util.ui.JBUI;

/**
 * Soar constant
 *
 * @author HelloWoodes
 * @date 2018/11/23 20:13
 */
public class Constant {

    public static final String BLANK_STRING = "";
    public static final String EQUAL_LABEL = "=";
    public static final String LINE_BREAK = "\n";

    public static final String INIT = "Init";
    public static final String CHECK = "Check";

    public static final JBDimension DIALOG_SIZE = JBUI.size(700, 480);

    public static final String SOAR_PLUGIN_ID = "io.github.helloworlde.soar";
    public static final String SOAR_DISPLAY_NAME = "Soar";
    public static final String SOAR_HELP_TOPIC = "help.soar.configuration";
    public static final String JDBC_PREFIX = "jdbc:mysql://";
    public static final String MYSQL_DRIVER_CLASS = "com.mysql.jdbc.Driver";
    public static final String VALIDATE_SQL = "SELECT 1";
    public static final String SUCCESSFUL = "Successful";
    public static final String FAILED = "Failed";
    public static final String ANALYSIS_RESULT_TITLE = "Analysis Result";
    public static final String NOT_USE_SSL = "?useSSL=false";
    public static final String DEFAULT_TIMEOUT_STRING_VALUE = "30";
    public static final String DEFAULT_DB_PORT = "3306";

    public static final String CONFIG_FILE_LOCATION_KEY = "-config";
    public static final String ONLINE_DSN_KEY = "-online-dsn";
    public static final String TEST_DSN_KEY = "-test-dsn";
    public static final String CONNECTION_TIMEOUT_KEY = "-conn-time-out";

    public static final String EXPLAIN_TYPE_KEY = "-explain-type";
    public static final String EXPLAIN_FORMAT_KEY = "-explain-format";
    public static final String EXPLAIN_SQL_REPORT_TYPE_KEY = "-explain-sql-report-type";

    public static final String DROP_TEMP_TABLE_KEY = "-drop-test-temporary";
    public static final String ALLOW_ONLINE_AS_TEST_KEY = "-allow-online-as-test";
    public static final String BLACKLIST_FILE_LOCATION_KEY = "-blacklist";
    public static final String SAMPLING_KEY = "-sampling";

    public static final String REPORT_TYPE_KEY = "-report-type";
    public static final String VERSION_KEY = "-version";
    public static final String QUERY_KEY = "-query";
    public static final String QUERY_TIMEOUT_KEY = "-query-time-out";

    public static final String HTML_TAG_PREFIX = "<html>";
    public static final String HTML_TAG_SUFFIX = "</html>";
    public static final String HTML_PARAGRAPH_TEMPLATE = "<p>%s</p>";
    public static final String SOAR_RESULT_BODY_TAG = "<body onload=load()>";
    public static final String SOAR_RESULT_BODY_TAG_REGEX = "<body onload=load\\(\\)>";


    public static final String SYSTEM_PROPERTIES_USER_HOME = "user.home";
    public static final String SYSTEM_PROPERTIES_OS_NAME = "os.name";
    public static final String MAC = "mac";
    public static final String DARWIN = "darwin";
    public static final String WINDOWS = "windows";
    public static final String LINUX = "linux";

    public static final String SOAR_RELATIVE_PATH = "/.soar/soar";
    public static final String CONFIG_YAML_RELATIVE_PATH = "/.soar/soar.yaml";
    public static final String CONFIG_BLACKLIST_RELATIVE_PATH = "/.soar/soar.blacklist";

    public static final String SOAR_RELEASE_URL = "https://api.github.com/repos/XiaoMi/Soar/releases";
    public static final String DEFAULT_SOAR_DOWNLOAD_URL = "https://github.com/XiaoMi/soar/releases/download/0.9.0/soar.linux-amd64";
    public static final String ASSETS_KEY = "assets";
    public static final String NAME_KEY = "name";
    public static final String SOAR_PREFIX_KEY = "soar.";
    public static final String AMD_SUFFIX_KEY = "-amd64";
    public static final String BROWSER_DOWNLOAD_URL_KEY = "browser_download_url";
    public static final String INSTALL_COMMAND_TEMPLATE =
            "You can reference https://github.com/XiaoMi/Soar for install guidance or get latest version\n\n" +
                    "Or you can execute command like below directly if Go environment is configured:\n\n" +
                    "wget %s -O %s/.soar/soar\n" +
                    "chmod a+x %s/.soar/soar\n\n" +
                    "The configuration yaml file and blacklist could be found in Soar repository also";

    public static final String SOAR_CONFIG_YAML_TEMPLATE =
            "# 线上环境配置\n" +
                    "online-dsn:\n" +
                    "  addr: 127.0.0.1:3306\n" +
                    "  schema: online\n" +
                    "  user: root\n" +
                    "  password: 123456\n" +
                    "  disable: false\n" +
                    "# 测试环境配置\n" +
                    "test-dsn:\n" +
                    "  addr: 127.0.0.1:3306\n" +
                    "  schema: test\n" +
                    "  user: root\n" +
                    "  password: 123456\n" +
                    "  disable: false\n" +
                    "# 是否允许测试环境与线上环境配置相同\n" +
                    "allow-online-as-test: true\n" +
                    "# 是否清理测试时产生的临时文件\n" +
                    "drop-test-temporary: true\n" +
                    "# 语法检查小工具\n" +
                    "only-syntax-check: false\n" +
                    "sampling-data-factor: 100\n" +
                    "sampling: true\n" +
                    "# 日志级别，[0:Emergency, 1:Alert, 2:Critical, 3:Error, 4:Warning, 5:Notice, 6:Informational, 7:Debug]\n" +
                    "log-level: 7\n" +
                    "log-output: ./soar.log\n" +
                    "# 优化建议输出格式\n" +
                    "report-type: markdown\n" +
                    "ignore-rules:\n" +
                    "  - \"\"\n" +
                    "# 黑名单中的 SQL 将不会给评审意见。一行一条 SQL，可以是正则也可以是指纹，填写指纹时注意问号需要加反斜线转义。\n" +
                    "blacklist: ./soar.blacklist\n" +
                    "# 启发式算法相关配置\n" +
                    "max-join-table-count: 5\n" +
                    "max-group-by-cols-count: 5\n" +
                    "max-distinct-count: 5\n" +
                    "max-index-cols-count: 5\n" +
                    "max-total-rows: 9999999\n" +
                    "spaghetti-query-length: 2048\n" +
                    "allow-drop-index: false\n" +
                    "# EXPLAIN相关配置\n" +
                    "explain-sql-report-type: pretty\n" +
                    "explain-type: extended\n" +
                    "explain-format: traditional\n" +
                    "explain-warn-select-type:\n" +
                    "  - \"\"\n" +
                    "explain-warn-access-type:\n" +
                    "  - ALL\n" +
                    "explain-max-keys: 3\n" +
                    "explain-min-keys: 0\n" +
                    "explain-max-rows: 10000\n" +
                    "explain-warn-extra:\n" +
                    "  - \"\"\n" +
                    "explain-max-filtered: 100\n" +
                    "explain-warn-scalability:\n" +
                    "  - O(n)\n" +
                    "query: \"\"\n" +
                    "list-heuristic-rules: false\n" +
                    "list-test-sqls: false\n" +
                    "verbose: true";
}
