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

class SanityCheckReport {
    public enum Result {
        PASS,
        FAIL
    }

    protected String name

    protected String classification

    protected List<Map> report

    public SanityCheckReport() {
        this.name = name
        this.classification = classification
        this.report = []
    }

    private void setName(String arg) {

    }

    private void setClassification(String arg) {

    }

    private void setReport(List<Map> arg) {

    }

    public SanityCheckReport reportingOn(String name, String classification) {
        this.name = name
        this.classification = classification
        return this
    }

    public void pass(String check, String checkDescription) {
        report << [
            result: Result.PASS,
            name: name,
            classification: classification,
            check: check,
            checkDescription: checkDescription
        ]
    }

    public void fail(String check, String checkDescription) {
        report << [
            result: Result.FAIL,
            name: name,
            classification: classification,
            check: check,
            checkDescription: checkDescription
        ]
    }

    public boolean hasPasses() {
        return report.any { it.result == Result.PASS }
    }

    public boolean hasFailures() {
        return report.any { it.result == Result.FAIL }
    }

    public int countPasses() {
        return getPasses().size()
    }

    public int countFailures() {
        return getFailures().size()
    }

    public List<Map> getPasses() {
        return report.findAll { it.result == Result.PASS }
    }

    public List<Map> getFailures() {
        return report.findAll { it.result == Result.FAIL }
    }
}
