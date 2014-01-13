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
class SanityChecker {

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
     * <p>By default, the <code>classification</code> is <b>entity</b>.</p>
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
     * By default, the behavior of <b>SanityChecker</b> is to fail any
     * check if the <b><code>entity</code></b> is <code>null</code>.
     * </p>
     *
     * <p>This behavior can be overridden at the level of any sanity check.</p>
     */
    protected boolean allowPassOnNull

    protected SanityCheckReport report

    /**
     * Constructs a <b>SanityChecker</b> which disallows
     * <code>null</code> to pass sanity checks.
     */
    public SanityChecker() {
        this(false)
    }

    /**
     * Constructs a <b>SanityChecker</b>, specifying the allowance
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
    public SanityChecker(boolean allowPassOnNull) {
        this.allowPassOnNull = allowPassOnNull
        this.report = new SanityCheckReport()
    }

    protected void setName(String arg) {

    }

    protected void setEntity(Object arg) {

    }

    protected void setClassification(String arg) {

    }

    protected void setReport(SanityCheckReport arg) {

    }

    /**
     * Allow <code>null</code> checks to be a valid pass-condition.
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
    public SanityChecker allowPassOnNull() {
        this.allowPassOnNull = true
        return this
    }


    /**
     * Disallow <code>null</code> checks to be a valid pass-condition.
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
    public SanityChecker disallowPassOnNull() {
        this.allowPassOnNull = false
        return this
    }

    /**
     * Run the checks via <b>Closure</b>.
     *
     * @param   closure
     *
     * The <b>Closure</b> containing the sanity checks to execute on.
     */
    public SanityChecker runChecks(Closure closure) {
        Closure runClosure = closure.clone()

        runClosure.delegate = this
        runClosure.resolveStrategy = Closure.OWNER_FIRST

        runClosure()

        return runClosure.delegate
    }

    public SanityChecker check(String name, Object entity) throws IllegalArgumentException {
        check(name, entity, null)
    }

    public SanityChecker check(String name, Object entity, String classification) throws IllegalArgumentException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("The name of the ${classification ?: 'entity'} being checked against was not provided.")
        }

        this.name = name ?: entity.getClass().name
        this.entity = entity
        this.classification = classification ?: 'entity'

        return this
    }

    protected void pass(String check, String checkDescription) {
        report.pass(name, classification, check, checkDescription)
    }

    protected void fail(String check, String checkDescription) {
        report.fail(name, classification, check, checkDescription)
    }

    public boolean hasPasses() {
        return report.hasPasses()
    }

    public boolean hasFailures() {
        return report.hasFailures()
    }

    public int countPasses() {
        return report.countPasses()
    }

    public int countFailures() {
        return report.countFailures()
    }

    public List<Map> getPasses() {
        return report.passes
    }

    public List<Map> getFailures() {
        return report.failures
    }

    protected void checkYourselfBeforeYouWreckYourself() throws IllegalStateException {
        if (name == null || name.isEmpty()) {
            throw new IllegalStateException("A sanity check has been performed without any indication of the name (and possibly classification) of what is being checked against; check() must be used before running any sanity checks.")
        }
    }

    public boolean isNotNull() throws IllegalStateException {
        checkYourselfBeforeYouWreckYourself()

        if (entity == null) {
            fail('isNotNull', "Cannot be null")
            return false
        }

        pass('isNotNull', "Cannot be null")
        return true
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
