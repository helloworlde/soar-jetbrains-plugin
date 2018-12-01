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

/**
 * Soar constant
 *
 * @author HelloWoodes
 * @date 2018/11/23 20:13
 */
public class Constant {

    public static final String BLANK_STRING = "";

    public static final String SOAR_PLUGIN_ID = "io.github.helloworlde.soar";
    public static final String SOAR_DISPLAY_NAME = "Soar";
    public static final String SOAR_HELP_TOPIC = "help.soar.configuration";
    public static final String JDBC_PREFIX = "jdbc:mysql://";
    public static final String MYSQL_DRIVER_CLASS = "com.mysql.jdbc.Driver";
    public static final String VALIDATE_SQL = "SELECT 1";
    public static final String SUCCESSFUL = "Successful";
    public static final String FAILED = "Failed";
    public static final String NOT_USE_SSL = "?useSSL=false";
    public static final String DEFAULT_TIMEOUT_STRING_VALUE = "30";
    public static final long DEFAULT_TIMEOUT_LONG_VALUE = 30;
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

    public static final String SYSTEM_PROPERTIES_USER_HOME = "user.home";
    public static final String SYSTEM_PROPERTIES_OS_NAME = "os.name";
    public static final String MAC = "mac";
    public static final String DARWIN = "darwin";
    public static final String WINDOWS = "windows";
    public static final String LINUX = "linux";

    public static final String SOAR_RELEASE_URL = "https://api.github.com/repos/XiaoMi/Soar/releases";
    public static final String DEFAULT_SOAR_DOWNLOAD_URL = "https://github.com/XiaoMi/soar/releases/download/0.9.0/soar.linux-amd64";
    public static final String ASSETS_KEY = "assets";
    public static final String NAME_KEY = "name";
    public static final String SOAR_PREFIX_KEY = "soar.";
    public static final String AMD_SUFFIX_KEY = "-amd64";
    public static final String BROWSER_DOWNLOAD_URL_KEY = "browser_download_url";
    public static final String INSTALL_COMMAND_TEMPLATE = "<html><body><div style=\"margin: 10px;\"><div><p>You can reference <label>https://github.com/XiaoMi/Soar</label> for install guidance and get latest version</p><br><p>Or you can execute command like below:</p><br><p><code>wget %s -O %s/.soar/soar</code><br><code>chmod a+x %s/.soar/soar</code></p><br><p>Then input <code>%s/.soar/soar</code> to here</p></div></div></body></html>";


}
