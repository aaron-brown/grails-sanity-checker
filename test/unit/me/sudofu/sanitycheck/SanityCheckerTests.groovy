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

    enum SampleUserClass {
        SAMPLE_USER_CLASS
    }

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

        def fail

        fail = shouldFail(IllegalArgumentException) {
            checker.check(null, "bar")
        }

        fail = shouldFail(IllegalArgumentException) {
            checker.check(null, "")
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

    void testIsNotEmptyOnIncompatibleEntity() {
        assertFalse(new SanityChecker().check("foo", 12345).isNotEmpty())
    }

    void testIsNotEmptyPassIfNullCases() {
        SanityChecker checker = new SanityChecker()

        checker.check("foo", null)
        assertTrue(checker.allowPassOnNull().isNotEmpty())
        assertTrue(checker.disallowPassOnNull().isNotEmpty(true))

        checker.check("foo", "")
        assertFalse(checker.allowPassOnNull().isNotEmpty())
        assertFalse(checker.disallowPassOnNull().isNotEmpty(true))

        checker.check("foo", '')
        assertFalse(checker.allowPassOnNull().isNotEmpty())
        assertFalse(checker.disallowPassOnNull().isNotEmpty(true))

        checker.check("foo", """""")
        assertFalse(checker.allowPassOnNull().isNotEmpty())
        assertFalse(checker.disallowPassOnNull().isNotEmpty(true))

        checker.check("foo", '''''')
        assertFalse(checker.allowPassOnNull().isNotEmpty())
        assertFalse(checker.disallowPassOnNull().isNotEmpty(true))

        checker.check("foo", [])
        assertFalse(checker.allowPassOnNull().isNotEmpty())
        assertFalse(checker.disallowPassOnNull().isNotEmpty(true))

        checker.check("foo", [:])
        assertFalse(checker.allowPassOnNull().isNotEmpty())
        assertFalse(checker.disallowPassOnNull().isNotEmpty(true))
    }

    void testIsNotEmpty() {
        assertTrue(new SanityChecker().check("foo", " ").isNotEmpty())
        assertTrue(new SanityChecker().check("foo", ' ').isNotEmpty())

        assertTrue(new SanityChecker().check("foo", """ """).isNotEmpty())
        assertTrue(new SanityChecker().check("foo", ''' ''').isNotEmpty())

        assertTrue(new SanityChecker().check("foo", [null]).isNotEmpty())
        assertTrue(new SanityChecker().check("foo", ["a"]).isNotEmpty())

        assertTrue(new SanityChecker().check("foo", [a: null]).isNotEmpty())
        assertTrue(new SanityChecker().check("foo", [a: "a"]).isNotEmpty())

        assertFalse(new SanityChecker().check("foo", null).isNotEmpty())
        assertFalse(new SanityChecker().check("foo", "").isNotEmpty())
        assertFalse(new SanityChecker().check("foo", '').isNotEmpty())
        assertFalse(new SanityChecker().check("foo", """""").isNotEmpty())
        assertFalse(new SanityChecker().check("foo", '''''').isNotEmpty())
        assertFalse(new SanityChecker().check("foo", []).isNotEmpty())
        assertFalse(new SanityChecker().check("foo", [:]).isNotEmpty())
    }

    void testRespondsToPassIfNullCases() {
        SanityChecker checker = new SanityChecker()

        checker.check("foo", null)
        assertTrue(checker.allowPassOnNull().respondsTo('foo'))
        assertTrue(checker.disallowPassOnNull().respondsTo('foo', true))

        checker.check("foo", UUID.randomUUID())
        assertFalse(checker.allowPassOnNull().respondsTo('isEmpty'))
        assertFalse(checker.disallowPassOnNull().respondsTo('isEmpty', true))
    }

    void testRespondsTo() {

        assertTrue(new SanityChecker().check("foo", 1).respondsTo('byteValue'))
        assertFalse(new SanityChecker().check("foo", 1).respondsTo('isEmpty'))

        assertFalse(new SanityChecker().check("foo", null).respondsTo('isEmpty'))

        def fail

        fail = shouldFail(IllegalArgumentException) {
            new SanityChecker().check("foo", null).respondsTo(null)
        }

        fail = shouldFail(IllegalArgumentException) {
            new SanityChecker().check("foo", null).respondsTo("")
        }
    }

    void testExactClassMatchPassIfNullCases() {
        SanityChecker checker = new SanityChecker()

        checker.check("foo", null)
        assertTrue(checker.allowPassOnNull().exactClassMatch(Class))
        assertTrue(checker.disallowPassOnNull().exactClassMatch(Class, true))

        checker.check("foo", 1)
        assertFalse(checker.allowPassOnNull().exactClassMatch(String))
        assertFalse(checker.disallowPassOnNull().exactClassMatch(String, true))
    }

    void testExactClassMatch() {

        assertTrue(new SanityChecker().check("foo", SampleUserClass.SAMPLE_USER_CLASS).exactClassMatch(SampleUserClass))

        assertFalse(new SanityChecker().check("foo", null).exactClassMatch(SampleUserClass))

        assertFalse(new SanityChecker().check("foo", 1).exactClassMatch(SampleUserClass))

        def fail

        fail = shouldFail(IllegalArgumentException) {
            new SanityChecker().check("foo", null).exactClassMatch(null)
        }

        fail = shouldFail(IllegalArgumentException) {
            new SanityChecker().check("foo", null).exactClassMatch(null, true)
        }
    }

    void testClassMatchPassIfNullCases() {
        SanityChecker checker = new SanityChecker()

        checker.check("foo", null)
        assertTrue(checker.allowPassOnNull().classMatch(Class))
        assertTrue(checker.disallowPassOnNull().classMatch(Class, true))

        checker.check("foo", 1)
        assertFalse(checker.allowPassOnNull().classMatch(String))
        assertFalse(checker.disallowPassOnNull().classMatch(String, true))
    }

    void testClassMatch() {

        assertTrue(new SanityChecker().check("foo", 1).classMatch(Integer))
        assertTrue(new SanityChecker().check("foo", 1).classMatch(Number))
        assertFalse(new SanityChecker().check("foo", 1).classMatch(String))

        assertFalse(new SanityChecker().check("foo", null).classMatch(Integer))

        def fail

        fail = shouldFail(IllegalArgumentException) {
            new SanityChecker().check("foo", null).classMatch(null)
        }

        fail = shouldFail(IllegalArgumentException) {
            new SanityChecker().check("foo", null).classMatch(null, true)
        }
    }

    void testRunChecks01() {
        SanityChecker checker = new SanityChecker()

        assertFalse(checker.hasPasses())
        assertFalse(checker.hasFailures())

        assertEquals(0, checker.countPasses())
        assertEquals(0, checker.countFailures())

        checker.runChecks {

            check("foo", 1)

            // Implicit isNotNull
            exactClassMatch(Integer)

            // Implicit isNotNull
            classMatch(Number)

            check("bar", null)

            // Implict isNotNull
            exactClassMatch(Boolean)

            check("baz", [1, 2, 3, 4])

            // Implicit isNotNull
            // Implicit respondsTo
            isNotEmpty()

            check("bat", "")

            // Implicit isNotNull
            exactClassMatch(String)

            // Implicit isNotNull
            // Implicit respondsTo
            isNotEmpty()
        }

        assertTrue(checker.hasPasses())
        assertTrue(checker.hasFailures())

        assertEquals(11, checker.countPasses())
        assertEquals(3, checker.countFailures())
    }

    void testRunChecks02() {
        SanityChecker checker = new SanityChecker()

        assertFalse(checker.hasPasses())
        assertFalse(checker.hasFailures())

        assertEquals(0, checker.countPasses())
        assertEquals(0, checker.countFailures())

        checker.runChecks {

            allowPassOnNull()

            check("foo", 1)

            // Implicit isNotNull
            exactClassMatch(Integer)

            // Implicit isNotNull
            classMatch(Number)

            check("bar", null)

            // Passes because null.
            exactClassMatch(Boolean)

            check("baz", [1, 2, 3, 4])

            // Implicit isNotNull
            // Implicit respondsTo
            isNotEmpty()

            check("bat", "")

            // Implicit isNotNull
            exactClassMatch(String)

            // Implicit isNotNull
            // Implicit respondsTo
            isNotEmpty()
        }

        assertTrue(checker.hasPasses())
        assertTrue(checker.hasFailures())

        assertEquals(12, checker.countPasses())
        assertEquals(1, checker.countFailures())
    }

    void testCheckYourselfBeforeYouWreckYourself() {
        def fail

        fail = shouldFail(IllegalStateException) {
            new SanityChecker().isNotNull()
        }

        fail = shouldFail(IllegalStateException) {
            new SanityChecker().isNotEmpty()
        }

        fail = shouldFail(IllegalStateException) {
            new SanityChecker().isNotEmpty(false)
        }

        fail = shouldFail(IllegalStateException) {
            new SanityChecker().respondsTo('foo')
        }

        fail = shouldFail(IllegalStateException) {
            new SanityChecker().respondsTo('foo', false)
        }

        fail = shouldFail(IllegalStateException) {
            new SanityChecker().exactClassMatch(String)
        }

        fail = shouldFail(IllegalStateException) {
            new SanityChecker().exactClassMatch(String, false)
        }

        fail = shouldFail(IllegalStateException) {
            new SanityChecker().classMatch(Number)
        }

        fail = shouldFail(IllegalStateException) {
            new SanityChecker().classMatch(Number, false)
        }
    }
}
