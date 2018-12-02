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
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.util.io.FileUtil;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static io.github.hellowoodes.soar.constant.Constant.DIALOG_SIZE;

/**
 * Edit configuration file content dialog
 *
 * @author HelloWoodes
 */
public class EditContentDialog {
    private final Project project;

    EditContentDialog(@Nullable Project project) {
        this.project = project;
    }


    /**
     * Show edit content dialog
     *
     * @param path    Configuration file path
     * @param content Configuration file content
     */
    public void showContentDialog(String path, String content) {
        String fileExtension = FileUtil.getExtension(path);
        EditorFactory editorFactory = EditorFactory.getInstance();
        Document document = editorFactory.createDocument(content);
        document.setReadOnly(false);

        ApplicationManager.getApplication().runWriteAction(() -> {
            document.setText(content);
        });

        Editor editor = editorFactory.createEditor(document, null, FileTypeManager.getInstance().getFileTypeByExtension(fileExtension), false);

        JComponent component = editor.getComponent();
        component.setEnabled(true);
        component.setPreferredSize(DIALOG_SIZE);
        component.setAutoscrolls(true);

        JComponent contentComponent = editor.getContentComponent();

        DialogBuilder dialog = new DialogBuilder(project);
        dialog.setTitle(path);
        dialog.centerPanel(component).setPreferredFocusComponent(contentComponent);
        dialog.addOkAction();
        dialog.show();
    }
}
