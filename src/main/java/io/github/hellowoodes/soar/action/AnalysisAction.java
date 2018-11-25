package io.github.hellowoodes.soar.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.util.ui.UIUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * Analysis action
 *
 * @author HelloWoodes
 * @date 2018/11/4 23:12
 */
public class AnalysisAction extends AnAction {

    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setEnabledAndVisible(true);
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        event.getPresentation().setEnabledAndVisible(true);
        Project project = event.getProject();

        final Editor editor = event.getRequiredData(CommonDataKeys.EDITOR);

        String analysisResult = editor.getSelectionModel().getSelectedText();
        analysisResult = StringUtils.isNotBlank(analysisResult) ? analysisResult : "Please choose SQL to analysis";

        JBPopupFactory factory = JBPopupFactory.getInstance();
        factory.createHtmlTextBalloonBuilder(analysisResult, null, UIUtil.getWindowColor(), null)
                .setBorderColor(UIUtil.getWindowColor())
                .setShadow(true)
                .setFadeoutTime(5000)
                .setDialogMode(true)
                .setHideOnAction(false)
                .createBalloon()
                .show(factory.guessBestPopupLocation(editor), Balloon.Position.below);
    }

}
