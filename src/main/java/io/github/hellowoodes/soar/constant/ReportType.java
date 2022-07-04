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

package io.github.hellowoodes.soar.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author hellowoodes
 * @date 2018-11-25 22:26
 */
@AllArgsConstructor
@Getter
public enum ReportType {
    /**
     * JSON Format result
     */
    JSON("json"),
    /**
     * JSON Format result
     */
    MARKDOWN("markdown"),
    /**
     * JSON Format result
     */
    HTML("html"),
    /**
     * JSON Format result
     */
    PRETTY("pretty");


    private String type;
}
