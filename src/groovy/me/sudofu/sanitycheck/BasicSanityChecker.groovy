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
 * An implementation of
 * {@link me.sudofu.sanitycheck.SanityChecker SanityChecker} which
 * provides mnemonics for sanity-checking various kinds of Classes.
 *
 * @author Aaron Brown
 *
 * @see SanityChecker
 * @see StringCoerciveSanityChecker
 */
class BasicSanityChecker extends SanityChecker {

    /**
     * Constructs a <b>BasicSanityChecker</b> with the intial behavior
     * of disallowing <code>null</code> to pass sanity checks.
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
     *
     * If provided, this behavior can be overridden per sanity check.
     * </p>
     */
    public BasicSanityChecker(boolean allowPassOnNull) {
        super(allowPassOnNull)
    }

    /**
     * Run a sanity check to ensure the entity is a(n)
     * <b><code>Boolean</code></b>.
     *
     * <p>Default behavior specified by {@link #allowPassOnNull allowPassOnNull}</p>
     *
     * @return
     *
     * <code>true</code> if the test passes, <code>false</code>
     * otherwise.
     *
     * @throws  IllegalStateException
     *
     * An entity must be declared via a <b><code>check()</code></b>
     * invokation before any sanity checks can be performed.
     */
    public boolean isBoolean() throws IllegalStateException {
        isBoolean(allowPassOnNull)
    }

    /**
     * Run a sanity check to ensure the entity is a(n)
     * <b><code>Boolean</code></b>.
     *
     * @param   allowPassOnNull
     *
     * Explicitly declare or override the behavior of
     * {@link #allowPassOnNull allowPassOnNull}
     *
     * @return
     *
     * <code>true</code> if the test passes, <code>false</code>
     * otherwise.
     *
     * @throws  IllegalStateException
     *
     * An entity must be declared via a <b><code>check()</code></b>
     * invokation before any sanity checks can be performed.
     */
    public boolean isBoolean(boolean allowPassOnNull) throws IllegalStateException {
        exactClassMatch(Boolean, allowPassOnNull)
    }

    /**
     * Run a sanity check to ensure the entity is a(n)
     * <b><code>String</code></b>.
     *
     * <p>Default behavior specified by {@link #allowPassOnNull allowPassOnNull}</p>
     *
     * @return
     *
     * <code>true</code> if the test passes, <code>false</code>
     * otherwise.
     *
     * @throws  IllegalStateException
     *
     * An entity must be declared via a <b><code>check()</code></b>
     * invokation before any sanity checks can be performed.
     */
    public boolean isString() throws IllegalStateException {
        isString(allowPassOnNull)
    }

    /**
     * Run a sanity check to ensure the entity is a(n)
     * <b><code>String</code></b>.
     *
     * @param   allowPassOnNull
     *
     * Explicitly declare or override the behavior of
     * {@link #allowPassOnNull allowPassOnNull}
     *
     * @return
     *
     * <code>true</code> if the test passes, <code>false</code>
     * otherwise.
     *
     * @throws  IllegalStateException
     *
     * An entity must be declared via a <b><code>check()</code></b>
     * invokation before any sanity checks can be performed.
     */
    public boolean isString(boolean allowPassOnNull) throws IllegalStateException {
        exactClassMatch(String, allowPassOnNull)
    }

    /**
     * Run a sanity check to ensure the entity is a(n)
     * <b><code>Number</code></b>.
     *
     * <p>Default behavior specified by {@link #allowPassOnNull allowPassOnNull}</p>
     *
     * @return
     *
     * <code>true</code> if the test passes, <code>false</code>
     * otherwise.
     *
     * @throws  IllegalStateException
     *
     * An entity must be declared via a <b><code>check()</code></b>
     * invokation before any sanity checks can be performed.
     */
    public boolean isNumber() throws IllegalStateException {
        isNumber(allowPassOnNull)
    }

    /**
     * Run a sanity check to ensure the entity is a(n)
     * <b><code>Number</code></b>.
     *
     * @param   allowPassOnNull
     *
     * Explicitly declare or override the behavior of
     * {@link #allowPassOnNull allowPassOnNull}
     *
     * @return
     *
     * <code>true</code> if the test passes, <code>false</code>
     * otherwise.
     *
     * @throws  IllegalStateException
     *
     * An entity must be declared via a <b><code>check()</code></b>
     * invokation before any sanity checks can be performed.
     */
    public boolean isNumber(boolean allowPassOnNull) throws IllegalStateException {
        classMatch(Number, allowPassOnNull)
    }

    /**
     * Run a sanity check to ensure the entity is a(n)
     * <b><code>Integer</code></b>.
     *
     * <p>Default behavior specified by {@link #allowPassOnNull allowPassOnNull}</p>
     *
     * @return
     *
     * <code>true</code> if the test passes, <code>false</code>
     * otherwise.
     *
     * @throws  IllegalStateException
     *
     * An entity must be declared via a <b><code>check()</code></b>
     * invokation before any sanity checks can be performed.
     */
    public boolean isInteger() throws IllegalStateException {
        isInteger(allowPassOnNull)
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
     * <code>true</code> if the test passes, <code>false</code>
     * otherwise.
     *
     * @throws  IllegalStateException
     *
     * An entity must be declared via a <b><code>check()</code></b>
     * invokation before any sanity checks can be performed.
     */
    public boolean isInteger(boolean allowPassOnNull) throws IllegalStateException {
        exactClassMatch(Integer, allowPassOnNull)
    }

    /**
     * Run a sanity check to ensure the entity is a(n)
     * <b><code>Long</code></b>.
     *
     * <p>Default behavior specified by {@link #allowPassOnNull allowPassOnNull}</p>
     *
     * @return
     *
     * <code>true</code> if the test passes, <code>false</code>
     * otherwise.
     *
     * @throws  IllegalStateException
     *
     * An entity must be declared via a <b><code>check()</code></b>
     * invokation before any sanity checks can be performed.
     */
    public boolean isLong() throws IllegalStateException {
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
     * <code>true</code> if the test passes, <code>false</code>
     * otherwise.
     *
     * @throws  IllegalStateException
     *
     * An entity must be declared via a <b><code>check()</code></b>
     * invokation before any sanity checks can be performed.
     */
    public boolean isLong(boolean allowPassOnNull) throws IllegalStateException {
        exactClassMatch(Long, allowPassOnNull)
    }

    /**
     * Run a sanity check to ensure the entity is a(n)
     * <b><code>BigDecimal</code></b>.
     *
     * <p>Default behavior specified by {@link #allowPassOnNull allowPassOnNull}</p>
     *
     * @return
     *
     * <code>true</code> if the test passes, <code>false</code>
     * otherwise.
     *
     * @throws  IllegalStateException
     *
     * An entity must be declared via a <b><code>check()</code></b>
     * invokation before any sanity checks can be performed.
     */
    public boolean isBigDecimal() throws IllegalStateException {
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
     * <code>true</code> if the test passes, <code>false</code>
     * otherwise.
     *
     * @throws  IllegalStateException
     *
     * An entity must be declared via a <b><code>check()</code></b>
     * invokation before any sanity checks can be performed.
     */
    public boolean isBigDecimal(boolean allowPassOnNull) throws IllegalStateException {
        exactClassMatch(BigDecimal, allowPassOnNull)
    }

    /**
     * Run a sanity check to ensure the entity is a(n)
     * <b><code>Double</code></b>.
     *
     * <p>Default behavior specified by {@link #allowPassOnNull allowPassOnNull}</p>
     *
     * @return
     *
     * <code>true</code> if the test passes, <code>false</code>
     * otherwise.
     *
     * @throws  IllegalStateException
     *
     * An entity must be declared via a <b><code>check()</code></b>
     * invokation before any sanity checks can be performed.
     */
    public boolean isDouble() throws IllegalStateException {
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
     * <code>true</code> if the test passes, <code>false</code>
     * otherwise.
     *
     * @throws  IllegalStateException
     *
     * An entity must be declared via a <b><code>check()</code></b>
     * invokation before any sanity checks can be performed.
     */
    public boolean isDouble(boolean allowPassOnNull) throws IllegalStateException {
        exactClassMatch(Double, allowPassOnNull)
    }

    /**
     * Run a sanity check to ensure the entity is a(n)
     * <b><code>List</code></b>.
     *
     * <p>Default behavior specified by {@link #allowPassOnNull allowPassOnNull}</p>
     *
     * @return
     *
     * <code>true</code> if the test passes, <code>false</code>
     * otherwise.
     *
     * @throws  IllegalStateException
     *
     * An entity must be declared via a <b><code>check()</code></b>
     * invokation before any sanity checks can be performed.
     */
    public boolean isList() throws IllegalStateException {
        isList(allowPassOnNull)
    }

    /**
     * Run a sanity check to ensure the entity is a(n)
     * <b><code>List</code></b>.
     *
     * @param   allowPassOnNull
     *
     * Explicitly declare or override the behavior of
     * {@link #allowPassOnNull allowPassOnNull}
     *
     * @return
     *
     * <code>true</code> if the test passes, <code>false</code>
     * otherwise.
     *
     * @throws  IllegalStateException
     *
     * An entity must be declared via a <b><code>check()</code></b>
     * invokation before any sanity checks can be performed.
     */
    public boolean isList(boolean allowPassOnNull) throws IllegalStateException {
        classMatch(List, allowPassOnNull)
    }

    /**
     * Run a sanity check to ensure the entity is a(n)
     * <b><code>Map</code></b>.
     *
     * <p>Default behavior specified by {@link #allowPassOnNull allowPassOnNull}</p>
     *
     * @return
     *
     * <code>true</code> if the test passes, <code>false</code>
     * otherwise.
     *
     * @throws  IllegalStateException
     *
     * An entity must be declared via a <b><code>check()</code></b>
     * invokation before any sanity checks can be performed.
     */
    public boolean isMap() throws IllegalStateException {
        isMap(allowPassOnNull)
    }

    /**
     * Run a sanity check to ensure the entity is a(n)
     * <b><code>Map</code></b>.
     *
     * @param   allowPassOnNull
     *
     * Explicitly declare or override the behavior of
     * {@link #allowPassOnNull allowPassOnNull}
     *
     * @return
     *
     * <code>true</code> if the test passes, <code>false</code>
     * otherwise.
     *
     * @throws  IllegalStateException
     *
     * An entity must be declared via a <b><code>check()</code></b>
     * invokation before any sanity checks can be performed.
     */
    public boolean isMap(boolean allowPassOnNull) throws IllegalStateException {
        classMatch(Map, allowPassOnNull)
    }
}
