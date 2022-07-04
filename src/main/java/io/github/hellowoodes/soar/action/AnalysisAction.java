package io.github.hellowoodes.soar.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.util.ui.JBFont;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import io.github.hellowoodes.soar.constant.SoarAction;
import io.github.hellowoodes.soar.util.CommandUtil;
import io.github.hellowoodes.soar.util.NotifyUtil;
import io.github.hellowoodes.soar.util.SoarUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static io.github.hellowoodes.soar.constant.Constant.*;

/**
 * Analysis action
 *
 * @author HelloWoodes
 * @date 2018/11/4 23:12
 */
@Slf4j
public class AnalysisAction extends AnAction {

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabledAndVisible(true);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        try {
            event.getPresentation().setEnabledAndVisible(true);
            final Editor editor = event.getRequiredData(CommonDataKeys.EDITOR);
            String selectedText = editor.getSelectionModel().getSelectedText();
            log.info(selectedText);
            ProgressManager.getInstance().run(new Task.Backgroundable(event.getProject(), "Analysis SQL") {
                @Override
                public void run(@NotNull ProgressIndicator progressIndicator) {
                    progressIndicator.setText("Soar: Analysis SQL");
                    log.info("Soar: Analysis SQL");

                    progressIndicator.setText("Soar: Splicing command parameters");
                    log.info("Soar: Splicing command parameters");

                    List<String> commandList = CommandUtil.getCommandList(selectedText, SoarAction.ANALYSIS);

                    Future<String> result = ApplicationManager.getApplication().executeOnPooledThread(() -> {
                        try {
                            progressIndicator.setText("Soar: Executing analysis command");
                            String originHtmlResult = CommandUtil.executeCommand(commandList);

                            progressIndicator.setText("Soar: Parse result content");
                            return SoarUtil.trimResultUselessContent(originHtmlResult);
                        } catch (Exception e) {
                            log.info(e.getMessage(), e);
                            return null;
                        }
                    });

                    progressIndicator.setText("Soar: Rendering result Dialog");
                    JPanel panel = new JPanel(new BorderLayout(0, 0));

                    // Result editor pane
                    JEditorPane messagePane = new JEditorPane();
                    messagePane.setEditorKit(getHTMLKit());
                    messagePane.setEditable(false);

                    try {
                        String resultContent = Optional.of(result.get()).orElseThrow(IllegalArgumentException::new);

                        messagePane.setText(resultContent);

                        JScrollPane scrollPane = ScrollPaneFactory.createScrollPane(messagePane,
                                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                        scrollPane.setPreferredSize(DIALOG_SIZE);
                        panel.add(scrollPane, BorderLayout.CENTER);

                        ApplicationManager.getApplication().invokeLater(() -> {
                            // Result Dialog
                            DialogBuilder dialog = new DialogBuilder();
                            dialog.setTitle(ANALYSIS_RESULT_TITLE);
                            dialog.centerPanel(panel);
                            dialog.addOkAction();
                            dialog.show();
                        });
                        progressIndicator.setText("Soar: Analysis completed");
                    } catch (ExecutionException | InterruptedException e) {
                        log.info(e.getMessage(), e);
                        e.printStackTrace();
                        NotifyUtil.showErrorMessageDialog("Analysis failed", "Get result failed, error message is : " + NotifyUtil.getExceptionMessage(e));
                    } catch (Exception e) {
                        log.info(e.getMessage(), e);

                        NotifyUtil.showErrorMessageDialog("Soar is not installed correctly", "Please configure Soar in Setting -> Soar");
                    }
                }
            });
        } catch (Exception e) {
            NotifyUtil.showErrorMessageDialog("Execute action failed", COMMON_ERROR_MESSAGE);
        }
    }

    /**
     * Set html kit style
     *
     * @return The html kit
     */
    @NotNull
    private static HTMLEditorKit getHTMLKit() {
        HTMLEditorKit kit = UIUtil.getHTMLEditorKit();
        JBFont font = JBUI.Fonts.label(14);
        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule(String.format("body {font-family: %s;font-size: %s; text-align: left; margin:0 10px 0 10px;}",
                font.getFamily(), font.getSize()));

        return kit;
    }
}
