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

    public final String name

    public final Object entity

    public final String classification

    public final boolean allowPassOnNull

    public SanityChecker(String name, Object entity) {
        this.name = name
        this.entity = entity
        this.classification = "parameter"
        this.allowPassOnNull = false
    }

    public SanityChecker(String name, Object entity, String classification) {
        this.name = name
        this.entity = entity
        this.classification = classification
        this.allowPassOnNull = false
    }

    public SanityChecker(String name, Object entity, boolean allowPassOnNull) {
        this.name = name
        this.entity = entity
        this.classification = "parameter"
        this.allowPassOnNull = allowPassOnNull
    }

    public SanityChecker(String name, Object entity, String classification, boolean allowPassOnNull) {
        this.name = name
        this.entity = entity
        this.classification = classification
        this.allowPassOnNull = allowPassOnNull
    }

    public void isNotNull() throws IllegalArgumentException {
        if (entity == null) {
            throw new IllegalArgumentException("${classification.capitalize()} ${name} is null.")
        }
    }

    public void isNotEmpty() throws IllegalArgumentException {
        isNotEmpty(allowPassOnNull)
    }

    public void isNotEmpty(boolean allowPassOnNull) throws IllegalArgumentException {
        if (allowPassOnNull) {
            return
        }

        isNotNull()

        if (entity.getMetaClass().respondsTo(entity, 'isEmpty')) {
            if (entity.isEmpty()) {
                throw new IllegalArgumentException("${classification.capitalize()} ${name} is empty.")
            }
        }
        else {
            throw new IllegalArgumentException("${classification.capitalize()} ${name} does not have an isEmpty() method.")
        }
    }

    public void isString() throws IllegalArgumentException {
        isString(allowPassOnNull)
    }

    public void isString(boolean allowPassOnNull) throws IllegalArgumentException {
        if (allowPassOnNull) {
            return
        }

        isNotNull()

        if (entity.getClass() != String) {
            throw new IllegalArgumentException("${classification.capitalize()} ${name} is not a String.")
        }
    }
}
