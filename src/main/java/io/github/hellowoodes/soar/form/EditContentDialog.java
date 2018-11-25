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

import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.EditorTextField;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * Edit configuration file content dialog
 *
 * @author HelloWoodes
 */
public class EditContentDialog extends DialogWrapper {
    private JPanel contentPane;
    private EditorTextField editorContentField;

    EditContentDialog(@Nullable Project project) {
        super(project);
        setModal(true);
        init();
    }


    /**
     * Show edit content dialog
     *
     * @param path    Configuration file path
     * @param content Configuration file content
     */
    public void showContentPane(String path, String content) {
        this.setTitle(path);
        this.setModal(true);
        this.setSize(600, 400);
        this.setCancelButtonText("Close");
        this.setOKButtonText("Apply");
        this.setAutoAdjustable(true);
        this.setOKActionEnabled(true);
        this.setResizable(true);

        editorContentField.setSize(600, 400);
        editorContentField.setText(content);
        editorContentField.setFileType(FileTypeManager.getInstance().getFileTypeByExtension(".yaml"));
        editorContentField.setFont(Font.getFont("Source Code Pro"));
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return contentPane;
    }
}
