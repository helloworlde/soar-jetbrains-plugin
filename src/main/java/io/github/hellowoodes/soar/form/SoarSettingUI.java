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

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBPanel;
import io.github.hellowoodes.soar.config.SoarSettings;
import io.github.hellowoodes.soar.util.DatabaseUtil;
import io.github.hellowoodes.soar.util.NotifyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static io.github.hellowoodes.soar.constant.Constant.FAILED;
import static io.github.hellowoodes.soar.constant.Constant.SUCCESSFUL;

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
    private JLabel onlineDBUrl;
    private JButton onlineDBTestBtn;
    private JLabel onlineDBTestResultLabel;

    /**
     * Test Database config component
     */
    private JTextField testDBHost;
    private JTextField testDBPort;
    private JTextField testDatabase;
    private JTextField testDBUser;
    private JPasswordField testDBPassword;
    private JLabel testDBUrl;
    private JButton testDBTestBtn;
    private JLabel testDBTestResultLabel;

    /**
     * Other config component
     */
    private JCheckBox allowRepeatDBConfig;
    private JCheckBox allowSampling;
    private JCheckBox clearTempTable;

    /**
     * File config component
     */
    private TextFieldWithBrowseButton fileConfigYamlFilePath;
    private TextFieldWithBrowseButton fileConfigBlackListLFilePath;
    private JButton fileConfigYamlFileEditBtn;
    private JButton fileConfigBlacklistFileEditBtn;
    private TextFieldWithBrowseButton soarLocation;
    private JPanel manualConfigPanel;
    private JPanel fileConfigPanel;
    private JRadioButton fileConfigBtn;
    private JRadioButton manualConfigBtn;

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
        loadFileConfigComponentListener(soarLocation, null, "Path to Soar", null);
        loadFileConfigComponentListener(fileConfigYamlFilePath, fileConfigYamlFileEditBtn, "Path to Soar Configuration File", "yaml");
        loadFileConfigComponentListener(fileConfigBlackListLFilePath, fileConfigBlacklistFileEditBtn, "Path to Soar Black List Configuration File", "blacklist");
        loadDBComponentListener(onlineDBHost, onlineDBPort, onlineDatabase, onlineDBUser, onlineDBPassword, onlineDBUrl, onlineDBTestResultLabel, onlineDBTestBtn);
        loadDBComponentListener(testDBHost, testDBPort, testDatabase, testDBUser, testDBPassword, testDBUrl, testDBTestResultLabel, testDBTestBtn);
        loadRadioComponentListener(fileConfigBtn, fileConfigPanel, manualConfigPanel);
        loadRadioComponentListener(manualConfigBtn, manualConfigPanel, fileConfigPanel);
    }

    /**
     * Switch configuration panel
     *
     * @param radioButton  Current radio button
     * @param bindingPanel Current binding configuration panel
     * @param anotherPanel Another not bind configuration panel
     */
    private void loadRadioComponentListener(JRadioButton radioButton, JPanel bindingPanel, JPanel anotherPanel) {
        radioButton.addActionListener(e -> {
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
    private void loadDBComponentListener(JTextField host, JTextField port, JTextField database, JTextField user, JPasswordField password, JLabel url, JLabel resultLabel, JButton button) {
        DocumentAdapter documentAdapter = new DocumentAdapter() {
            @Override
            protected void textChanged(@NotNull DocumentEvent documentEvent) {
                modifyDBUrl(host, port, database, url, resultLabel);
            }
        };

        host.getDocument().addDocumentListener(documentAdapter);
        port.getDocument().addDocumentListener(documentAdapter);
        database.getDocument().addDocumentListener(documentAdapter);
        user.getDocument().addDocumentListener(documentAdapter);
        password.getDocument().addDocumentListener(documentAdapter);

        button.addActionListener(e -> {
            try {
                DatabaseUtil.validateParam(host, port, user, password);
                boolean connectionSuccess = DatabaseUtil.validateConnection(url.getText(), user.getText(), new String(password.getPassword()));
                if (connectionSuccess) {
                    modifyConnectionResultLabel(resultLabel, true, true, SUCCESSFUL);
                } else {
                    NotifyUtil.showDBErrorMessageDialog("Validate Connection Result Failed");
                }
            } catch (Exception ex) {
                showConnectionErrorMessage(ex, resultLabel);
                log.error(ex.getMessage(), e);
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
            editBtn.addActionListener(e -> {
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
    private void showConnectionErrorMessage(Exception ex, JLabel resultLabel) {
        modifyConnectionResultLabel(resultLabel, true, false, FAILED);
        String message = NotifyUtil.getExceptionMessage(ex);
        NotifyUtil.showDBErrorMessageDialog(message);
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
    private void modifyDBUrl(JTextField host, JTextField port, JTextField database, JLabel url, JLabel resultLabel) {
        url.setText(DatabaseUtil.parseUrl(host, port, database));
        modifyConnectionResultLabel(resultLabel, false, false, null);
    }

    /**
     * Change database connection test result label text
     *
     * @param resultLabel Database connection test result label component
     * @param isShow      If show result label
     * @param isSuccess   If connection test is success
     * @param content     Label text content
     */
    private void modifyConnectionResultLabel(JLabel resultLabel, Boolean isShow, Boolean isSuccess, String content) {
        resultLabel.setVisible(isShow);
        resultLabel.setText(isShow ? content : null);
        resultLabel.setForeground(isSuccess ? JBColor.GREEN : JBColor.RED);
    }

    /**
     * Set setting value to component
     *
     * @param settings Setting values
     */
    private void loadSettings(SoarSettings settings) {
        // Soar location config
        soarLocation.setText(settings.getSoarLocation());

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
        onlineDBPort.setText(settings.getOnlineDBPort());
        onlineDBUser.setText(settings.getOnlineDBUser());
        onlineDBPassword.setText(settings.getOnlineDBPassword());
        onlineDBUrl.setText(settings.getOnlineDBUrl());

        // Test Database config
        testDBHost.setText(settings.getTestDBHost());
        testDBPort.setText(settings.getTestDBPort());
        testDatabase.setText(settings.getTestDatabase());
        testDBUser.setText(settings.getTestDBUser());
        testDBPassword.setText(settings.getTestDBPassword());
        testDBUrl.setText(settings.getTestDBUrl());

        // Other config
        allowRepeatDBConfig.setSelected(settings.isAllowRepeatDBConfig());
        allowSampling.setSelected(settings.isAllowSampling());
        clearTempTable.setSelected(settings.isClearTempTable());
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
        settings.setSoarLocation(soarLocation.getText());

        // Config panel
        settings.setManualConfig(manualConfigBtn.isSelected());

        // File config
        settings.setFileConfigYamlFilePath(fileConfigYamlFilePath.getText());
        settings.setFileConfigBlackListLFilePath(fileConfigBlackListLFilePath.getText());

        // Online Database config
        settings.setOnlineDBHost(onlineDBHost.getText());
        settings.setOnlineDBPort(onlineDBPort.getText());
        settings.setOnlineDBUser(onlineDBUser.getText());
        settings.setOnlineDBPassword(new String(onlineDBPassword.getPassword()));
        settings.setOnlineDBUrl(DatabaseUtil.parseUrl(onlineDBHost, onlineDBPort, onlineDatabase));
        settings.setOnlineDSN(DatabaseUtil.parseDSN(onlineDBHost, onlineDBPort, onlineDBUser, onlineDBPassword, onlineDatabase));

        // Test Database config
        settings.setTestDBHost(testDBHost.getText());
        settings.setTestDBPort(testDBPort.getText());
        settings.setTestDBUser(testDBUser.getText());
        settings.setTestDBPassword(new String(testDBPassword.getPassword()));
        settings.setTestDBUrl(DatabaseUtil.parseUrl(testDBHost, testDBPort, testDatabase));
        settings.setTestDSN(DatabaseUtil.parseDSN(testDBHost, testDBPort, testDBUser, testDBPassword, testDatabase));

        // Other config
        settings.setAllowRepeatDBConfig(allowRepeatDBConfig.isSelected());
        settings.setAllowSampling(allowSampling.isSelected());
        settings.setClearTempTable(clearTempTable.isSelected());
    }

    /**
     * Check if setting value is modified
     *
     * @return If modified
     */
    public boolean isModified() {
        return !soarLocation.getText().equals(ObjectUtils.defaultIfNull(settings.getSoarLocation(), "")) ||
                (manualConfigBtn.isSelected() != settings.isManualConfig()) ||
                !onlineDBHost.getText().equals(ObjectUtils.defaultIfNull(settings.getOnlineDBHost(), "")) ||
                !onlineDBPort.getText().equals(ObjectUtils.defaultIfNull(settings.getOnlineDBPort(), "")) ||
                !onlineDatabase.getText().equals(ObjectUtils.defaultIfNull(settings.getOnlineDatabase(), "")) ||
                !onlineDBUser.getText().equals(ObjectUtils.defaultIfNull(settings.getOnlineDBUser(), "")) ||
                !(new String(onlineDBPassword.getPassword())).equals(ObjectUtils.defaultIfNull(settings.getOnlineDBPassword(), "")) ||
                !testDBHost.getText().equals(ObjectUtils.defaultIfNull(settings.getTestDBHost(), "")) ||
                !testDBPort.getText().equals(ObjectUtils.defaultIfNull(settings.getTestDBPort(), "")) ||
                !testDatabase.getText().equals(ObjectUtils.defaultIfNull(settings.getTestDatabase(), "")) ||
                !testDBUser.getText().equals(ObjectUtils.defaultIfNull(settings.getTestDBUser(), "")) ||
                !(new String(testDBPassword.getPassword())).equals(ObjectUtils.defaultIfNull(settings.getTestDBPassword(), "")) ||
                !fileConfigYamlFilePath.getText().equals(ObjectUtils.defaultIfNull(settings.getFileConfigYamlFilePath(), "")) ||
                !fileConfigBlackListLFilePath.getText().equals(ObjectUtils.defaultIfNull(settings.getFileConfigBlackListLFilePath(), ""));
    }


    /**
     * Display edit configuration file dialog
     *
     * @param path Configuration file path
     */
    private void showEditFileDialog(String path) {
        try {
            String content = FileUtil.loadFile(new File(path));
            EditContentDialog editContentDialog = new EditContentDialog(null);
            editContentDialog.showContentPane(path, content);
            editContentDialog.show();
        } catch (IOException e) {
            e.printStackTrace();
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