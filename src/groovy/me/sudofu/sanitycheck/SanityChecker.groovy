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

class SanityChecker {

    private final String name

    private final Object entity

    private final  String classification

    public SanityChecker(String name, Object entity) {
        this.name = name
        this.entity = entity
        this.classification = "parameter"
    }

    public SanityChecker(String name, Object entity, String classification) {
        this.name = name
        this.entity = entity
        this.classification = classification
    }

    void isNotNull() throws IllegalArgumentException {
        if (entity == null) {
            throw new IllegalArgumentException("${classification.capitalize()} ${name} is null.")
        }
    }

    void isNotEmpty() {
        if (entity == null) {
            throw new IllegalArgumentException("${classification.capitalize()} ${name} is null, therefore transitively empty.")
        }
        else {
            if (entity.getMetaClass().respondsTo(entity, 'isEmpty')) {
                if (entity.isEmpty()) {
                    throw new IllegalArgumentException("${classification.capitalize()} ${name} is empty.")
                }
            }
            else {
                throw new IllegalArgumentException("${classification.capitalize()} ${name} does not have an isEmpty() method.")
            }
        }
    }

    void isString() {
        if (entity.getClass() != String) {
            throw new IllegalArgumentException("${classification.capitalize()} ${name} is not a String.")
        }
    }
}
