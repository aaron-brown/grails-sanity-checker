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

    protected UUID currentReport

    protected List<SanityCheckReport> reports

    protected boolean suppressPasses

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
        this(name, entity, null)
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
        this.name = name ?: entity.getClass().name
        this.entity = entity
        this.classification = classification ?: 'entity'
        this.allowPassOnNull = false

        SanityCheckReport newReport = new SanityCheckReport(this.name, this.classification)

        this.reports = [newReport]
        this.currentReport = newReport.id
        this.suppressPasses = true
    }

    private setName(String arg) {

    }

    private setEntity(Object arg) {

    }

    private setClassification(String arg) {

    }

    public checkOn(String name, Object entity) {
        checkOn(name, entity, null)
    }

    public checkOn(String name, Object entity, String classification) {
        this.name = name ?: entity.getClass().name
        this.entity = entity
        this.classification = classification ?: 'entity'

        SanityCheckReport newReport = new SanityCheckReport(this.name, this.classification)

        this.reports << newReport
        this.currentReport = newReport.id
    }

    private void setCurrentReport(UUID arg) {

    }

    private void setReports(List<SanityCheckReport> arg) {

    }

    protected void pass(String check, String checkDescription) {
        if (! suppressPasses) {
            reports.find { it.id == currentReport }.pass(check, checkDescription)
        }
    }

    protected void fail(String check, String checkDescription) {
        reports.find { it.id == currentReport }.fail(check, checkDescription)
    }

    public boolean hasPasses() {
        return reports.any { it.hasPasses() }
    }

    public boolean hasFailures() {
        return reports.any { it.hasFailures() }
    }

    public int countPasses() {
        return getPasses().size()
    }

    public int countFailures() {
        return getFailures().size()
    }

    public List<Map> getPasses() {
        return reports*.getPasses()
    }

    public List<Map> getFailures() {
        return reports*.getFailures()
    }

    public void checkFor() {
        Closure runClosure = closure.clone()

        runClosure.delegate = this.newInstance(name, entity)
        runClosure.resolveStrategy = Closure.DELEGATE_FIRST

        runClosure()
    }

    public boolean isNotNull() {
        if (entity == null) {
            fail('isNotNull', "Cannot be null")
            return false
        }

        pass('isNotNull', "Cannot be null")
        return true
    }

    public boolean isNotEmpty() {
        isNotEmpty(allowPassOnNull)
    }

    public boolean isNotEmpty(boolean allowPassOnNull) {
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

    public boolean isBoolean() {
        isBoolean(allowPassOnNull)
    }

    public boolean isBoolean(boolean allowPassOnNull) {
        exactClassMatch(Boolean, allowPassOnNull)
    }

    public boolean isString() {
        isString(allowPassOnNull)
    }

    public boolean isString(boolean allowPassOnNull) {
        exactClassMatch(String, allowPassOnNull)
    }

    public boolean isNumber() {
        isNumber(allowPassOnNull)
    }

    public boolean isNumber(boolean allowPassOnNull) {
        classMatch(Number, allowPassOnNull)
    }

    public boolean isInteger() {
        isInteger(allowPassOnNull)
    }

    public boolean isInteger(boolean allowPassOnNull) {
        exactClassMatch(Integer, allowPassOnNull)
    }

    public boolean isLong() {
        isLong(allowPassOnNull)
    }

    public boolean isLong(boolean allowPassOnNull) {
        exactClassMatch(Long, allowPassOnNull)
    }

    public boolean isBigDecimal() {
        isBigDecimal(allowPassOnNull)
    }

    public boolean isBigDecimal(boolean allowPassOnNull) {
        exactClassMatch(BigDecimal, allowPassOnNull)
    }

    public boolean isDouble() {
        isDouble(allowPassOnNull)
    }

    public boolean isDouble(boolean allowPassOnNull) {
        exactClassMatch(Double, allowPassOnNull)
    }

    public boolean isList() {
        isList(allowPassOnNull)
    }

    public boolean isList(boolean allowPassOnNull) {
        classMatch(List, allowPassOnNull)
    }

    public boolean isMap() {
        isMap(allowPassOnNull)
    }

    public boolean isMap(boolean allowPassOnNull) {
        classMatch(Map, allowPassOnNull)
    }

    public boolean respondsTo(String method) {
        respondsTo(method, allowPassOnNull)
    }

    public boolean respondsTo(String method, boolean allowPassOnNull) {
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

    public boolean exactClassMatch(Class clazz) {
        exactClassMatch(clazz, allowPassOnNull)
    }

    public boolean exactClassMatch(Class clazz, boolean allowPassOnNull) {
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

    public boolean classMatch(Class clazz) {
        classMatch(clazz, allowPassOnNull)
    }

    public boolean classMatch(Class clazz, boolean allowPassOnNull) {
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
