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

/**
 * A utility for performing a wide variety of basic sanity checks on
 * input, data, parameters, arguments, etc.
 *
 * <p>
 * The <b>SanityChecker</b> provides a variety of basic sanity checks.
 * For each check performed, if any should fail an
 * {@link java.lang.IllegalArgumentException IllegalArgumentException}
 * will be thrown, giving a uniform error message.
 * </p>
 *
 * <p>
 * While the use-cases for the <b>SanityChecker</b> itself may be
 * somewhat limited, it gives a good platform for easy expansion and
 * incorperation into more complex derivates of itself.
 * </p>
 *
 * @author Aaron Brown
 */
class SanityChecker {

    /**
     * The name of the entity (for error context).
     *
     * <p>For example, the name of a method parameter.</p>
     */
    public final String name

    /**
     * The entity to perform checks on.
     *
     * <p>The value of the entity (intended to be unmondified).</p>
     */
    public final Object entity

    /**
     * The classification of the entity (for error context).
     *
     * <p>By default, the <code>classification</code> is <b>parameter</b>.</p>
     *
     * <p>
     * Other examples:
     * <ul>
     * <li>field</li>
     * <li>variable</li>
     * <li>input</li>
     * <li>argument</li>
     * </ul>
     * </p>
     */
    public final String classification

    /**
     * Allows the sanity check to allow <code>null</code> entities to
     * pass the check.
     *
     * <p>
     * By default, the behavior of <b>SanityChecker</b> is to fail any
     * check if the <b><code>entity</code></b> is <code>null</code>.
     * </p>
     *
     * <p>This behavior can be overridden at the level of any sanity check.</p>
     */
    public final boolean allowPassOnNull

    /**
     * Constructs a basic <b>SanityChecker</b> with <i>parameter</i> as
     * the <b><code>classification</code></b> and which disallows
     * <code>null</code> to pass sanity checks.
     *
     * @param   name
     *
     * A human-understandable label for the <code>entity</code>.
     *
     * @param   entity
     *
     * The entity to perform the sanity check(s) on.
     */
    public SanityChecker(String name, Object entity) {
        this.name = name
        this.entity = entity
        this.classification = "parameter"
        this.allowPassOnNull = false
    }

    /**
     * Constructs a basic <b>SanityChecker</b> which disallows
     * <code>null</code> to pass sanity checks.
     *
     * @param   name
     *
     * A human-understandable label for the <code>entity</code>.
     *
     * @param   entity
     *
     * The entity to perform the sanity check(s) on.
     *
     * @param   classification
     *
     * A human-understandable classification for the <code>entity</code>.
     */
    public SanityChecker(String name, Object entity, String classification) {
        this.name = name
        this.entity = entity
        this.classification = classification
        this.allowPassOnNull = false
    }

    /**
     * Constructs a basic <b>SanityChecker</b> with <i>parameter</i> as
     * the <b><code>classification</code></b>.
     *
     * @param   name
     *
     * A human-understandable label for the <code>entity</code>.
     *
     * @param   entity
     *
     * The entity to perform the sanity check(s) on.
     *
     * @param   allowPassOnNull
     *
     * Specifies whether to allow or disallow <code>null</code> entities
     * to pass sanity checks.
     */
    public SanityChecker(String name, Object entity, boolean allowPassOnNull) {
        this.name = name
        this.entity = entity
        this.classification = "parameter"
        this.allowPassOnNull = allowPassOnNull
    }

    /**
     * Constructs a basic <b>SanityChecker</b>.
     *
     * @param   name
     *
     * A human-understandable label for the <code>entity</code>.
     *
     * @param   entity
     *
     * The entity to perform the sanity check(s) on.
     *
     * @param   classification
     *
     * A human-understandable classification for the <code>entity</code>.
     *
     * @param   allowPassOnNull
     *
     * Specifies whether to allow or disallow <code>null</code> entities
     * to pass sanity checks.
     */
    public SanityChecker(String name, Object entity, String classification, boolean allowPassOnNull) {
        this.name = name
        this.entity = entity
        this.classification = classification
        this.allowPassOnNull = allowPassOnNull
    }

    public static SanityChecker checkFor(String name, Object entity, Closure closure) throws IllegalArgumentException {
        Closure runClosure = closure.clone()

        runClosure.delegate = this.newInstance(name, entity)
        runClosure.resolveStrategy = Closure.DELEGATE_FIRST

        runClosure()

        return runClosure.delegate
    }

    public static SanityChecker checkFor(String name, Object entity, String classification, Closure closure) throws IllegalArgumentException {
        Closure runClosure = closure.clone()

        runClosure.delegate = this.newInstance(name, entity, classification)
        runClosure.resolveStrategy = Closure.DELEGATE_FIRST

        runClosure()

        return runClosure.delegate
    }

    public static SanityChecker checkFor(String name, Object entity, boolean allowPassOnNull, Closure closure) throws IllegalArgumentException {
        Closure runClosure = closure.clone()

        runClosure.delegate = this.newInstance(name, entity, allowPassOnNull)
        runClosure.resolveStrategy = Closure.DELEGATE_FIRST

        runClosure()

        return runClosure.delegate
    }

    public static SanityChecker checkFor(String name, Object entity, String classification, boolean allowPassOnNull, Closure closure) throws IllegalArgumentException {
        Closure runClosure = closure.clone()

        runClosure.delegate = this.newInstance(name, entity, classification, allowPassOnNull)
        runClosure.resolveStrategy = Closure.DELEGATE_FIRST

        runClosure()

        return runClosure.delegate
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
        if (allowPassOnNull && entity == null) {
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

    public void isBoolean() throws IllegalArgumentException {
        isBoolean(allowPassOnNull)
    }

    public void isBoolean(boolean allowPassOnNull) throws IllegalArgumentException {
        if (allowPassOnNull && entity == null) {
            return
        }

        isNotNull()

        if (entity.getClass() != Boolean) {
            throw new IllegalArgumentException("${classification.capitalize()} ${name} is not a Boolean.")
        }
    }

    public void isString() throws IllegalArgumentException {
        isString(allowPassOnNull)
    }

    public void isString(boolean allowPassOnNull) throws IllegalArgumentException {
        if (allowPassOnNull && entity == null) {
            return
        }

        isNotNull()

        if (entity.getClass() != String) {
            throw new IllegalArgumentException("${classification.capitalize()} ${name} is not a String.")
        }
    }

    public void isNumber() throws IllegalArgumentException {
        isNumber(allowPassOnNull)
    }

    public void isNumber(boolean allowPassOnNull) throws IllegalArgumentException {
        if (allowPassOnNull && entity == null) {
            return
        }

        isNotNull()

        if (entity in Number == false) {
            throw new IllegalArgumentException("${classification.capitalize()} ${name} is not a Number (or Subclass thereof).")
        }
    }

    public void isInteger() throws IllegalArgumentException {
        isInteger(allowPassOnNull)
    }

    public void isInteger(boolean allowPassOnNull) throws IllegalArgumentException {
        if (allowPassOnNull && entity == null) {
            return
        }

        isNotNull()

        if (entity.getClass() != Integer) {
            throw new IllegalArgumentException("${classification.capitalize()} ${name} is not an Integer.")
        }
    }

    public void isLong() throws IllegalArgumentException {
        isLong(allowPassOnNull)
    }

    public void isLong(boolean allowPassOnNull) throws IllegalArgumentException {
        if (allowPassOnNull && entity == null) {
            return
        }

        isNotNull()

        if (entity.getClass() != Long) {
            throw new IllegalArgumentException("${classification.capitalize()} ${name} is not a Long.")
        }
    }

    public void isBigDecimal() throws IllegalArgumentException {
        isBigDecimal(allowPassOnNull)
    }

    public void isBigDecimal(boolean allowPassOnNull) throws IllegalArgumentException {
        if (allowPassOnNull && entity == null) {
            return
        }

        isNotNull()

        if (entity.getClass() != BigDecimal) {
            throw new IllegalArgumentException("${classification.capitalize()} ${name} is not a BigDecimal.")
        }
    }

    public void isDouble() throws IllegalArgumentException {
        isDouble(allowPassOnNull)
    }

    public void isDouble(boolean allowPassOnNull) throws IllegalArgumentException {
        if (allowPassOnNull && entity == null) {
            return
        }

        isNotNull()

        if (entity.getClass() != Double) {
            throw new IllegalArgumentException("${classification.capitalize()} ${name} is not a Double.")
        }
    }

    public void isList() throws IllegalArgumentException {
        isList(allowPassOnNull)
    }

    public void isList(boolean allowPassOnNull) throws IllegalArgumentException {
        if (allowPassOnNull && entity == null) {
            return
        }

        isNotNull()

        if (entity in List == false) {
            throw new IllegalArgumentException("${classification.capitalize()} ${name} is not an implementation of List.")
        }
    }

    public void isMap() throws IllegalArgumentException {
        isMap(allowPassOnNull)
    }

    public void isMap(boolean allowPassOnNull) throws IllegalArgumentException {
        if (allowPassOnNull && entity == null) {
            return
        }

        isNotNull()

        if (entity in Map == false) {
            throw new IllegalArgumentException("${classification.capitalize()} ${name} is not an implementation of Map.")
        }
    }
}
