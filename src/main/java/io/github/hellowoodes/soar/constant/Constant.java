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
    public static final String QUERY_KEY = "-query";
    public static final String QUERY_TIMEOUT_KEY = "-query-time-out";


}
