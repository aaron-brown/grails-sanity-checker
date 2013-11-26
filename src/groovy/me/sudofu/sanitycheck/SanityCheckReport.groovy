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

    protected UUID id

    protected String name

    protected String classification

    protected List<Map> report

    public SanityCheckReport(String name, String classification) {
        this.id = UUID.randomUUID()
        this.name = name
        this.classification = classification
        this.report = []
    }

    private setReport(List<Map> arg) {

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

    public boolean countPasses() {
        return getPasses().size()
    }

    public boolean countFailures() {
        return getFailures().size()
    }

    public List<Map> getPasses() {
        return report.findAll { it.result == Result.PASS }
    }

    public List<Map> getFailures() {
        return report.findAll { it.result == Result.FAIL }
    }
}
