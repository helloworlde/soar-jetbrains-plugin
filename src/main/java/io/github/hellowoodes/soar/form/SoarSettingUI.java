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

package io.github.hellowoodes.soar.form;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.ui.Colors;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.UIUtil;
import io.github.hellowoodes.soar.config.SoarSettings;
import io.github.hellowoodes.soar.util.CommandUtil;
import io.github.hellowoodes.soar.util.DatabaseUtil;
import io.github.hellowoodes.soar.util.NotifyUtil;
import io.github.hellowoodes.soar.util.SoarUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.github.hellowoodes.soar.constant.Constant.*;

/**
 * Soar setting UI component
 *
 * @author HelloWoodes
 * @date 2018/11/13 23:10
 */
@Slf4j
public class SoarSettingUI extends JFrame {

    private JPanel rootPanel;

    /**
     * Online Database config component
     */
    private JTextField onlineDBHost;
    private JTextField onlineDBPort;
    private JTextField onlineDatabase;
    private JTextField onlineDBUser;
    private JPasswordField onlineDBPassword;
    private JBLabel onlineDBUrl;
    private JButton onlineDBTestBtn;
    private JBLabel onlineDBTestResultLabel;

    /**
     * Test Database config component
     */
    private JTextField testDBHost;
    private JTextField testDBPort;
    private JTextField testDatabase;
    private JTextField testDBUser;
    private JPasswordField testDBPassword;
    private JBLabel testDBUrl;
    private JButton testDBTestBtn;
    private JBLabel testDBTestResultLabel;

    /**
     * Other config component
     */
    private JCheckBox allowRepeatDBConfig;
    private JCheckBox allowSampling;
    private JCheckBox clearTempTable;

    /**
     * Timeout config component
     */
    private JTextField connectTimeout;
    private JTextField queryTimeout;

    /**
     * Soar config component
     */
    private TextFieldWithBrowseButton soarLocationPath;
    private JButton soarCheckBtn;
    private JBLabel soarCheckResultLabel;

    /**
     * Config type component
     */
    private JRadioButton fileConfigBtn;
    private JRadioButton manualConfigBtn;

    /**
     * File config component
     */
    private TextFieldWithBrowseButton fileConfigYamlFilePath;
    private TextFieldWithBrowseButton fileConfigBlackListLFilePath;
    private JButton fileConfigYamlFileEditBtn;
    private JButton fileConfigBlacklistFileEditBtn;

    private JPanel manualConfigPanel;
    private JPanel fileConfigPanel;

    private final SoarSettings settings;

    /**
     * Set value to setting panel
     *
     * @param settings Setting values
     */
    public SoarSettingUI(SoarSettings settings) {
        this.settings = settings;
        loadSettings(settings);
        loadListener();
    }

    /**
     * Get Root panel
     *
     * @return Root panel
     */
    public JComponent getRootPanel() {
        return rootPanel;
    }

    /**
     * Create UI when open setting panel
     */
    private void createUIComponents() {
        rootPanel = new JBPanel<>();
    }

    /**
     * Load listener to component
     */
    private void loadListener() {
        loadFileConfigComponentListener(soarLocationPath, null, "Path to Soar", null);
        loadFileConfigComponentListener(fileConfigYamlFilePath, fileConfigYamlFileEditBtn, "Path to Soar Configuration File", "yaml");
        loadFileConfigComponentListener(fileConfigBlackListLFilePath, fileConfigBlacklistFileEditBtn, "Path to Soar Black List Configuration File", "blacklist");

        loadDBComponentListener(onlineDBHost, onlineDBPort, onlineDatabase, onlineDBUser, onlineDBPassword, onlineDBUrl, onlineDBTestResultLabel, onlineDBTestBtn);
        loadDBComponentListener(testDBHost, testDBPort, testDatabase, testDBUser, testDBPassword, testDBUrl, testDBTestResultLabel, testDBTestBtn);

        loadRadioComponentListener(fileConfigBtn, fileConfigPanel, manualConfigPanel);
        loadRadioComponentListener(manualConfigBtn, manualConfigPanel, fileConfigPanel);

        loadSoarCheckComponentListener(soarCheckBtn, soarCheckResultLabel);
    }


    /**
     * Load Soar check listener
     *
     * @param checkBtn    Check button
     * @param resultLabel Result message label
     */
    private void loadSoarCheckComponentListener(JButton checkBtn, JBLabel resultLabel) {
        checkBtn.addActionListener(event -> {
            ProgressManager.getInstance().run(new Task.Backgroundable(null, "Executing Format SQL") {
                @Override
                public void run(@NotNull ProgressIndicator progressIndicator) {
                    try {
                        hideResultLabel(resultLabel);
                        String soarLocation = soarLocationPath.getText();
                        if (StringUtils.isBlank(soarLocation)) {
                            throw new IllegalArgumentException("Please input Soar location first");
                        }

                        progressIndicator.setText("Soar: Start check Soar");

                        progressIndicator.setText("Soar: Get execute command");
                        List<String> commandList = new ArrayList<>();
                        commandList.add(soarLocation);
                        commandList.add(VERSION_KEY);

                        progressIndicator.setText("Soar: Executing check version command");
                        String result = CommandUtil.executeCommand(commandList);

                        progressIndicator.setText("Soar: Handler check result message");
                        result = SoarUtil.convertResultAsHtml(result);

                        modifyResultLabel(resultLabel, true, true, SUCCESSFUL, null);
                        resultLabel.setToolTipText(result);
                        progressIndicator.setText("Soar: Check action completed");
                    } catch (Exception e) {
                        try {
                            progressIndicator.setText("Soar: Getting latest Soar download url");
                            String content = SoarUtil.getSoarInstallContent();
                            NotifyUtil.showTipsDialog("Soar is not installed correctly", content);
                            ApplicationManager.getApplication().executeOnPooledThread(() -> SoarUtil.initialConfigFilePath(fileConfigYamlFilePath, CONFIG_YAML_RELATIVE_PATH, SOAR_CONFIG_YAML_TEMPLATE));
                            ApplicationManager.getApplication().executeOnPooledThread(() -> SoarUtil.initialConfigFilePath(fileConfigBlackListLFilePath, CONFIG_BLACKLIST_RELATIVE_PATH, BLANK_STRING));
                            ApplicationManager.getApplication().executeOnPooledThread(() -> SoarUtil.initialConfigFilePath(soarLocationPath, SOAR_RELATIVE_PATH, null));
                            soarCheckBtn.setText(CHECK);
                        } catch (Exception e1) {
                            String errorMessage = NotifyUtil.getExceptionMessage(e1);
                            NotifyUtil.showErrorMessageDialog("Soar check failed", errorMessage);
                        }
                    }
                }
            });
        });
    }

    /**
     * Hide result label
     *
     * @param resultLabel Result label component
     */
    private void hideResultLabel(@NotNull JBLabel resultLabel) {
        modifyResultLabel(resultLabel, false, false, SUCCESSFUL, null);
    }

    /**
     * Change database connection test result label text
     *
     * @param resultLabel   Database connection test result label component
     * @param isShow        If show result label
     * @param isSuccess     If connection test is success
     * @param content       Label text content
     * @param detailMessage Label text content
     */
    private void modifyResultLabel(@NotNull JBLabel resultLabel, @NotNull Boolean isShow, @NotNull Boolean isSuccess, String content, String detailMessage) {
        resultLabel.setVisible(isShow);
        resultLabel.setText(isShow ? content : null);
        resultLabel.setToolTipText(isShow ? detailMessage : null);
        Color green = UIUtil.isUnderDarcula() ? JBColor.GREEN : Colors.DARK_GREEN;
        resultLabel.setForeground(isSuccess ? green : JBColor.RED);
    }

    /**
     * Switch configuration panel
     *
     * @param radioButton  Current radio button
     * @param bindingPanel Current binding configuration panel
     * @param anotherPanel Another not bind configuration panel
     */
    private void loadRadioComponentListener(JRadioButton radioButton, JPanel bindingPanel, JPanel anotherPanel) {
        radioButton.addActionListener(event -> {
            bindingPanel.setVisible(radioButton.isSelected());
            anotherPanel.setVisible(!radioButton.isSelected());
        });
    }

    /**
     * Load database related component listener
     *
     * @param host        Database host component
     * @param port        Database port component
     * @param database    Database name component
     * @param user        Database user component
     * @param password    Database password component
     * @param url         Database connection url component
     * @param resultLabel Database connection test result label component
     * @param button      Database connection test button component
     */
    private void loadDBComponentListener(JTextField host, JTextField port, JTextField database, JTextField user, JPasswordField password, JBLabel url, JBLabel resultLabel, JButton button) {
        DocumentAdapter documentAdapter = new DocumentAdapter() {
            @Override
            protected void textChanged(@NotNull DocumentEvent documentEvent) {
                modifyDBUrl(host, port, database, url, resultLabel);
            }
        };

        DocumentAdapter hostDocumentAdapter = new DocumentAdapter() {
            @Override
            protected void textChanged(@NotNull DocumentEvent documentEvent) {
                modifyDBUrl(host, port, database, url, resultLabel);
                String hostContent = host.getText();
                if (LOCALHOST.equals(hostContent)) {
                    NotifyUtil.showNotifyPopup(host, "The host \"localhost\" is not support, please replace as \"127.0.0.1\"");
                }
            }
        };

        DocumentAdapter passwordDocumentAdapter = new DocumentAdapter() {
            @Override
            protected void textChanged(@NotNull DocumentEvent documentEvent) {
                String passwordContent = new String(password.getPassword());
                if (StringUtils.containsAny(NOT_SUPPORT_CHARACTER, passwordContent)) {
                    NotifyUtil.showNotifyPopup(password, "Your password have special symbol, please use file config, or explain will not be used in analysis");
                }
            }
        };

        host.getDocument().addDocumentListener(hostDocumentAdapter);
        port.getDocument().addDocumentListener(documentAdapter);
        database.getDocument().addDocumentListener(documentAdapter);
        user.getDocument().addDocumentListener(documentAdapter);
        password.getDocument().addDocumentListener(passwordDocumentAdapter);

        button.addActionListener(event -> {
            try {
                hideResultLabel(resultLabel);
                DatabaseUtil.validateParam(host, port, user);
                boolean connectionSuccess = DatabaseUtil.validateConnection(url.getText(), user.getText(), new String(password.getPassword()));
                if (connectionSuccess) {
                    modifyResultLabel(resultLabel, true, true, SUCCESSFUL, null);
                } else {
                    NotifyUtil.showDBErrorMessageDialog("Validate Connection Result Failed");
                }
            } catch (Exception e) {
                showConnectionErrorMessage(e, resultLabel);
            }
        });
    }

    /**
     * Load file config component listener
     *
     * @param filePathInput File path input component
     * @param editBtn       Edit button component
     * @param title         Edit dialog title
     * @param extension     Edited file extension
     */
    private void loadFileConfigComponentListener(TextFieldWithBrowseButton filePathInput, JButton editBtn, String title, String extension) {
        filePathInput.addBrowseFolderListener(title, null, null,
                FileChooserDescriptorFactory.createSingleFileDescriptor(extension));
        if (editBtn != null) {
            editBtn.addActionListener(event -> {
                try {
                    validateConfigFile(filePathInput, extension);
                    showEditFileDialog(filePathInput.getText());
                } catch (Exception ex) {
                    String message = NotifyUtil.getExceptionMessage(ex);
                    NotifyUtil.showEditFileErrorMessageDialog(message);
                }
            });
        }
    }

    /**
     * Show database connection result message
     *
     * @param ex          Exception object
     * @param resultLabel Database connection test result label
     */
    private void showConnectionErrorMessage(Exception ex, JBLabel resultLabel) {
        String detailMessage = NotifyUtil.getExceptionMessage(ex);
        modifyResultLabel(resultLabel, true, false, FAILED, detailMessage);
        NotifyUtil.showDBErrorMessageDialog(detailMessage);
    }


    /**
     * Change database url label text
     *
     * @param host        Database host component
     * @param port        Database port component
     * @param database    Database name component
     * @param url         Database connection url component
     * @param resultLabel Database connection test result label component
     */
    private void modifyDBUrl(JTextField host, JTextField port, JTextField database, JBLabel url, JBLabel resultLabel) {
        url.setText(DatabaseUtil.parseUrl(host, port, database));
        hideResultLabel(resultLabel);
    }


    /**
     * Set setting value to component
     *
     * @param settings Setting values
     */
    private void loadSettings(SoarSettings settings) {
        // Soar location config
        String soarLocation = settings.getSoarLocation();
        soarLocationPath.setText(soarLocation);
        soarCheckBtn.setText(StringUtils.isBlank(soarLocation) ? INIT : CHECK);

        // Which type config
        manualConfigBtn.setSelected(settings.isManualConfig());
        manualConfigPanel.setVisible(settings.isManualConfig());
        fileConfigBtn.setSelected(!settings.isManualConfig());
        fileConfigPanel.setVisible(!settings.isManualConfig());

        // File config
        fileConfigYamlFilePath.setText(settings.getFileConfigYamlFilePath());
        fileConfigBlackListLFilePath.setText(settings.getFileConfigBlackListLFilePath());

        // Online Database config
        onlineDBHost.setText(settings.getOnlineDBHost());
        onlineDatabase.setText(settings.getOnlineDatabase());
        onlineDBPort.setText(StringUtils.defaultIfBlank(settings.getOnlineDBPort(), DEFAULT_DB_PORT));
        onlineDBUser.setText(settings.getOnlineDBUser());
        onlineDBPassword.setText(settings.getOnlineDBPassword());
        onlineDBUrl.setText(settings.getOnlineDBUrl());

        // Test Database config
        testDBHost.setText(settings.getTestDBHost());
        testDBPort.setText(StringUtils.defaultIfBlank(settings.getTestDBPort(), DEFAULT_DB_PORT));
        testDatabase.setText(settings.getTestDatabase());
        testDBUser.setText(settings.getTestDBUser());
        testDBPassword.setText(settings.getTestDBPassword());
        testDBUrl.setText(settings.getTestDBUrl());

        // Other config
        allowRepeatDBConfig.setSelected(settings.isAllowRepeatDBConfig());
        allowSampling.setSelected(settings.isAllowSampling());
        clearTempTable.setSelected(settings.isClearTempTable());

        // Timeout config
        connectTimeout.setText(StringUtils.defaultIfBlank(settings.getConnectionTimeout(), DEFAULT_TIMEOUT_STRING_VALUE));
        queryTimeout.setText(StringUtils.defaultIfBlank(settings.getQueryTimeout(), DEFAULT_TIMEOUT_STRING_VALUE));
    }


    /**
     * Reset component value as initial
     */
    public void reset() {
        loadSettings(this.settings);
    }

    /**
     * Apply modified value
     *
     * @param settings Setting values object
     */
    public void apply(SoarSettings settings) {
        // Soar config
        settings.setSoarLocation(soarLocationPath.getText());

        // Config panel
        settings.setManualConfig(manualConfigBtn.isSelected());

        // File config
        settings.setFileConfigYamlFilePath(fileConfigYamlFilePath.getText());
        settings.setFileConfigBlackListLFilePath(fileConfigBlackListLFilePath.getText());

        // Online Database config
        settings.setOnlineDBHost(onlineDBHost.getText());
        settings.setOnlineDBPort(getComponentNumValueOrDefault(onlineDBPort, DEFAULT_DB_PORT));
        settings.setOnlineDBUser(onlineDBUser.getText());
        settings.setOnlineDatabase(onlineDatabase.getText());
        settings.setOnlineDBPassword(new String(onlineDBPassword.getPassword()));
        settings.setOnlineDBUrl(DatabaseUtil.parseUrl(onlineDBHost, onlineDBPort, onlineDatabase));
        settings.setOnlineDSN(DatabaseUtil.parseDSN(onlineDBHost, onlineDBPort, onlineDBUser, onlineDBPassword, onlineDatabase));

        // Test Database config
        settings.setTestDBHost(testDBHost.getText());
        settings.setTestDBPort(getComponentNumValueOrDefault(testDBPort, DEFAULT_DB_PORT));
        settings.setTestDBUser(testDBUser.getText());
        settings.setTestDatabase(testDatabase.getText());
        settings.setTestDBPassword(new String(testDBPassword.getPassword()));
        settings.setTestDBUrl(DatabaseUtil.parseUrl(testDBHost, testDBPort, testDatabase));
        settings.setTestDSN(DatabaseUtil.parseDSN(testDBHost, testDBPort, testDBUser, testDBPassword, testDatabase));

        // Other config
        settings.setAllowRepeatDBConfig(allowRepeatDBConfig.isSelected());
        settings.setAllowSampling(allowSampling.isSelected());
        settings.setClearTempTable(clearTempTable.isSelected());

        // Timeout config
        settings.setConnectionTimeout(getComponentNumValueOrDefault(connectTimeout, DEFAULT_TIMEOUT_STRING_VALUE));
        settings.setQueryTimeout(getComponentNumValueOrDefault(queryTimeout, DEFAULT_TIMEOUT_STRING_VALUE));
    }

    /**
     * Get component number value
     *
     * @param component    The component
     * @param defaultValue Default value
     * @return Component value or default value if component value is invalid
     */
    private String getComponentNumValueOrDefault(JTextField component, String defaultValue) {
        return Optional.of(component.getText())
                .filter(StringUtils::isNoneBlank)
                .filter(NumberUtils::isNumber)
                .orElse(defaultValue);
    }

    /**
     * Check if setting value is modified
     *
     * @return If modified
     */
    public boolean isModified() {
        return !soarLocationPath.getText().equals(ObjectUtils.defaultIfNull(settings.getSoarLocation(), BLANK_STRING)) ||
                (manualConfigBtn.isSelected() != settings.isManualConfig()) ||

                !fileConfigYamlFilePath.getText().equals(ObjectUtils.defaultIfNull(settings.getFileConfigYamlFilePath(), BLANK_STRING)) ||
                !fileConfigBlackListLFilePath.getText().equals(ObjectUtils.defaultIfNull(settings.getFileConfigBlackListLFilePath(), BLANK_STRING)) ||

                !onlineDBHost.getText().equals(ObjectUtils.defaultIfNull(settings.getOnlineDBHost(), BLANK_STRING)) ||
                !onlineDBPort.getText().equals(ObjectUtils.defaultIfNull(settings.getOnlineDBPort(), DEFAULT_DB_PORT)) ||
                !onlineDatabase.getText().equals(ObjectUtils.defaultIfNull(settings.getOnlineDatabase(), BLANK_STRING)) ||
                !onlineDBUser.getText().equals(ObjectUtils.defaultIfNull(settings.getOnlineDBUser(), BLANK_STRING)) ||
                !(new String(onlineDBPassword.getPassword())).equals(ObjectUtils.defaultIfNull(settings.getOnlineDBPassword(), BLANK_STRING)) ||

                !testDBHost.getText().equals(ObjectUtils.defaultIfNull(settings.getTestDBHost(), BLANK_STRING)) ||
                !testDBPort.getText().equals(ObjectUtils.defaultIfNull(settings.getTestDBPort(), DEFAULT_DB_PORT)) ||
                !testDatabase.getText().equals(ObjectUtils.defaultIfNull(settings.getTestDatabase(), BLANK_STRING)) ||
                !testDBUser.getText().equals(ObjectUtils.defaultIfNull(settings.getTestDBUser(), BLANK_STRING)) ||
                !(new String(testDBPassword.getPassword())).equals(ObjectUtils.defaultIfNull(settings.getTestDBPassword(), BLANK_STRING)) ||

                (allowRepeatDBConfig.isSelected() != settings.isAllowRepeatDBConfig()) ||
                (allowSampling.isSelected() != settings.isAllowSampling()) ||
                (clearTempTable.isSelected() != settings.isClearTempTable()) ||

                !connectTimeout.getText().equals(ObjectUtils.defaultIfNull(settings.getConnectionTimeout(), DEFAULT_TIMEOUT_STRING_VALUE)) ||
                !queryTimeout.getText().equals(ObjectUtils.defaultIfNull(settings.getQueryTimeout(), DEFAULT_TIMEOUT_STRING_VALUE));
    }


    /**
     * Display edit configuration file dialog
     *
     * @param path Configuration file path
     */
    private void showEditFileDialog(String path) {
        try {
            String content = FileUtil.loadFile(new File(path), StandardCharsets.UTF_8);
            EditContentDialog editContentDialog = new EditContentDialog(null);
            editContentDialog.showContentDialog(path, content);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Check if the configuration file path is valid
     *
     * @param filePathInput   Configuration component
     * @param expectExtension Expect file extension
     */
    private void validateConfigFile(TextFieldWithBrowseButton filePathInput, String expectExtension) throws Exception {
        String filePath = filePathInput.getText();
        if (StringUtils.isBlank(filePath)) {
            throw new IllegalArgumentException("Please input file path");
        }

        if (!FileUtil.exists(filePath)) {
            throw new FileNotFoundException("Didn't find file in the path ".concat(filePath));
        }

        String fileExtension = FileUtil.getExtension(filePath);
        if (!expectExtension.equals(fileExtension)) {
            String errorMessage = String.format("File extension is not correct, expect is '%s', The real is : '%s'", expectExtension, fileExtension);
            throw new IllegalArgumentException(errorMessage);
        }
    }
}