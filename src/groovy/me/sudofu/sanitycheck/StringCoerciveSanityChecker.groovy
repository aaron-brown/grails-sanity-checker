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

import org.codehaus.groovy.runtime.GStringImpl

/**
 * An implementation of
 * {@link me.sudofu.sanitycheck.SanityChecker SanityChecker} which
 * provides mnemonics for sanity-checking entities and coercing them
 * if possible.
 *
 * <p>
 * The <b><code>StringCoerciveSanityChecker</code></b> is designed to
 * not only provide sanity checks, but also coerce
 * <b><code>String</code></b> objects into the mnemonic datatype being
 * checked for.
 * </p>
 *
 * <p>
 * This has the distinct advantage of being used in places where data
 * might be normalized, or even allows for that normalization to occur.
 * For example, a REST resource that operates on JSON. Or,
 * for sanity-checking Groovy Named-Parameter method signatures. The
 * very best use-case for this checker is when working with a
 * <code>Map</code>, since all methods return the entity that is being
 * checked: pass or fail, coerced or not.
 * </p>
 *
 * <p>
 * An example of use:
 *
 * <code><pre>
 * Map normalizedInput = [
 *     name: "John Doe",
 *     age: "42",
 *     height: '172.72',
 * ]
 *
 * SanityChecker checker = new StringCoerciveSanityChecker()
 *
 * normalizedInput.with {
 *     checker.runChecks {
 *
 *         check('name', name)
 *         isString()
 *         isNotEmpty()
 *
 *         age = check('age', age).isInteger()
 *
 *         height = check('height', height).isDouble()
 *
 *         maxLong = check('maxLong', maxLong).isInteger()
 *     }
 * }
 * </pre></code>
 * </p>
 *
 * <p>
 * Note that the behavior of coercion is based on Groovy buil-ins.
 * Therefore, should Groovy alter the behavior of its coercion, that
 * will also alter the behavior of this checker.
 * </p>
 *
 * @author Aaron Brown
 *
 * @see SanityChecker
 * @see BasicSanityChecker
 */
class StringCoerciveSanityChecker extends SanityChecker {

    /**
     * Constructs a <b>StringCoerciveSanityChecker</b> which disallows
     * <code>null</code> to pass sanity checks.
     */
    public StringCoerciveSanityChecker() {
        super()
    }

    /**
     * Constructs a <b>StringCoerciveSanityChecker</b>, specifying the allowance
     * of <code>null</code> passing sanity cheks.
     *
     * <p>
     * There are two distinct behaviors of a <b>SanityChecker</b>:
     *
     * <ol>
     * <li>
     * Implicitly check for <code>null</code>, and <i>disallow</i>
     * <code>null</code>s to pass the sanity check.
     * </li>
     * <li>
     * Implicitly check for <code>null</code>, and <i>allow</i>
     * <code>null</code>s to pass the sanity check.
     * </li>
     * </ol>
     * </p>
     */
    public StringCoerciveSanityChecker(boolean allowPassOnNull) {
        super(allowPassOnNull)
    }

    /**
     * Run a sanity check to ensure the entity is a
     * <b><code>String</code></b>.
     *
     * <p>Default behavior specified by {@link #allowPassOnNull allowPassOnNull}</p>
     *
     * @return
     *
     * The entity that was passed checked.
     *
     * @throws  IllegalStateException
     *
     * An entity must be declared via a <b><code>check()</code></b>
     * invokation before any sanity checks can be performed.
     */
    public Object isString() throws IllegalStateException {
        isString(allowPassOnNull)
    }

    /**
     * Run a sanity check to ensure the entity is a(n)
     * <b><code>boolean</code></b>.
     *
     * @param   allowPassOnNull
     *
     * Explicitly declare or override the behavior of
     * {@link #allowPassOnNull allowPassOnNull}
     *
     * @return
     *
     * The entity that was passed checked.
     *
     * @throws  IllegalStateException
     *
     * An entity must be declared via a <b><code>check()</code></b>
     * invokation before any sanity checks can be performed.
     */
    public Object isString(boolean allowPassOnNull) throws IllegalStateException {
        exactClassMatch(String, allowPassOnNull)
        return entity
    }

    /**
     * Run a sanity check to ensure the entity is a(n)
     * <b><code>Integer</code></b>.
     *
     * <p>Default behavior specified by {@link #allowPassOnNull allowPassOnNull}</p>
     *
     * @return
     *
     * The entity that was checked; or, if a <code>String</code> and
     * coercible to <code>Integer</code>, then the <code>Integer</code>.
     *
     * @throws  IllegalStateException
     *
     * An entity must be declared via a <b><code>check()</code></b>
     * invokation before any sanity checks can be performed.
     */
    public Object isInteger() throws IllegalStateException {
        return isInteger(allowPassOnNull)
    }

    /**
     * Run a sanity check to ensure the entity is a(n)
     * <b><code>Integer</code></b>.
     *
     * @param   allowPassOnNull
     *
     * Explicitly declare or override the behavior of
     * {@link #allowPassOnNull allowPassOnNull}
     *
     * @return
     *
     * The entity that was checked; or, if a <code>String</code> and
     * coercible to <code>Integer</code>, then the <code>Integer</code>.
     *
     * @throws  IllegalStateException
     *
     * An entity must be declared via a <b><code>check()</code></b>
     * invokation before any sanity checks can be performed.
     */
    public Object isInteger(boolean allowPassOnNull) throws IllegalStateException {
        checkYourselfBeforeYouWreckYourself()

        if (allowPassOnNull && entity == null) {
            return entity
        }

        isNotNull()

        if (entityIsCoercible()) {
            if (entity.isInteger()) {
                pass('coercibleClassMatch', 'Must be a(n) Integer, or String coercible to Integer')
                return entity.toInteger()
            }
            else {
                fail('coercibleClassMatch', 'Must be a(n) Integer, or String coercible to Integer')
            }
        }

        exactClassMatch(Integer)

        return entity
    }

    /**
     * Run a sanity check to ensure the entity is a(n)
     * <b><code>Long</code></b>.
     *
     * <p>Default behavior specified by {@link #allowPassOnNull allowPassOnNull}</p>
     *
     * @return
     *
     * The entity that was checked; or, if a <code>String</code> and
     * coercible to <code>Long</code>, then the <code>Long</code>.
     *
     * @throws  IllegalStateException
     *
     * An entity must be declared via a <b><code>check()</code></b>
     * invokation before any sanity checks can be performed.
     */
    public Object isLong() throws IllegalStateException {
        isLong(allowPassOnNull)
    }

    /**
     * Run a sanity check to ensure the entity is a(n)
     * <b><code>Long</code></b>.
     *
     * @param   allowPassOnNull
     *
     * Explicitly declare or override the behavior of
     * {@link #allowPassOnNull allowPassOnNull}
     *
     * @return
     *
     * The entity that was checked; or, if a <code>String</code> and
     * coercible to <code>Long</code>, then the <code>Long</code>.
     *
     * @throws  IllegalStateException
     *
     * An entity must be declared via a <b><code>check()</code></b>
     * invokation before any sanity checks can be performed.
     */
    public Object isLong(boolean allowPassOnNull) throws IllegalStateException {
        checkYourselfBeforeYouWreckYourself()

        if (allowPassOnNull && entity == null) {
            return entity
        }

        isNotNull()

        if (entityIsCoercible()) {
            if (entity.isLong()) {
                pass('coercibleClassMatch', 'Must be a(n) Long, or String coercible to Long')
                return entity.toLong()
            }
            else {
                fail('coercibleClassMatch', 'Must be a(n) Long, or String coercible to Long')
            }
        }

        exactClassMatch(Long)

        return entity
    }

    /**
     * Run a sanity check to ensure the entity is a(n)
     * <b><code>BigDecimal</code></b>.
     *
     * <p>Default behavior specified by {@link #allowPassOnNull allowPassOnNull}</p>
     *
     * @return
     *
     * The entity that was checked; or, if a <code>String</code> and
     * coercible to <code>BigDecimal</code>, then the <code>BigDecimal</code>.
     *
     * @throws  IllegalStateException
     *
     * An entity must be declared via a <b><code>check()</code></b>
     * invokation before any sanity checks can be performed.
     */
    public Object isBigDecimal() throws IllegalStateException {
        isBigDecimal(allowPassOnNull)
    }

    /**
     * Run a sanity check to ensure the entity is a(n)
     * <b><code>BigDecimal</code></b>.
     *
     * @param   allowPassOnNull
     *
     * Explicitly declare or override the behavior of
     * {@link #allowPassOnNull allowPassOnNull}
     *
     * @return
     *
     * The entity that was checked; or, if a <code>String</code> and
     * coercible to <code>BigDecimal</code>, then the <code>BigDecimal</code>.
     *
     * @throws  IllegalStateException
     *
     * An entity must be declared via a <b><code>check()</code></b>
     * invokation before any sanity checks can be performed.
     */
    public Object isBigDecimal(boolean allowPassOnNull) throws IllegalStateException {
        checkYourselfBeforeYouWreckYourself()

        if (allowPassOnNull && entity == null) {
            return entity
        }

        isNotNull()

        if (entityIsCoercible()) {
            if (entity.isBigDecimal()) {
                pass('coercibleClassMatch', 'Must be a(n) BigDecimal, or String coercible to BigDecimal')
                return entity.toBigDecimal()
            }
            else {
                fail('coercibleClassMatch', 'Must be a(n) BigDecimal, or String coercible to BigDecimal')
            }
        }

        exactClassMatch(BigDecimal)

        return entity
    }

    /**
     * Run a sanity check to ensure the entity is a(n)
     * <b><code>Double</code></b>.
     *
     * <p>Default behavior specified by {@link #allowPassOnNull allowPassOnNull}</p>
     *
     * @return
     *
     * The entity that was checked; or, if a <code>String</code> and
     * coercible to <code>Double</code>, then the <code>Double</code>.
     *
     * @throws  IllegalStateException
     *
     * An entity must be declared via a <b><code>check()</code></b>
     * invokation before any sanity checks can be performed.
     */
    public Object isDouble() throws IllegalStateException {
        isDouble(allowPassOnNull)
    }

    /**
     * Run a sanity check to ensure the entity is a(n)
     * <b><code>Double</code></b>.
     *
     * @param   allowPassOnNull
     *
     * Explicitly declare or override the behavior of
     * {@link #allowPassOnNull allowPassOnNull}
     *
     * @return
     *
     * The entity that was checked; or, if a <code>String</code> and
     * coercible to <code>Double</code>, then the <code>Double</code>.
     *
     * @throws  IllegalStateException
     *
     * An entity must be declared via a <b><code>check()</code></b>
     * invokation before any sanity checks can be performed.
     */
    public Object isDouble(boolean allowPassOnNull) throws IllegalStateException {
        checkYourselfBeforeYouWreckYourself()

        if (allowPassOnNull && entity == null) {
            return entity
        }

        isNotNull()

        if (entityIsCoercible()) {
            if (entity.isDouble()) {
                pass('coercibleClassMatch', 'Must be a(n) Double, or String coercible to Double')
                return entity.toDouble()
            }
            else {
                fail('coercibleClassMatch', 'Must be a(n) Double, or String coercible to Double')
            }
        }

        exactClassMatch(Double)

        return entity
    }

    /**
     * Detect coercible entities.
     *
     * @return
     *
     * <code>true</code> if the entity is coercible, <code>false</code>
     * otherwise.
     */
    protected boolean entityIsCoercible() {
        return [String, GStringImpl].any { entity in it }
    }
}
