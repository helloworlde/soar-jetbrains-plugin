package io.github.hellowoodes.soar.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.editor.Editor;
import org.jetbrains.annotations.NotNull;

/**
 * Soar action group
 *
 * @author HelloWoodes
 * @date 2018/11/20 10:53
 */
public class SoarActionGroup extends DefaultActionGroup {

    @Override
    public void update(@NotNull AnActionEvent event) {
        // Enable/disable depending on whether user is editing
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        event.getPresentation().setEnabled(editor != null);
        // Always make visible.
        event.getPresentation().setVisible(true);
        // Take this opportunity to set an icon for the menu entry.
        // event.getPresentation().setIcon(UIUtil.getBalloonInformationIcon());
    }
}

