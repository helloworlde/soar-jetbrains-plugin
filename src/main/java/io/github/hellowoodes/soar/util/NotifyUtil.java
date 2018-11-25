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

import com.intellij.openapi.ui.Messages;

import javax.swing.*;
import java.util.Optional;

/**
 * Notify util
 *
 * @author HelloWoodes
 * @date 2018-11-25 17:02
 */
public class NotifyUtil {

    /**
     * Show Database error message dialog
     *
     * @param message Error message content
     */
    public static void showDBErrorMessageDialog(String message) {
        showErrorMessageDialog("Connection Failed!", message);
    }

    /**
     * Show edit file error message dialog
     *
     * @param message Error message content
     */
    public static void showEditFileErrorMessageDialog(String message) {
        showErrorMessageDialog("Open configuration file Failed!", message);
    }

    /**
     * Show error message dialog
     *
     * @param title   Message title
     * @param message Error message content
     */
    public static void showErrorMessageDialog(String title, String message) {
        showMessageDialog(title, message, Messages.getErrorIcon());
    }

    /**
     * Show message dialog
     *
     * @param title   Message title
     * @param message Message content
     * @param icon    Message icon
     */
    public static void showMessageDialog(String title, String message, Icon icon) {
        Messages.showMessageDialog(message, title, icon);
    }

    /**
     * Get exception cause message
     *
     * @param ex Exception
     * @return Exception cause message
     */
    public static String getExceptionMessage(Exception ex) {
        Optional<Exception> optionalException = Optional.of(ex);
        return optionalException.map(cause -> ex.getCause())
                .map(Throwable::getMessage)
                .orElse(ex.getMessage());
    }
}
