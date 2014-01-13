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
 * A derivative of the <b><code>StringCoerciveSanityChecker</code></b> which includes
 * automatic handling of Groovy <i>String coercion</i> methods.
 *
 * <p>
 * The <b>StringCoerciveSanityChecker</b> provides many of the basic sanity
 * checks found in the <b><code>StringCoerciveSanityChecker</code></b>, however it
 * takes an additional step in handling <b><code>String</code></b>
 * based on its coercive properties to determine if they pass the check.
 * </p>
 *
 * <p>
 * The <b>StringCoerciveSanityChecker</b> is best used in situations where
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
 * {@link me.sudofu.sanitycheck.StringCoerciveSanityChecker StringCoerciveSanityChecker};
 * it performs more strict type-checking.
 * </p>
 *
 * @author Aaron Brown
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

    public Object isString() throws IllegalStateException {
        isString(allowPassOnNull)
    }

    public Object isString(boolean allowPassOnNull) throws IllegalStateException {
        exactClassMatch(String, allowPassOnNull)
        return entity
    }

    public Object isInteger() throws IllegalStateException {
        return isInteger(allowPassOnNull)
    }

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

    public Object isLong() throws IllegalStateException {
        isLong(allowPassOnNull)
    }

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

    public Object isBigDecimal() throws IllegalStateException {
        isBigDecimal(allowPassOnNull)
    }

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

    public Object isDouble() throws IllegalStateException {
        isDouble(allowPassOnNull)
    }

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

    protected boolean entityIsCoercible() {
        return [String, GStringImpl].any { entity in it }
    }
}
