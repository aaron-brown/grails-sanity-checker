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

import me.sudofu.sanitycheck.SanityCheckReport

/**
 * A utility for performing a wide variety of basic sanity checks on
 * input, data, parameters, arguments, etc.
 *
 * <p>
 * The <b>SanityChecker</b> provides a variety of basic sanity checks.
 * For each check performed, if any should fail an
 * {@link java.lang.SanityCheckException SanityCheckException}
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
class BasicSanityChecker extends SanityChecker {

    /**
     * Constructs a <b>BasicSanityChecker</b> which disallows
     * <code>null</code> to pass sanity checks.
     */
    public BasicSanityChecker() {
        super()
    }

    /**
     * Constructs a <b>BasicSanityChecker</b>, specifying the allowance
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
    public BasicSanityChecker(boolean allowPassOnNull) {
        super(allowPassOnNull)
    }

    public boolean isNotEmpty() throws IllegalStateException {
        isNotEmpty(allowPassOnNull)
    }

    public boolean isNotEmpty(boolean allowPassOnNull) throws IllegalStateException {
        checkYourselfBeforeYouWreckYourself()

        if (allowPassOnNull && entity == null) {
            pass('isNotEmpty', 'Cannot be empty')
            return true
        }

        if (! respondsTo('isEmpty')) {
            fail('isNotEmpty', 'Cannot be empty')
            return false
        }

        if (entity.isEmpty()) {
            fail('isNotEmpty', 'Cannot be empty')
            return false
        }

        pass('isNotEmpty', 'Cannot be empty')
        return true
    }

    public boolean isBoolean() throws IllegalStateException {
        isBoolean(allowPassOnNull)
    }

    public boolean isBoolean(boolean allowPassOnNull) throws IllegalStateException {
        exactClassMatch(Boolean, allowPassOnNull)
    }

    public boolean isString() throws IllegalStateException {
        isString(allowPassOnNull)
    }

    public boolean isString(boolean allowPassOnNull) throws IllegalStateException {
        exactClassMatch(String, allowPassOnNull)
    }

    public boolean isNumber() throws IllegalStateException {
        isNumber(allowPassOnNull)
    }

    public boolean isNumber(boolean allowPassOnNull) throws IllegalStateException {
        classMatch(Number, allowPassOnNull)
    }

    public boolean isInteger() throws IllegalStateException {
        isInteger(allowPassOnNull)
    }

    public boolean isInteger(boolean allowPassOnNull) throws IllegalStateException {
        exactClassMatch(Integer, allowPassOnNull)
    }

    public boolean isLong() throws IllegalStateException {
        isLong(allowPassOnNull)
    }

    public boolean isLong(boolean allowPassOnNull) throws IllegalStateException {
        exactClassMatch(Long, allowPassOnNull)
    }

    public boolean isBigDecimal() throws IllegalStateException {
        isBigDecimal(allowPassOnNull)
    }

    public boolean isBigDecimal(boolean allowPassOnNull) throws IllegalStateException {
        exactClassMatch(BigDecimal, allowPassOnNull)
    }

    public boolean isDouble() throws IllegalStateException {
        isDouble(allowPassOnNull)
    }

    public boolean isDouble(boolean allowPassOnNull) throws IllegalStateException {
        exactClassMatch(Double, allowPassOnNull)
    }

    public boolean isList() throws IllegalStateException {
        isList(allowPassOnNull)
    }

    public boolean isList(boolean allowPassOnNull) throws IllegalStateException {
        classMatch(List, allowPassOnNull)
    }

    public boolean isMap() throws IllegalStateException {
        isMap(allowPassOnNull)
    }

    public boolean isMap(boolean allowPassOnNull) throws IllegalStateException {
        classMatch(Map, allowPassOnNull)
    }

    public boolean respondsTo(String method) {
        respondsTo(method, allowPassOnNull)
    }

    public boolean respondsTo(String method, boolean allowPassOnNull) throws IllegalStateException {
        checkYourselfBeforeYouWreckYourself()

        if (allowPassOnNull && entity == null) {
            pass('respondsTo', "Must respond to ${method}")
            return true
        }

        isNotNull()

        if (! entity.getMetaClass().respondsTo(entity, method)) {
            fail('respondsTo', "Must respond to ${method}")
            return false
        }

        pass('respondsTo', "Must respond to ${method}")
        return true
    }

    public boolean exactClassMatch(Class clazz) throws IllegalStateException {
        exactClassMatch(clazz, allowPassOnNull)
    }

    public boolean exactClassMatch(Class clazz, boolean allowPassOnNull) throws IllegalStateException {
        checkYourselfBeforeYouWreckYourself()

        if (allowPassOnNull && entity == null) {
            pass('exactClassMatch', "Must be a(n) ${clazz}")
            return true
        }

        if (! isNotNull()) {
            fail('exactClassMatch', "Must be a(n) ${clazz}")
            return false
        }

        if (entity.getClass() != clazz) {
            fail('exactClassMatch', "Must be a(n) ${clazz}")
            return false
        }

        pass('exactClassMatch', "Must be a(n) ${clazz}")
        return true
    }

    public boolean classMatch(Class clazz) throws IllegalStateException {
        classMatch(clazz, allowPassOnNull)
    }

    public boolean classMatch(Class clazz, boolean allowPassOnNull) throws IllegalStateException {
        checkYourselfBeforeYouWreckYourself()

        if (allowPassOnNull && entity == null) {
            pass('exactClassMatch', "Must be a(n) ${clazz} (or subclass thereof)")
            return true
        }

        if (! isNotNull()) {
            fail('exactClassMatch', "Must be a(n) ${clazz} (or subclass thereof)")
            return false
        }

        if (entity in clazz == false) {
            fail('exactClassMatch', "Must be a(n) ${clazz} (or subclass thereof)")
            return false
        }

        pass('exactClassMatch', "Must be a(n) ${clazz} (or subclass thereof)")
        return true
    }
}
