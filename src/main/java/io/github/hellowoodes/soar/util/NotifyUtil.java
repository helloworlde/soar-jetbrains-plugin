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
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.UIUtil;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

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
        ApplicationManager.getApplication().invokeLater(() -> Messages.showMessageDialog(message, title, icon));
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

    /**
     * Show tip dialog
     *
     * @param title   Tip title
     * @param content Tip content
     */
    public static void showTipsDialog(String title, String content) {
        ApplicationManager.getApplication().invokeLater(() -> {
            JBLabel label = new JBLabel();
            label.setText(content);
            DialogBuilder dialog = new DialogBuilder();
            dialog.setTitle(title);
            dialog.centerPanel(label);
            dialog.addOkAction();
            dialog.show();
        });
    }

    /**
     * Show notify popup around component
     *
     * @param component      The component which show popup
     * @param messageContent Popup message content
     */
    public static void showNotifyPopup(@NotNull JComponent component, String messageContent) {
        if (StringUtils.isNoneBlank(messageContent)) {
            ApplicationManager.getApplication().invokeLater(() -> {
                JBPopupFactory factory = JBPopupFactory.getInstance();
                factory.createHtmlTextBalloonBuilder(messageContent, null, UIUtil.getWindowColor(), null)
                        .setBorderColor(UIUtil.getWindowColor())
                        .setShadow(true)
                        .setFadeoutTime(5000)
                        .setDialogMode(true)
                        .setHideOnAction(false)
                        .createBalloon()
                        .show(RelativePoint.getCenterOf(component), Balloon.Position.below);
            });
        }
    }
}
