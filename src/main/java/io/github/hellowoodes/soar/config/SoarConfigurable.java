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

package io.github.hellowoodes.soar.config;

import com.intellij.openapi.options.SearchableConfigurable;
import io.github.hellowoodes.soar.form.SoarSettingUI;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static io.github.hellowoodes.soar.constant.Constant.*;

/**
 * Soar configurable
 *
 * @author HelloWoodes
 * @date 2018/11/17 23:44
 */
@Slf4j
public class SoarConfigurable implements SearchableConfigurable {

    private SoarSettings settings;

    private SoarSettingUI settingUI;

    public SoarConfigurable() {
        settings = SoarSettings.getInstance();
        settingUI = new SoarSettingUI(settings);
    }

    @Nullable
    @Override
    public Runnable enableSearch(String option) {
        return null;
    }

    @NotNull
    @Override
    public String getId() {
        return SOAR_PLUGIN_ID;
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return SOAR_DISPLAY_NAME;
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return SOAR_HELP_TOPIC;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return settingUI.getRootPanel();
    }

    @Override
    public boolean isModified() {
        return settingUI.isModified();
    }

    @Override
    public void apply() {
        settingUI.apply(settings);
    }

    @Override
    public void reset() {
        settingUI.reset();
    }

    @Override
    public void disposeUIResources() {
        this.settingUI = null;
    }
}
