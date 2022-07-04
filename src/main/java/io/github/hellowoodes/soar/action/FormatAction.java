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

package io.github.hellowoodes.soar.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import io.github.hellowoodes.soar.constant.SoarAction;
import io.github.hellowoodes.soar.util.CommandUtil;
import io.github.hellowoodes.soar.util.DatabaseUtil;
import io.github.hellowoodes.soar.util.NotifyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static io.github.hellowoodes.soar.constant.Constant.DOUBLE_LINE_BREAK;
import static io.github.hellowoodes.soar.constant.Constant.LINE_BREAK;

/**
 * Soar format action
 *
 * @author hellowoodes
 * @date 2018-11-25 22:07
 */
@Slf4j
public class FormatAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        try {
            final Project project = event.getProject();
            final Editor editor = event.getRequiredData(CommonDataKeys.EDITOR);
            final Document document = editor.getDocument();
            final SelectionModel selectionModel = editor.getSelectionModel();
            final int start = selectionModel.getSelectionStart();
            final int end = selectionModel.getSelectionEnd();

            String selectedText = selectionModel.getSelectedText();

            ProgressManager.getInstance().run(new Task.Backgroundable(event.getProject(), "Executing Format SQL") {
                @Override
                public void run(@NotNull ProgressIndicator progressIndicator) {
                    try {
                        progressIndicator.setText("Soar: Start parse SQL");

                        progressIndicator.setText("Soar: Validating SQL");
                        DatabaseUtil.validateSQL(selectedText);

                        progressIndicator.setText("Soar: Splicing command parameters");
                        List<String> commandList = CommandUtil.getCommandList(selectedText, SoarAction.FORMAT);

                        progressIndicator.setText("Soar: Executing format SQL command");
                        String result = StringUtils.trim(CommandUtil.executeCommand(commandList)).replaceAll(DOUBLE_LINE_BREAK, LINE_BREAK);

                        progressIndicator.setText("Soar: Replace selected sql as formatted");
                        WriteCommandAction.runWriteCommandAction(project, () ->
                                document.replaceString(start, end, result)
                        );

                        progressIndicator.setText("Soar: Format SQL completed");
                    } catch (Exception e) {
                        NotifyUtil.showErrorMessageDialog("Execute action failed", NotifyUtil.getExceptionMessage(e));
                    }
                }
            });
        } catch (Exception e) {
            NotifyUtil.showErrorMessageDialog("Execute action failed", NotifyUtil.getExceptionMessage(e));
        }
    }
}


