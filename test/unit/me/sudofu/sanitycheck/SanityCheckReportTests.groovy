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
import me.sudofu.sanitycheck.SanityCheckReport.Result

import static org.junit.Assert.*

import grails.test.mixin.*
import grails.test.mixin.support.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class SanityCheckReportTests {

    void setUp() {
        // Setup logic here
    }

    void tearDown() {
        // Tear down logic here
    }

    void testConstructor() {
        SanityCheckReport report = new SanityCheckReport()

        assertEquals([], report.report)

        assertFalse(report.hasPasses())
        assertFalse(report.hasFailures())

        assertEquals(0, report.countPasses())
        assertEquals(0, report.countFailures())

        assertEquals([], report.passes)
        assertEquals([], report.failures)
    }

    void testUnmodifiableSetters() {
        SanityCheckReport report = new SanityCheckReport()

        report.report = [1, 2, 3,]

        assertFalse(report.hasPasses())
        assertFalse(report.hasFailures())

        assertEquals(0, report.countPasses())
        assertEquals(0, report.countFailures())

        assertEquals([], report.passes)
        assertEquals([], report.failures)
    }

    void testPass() {
        SanityCheckReport report = new SanityCheckReport()

        report.pass('foo', 'param', 'bar', 'baz')

        assertTrue(report.hasPasses())
        assertFalse(report.hasFailures())

        assertEquals(1, report.countPasses())
        assertEquals(0, report.countFailures())

        assertEquals([], report.failures)

        Map entry = report.passes[0]

        assertEquals(Result.PASS, entry.result)
        assertEquals('foo', entry.name)
        assertEquals('param', entry.classification)
        assertEquals('bar', entry.check)
        assertEquals('baz', entry.checkDescription)
    }

    void testFail() {
        SanityCheckReport report = new SanityCheckReport()

        report.fail('foo', 'param', 'bar', 'baz')

        assertFalse(report.hasPasses())
        assertTrue(report.hasFailures())

        assertEquals(0, report.countPasses())
        assertEquals(1, report.countFailures())

        assertEquals([], report.passes)

        Map entry = report.failures[0]

        assertEquals(Result.FAIL, entry.result)
        assertEquals('foo', entry.name)
        assertEquals('param', entry.classification)
        assertEquals('bar', entry.check)
        assertEquals('baz', entry.checkDescription)
    }

    void testResults() {
        SanityCheckReport report = new SanityCheckReport()

        report.pass('foo', 'param', 'bar', 'baz')
        report.fail('foo', 'param', 'bar', 'baz')

        assertEquals(2, report.countResults())

        assertTrue(report.results.any { it == report.passes[0] })
        assertTrue(report.results.any { it == report.failures[0] })
    }
}
