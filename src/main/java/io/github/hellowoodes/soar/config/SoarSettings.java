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

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import lombok.Data;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Soar setting properties entity
 *
 * @author HelloWoodes
 * @date 2018/11/14 23:10
 */
@Data
@ToString
@State(name = "SoarSettings", storages = {@Storage(file = "$APP_CONFIG$/SoarSettings.xml")})
public class SoarSettings implements PersistentStateComponent<SoarSettings> {

    /**
     * Config file path
     */
    private String soarLocation;
    private boolean manualConfig;
    private String fileConfigYamlFilePath;
    private String fileConfigBlackListLFilePath;

    /**
     * Online DB config
     */
    private String onlineDBHost;
    private String onlineDBPort;
    private String onlineDatabase;
    private String onlineDBUser;
    private String onlineDBPassword;
    private String onlineDBUrl;
    private String onlineDSN;

    /**
     * Test DB config
     */
    private String testDBHost;
    private String testDBPort;
    private String testDatabase;
    private String testDBUser;
    private String testDBPassword;
    private String testDBUrl;
    private String testDSN;

    /**
     * Other config
     */
    private boolean allowRepeatDBConfig;
    private boolean allowSampling;
    private boolean clearTempTable;
    private String connectionTimeout;
    private String queryTimeout;

    /**
     * Get SoarSetting instance
     *
     * @return SoarSetting instance
     */
    public static SoarSettings getInstance() {
        return ServiceManager.getService(SoarSettings.class);
    }

    @Nullable
    @Override
    public SoarSettings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull SoarSettings state) {
        XmlSerializerUtil.copyBean(state, this);
    }

}
