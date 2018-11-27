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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static io.github.hellowoodes.soar.constant.Constant.*;

/**
 * Database related util
 *
 * @author HelloWoodes
 * @date 2018-11-25 16:37
 */
public class DatabaseUtil {

    /**
     * Parse Database DSN
     *
     * @param host     Database host component
     * @param port     Database port component
     * @param user     Database user component
     * @param password Database password component
     * @param database Database database component
     * @return Database DSN
     */
    public static String parseDSN(JTextField host, JTextField port, JTextField user, JPasswordField password, JTextField database) {
        return user.getText()
                .concat(":")
                .concat(new String(password.getPassword()))
                .concat("@")
                .concat(host.getText())
                .concat(":")
                .concat(port.getText())
                .concat("/")
                .concat(database.getText());
    }

    /**
     * Parse Databse URL
     *
     * @param host     Database host component
     * @param port     Database port component
     * @param database Database database component
     * @return Database URL
     */
    public static String parseUrl(JTextField host, JTextField port, JTextField database) {
        return JDBC_PREFIX
                .concat(host.getText())
                .concat(":")
                .concat(port.getText())
                .concat("/")
                .concat(database.getText());

    }

    /**
     * Validate Database connection
     *
     * @param url      Database URL
     * @param user     Database user
     * @param password Database password
     * @return If connection success, return 1, or return 0
     * @throws Exception If connection failed, will throw Exception
     */
    public static boolean validateConnection(String url, String user, String password) throws Exception {
        url = url.concat(NOT_USE_SSL);

        Class.forName(MYSQL_DRIVER_CLASS);
        int result = 0;
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(VALIDATE_SQL);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.first()) {
                result = resultSet.getInt(1);
            }
        }
        return result == 1;
    }


    /**
     * Check Database configuration param if valid
     *
     * @param host     Database host component
     * @param port     Database port component
     * @param user     Database user component
     * @param password Database password component
     * @throws IllegalArgumentException If configuration is not valid, will throw IllegalArgumentException
     */
    public static void validateParam(JTextField host, JTextField port, JTextField user, JPasswordField password) throws IllegalArgumentException {
        if (StringUtils.isBlank(host.getText())) {
            throw new IllegalArgumentException("Please input valid Database host, It should be IP address or domain");
        }

        if (StringUtils.isBlank(port.getText()) || !NumberUtils.isNumber(port.getText())) {
            throw new IllegalArgumentException("Please input valid Database port, It should be a number");
        }

        if (StringUtils.isBlank(user.getText())) {
            throw new IllegalArgumentException("Please input username of Database");
        }

        if (StringUtils.isBlank(new String(password.getPassword()))) {
            throw new IllegalArgumentException("Please input password of Database");
        }
    }

    /**
     * Validate if SQL statement is valid
     *
     * @param content SQL statement
     * @throws Exception Throw Exception when SQL is invalid
     */
    public static void validateSQL(String content) throws Exception {
        if (StringUtils.isBlank(content)) {
            throw new IllegalArgumentException("Please select valid SQL statement");
        }
    }
}
