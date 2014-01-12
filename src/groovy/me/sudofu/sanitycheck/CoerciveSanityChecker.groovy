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

import me.sudofu.sanitycheck.SanityChecker

import me.sudofu.sanitycheck.SanityCheckException

import org.codehaus.groovy.runtime.GStringImpl

/**
 * A derivative of the <b><code>CoerciveSanityChecker</code></b> which includes
 * automatic handling of Groovy <i>String coercion</i> methods.
 *
 * <p>
 * The <b>CoerciveSanityChecker</b> provides many of the basic sanity
 * checks found in the <b><code>CoerciveSanityChecker</code></b>, however it
 * takes an additional step in handling <b><code>String</code></b>
 * based on its coercive properties to determine if they pass the check.
 * </p>
 *
 * <p>
 * The <b>CoerciveSanityChecker</b> is best used in situations where
 * data is either normalized as <b><code>String</code></b>, or where
 * data can be either the datatype <i>or</i> a (coercible)
 * <b><code>String</code></b>.
 * </p>
 *
 * <p>
 * There is also the benifit of automatically passing the coerced
 * <b><code>entity</code></b> back, should it pass the check.
 * </p>
 *
 * <p>
 * <b>Note</b>: In the cases where non-coercive sanity checks need to be
 * performed, use the
 * {@link me.sudofu.sanitycheck.CoerciveSanityChecker CoerciveSanityChecker};
 * it performs more strict type-checking.
 * </p>
 *
 * @author Aaron Brown
 */
class CoerciveSanityChecker {

    /**
     * The name of the entity (for error context).
     *
     * <p>For example, the name of a method parameter.</p>
     */
    protected String name

    /**
     * The entity to perform checks on.
     *
     * <p>The value of the entity (intended to be unmondified).</p>
     */
    protected Object entity

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
    protected String classification

    /**
     * Allows the sanity check to allow <code>null</code> entities to
     * pass the check.
     *
     * <p>
     * By default, the behavior of <b>CoerciveSanityChecker</b> is to fail any
     * check if the <b><code>entity</code></b> is <code>null</code>.
     * </p>
     *
     * <p>This behavior can be overridden at the level of any sanity check.</p>
     */
    public final boolean allowPassOnNull

    /**
     * Constructs a basic <b>CoerciveSanityChecker</b> with <i>parameter</i> as
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
    public CoerciveSanityChecker(String name, Object entity) {
        this.name = name
        this.entity = entity
        this.classification = "parameter"
        this.allowPassOnNull = false
    }

    /**
     * Constructs a basic <b>CoerciveSanityChecker</b> which disallows
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
    public CoerciveSanityChecker(String name, Object entity, String classification) {
        this.name = name
        this.entity = entity
        this.classification = classification
        this.allowPassOnNull = false
    }

    /**
     * Constructs a basic <b>CoerciveSanityChecker</b> with <i>parameter</i> as
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
    public CoerciveSanityChecker(String name, Object entity, boolean allowPassOnNull) {
        this.name = name
        this.entity = entity
        this.classification = "parameter"
        this.allowPassOnNull = allowPassOnNull
    }

    /**
     * Constructs a basic <b>CoerciveSanityChecker</b>.
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
    public CoerciveSanityChecker(String name, Object entity, String classification, boolean allowPassOnNull) {
        this.name = name
        this.entity = entity
        this.classification = classification
        this.allowPassOnNull = allowPassOnNull
    }

    public CoerciveSanityChecker(SanityChecker sanityChecker) {
        this.name = sanityChecker.name
        this.entity = sanityChecker.entity
        this.classification = sanityChecker.classification
        this.allowPassOnNull = sanityChecker.allowPassOnNull
    }

    public static CoerciveSanityChecker checkFor(String name, Object entity, Closure closure) throws SanityCheckException {
        Closure runClosure = closure.clone()

        runClosure.delegate = this.newInstance(name, entity)
        runClosure.resolveStrategy = Closure.DELEGATE_FIRST

        runClosure()

        return runClosure.delegate
    }

    public static CoerciveSanityChecker checkFor(String name, Object entity, String classification, Closure closure) throws SanityCheckException {
        Closure runClosure = closure.clone()

        runClosure.delegate = this.newInstance(name, entity, classification)
        runClosure.resolveStrategy = Closure.DELEGATE_FIRST

        runClosure()

        return runClosure.delegate
    }

    public static CoerciveSanityChecker checkFor(String name, Object entity, boolean allowPassOnNull, Closure closure) throws SanityCheckException {
        Closure runClosure = closure.clone()

        runClosure.delegate = this.newInstance(name, entity, allowPassOnNull)
        runClosure.resolveStrategy = Closure.DELEGATE_FIRST

        runClosure()

        return runClosure.delegate
    }

    public static CoerciveSanityChecker checkFor(String name, Object entity, String classification, boolean allowPassOnNull, Closure closure) throws SanityCheckException {
        Closure runClosure = closure.clone()

        runClosure.delegate = this.newInstance(name, entity, classification, allowPassOnNull)
        runClosure.resolveStrategy = Closure.DELEGATE_FIRST

        runClosure()

        return runClosure.delegate
    }

    public static CoerciveSanityChecker checkFor(SanityChecker sanityChecker, Closure closure) {
        Closure runClosure = closure.clone()

        runClosure.delegate = this.newInstance(sanityChecker)
        runClosure.resolveStrategy = Closure.DELEGATE_FIRST

        runClosure()

        return runClosure.delegate
    }

    public void isNotNull() throws SanityCheckException {
        if (entity == null) {
            throw new SanityCheckException(name, entity, classification, "Cannot be null")
        }
    }

    public void isNotEmpty() throws SanityCheckException {
        isNotEmpty(allowPassOnNull)
    }

    public void isNotEmpty(boolean allowPassOnNull) throws SanityCheckException {
        if (allowPassOnNull && entity == null) {
            return
        }

        isNotNull()

        if (entity.getMetaClass().respondsTo(entity, 'isEmpty')) {
            if (entity.isEmpty()) {
            throw new SanityCheckException(name, entity, classification, "Cannot be empty")
            }
        }
        else {
            throw new SanityCheckException(name, entity, classification, "Does not respond to isEmpty() method (cannot be empty)")
        }
    }

    public String isString() throws SanityCheckException {
        isString(allowPassOnNull)
    }

    public String isString(boolean allowPassOnNull) throws SanityCheckException {
        if (allowPassOnNull && entity == null) {
            return entity
        }

        isNotNull()

        if (entity.getClass() != String) {
            throw new SanityCheckException(name, entity, classification, "Must be a String")
        }

        return entity
    }

    public void isNumber() throws SanityCheckException {
        isNumber(allowPassOnNull)
    }

    public void isNumber(boolean allowPassOnNull) throws SanityCheckException {
        if (allowPassOnNull && entity == null) {
            return
        }

        isNotNull()

        if (entityIsCoercible()) {
            if (entity.isNumber()) {
                return
            }
            else {
                throw new SanityCheckException(name, entity, classification, "Must be a (coercible) Number")
            }
        }

        if (entity in Number == false) {
            throw new SanityCheckException(name, entity, classification, "Must be a (coercible) Number")
        }
    }

    public Integer isInteger() throws SanityCheckException {
        return isInteger(allowPassOnNull)
    }

    public Integer isInteger(boolean allowPassOnNull) throws SanityCheckException {
        if (allowPassOnNull && entity == null) {
            return entity
        }

        isNotNull()

        if (entityIsCoercible()) {
            if (entity.isInteger()) {
                return entity.toInteger()
            }
            else {
                throw new SanityCheckException(name, entity, classification, "Must be a (coercible) Integer")
            }
        }

        if (entity.getClass() != Integer) {
            throw new SanityCheckException(name, entity, classification, "Must be a (coercible) Integer")
        }

        return entity
    }

    public Long isLong() throws SanityCheckException {
        isLong(allowPassOnNull)
    }

    public Long isLong(boolean allowPassOnNull) throws SanityCheckException {
        if (allowPassOnNull && entity == null) {
            return entity
        }

        isNotNull()

        if (entityIsCoercible()) {
            if (entity.isLong()) {
                return entity.toLong()
            }
            else {
                throw new SanityCheckException(name, entity, classification, "Must be a (coercible) Long")
            }
        }

        if (entity.getClass() != Long) {
            throw new SanityCheckException(name, entity, classification, "Must be a (coercible) Long")
        }

        return entity
    }

    public BigDecimal isBigDecimal() throws SanityCheckException {
        isBigDecimal(allowPassOnNull)
    }

    public BigDecimal isBigDecimal(boolean allowPassOnNull) throws SanityCheckException {
        if (allowPassOnNull && entity == null) {
            return entity
        }

        isNotNull()

        if (entityIsCoercible()) {
            if (entity.isBigDecimal()) {
                return entity.toBigDecimal()
            }
            else {
                throw new SanityCheckException(name, entity, classification, "Must be a (coercible) BigDecimal")
            }
        }

        if (entity.getClass() != BigDecimal) {
            throw new SanityCheckException(name, entity, classification, "Must be a (coercible) BigDecimal")
        }

        return entity
    }

    public Double isDouble() throws SanityCheckException {
        isDouble(allowPassOnNull)
    }

    public Double isDouble(boolean allowPassOnNull) throws SanityCheckException {
        if (allowPassOnNull && entity == null) {
            return entity
        }

        isNotNull()

        if (entityIsCoercible()) {
            if (entity.isDouble()) {
                return entity.toDouble()
            }
            else {
                throw new SanityCheckException(name, entity, classification, "Must be a (coercible) Double")
            }
        }

        if (entity.getClass() != Double) {
            throw new SanityCheckException(name, entity, classification, "Must be a (coercible) Double")
        }

        return entity
    }

    protected boolean entityIsCoercible() {
        return [String, GStringImpl].any { entity in it }
    }
}
