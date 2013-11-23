/*
 * Copyright 2013 Aaron Brown
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
 */
package me.sudofu.sanitycheck

class SanityCheckException extends Exception {
    public final String name
    public final Object entity
    public final String classification

    SanityCheckException(String name, Object entity, String classification, String conditionDescription) {
        super("${classification.capitalize()} ${name} failed the following sanity check: ${conditionDescription}")
        this.name = name
        this.entity = entity
        this.classification = classification
    }
}
