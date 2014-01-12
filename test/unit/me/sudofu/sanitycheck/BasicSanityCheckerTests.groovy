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
import me.sudofu.sanitycheck.BasicSanityChecker

import static org.junit.Assert.*

import grails.test.mixin.*
import grails.test.mixin.support.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class BasicSanityCheckerTests {

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
        BasicSanityChecker checker = new BasicSanityChecker()

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
        BasicSanityChecker checker = new BasicSanityChecker(true)

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
        BasicSanityChecker checker = new BasicSanityChecker()

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
        BasicSanityChecker checker = new BasicSanityChecker()

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
        assertFalse(new BasicSanityChecker().allowPassOnNull)
        assertFalse(new BasicSanityChecker(false).allowPassOnNull)
        assertTrue(new BasicSanityChecker(true).allowPassOnNull)

        BasicSanityChecker checker = new BasicSanityChecker()

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
        BasicSanityChecker checker = new BasicSanityChecker()

        checker.check("foo", "hello")
        assertTrue(checker.isNotNull())

        checker.check("foo", null)

        assertFalse(checker.isNotNull())
        assertFalse(checker.allowPassOnNull().isNotNull())
    }

    void testIsNotEmptyOnIncompatibleEntity() {
        assertFalse(new BasicSanityChecker().check("foo", 12345).isNotEmpty())
    }

    void testIsNotEmptyPassIfNullCases() {
        BasicSanityChecker checker = new BasicSanityChecker()

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
        assertTrue(new BasicSanityChecker().check("foo", " ").isNotEmpty())
        assertTrue(new BasicSanityChecker().check("foo", ' ').isNotEmpty())

        assertTrue(new BasicSanityChecker().check("foo", """ """).isNotEmpty())
        assertTrue(new BasicSanityChecker().check("foo", ''' ''').isNotEmpty())

        assertTrue(new BasicSanityChecker().check("foo", [null]).isNotEmpty())
        assertTrue(new BasicSanityChecker().check("foo", ["a"]).isNotEmpty())

        assertTrue(new BasicSanityChecker().check("foo", [a: null]).isNotEmpty())
        assertTrue(new BasicSanityChecker().check("foo", [a: "a"]).isNotEmpty())

        assertFalse(new BasicSanityChecker().check("foo", null).isNotEmpty())
        assertFalse(new BasicSanityChecker().check("foo", "").isNotEmpty())
        assertFalse(new BasicSanityChecker().check("foo", '').isNotEmpty())
        assertFalse(new BasicSanityChecker().check("foo", """""").isNotEmpty())
        assertFalse(new BasicSanityChecker().check("foo", '''''').isNotEmpty())
        assertFalse(new BasicSanityChecker().check("foo", []).isNotEmpty())
        assertFalse(new BasicSanityChecker().check("foo", [:]).isNotEmpty())
    }

    void testIsBooleanPassIfNullCases() {
        BasicSanityChecker checker = new BasicSanityChecker()

        checker.check("foo", null)
        assertTrue(checker.allowPassOnNull().isBoolean())
        assertTrue(checker.disallowPassOnNull().isBoolean(true))

        checker.check("foo", 1)
        assertFalse(checker.allowPassOnNull().isBoolean())
        assertFalse(checker.disallowPassOnNull().isBoolean(true))
    }

    void testIsBoolean() {

        assertTrue(new BasicSanityChecker().check("foo", true).isBoolean())
        assertTrue(new BasicSanityChecker().check("foo", false).isBoolean())
        assertTrue(new BasicSanityChecker().check("foo", 1.asBoolean()).isBoolean())
        assertTrue(new BasicSanityChecker().check("foo", 0.asBoolean()).isBoolean())

        assertFalse(new BasicSanityChecker().check("foo", null).isBoolean())

        assertFalse(new BasicSanityChecker().check("foo", 0).isBoolean())
        assertFalse(new BasicSanityChecker().check("foo", 1).isBoolean())

        assertFalse(new BasicSanityChecker().check("foo", "true").isBoolean())
        assertFalse(new BasicSanityChecker().check("foo", "false").isBoolean())

        assertFalse(new BasicSanityChecker().check("foo", "${true}").isBoolean())
        assertFalse(new BasicSanityChecker().check("foo", "${false}").isBoolean())
    }

    void testIsStringPassIfNullCases() {
        BasicSanityChecker checker = new BasicSanityChecker()

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

        assertTrue(new BasicSanityChecker().check("foo", '').isString())
        assertTrue(new BasicSanityChecker().check("foo", "").isString())
        assertTrue(new BasicSanityChecker().check("foo", '''''').isString())
        assertTrue(new BasicSanityChecker().check("foo", """""").isString())
        assertTrue(new BasicSanityChecker().check("foo", 1 as String).isString())
        assertTrue(new BasicSanityChecker().check("foo", "${1}" as String).isString())
        assertTrue(new BasicSanityChecker().check("foo", "${null}" as String).isString())

        assertFalse(new BasicSanityChecker().check("foo", null).isString())
        assertFalse(new BasicSanityChecker().check("foo", 1).isString())
        assertFalse(new BasicSanityChecker().check("foo", "${1}").isString())
        assertFalse(new BasicSanityChecker().check("foo", "${null}").isString())
        assertFalse(new BasicSanityChecker().check("foo", "${}").isString())
    }

    void testIsNumberPassIfNullCases() {
        BasicSanityChecker checker = new BasicSanityChecker()

        checker.check("foo", null)
        assertTrue(checker.allowPassOnNull().isNumber())
        assertTrue(checker.disallowPassOnNull().isNumber(true))

        checker.check("foo", "1")
        assertFalse(checker.allowPassOnNull().isNumber())
        assertFalse(checker.disallowPassOnNull().isNumber(true))
    }

    void testIsNumber() {
        assertTrue(new BasicSanityChecker().check("foo", 1).isNumber())
        assertTrue(new BasicSanityChecker().check("foo", 1I).isNumber())
        assertTrue(new BasicSanityChecker().check("foo", 1L).isNumber())
        assertTrue(new BasicSanityChecker().check("foo", 1G).isNumber())
        assertTrue(new BasicSanityChecker().check("foo", 1D).isNumber())
        assertTrue(new BasicSanityChecker().check("foo", 1F).isNumber())
        assertTrue(new BasicSanityChecker().check("foo", 1.0).isNumber())
        assertTrue(new BasicSanityChecker().check("foo", 1.0G).isNumber())
        assertTrue(new BasicSanityChecker().check("foo", 1.0D).isNumber())
        assertTrue(new BasicSanityChecker().check("foo", 1.0F).isNumber())
        assertTrue(new BasicSanityChecker().check("foo", 1E10).isNumber())
        assertTrue(new BasicSanityChecker().check("foo", 1E10G).isNumber())
        assertTrue(new BasicSanityChecker().check("foo", 1E10D).isNumber())
        assertTrue(new BasicSanityChecker().check("foo", 1E10F).isNumber())
        assertTrue(new BasicSanityChecker().check("foo", 1.0E10).isNumber())
        assertTrue(new BasicSanityChecker().check("foo", 1.0E10G).isNumber())
        assertTrue(new BasicSanityChecker().check("foo", 1.0E10D).isNumber())
        assertTrue(new BasicSanityChecker().check("foo", 1.0E10F).isNumber())
        assertTrue(new BasicSanityChecker().check("foo", 01).isNumber())
        assertTrue(new BasicSanityChecker().check("foo", 01I).isNumber())
        assertTrue(new BasicSanityChecker().check("foo", 01L).isNumber())
        assertTrue(new BasicSanityChecker().check("foo", 01G).isNumber())
        assertTrue(new BasicSanityChecker().check("foo", 0x1).isNumber())
        assertTrue(new BasicSanityChecker().check("foo", 0x1I).isNumber())
        assertTrue(new BasicSanityChecker().check("foo", 0x1L).isNumber())
        assertTrue(new BasicSanityChecker().check("foo", 0x1G).isNumber())

        assertFalse(new BasicSanityChecker().check("foo", null).isNumber())
        assertFalse(new BasicSanityChecker().check("foo", "1").isNumber())
        assertFalse(new BasicSanityChecker().check("foo", "${1}").isNumber())
    }

    void testIsIntegerPassIfNullCases() {
        BasicSanityChecker checker = new BasicSanityChecker()

        checker.check("foo", null)
        assertTrue(checker.allowPassOnNull().isInteger())
        assertTrue(checker.disallowPassOnNull().isInteger(true))

        checker.check("foo", "1")
        assertFalse(checker.allowPassOnNull().isInteger())
        assertFalse(checker.disallowPassOnNull().isInteger(true))
    }

    void testIsInteger() {
        assertTrue(new BasicSanityChecker().check("foo", 1).isInteger())
        assertTrue(new BasicSanityChecker().check("foo", 1I).isInteger())
        assertTrue(new BasicSanityChecker().check("foo", 01).isInteger())
        assertTrue(new BasicSanityChecker().check("foo", 01I).isInteger())
        assertTrue(new BasicSanityChecker().check("foo", 0x1).isInteger())
        assertTrue(new BasicSanityChecker().check("foo", 0x1I).isInteger())

        assertFalse(new BasicSanityChecker().check("foo", null).isInteger())
        assertFalse(new BasicSanityChecker().check("foo", 1L).isInteger())
        assertFalse(new BasicSanityChecker().check("foo", 1.0).isInteger())
        assertFalse(new BasicSanityChecker().check("foo", Long.MAX_VALUE).isInteger())
        assertFalse(new BasicSanityChecker().check("foo", 9223372036854775807).isInteger())
        assertFalse(new BasicSanityChecker().check("foo", "1").isInteger())
        assertFalse(new BasicSanityChecker().check("foo", "${1}").isInteger())
    }

    void testIsLongPassIfNullCases() {
        BasicSanityChecker checker = new BasicSanityChecker()

        checker.check("foo", null)
        assertTrue(checker.allowPassOnNull().isLong())
        assertTrue(checker.disallowPassOnNull().isLong(true))

        checker.check("foo", "1")
        assertFalse(checker.allowPassOnNull().isLong())
        assertFalse(checker.disallowPassOnNull().isLong(true))
    }

    void testIsLong() {

        assertTrue(new BasicSanityChecker().check("foo", 1L).isLong())
        assertTrue(new BasicSanityChecker().check("foo", 01L).isLong())
        assertTrue(new BasicSanityChecker().check("foo", 0x1L).isLong())

        assertFalse(new BasicSanityChecker().check("foo", null).isLong())
        assertFalse(new BasicSanityChecker().check("foo", 1).isLong())
        assertFalse(new BasicSanityChecker().check("foo", 1.0).isLong())
        assertFalse(new BasicSanityChecker().check("foo", Integer.MAX_VALUE).isLong())
        assertFalse(new BasicSanityChecker().check("foo", "1L").isLong())
        assertFalse(new BasicSanityChecker().check("foo", "${1L}").isLong())
    }

    void testIsBigDecimalPassIfNullCases() {
        BasicSanityChecker checker = new BasicSanityChecker()

        checker.check("foo", null)
        assertTrue(checker.allowPassOnNull().isBigDecimal())
        assertTrue(checker.disallowPassOnNull().isBigDecimal(true))

        checker.check("foo", "1")
        assertFalse(checker.allowPassOnNull().isBigDecimal())
        assertFalse(checker.disallowPassOnNull().isBigDecimal(true))
    }

    void testIsBigDecimal() {

        assertTrue(new BasicSanityChecker().check("foo", 1.0).isBigDecimal())
        assertTrue(new BasicSanityChecker().check("foo", 1.0G).isBigDecimal())

        assertFalse(new BasicSanityChecker().check("foo", null).isBigDecimal())
        assertFalse(new BasicSanityChecker().check("foo", 1).isBigDecimal())
        assertFalse(new BasicSanityChecker().check("foo", 1G).isBigDecimal())
        assertFalse(new BasicSanityChecker().check("foo", Double.MAX_VALUE).isBigDecimal())
        assertFalse(new BasicSanityChecker().check("foo", "1.0G").isBigDecimal())
        assertFalse(new BasicSanityChecker().check("foo", "${1.0G}").isBigDecimal())
    }

    void testIsDoublePassIfNullCases() {
        BasicSanityChecker checker = new BasicSanityChecker()

        checker.check("foo", null)
        assertTrue(checker.allowPassOnNull().isDouble())
        assertTrue(checker.disallowPassOnNull().isDouble(true))

        checker.check("foo", "1")
        assertFalse(checker.allowPassOnNull().isDouble())
        assertFalse(checker.disallowPassOnNull().isDouble(true))
    }

    void testIsDouble() {

        assertTrue(new BasicSanityChecker().check("foo", 1D).isDouble())
        assertTrue(new BasicSanityChecker().check("foo", 1.0D).isDouble())

        assertFalse(new BasicSanityChecker().check("foo", null).isDouble())
        assertFalse(new BasicSanityChecker().check("foo", 1).isDouble())
        assertFalse(new BasicSanityChecker().check("foo", 1.0).isDouble())
        assertFalse(new BasicSanityChecker().check("foo", "1D").isDouble())
        assertFalse(new BasicSanityChecker().check("foo", "1.0D").isDouble())
        assertFalse(new BasicSanityChecker().check("foo", "${1D}").isDouble())
        assertFalse(new BasicSanityChecker().check("foo", "${1.0D}").isDouble())
    }

    void testIsListPassIfNullCases() {
        BasicSanityChecker checker = new BasicSanityChecker()

        checker.check("foo", null)
        assertTrue(checker.allowPassOnNull().isList())
        assertTrue(checker.disallowPassOnNull().isList(true))

        checker.check("foo", "1")
        assertFalse(checker.allowPassOnNull().isList())
        assertFalse(checker.disallowPassOnNull().isList(true))
    }

    void testIsList() {

        assertTrue(new BasicSanityChecker().check("foo", []).isList())
        assertTrue(new BasicSanityChecker().check("foo", [1, 2, 3, "blue", "moon"]).isList())
        assertTrue(new BasicSanityChecker().check("foo", 1..10).isList())
        assertTrue(new BasicSanityChecker().check("foo", 'a'..'z').isList())
        assertTrue(new BasicSanityChecker().check("foo", [[a: 'apple'],[a: 'apricot']].a).isList())

        assertFalse(new BasicSanityChecker().check("foo", null).isList())
        assertFalse(new BasicSanityChecker().check("foo", 1..10 as int[]).isList())
        assertFalse(new BasicSanityChecker().check("foo", 'a'..'z' as String[]).isList())
        assertFalse(new BasicSanityChecker().check("foo", 1).isList())
        assertFalse(new BasicSanityChecker().check("foo", [:]).isList())
    }

    void testIsMapPassIfNullCases() {
        BasicSanityChecker checker = new BasicSanityChecker()

        checker.check("foo", null)
        assertTrue(checker.allowPassOnNull().isMap())
        assertTrue(checker.disallowPassOnNull().isMap(true))

        checker.check("foo", "1")
        assertFalse(checker.allowPassOnNull().isMap())
        assertFalse(checker.disallowPassOnNull().isMap(true))
    }

    void testIsMap() {

        assertTrue(new BasicSanityChecker().check("foo", [:]).isMap())
        assertTrue(new BasicSanityChecker().check("foo", [a: 'apple', b: 'banana']).isMap())

        assertFalse(new BasicSanityChecker().check("foo", null).isMap())
        assertFalse(new BasicSanityChecker().check("foo", 1).isMap())
        assertFalse(new BasicSanityChecker().check("foo", 1..10 as int[]).isMap())
        assertFalse(new BasicSanityChecker().check("foo", []).isMap())
    }

    void testExactClassMatchPassIfNullCases() {
        BasicSanityChecker checker = new BasicSanityChecker()

        checker.check("foo", null)
        assertTrue(checker.allowPassOnNull().exactClassMatch(Class))
        assertTrue(checker.disallowPassOnNull().exactClassMatch(Class, true))

        checker.check("foo", 1)
        assertFalse(checker.allowPassOnNull().exactClassMatch(String))
        assertFalse(checker.disallowPassOnNull().exactClassMatch(String, true))
    }

    void testExactClassMatch() {

        assertTrue(new BasicSanityChecker().check("foo", SampleUserClass.SAMPLE_USER_CLASS).exactClassMatch(SampleUserClass))

        assertFalse(new BasicSanityChecker().check("foo", null).exactClassMatch(SampleUserClass))

        assertFalse(new BasicSanityChecker().check("foo", 1).exactClassMatch(SampleUserClass))
    }

    void testClassMatchPassIfNullCases() {
        BasicSanityChecker checker = new BasicSanityChecker()

        checker.check("foo", null)
        assertTrue(checker.allowPassOnNull().classMatch(Class))
        assertTrue(checker.disallowPassOnNull().classMatch(Class, true))

        checker.check("foo", 1)
        assertFalse(checker.allowPassOnNull().classMatch(String))
        assertFalse(checker.disallowPassOnNull().classMatch(String, true))
    }

    void testClassMatch() {

        assertTrue(new BasicSanityChecker().check("foo", 1).classMatch(Integer))
        assertTrue(new BasicSanityChecker().check("foo", 1).classMatch(Number))
        assertFalse(new BasicSanityChecker().check("foo", 1).classMatch(String))

        assertFalse(new BasicSanityChecker().check("foo", null).classMatch(Integer))
    }

    void testRespondsToPassIfNullCases() {
        BasicSanityChecker checker = new BasicSanityChecker()

        checker.check("foo", null)
        assertTrue(checker.allowPassOnNull().respondsTo('foo'))
        assertTrue(checker.disallowPassOnNull().respondsTo('foo', true))

        checker.check("foo", UUID.randomUUID())
        assertFalse(checker.allowPassOnNull().respondsTo('isEmpty'))
        assertFalse(checker.disallowPassOnNull().respondsTo('isEmpty', true))
    }

    void testRespondsTo() {

        assertTrue(new BasicSanityChecker().check("foo", 1).respondsTo('byteValue'))
        assertFalse(new BasicSanityChecker().check("foo", 1).respondsTo('isEmpty'))

        assertFalse(new BasicSanityChecker().check("foo", null).respondsTo('isEmpty'))
    }

    void testRunChecks01() {
        BasicSanityChecker checker = new BasicSanityChecker()

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
        BasicSanityChecker checker = new BasicSanityChecker()

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
            new BasicSanityChecker().isNotNull()
        }

        fail = shouldFail(IllegalStateException) {
            new BasicSanityChecker().isNotEmpty()
        }

        fail = shouldFail(IllegalStateException) {
            new BasicSanityChecker().isNotEmpty(false)
        }

        fail = shouldFail(IllegalStateException) {
            new BasicSanityChecker().isBoolean()
        }

        fail = shouldFail(IllegalStateException) {
            new BasicSanityChecker().isBoolean(false)
        }

        fail = shouldFail(IllegalStateException) {
            new BasicSanityChecker().isString()
        }

        fail = shouldFail(IllegalStateException) {
            new BasicSanityChecker().isString(false)
        }

        fail = shouldFail(IllegalStateException) {
            new BasicSanityChecker().isNumber()
        }

        fail = shouldFail(IllegalStateException) {
            new BasicSanityChecker().isNumber(false)
        }

        fail = shouldFail(IllegalStateException) {
            new BasicSanityChecker().isInteger()
        }

        fail = shouldFail(IllegalStateException) {
            new BasicSanityChecker().isInteger(false)
        }

        fail = shouldFail(IllegalStateException) {
            new BasicSanityChecker().isLong()
        }

        fail = shouldFail(IllegalStateException) {
            new BasicSanityChecker().isLong(false)
        }

        fail = shouldFail(IllegalStateException) {
            new BasicSanityChecker().isBigDecimal()
        }

        fail = shouldFail(IllegalStateException) {
            new BasicSanityChecker().isBigDecimal(false)
        }

        fail = shouldFail(IllegalStateException) {
            new BasicSanityChecker().isDouble()
        }

        fail = shouldFail(IllegalStateException) {
            new BasicSanityChecker().isDouble(false)
        }

        fail = shouldFail(IllegalStateException) {
            new BasicSanityChecker().isList()
        }

        fail = shouldFail(IllegalStateException) {
            new BasicSanityChecker().isList(false)
        }

        fail = shouldFail(IllegalStateException) {
            new BasicSanityChecker().isMap()
        }

        fail = shouldFail(IllegalStateException) {
            new BasicSanityChecker().isMap(false)
        }

        fail = shouldFail(IllegalStateException) {
            new BasicSanityChecker().respondsTo('foo')
        }

        fail = shouldFail(IllegalStateException) {
            new BasicSanityChecker().respondsTo('foo', false)
        }

        fail = shouldFail(IllegalStateException) {
            new BasicSanityChecker().exactClassMatch(String)
        }

        fail = shouldFail(IllegalStateException) {
            new BasicSanityChecker().exactClassMatch(String, false)
        }

        fail = shouldFail(IllegalStateException) {
            new BasicSanityChecker().classMatch(Number)
        }

        fail = shouldFail(IllegalStateException) {
            new BasicSanityChecker().classMatch(Number, false)
        }
    }
}
