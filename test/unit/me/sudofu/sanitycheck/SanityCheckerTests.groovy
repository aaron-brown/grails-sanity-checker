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
import me.sudofu.sanitycheck.SanityCheckReport

import static org.junit.Assert.*

import grails.test.mixin.*
import grails.test.mixin.support.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class SanityCheckerTests {

    void setUp() {
        // Setup logic here
    }

    void tearDown() {
        // Tear down logic here
    }

    void testConstructor01() {
        SanityChecker checker = new SanityChecker()

        assertNull(checker.name)
        assertNull(checker.entity)
        assertNull(checker.classification)

        assertFalse(checker.allowPassOnNull)

        assertNotNull(checker.report)

        assertFalse(checker.hasPasses())
        assertFalse(checker.hasFailures())

        assertEquals(0, checker.countPasses())
        assertEquals(0, checker.countFailures())

        assertEquals([], checker.passes)
        assertEquals([], checker.failures)
    }

    void testConstructor02() {
        SanityChecker checker = new SanityChecker(true)

        assertNull(checker.name)
        assertNull(checker.entity)
        assertNull(checker.classification)

        assertTrue(checker.allowPassOnNull)

        assertNotNull(checker.report)

        assertFalse(checker.hasPasses())
        assertFalse(checker.hasFailures())

        assertEquals(0, checker.countPasses())
        assertEquals(0, checker.countFailures())

        assertEquals([], checker.passes)
        assertEquals([], checker.failures)
    }

    void testCheck() {
        SanityChecker checker = new SanityChecker()

        checker.check("foo", "bar")
        assertEquals("foo", checker.name)
        assertEquals("bar", checker.entity)
        assertEquals("entity", checker.classification)

        checker.check("bar", "baz")
        assertEquals("bar", checker.name)
        assertEquals("baz", checker.entity)
        assertEquals("entity", checker.classification)

        checker.check("foo", "bar", "baz")
        assertEquals("foo", checker.name)
        assertEquals("bar", checker.entity)
        assertEquals("baz", checker.classification)

        def fail = shouldFail(IllegalArgumentException) {
            checker.check(null, "bar")
        }
    }

    void testUnmodifiableSetters() {
        SanityChecker checker = new SanityChecker()

        checker.name = "apple"
        checker.entity = "banana"
        checker.classification = "cucumber"

        checker.report = new SanityCheckReport().pass('foo', 'bar', 'baz', 'bat')

        assertNull(checker.name)
        assertNull(checker.entity)
        assertNull(checker.classification)

        assertFalse(checker.hasPasses())
        assertFalse(checker.hasFailures())

        assertEquals(0, checker.countPasses())
        assertEquals(0, checker.countFailures())

        assertEquals([], checker.passes)
        assertEquals([], checker.failures)
    }

    void testAllowPassOnNull() {
        assertFalse(new SanityChecker().allowPassOnNull)
        assertFalse(new SanityChecker(false).allowPassOnNull)
        assertTrue(new SanityChecker(true).allowPassOnNull)

        SanityChecker checker = new SanityChecker()

        checker.allowPassOnNull = true
        assertTrue(checker.allowPassOnNull)

        checker.allowPassOnNull = false
        assertFalse(checker.allowPassOnNull)

        checker.allowPassOnNull()
        assertTrue(checker.allowPassOnNull)

        checker.disallowPassOnNull()
        assertFalse(checker.allowPassOnNull)
    }

    void testIsNotNull() {
        SanityChecker checker = new SanityChecker()

        checker.check("foo", "hello")
        assertTrue(checker.isNotNull())

        checker.check("foo", null)

        assertFalse(checker.isNotNull())
        assertFalse(checker.allowPassOnNull().isNotNull())
    }
}
