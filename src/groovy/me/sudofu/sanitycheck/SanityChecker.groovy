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

import grails.util.Holders

/**
 * An exentsible utility for performing a variety of basic or advanced
 * sanity checks on input, data, parameters, arguments, etc.
 *
 * <p>
 * The <b>SanityChecker</b> provides a small number of basic sanity
 * checks. For each check performed, its result of pass or failure is
 * recorded in a {@link me.sudofu.sanitycheck.SanityCheckReport SanityCheckReport}.
 * </p>
 *
 * <p>
 * While the use-cases for the <b>SanityChecker</b> itself may be
 * somewhat limited, it gives a good platform for easy extension and
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
     * <p>The value of the entity.</p>
     */
    protected Object entity

    /**
     * The classification of the entity (for context).
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
     * Indicates whether or not the <b><code>SanityChecker</code></b>
     * should allow <code>null</code> entities to pass sanity checks.
     *
     * <p>
     * By default, the behavior of <b><code>SanityChecker</code></b> is
     * to fail any check if the <b><code>entity</code></b> is
     * <code>null</code>.
     * </p>
     *
     * <p>If provided, this behavior can be overridden per sanity check.</p>
     */
    protected boolean allowPassOnNull

    /**
     * The <b><code>SanityCheckReport</code></b>.
     */
    protected SanityCheckReport report

    /**
     * Constructs a <b>SanityChecker</b> with the intial behavior of
     * disallowing <code>null</code> to pass sanity checks.
     */
    public SanityChecker() {
        this(Holders.config.sanityChecker.defaults.allowPassOnNull ?: false)
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
     *
     * If provided, this behavior can be overridden per sanity check.
     * </p>
     */
    public SanityChecker(boolean allowPassOnNull) {
        this.allowPassOnNull = allowPassOnNull
        this.report = new SanityCheckReport()
    }

    /**
     * Prevent setting.
     *
     * @param   arg
     *
     * Argument.
     */
    protected void setName(String arg) {

    }

    /**
     * Prevent setting.
     *
     * @param   arg
     *
     * Argument.
     */
    protected void setEntity(Object arg) {

    }

    /**
     * Prevent setting.
     *
     * @param   arg
     *
     * Argument.
     */
    protected void setClassification(String arg) {

    }

    /**
     * Prevent setting.
     *
     * @param   arg
     *
     * Argument.
     */
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
     *
     * If provided, this behavior can be overridden per sanity check.
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
     *
     * If provided, this behavior can be overridden per sanity check.
     * </p>
     */
    public SanityChecker disallowPassOnNull() {
        this.allowPassOnNull = false
        return this
    }

    /**
     * Run the checks via <b>Closure</b>.
     *
     * <p>
     * Note: The <code>Closure</code> resolve-strategy is owner-first,
     * therefore it is not necessary to explicitly declare
     * <code>owner</code> on variable names that collide with field
     * names.
     * </p>
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

    /**
     * Declare a new entity to check.
     *
     * @param   name
     *
     * The name of the entity being checked (for example, the name of
     * the parameter).
     *
     * @param   entity
     *
     * The value of the entity being checked.
     *
     * @throws  IllegalArgumentException
     *
     * A valid name <b>must</b> be provided; cannot be empty, cannot be
     * <code>null</code>.
     */
    public SanityChecker check(String name, Object entity) throws IllegalArgumentException {
        check(name, entity, null)
    }

    /**
     * Declare a new entity to check.
     *
     * @param   name
     *
     * The name of the entity being checked (for example, the name of
     * the parameter).
     *
     * @param   entity
     *
     * The value of the entity being checked.
     *
     * @param   classification
     *
     * The classification of the entity that is being checked
     * (for example, parameter); <b>entity</b> by default.
     *
     * @throws  IllegalArgumentException
     *
     * A valid name <b>must</b> be provided; cannot be empty, cannot be
     * <code>null</code>.
     */
    public SanityChecker check(String name, Object entity, String classification) throws IllegalArgumentException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("The name of the ${classification ?: 'entity'} being checked against was not provided.")
        }

        this.name = name ?: entity.getClass().name
        this.entity = entity
        this.classification = classification ?: Holders.config.sanityChecker.defaults.classification ?: 'entity'

        return this
    }

    /**
     * Use when a sanity check has passed.
     *
     * @param   check
     *
     * The check that was performed (for example, <b>isNotNull</b>).
     *
     * @param   checkDesscription
     *
     * A succinct description of the check, or its intended purpose
     * (for example, <i>Must not be <code>null</code></i>).
     */
    protected void pass(String check, String checkDescription) {
        report.pass(name, classification, check, checkDescription)
    }

    /**
     * Use when a sanity check has failed.
     *
     * @param   check
     *
     * The check that was performed (for example, <b>isNotNull</b>).
     *
     * @param   checkDesscription
     *
     * A succinct description of the check, or its intended purpose
     * (for example, <i>Must not be <code>null</code></i>).
     */
    protected void fail(String check, String checkDescription) {
        report.fail(name, classification, check, checkDescription)
    }

    /**
     * Determine if the report has instances of sanity checks that have
     * passed.
     *
     * @return
     *
     * <code>true</code> if at least one sanity check has passed;
     * <code>false</code> otherwise.
     */
    public boolean hasPasses() {
        return report.hasPasses()
    }

    /**
     * Determine if the report has instances of sanity checks that have
     * failed.
     *
     * @return
     *
     * <code>true</code> if at least one sanity check has failed;
     * <code>false</code> otherwise.
     */
    public boolean hasFailures() {
        return report.hasFailures()
    }

    /**
     * Count the number of results recorded.
     *
     * <p>
     * Note that this is an unpredictable number, as many sanity
     * checks run implicit checks such <code>isNotNull</code>.
     * </p>
     *
     * @return
     *
     * The number of results recorded, or <code>0</code> (zero) if no
     * checks have been run.
     */
    public int countResults() {
        return report.countResults()
    }

    /**
     * Count the number of sanity check passes.
     *
     * <p>
     * In practice, this number is unpredictable as some sanity checks
     * may have implicit sanity checks. For example, most sanity checks
     * will perform a check to ensure the entity is not <code>null</code>
     * before performing the actual check, which would mean for that
     * one check, <b>2</b> would be the total count (if both passed).
     * </p>
     *
     * @return
     *
     * The number of passed sanity check entries; <code>0</code> (zero)
     * otherwise.
     */
    public int countPasses() {
        return report.countPasses()
    }

    /**
     * Count the number of sanity check failures.
     *
     * <p>
     * In practice, this number is unpredictable as some sanity checks
     * may have implicit sanity checks. For example, most sanity checks
     * will perform a check to ensure the entity is not <code>null</code>
     * before performing the actual check, which would mean for that
     * one check, <b>2</b> would be the total count (if both failed).
     * </p>
     *
     * @return
     *
     * The number of failed sanity check entries; <code>0</code> (zero)
     * otherwise.
     */
    public int countFailures() {
        return report.countFailures()
    }

    /**
     * Retrieve all results of the sanity checks run.
     *
     * <p>
     * <table>
     *  <tr>
     *   <th>Key</th>
     *   <th>Description</th>
     *  </tr>
     *  <tr>
     *   <td>result</td>
     *   <td>Whether or not the check passed or failed.</td>
     *  </tr>
     *  <tr>
     *   <td>name</td>
     *   <td>The name of the entity checked against.</td>
     *  </tr>
     *  <tr>
     *   <td>classification</td>
     *   <td>The classification of the entity checked against.</td>
     *  </tr>
     *  <tr>
     *   <td>check</td>
     *   <td>A symbol / name of the check performed on the entity.</td>
     *  </tr>
     *  <tr>
     *   <td>checkDescription</td>
     *   <td>A succinct description or intended purpose of the check.</td>
     *  </tr>
     * </table>
     * </p>
     *
     * @return
     *
     * A <code>List</code> of the results of the checks that have been
     * run, described in <code>Map</code>s; an empty <code>List</code>
     * if no checks were performed.
     */
    public List<Map> getResults() {
        return report.results
    }

    /**
     * Retrieve the entries for sanity checks that have passed.
     *
     * <p>
     * <table>
     *  <tr>
     *   <th>Key</th>
     *   <th>Description</th>
     *  </tr>
     *  <tr>
     *   <td>result</td>
     *   <td>Whether or not the check passed or failed.</td>
     *  </tr>
     *  <tr>
     *   <td>name</td>
     *   <td>The name of the entity checked against.</td>
     *  </tr>
     *  <tr>
     *   <td>classification</td>
     *   <td>The classification of the entity checked against.</td>
     *  </tr>
     *  <tr>
     *   <td>check</td>
     *   <td>A symbol / name of the check performed on the entity.</td>
     *  </tr>
     *  <tr>
     *   <td>checkDescription</td>
     *   <td>A succinct description or intended purpose of the check.</td>
     *  </tr>
     * </table>
     * </p>
     *
     * @return
     *
     * A <code>List</code> of those checks that have passed, described
     * in <code>Map</code>s; an empty <code>List</code> if no checks
     * were performed, or no passes occured.
     */
    public List<Map> getPasses() {
        return report.passes
    }

    /**
     * Retrieve the entries for sanity checks that have failed.
     *
     * <p>
     * <table>
     *  <tr>
     *   <th>Key</th>
     *   <th>Description</th>
     *  </tr>
     *  <tr>
     *   <td>result</td>
     *   <td>Whether or not the check passed or failed.</td>
     *  </tr>
     *  <tr>
     *   <td>name</td>
     *   <td>The name of the entity checked against.</td>
     *  </tr>
     *  <tr>
     *   <td>classification</td>
     *   <td>The classification of the entity checked against.</td>
     *  </tr>
     *  <tr>
     *   <td>check</td>
     *   <td>A symbol / name of the check performed on the entity.</td>
     *  </tr>
     *  <tr>
     *   <td>checkDescription</td>
     *   <td>A succinct description or intended purpose of the check.</td>
     *  </tr>
     * </table>
     * </p>
     *
     * @return
     *
     * A <code>List</code> of those checks that have failed, described
     * in <code>Map</code>s; an empty <code>List</code> if no checks
     * were performed, or no failures occured.
     */
    public List<Map> getFailures() {
        return report.failures
    }

    /**
     * Use before sanity checks to ensure that a <code>check</code>
     * method was invoked.
     *
     * <p>Because if you don't, you'll go insane in the membrane.</p>
     *
     * @throws  IllegalStateException
     *
     * Thrown if it has been detected that neither
     * {@link #check(String,Object)} nor
     * {@link #check(String,Object,String)} has been invoked
     * before the first sanity check.
     */
    protected void checkYourselfBeforeYouWreckYourself() throws IllegalStateException {
        if (name == null || name.isEmpty()) {
            throw new IllegalStateException("A sanity check has been performed without any indication of the name (and possibly classification) of what is being checked against; check() must be used before running any sanity checks.")
        }
    }

    /**
     * Run a sanity check to ensure the entity is not <code>null</code>.
     *
     * <p>Is not influenced by {@link #allowPassOnNull allowPassOnNull}.</p>
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
    public boolean isNotNull() throws IllegalStateException {
        checkYourselfBeforeYouWreckYourself()

        if (entity == null) {
            fail('isNotNull', "Cannot be null")
            return false
        }

        pass('isNotNull', "Cannot be null")
        return true
    }

    /**
     * Run a sanity check to ensure the entity is not empty.
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
    public boolean isNotEmpty() throws IllegalStateException {
        isNotEmpty(allowPassOnNull)
    }

    /**
     * Run a sanity check to ensure the entity is not empty.
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

    /**
     * Run a sanity check to ensure the entity responds to a method signature
     * given.
     *
     * <p>Default behavior specified by {@link #allowPassOnNull allowPassOnNull}</p>
     *
     * <p>See {@link groovy.lang.MetaObjectProtocol.respondsTo(Object, String) respondsTo}</p>
     *
     * @param   method
     *
     * The method to test for.
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
     *
     * @throws  IllegalArgumentException
     *
     * <code>method</code> <b>must</b> be provided; cannot be empty,
     * cannot be <code>null</code>.
     */
    public boolean respondsTo(String method) throws IllegalStateException, IllegalArgumentException {
        respondsTo(method, allowPassOnNull)
    }

    /**
     * Run a sanity check to ensure the entity responds to a method signature
     * given.
     *
     * @param   method
     *
     * The method to test for.
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
     *
     * @throws  IllegalArgumentException
     *
     * <code>method</code> <b>must</b> be provided; cannot be empty,
     * cannot be <code>null</code>.
     */
    public boolean respondsTo(String method, boolean allowPassOnNull) throws IllegalStateException, IllegalArgumentException {
        checkYourselfBeforeYouWreckYourself()

        if (method == null || method.isEmpty()) {
            throw new IllegalArgumentException("A method must be specified for respondsTo sanity check.")
        }

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

    /**
     * Run a sanity check to ensure the entity is a specific
     * <b><code>Class</code></b>.
     *
     * <p>Default behavior specified by {@link #allowPassOnNull allowPassOnNull}</p>
     *
     * @param   clazz
     *
     * The <b><code>Class</code></b> to test for.
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
     *
     * @throws  IllegalArgumentException
     *
     * <code>clazz</code> <b>must</b> be provided; cannot be
     * <code>null</code>.
     */
    public boolean exactClassMatch(Class clazz) throws IllegalStateException, IllegalArgumentException{
        exactClassMatch(clazz, allowPassOnNull)
    }

    /**
     * Run a sanity check to ensure the entity is a specific
     * <b><code>Class</code></b>.
     *
     * @param   allowPassOnNull
     *
     * Explicitly declare or override the behavior of
     *
     * {@link #allowPassOnNull allowPassOnNull}
     *
     * @param   clazz
     *
     * The <b><code>Class</code></b> to test for.
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
     *
     * @throws  IllegalArgumentException
     *
     * <code>clazz</code> <b>must</b> be provided; cannot be
     * <code>null</code>.
     */
    public boolean exactClassMatch(Class clazz, boolean allowPassOnNull) throws IllegalStateException, IllegalArgumentException {
        checkYourselfBeforeYouWreckYourself()

        if (clazz == null) {
            throw new IllegalArgumentException("A Class (clazz) must be specified for exactClass sanity check.")
        }

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

    /**
     * Run a sanity check to ensure the entity is <i>or</i> extends
     * a specific <b><code>Class</code></b>.
     *
     * <p>Default behavior specified by {@link #allowPassOnNull allowPassOnNull}</p>
     *
     * @param   clazz
     *
     * The <b><code>Class</code></b> to test for.
     *
     * @return
     *
     * <code>true</code> if the test passes, <code>false</code>
     * otherwise.
     *
     * @throws  IllegalArgumentException
     *
     * <code>clazz</code> <b>must</b> be provided; cannot be
     * <code>null</code>.
     *
     * @throws  IllegalStateException
     *
     * An entity must be declared via a <b><code>check()</code></b>
     * invokation before any sanity checks can be performed.
     *
     * @throws  IllegalArgumentException
     *
     * <code>clazz</code> <b>must</b> be provided; cannot be
     * <code>null</code>.
     */
    public boolean classMatch(Class clazz) throws IllegalStateException, IllegalArgumentException {
        classMatch(clazz, allowPassOnNull)
    }

    /**
     * Run a sanity check to ensure the entity is <i>or</i> extends
     * a specific <b><code>Class</code></b>.
     *
     * @param   allowPassOnNull
     *
     * Explicitly declare or override the behavior of
     *
     * {@link #allowPassOnNull allowPassOnNull}
     *
     * @param   clazz
     *
     * The <b><code>Class</code></b> to test for.
     *
     * @return
     *
     * <code>true</code> if the test passes, <code>false</code>
     * otherwise.
     *
     * @throws  IllegalArgumentException
     *
     * <code>clazz</code> <b>must</b> be provided; cannot be
     * <code>null</code>.
     *
     * @throws  IllegalStateException
     *
     * An entity must be declared via a <b><code>check()</code></b>
     * invokation before any sanity checks can be performed.
     *
     * @throws  IllegalArgumentException
     *
     * <code>clazz</code> <b>must</b> be provided; cannot be
     * <code>null</code>.
     */
    public boolean classMatch(Class clazz, boolean allowPassOnNull) throws IllegalStateException, IllegalArgumentException {
        checkYourselfBeforeYouWreckYourself()

        if (clazz == null) {
            throw new IllegalArgumentException("A Class (clazz) must be specified for exactClass sanity check.")
        }

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
