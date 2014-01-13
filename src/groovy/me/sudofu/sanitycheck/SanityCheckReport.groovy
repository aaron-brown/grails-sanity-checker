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
 * Reporting structure for {@link me.sudofu.sanitycheck.SanityChecker SanityCheckers}.
 *
 * @author  Aaron Brown
 */
class SanityCheckReport {

    /**
     * Enumeration for possible outcomes of a sanity check.
     */
    public enum Result {
        PASS,
        FAIL
    }

    /**
     * The internal report structure.
     */
    protected List<Map> report

    /**
     * Constructs a <b><code>SanityCheckReport</code></b>.
     */
    public SanityCheckReport() {
        this.report = []
    }

    /**
     * Prevent setting.
     *
     * @param   arg
     *
     * Argument.
     */
    private void setReport(List<Map> arg) {

    }

    /**
     * Use when a sanity check has passed.
     *
     * @param   name
     *
     * The name of the entity being checked (for example, the name of
     * the parameter).
     *
     * @param   classification
     *
     * The classification of the entity that is being checked
     * (for example, parameter).
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
    public void pass(String name, String classification, String check, String checkDescription) {
        report << [
            result: Result.PASS,
            name: name,
            classification: classification,
            check: check,
            checkDescription: checkDescription
        ]
    }

    /**
     * Use when a sanity check has failed.
     *
     * @param   name
     *
     * The name of the entity being checked (for example, the name of
     * the parameter).
     *
     * @param   classification
     *
     * The classification of the entity that is being checked
     * (for example, parameter).
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
    public void fail(String name, String classification, String check, String checkDescription) {
        report << [
            result: Result.FAIL,
            name: name,
            classification: classification,
            check: check,
            checkDescription: checkDescription
        ]
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
        return report.any { it.result == Result.PASS }
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
        return report.any { it.result == Result.FAIL }
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
        return getPasses().size()
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
        return getFailures().size()
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
        return report.findAll { it.result == Result.PASS }
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
        return report.findAll { it.result == Result.FAIL }
    }
}
