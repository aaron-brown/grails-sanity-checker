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

        assertEquals("foo", checker.report.name)
        assertEquals("entity", checker.report.classification)

        checker.check("bar", "baz")
        assertEquals("bar", checker.name)
        assertEquals("baz", checker.entity)
        assertEquals("entity", checker.classification)

        assertEquals("bar", checker.report.name)
        assertEquals("entity", checker.report.classification)

        checker.check("foo", "bar", "baz")
        assertEquals("foo", checker.name)
        assertEquals("bar", checker.entity)
        assertEquals("baz", checker.classification)

        assertEquals("foo", checker.report.name)
        assertEquals("baz", checker.report.classification)

        def fail = shouldFail(IllegalArgumentException) {
            checker.check(null, "bar")
        }
    }

    void testUnmodifiableSetters() {
        SanityChecker checker = new SanityChecker()

        checker.name = "apple"
        checker.entity = "banana"
        checker.classification = "cucumber"

        checker.report = new SanityCheckReport().reportingOn('foo', 'bar')

        assertNull(checker.name)
        assertNull(checker.entity)
        assertNull(checker.classification)

        assertFalse('foo' == checker.report.name)
        assertFalse('bar' == checker.report.classification)

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

    void testIsBooleanPassIfNullCases() {
        SanityChecker checker = new SanityChecker()

        checker.check("foo", null)
        assertTrue(checker.allowPassOnNull().isBoolean())
        assertTrue(checker.disallowPassOnNull().isBoolean(true))

        checker.check("foo", 1)
        assertFalse(checker.allowPassOnNull().isBoolean())
        assertFalse(checker.disallowPassOnNull().isBoolean(true))
    }

    void testIsBoolean() {

        assertTrue(new SanityChecker().check("foo", true).isBoolean())
        assertTrue(new SanityChecker().check("foo", false).isBoolean())
        assertTrue(new SanityChecker().check("foo", 1.asBoolean()).isBoolean())
        assertTrue(new SanityChecker().check("foo", 0.asBoolean()).isBoolean())

        assertFalse(new SanityChecker().check("foo", null).isBoolean())

        assertFalse(new SanityChecker().check("foo", 0).isBoolean())
        assertFalse(new SanityChecker().check("foo", 1).isBoolean())

        assertFalse(new SanityChecker().check("foo", "true").isBoolean())
        assertFalse(new SanityChecker().check("foo", "false").isBoolean())

        assertFalse(new SanityChecker().check("foo", "${true}").isBoolean())
        assertFalse(new SanityChecker().check("foo", "${false}").isBoolean())
    }

    void testIsStringPassIfNullCases() {
        SanityChecker checker = new SanityChecker()

        checker.check("foo", null)
        assertTrue(checker.allowPassOnNull().isString())
        assertTrue(checker.disallowPassOnNull().isString(true))

        checker.check("foo", "${null}")
        assertFalse(checker.allowPassOnNull().isString())
        assertFalse(checker.disallowPassOnNull().isString(true))

        checker.check("foo", "${}")
        assertFalse(checker.allowPassOnNull().isString())
        assertFalse(checker.disallowPassOnNull().isString(true))
    }

    void testIsString() {

        assertTrue(new SanityChecker().check("foo", '').isString())
        assertTrue(new SanityChecker().check("foo", "").isString())
        assertTrue(new SanityChecker().check("foo", '''''').isString())
        assertTrue(new SanityChecker().check("foo", """""").isString())
        assertTrue(new SanityChecker().check("foo", 1 as String).isString())
        assertTrue(new SanityChecker().check("foo", "${1}" as String).isString())
        assertTrue(new SanityChecker().check("foo", "${null}" as String).isString())

        assertFalse(new SanityChecker().check("foo", null).isString())
        assertFalse(new SanityChecker().check("foo", 1).isString())
        assertFalse(new SanityChecker().check("foo", "${1}").isString())
        assertFalse(new SanityChecker().check("foo", "${null}").isString())
        assertFalse(new SanityChecker().check("foo", "${}").isString())
    }

    void testIsNumberPassIfNullCases() {
        SanityChecker checker = new SanityChecker()

        checker.check("foo", null)
        assertTrue(checker.allowPassOnNull().isNumber())
        assertTrue(checker.disallowPassOnNull().isNumber(true))

        checker.check("foo", "1")
        assertFalse(checker.allowPassOnNull().isNumber())
        assertFalse(checker.disallowPassOnNull().isNumber(true))
    }

    void testIsNumber() {
        assertTrue(new SanityChecker().check("foo", 1).isNumber())
        assertTrue(new SanityChecker().check("foo", 1I).isNumber())
        assertTrue(new SanityChecker().check("foo", 1L).isNumber())
        assertTrue(new SanityChecker().check("foo", 1G).isNumber())
        assertTrue(new SanityChecker().check("foo", 1D).isNumber())
        assertTrue(new SanityChecker().check("foo", 1F).isNumber())
        assertTrue(new SanityChecker().check("foo", 1.0).isNumber())
        assertTrue(new SanityChecker().check("foo", 1.0G).isNumber())
        assertTrue(new SanityChecker().check("foo", 1.0D).isNumber())
        assertTrue(new SanityChecker().check("foo", 1.0F).isNumber())
        assertTrue(new SanityChecker().check("foo", 1E10).isNumber())
        assertTrue(new SanityChecker().check("foo", 1E10G).isNumber())
        assertTrue(new SanityChecker().check("foo", 1E10D).isNumber())
        assertTrue(new SanityChecker().check("foo", 1E10F).isNumber())
        assertTrue(new SanityChecker().check("foo", 1.0E10).isNumber())
        assertTrue(new SanityChecker().check("foo", 1.0E10G).isNumber())
        assertTrue(new SanityChecker().check("foo", 1.0E10D).isNumber())
        assertTrue(new SanityChecker().check("foo", 1.0E10F).isNumber())
        assertTrue(new SanityChecker().check("foo", 01).isNumber())
        assertTrue(new SanityChecker().check("foo", 01I).isNumber())
        assertTrue(new SanityChecker().check("foo", 01L).isNumber())
        assertTrue(new SanityChecker().check("foo", 01G).isNumber())
        assertTrue(new SanityChecker().check("foo", 0x1).isNumber())
        assertTrue(new SanityChecker().check("foo", 0x1I).isNumber())
        assertTrue(new SanityChecker().check("foo", 0x1L).isNumber())
        assertTrue(new SanityChecker().check("foo", 0x1G).isNumber())

        assertFalse(new SanityChecker().check("foo", null).isNumber())
        assertFalse(new SanityChecker().check("foo", "1").isNumber())
        assertFalse(new SanityChecker().check("foo", "${1}").isNumber())
    }

    void testIsIntegerPassIfNullCases() {
        SanityChecker checker = new SanityChecker()

        checker.check("foo", null)
        assertTrue(checker.allowPassOnNull().isInteger())
        assertTrue(checker.disallowPassOnNull().isInteger(true))

        checker.check("foo", "1")
        assertFalse(checker.allowPassOnNull().isInteger())
        assertFalse(checker.disallowPassOnNull().isInteger(true))
    }

    void testIsInteger() {
        assertTrue(new SanityChecker().check("foo", 1).isInteger())
        assertTrue(new SanityChecker().check("foo", 1I).isInteger())
        assertTrue(new SanityChecker().check("foo", 01).isInteger())
        assertTrue(new SanityChecker().check("foo", 01I).isInteger())
        assertTrue(new SanityChecker().check("foo", 0x1).isInteger())
        assertTrue(new SanityChecker().check("foo", 0x1I).isInteger())

        assertFalse(new SanityChecker().check("foo", null).isInteger())
        assertFalse(new SanityChecker().check("foo", 1L).isInteger())
        assertFalse(new SanityChecker().check("foo", 1.0).isInteger())
        assertFalse(new SanityChecker().check("foo", Long.MAX_VALUE).isInteger())
        assertFalse(new SanityChecker().check("foo", 9223372036854775807).isInteger())
        assertFalse(new SanityChecker().check("foo", "1").isInteger())
        assertFalse(new SanityChecker().check("foo", "${1}").isInteger())
    }

    void testIsLongPassIfNullCases() {
        SanityChecker checker = new SanityChecker()

        checker.check("foo", null)
        assertTrue(checker.allowPassOnNull().isLong())
        assertTrue(checker.disallowPassOnNull().isLong(true))

        checker.check("foo", "1")
        assertFalse(checker.allowPassOnNull().isLong())
        assertFalse(checker.disallowPassOnNull().isLong(true))
    }

    void testIsLong() {

        assertTrue(new SanityChecker().check("foo", 1L).isLong())
        assertTrue(new SanityChecker().check("foo", 01L).isLong())
        assertTrue(new SanityChecker().check("foo", 0x1L).isLong())

        assertFalse(new SanityChecker().check("foo", null).isLong())
        assertFalse(new SanityChecker().check("foo", 1).isLong())
        assertFalse(new SanityChecker().check("foo", 1.0).isLong())
        assertFalse(new SanityChecker().check("foo", Integer.MAX_VALUE).isLong())
        assertFalse(new SanityChecker().check("foo", "1L").isLong())
        assertFalse(new SanityChecker().check("foo", "${1L}").isLong())
    }

    void testIsBigDecimalPassIfNullCases() {
        SanityChecker checker = new SanityChecker()

        checker.check("foo", null)
        assertTrue(checker.allowPassOnNull().isBigDecimal())
        assertTrue(checker.disallowPassOnNull().isBigDecimal(true))

        checker.check("foo", "1")
        assertFalse(checker.allowPassOnNull().isBigDecimal())
        assertFalse(checker.disallowPassOnNull().isBigDecimal(true))
    }

    void testIsBigDecimal() {

        assertTrue(new SanityChecker().check("foo", 1.0).isBigDecimal())
        assertTrue(new SanityChecker().check("foo", 1.0G).isBigDecimal())

        assertFalse(new SanityChecker().check("foo", null).isBigDecimal())
        assertFalse(new SanityChecker().check("foo", 1).isBigDecimal())
        assertFalse(new SanityChecker().check("foo", 1G).isBigDecimal())
        assertFalse(new SanityChecker().check("foo", Double.MAX_VALUE).isBigDecimal())
        assertFalse(new SanityChecker().check("foo", "1.0G").isBigDecimal())
        assertFalse(new SanityChecker().check("foo", "${1.0G}").isBigDecimal())
    }

    void testIsDoublePassIfNullCases() {
        SanityChecker checker = new SanityChecker()

        checker.check("foo", null)
        assertTrue(checker.allowPassOnNull().isDouble())
        assertTrue(checker.disallowPassOnNull().isDouble(true))

        checker.check("foo", "1")
        assertFalse(checker.allowPassOnNull().isDouble())
        assertFalse(checker.disallowPassOnNull().isDouble(true))
    }

    void testIsDouble() {

        assertTrue(new SanityChecker().check("foo", 1D).isDouble())
        assertTrue(new SanityChecker().check("foo", 1.0D).isDouble())

        assertFalse(new SanityChecker().check("foo", null).isDouble())
        assertFalse(new SanityChecker().check("foo", 1).isDouble())
        assertFalse(new SanityChecker().check("foo", 1.0).isDouble())
        assertFalse(new SanityChecker().check("foo", "1D").isDouble())
        assertFalse(new SanityChecker().check("foo", "1.0D").isDouble())
        assertFalse(new SanityChecker().check("foo", "${1D}").isDouble())
        assertFalse(new SanityChecker().check("foo", "${1.0D}").isDouble())
    }

    void testIsListPassIfNullCases() {
        SanityChecker checker = new SanityChecker()

        checker.check("foo", null)
        assertTrue(checker.allowPassOnNull().isList())
        assertTrue(checker.disallowPassOnNull().isList(true))

        checker.check("foo", "1")
        assertFalse(checker.allowPassOnNull().isList())
        assertFalse(checker.disallowPassOnNull().isList(true))
    }

    void testIsList() {

        assertTrue(new SanityChecker().check("foo", []).isList())
        assertTrue(new SanityChecker().check("foo", [1, 2, 3, "blue", "moon"]).isList())
        assertTrue(new SanityChecker().check("foo", 1..10).isList())
        assertTrue(new SanityChecker().check("foo", 'a'..'z').isList())
        assertTrue(new SanityChecker().check("foo", [[a: 'apple'],[a: 'apricot']].a).isList())

        assertFalse(new SanityChecker().check("foo", null).isList())
        assertFalse(new SanityChecker().check("foo", 1..10 as int[]).isList())
        assertFalse(new SanityChecker().check("foo", 'a'..'z' as String[]).isList())
        assertFalse(new SanityChecker().check("foo", 1).isList())
        assertFalse(new SanityChecker().check("foo", [:]).isList())
    }

    void testIsMapPassIfNullCases() {
        SanityChecker checker = new SanityChecker()

        checker.check("foo", null)
        assertTrue(checker.allowPassOnNull().isMap())
        assertTrue(checker.disallowPassOnNull().isMap(true))

        checker.check("foo", "1")
        assertFalse(checker.allowPassOnNull().isMap())
        assertFalse(checker.disallowPassOnNull().isMap(true))
    }

    void testIsMap() {

        assertTrue(new SanityChecker().check("foo", [:]).isMap())
        assertTrue(new SanityChecker().check("foo", [a: 'apple', b: 'banana']).isMap())

        assertFalse(new SanityChecker().check("foo", null).isMap())
        assertFalse(new SanityChecker().check("foo", 1).isMap())
        assertFalse(new SanityChecker().check("foo", 1..10 as int[]).isMap())
        assertFalse(new SanityChecker().check("foo", []).isMap())
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
            isInteger()

            check("bar", null)

            // Implicit isNotNull
            isBoolean()

            check("baz", [1, 2, 3, 4])

            // Implicit isNotNull
            // Implicit respondsTo
            isNotEmpty()

            check("bat", "")

            // Implicit isNotNull
            isString()

            // Implicit isNotNull
            // Implicit respondsTo
            isNotEmpty()
        }

        assertTrue(checker.hasPasses())
        assertTrue(checker.hasFailures())

        assertEquals(9, checker.countPasses())
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
            isInteger()

            check("bar", null)

            // Implicit isNotNull
            isBoolean()

            check("baz", [1, 2, 3, 4])

            // Implicit isNotNull
            // Implicit respondsTo
            isNotEmpty()

            check("bat", "")

            // Implicit isNotNull
            isString()

            // Implicit isNotNull
            // Implicit respondsTo
            isNotEmpty()
        }

        assertTrue(checker.hasPasses())
        assertTrue(checker.hasFailures())

        assertEquals(10, checker.countPasses())
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
            new SanityChecker().isBoolean()
        }

        fail = shouldFail(IllegalStateException) {
            new SanityChecker().isBoolean(false)
        }

        fail = shouldFail(IllegalStateException) {
            new SanityChecker().isString()
        }

        fail = shouldFail(IllegalStateException) {
            new SanityChecker().isString(false)
        }

        fail = shouldFail(IllegalStateException) {
            new SanityChecker().isNumber()
        }

        fail = shouldFail(IllegalStateException) {
            new SanityChecker().isNumber(false)
        }

        fail = shouldFail(IllegalStateException) {
            new SanityChecker().isInteger()
        }

        fail = shouldFail(IllegalStateException) {
            new SanityChecker().isInteger(false)
        }

        fail = shouldFail(IllegalStateException) {
            new SanityChecker().isLong()
        }

        fail = shouldFail(IllegalStateException) {
            new SanityChecker().isLong(false)
        }

        fail = shouldFail(IllegalStateException) {
            new SanityChecker().isBigDecimal()
        }

        fail = shouldFail(IllegalStateException) {
            new SanityChecker().isBigDecimal(false)
        }

        fail = shouldFail(IllegalStateException) {
            new SanityChecker().isDouble()
        }

        fail = shouldFail(IllegalStateException) {
            new SanityChecker().isDouble(false)
        }

        fail = shouldFail(IllegalStateException) {
            new SanityChecker().isList()
        }

        fail = shouldFail(IllegalStateException) {
            new SanityChecker().isList(false)
        }

        fail = shouldFail(IllegalStateException) {
            new SanityChecker().isMap()
        }

        fail = shouldFail(IllegalStateException) {
            new SanityChecker().isMap(false)
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
