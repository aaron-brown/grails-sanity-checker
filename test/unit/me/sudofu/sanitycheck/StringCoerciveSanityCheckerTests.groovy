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
import me.sudofu.sanitycheck.StringCoerciveSanityChecker

import static org.junit.Assert.*

import grails.test.mixin.*
import grails.test.mixin.support.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class StringCoerciveSanityCheckerTests {

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
        StringCoerciveSanityChecker checker = new StringCoerciveSanityChecker()

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
        StringCoerciveSanityChecker checker = new StringCoerciveSanityChecker(true)

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
        StringCoerciveSanityChecker checker = new StringCoerciveSanityChecker()

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
        StringCoerciveSanityChecker checker = new StringCoerciveSanityChecker()

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
        assertFalse(new StringCoerciveSanityChecker().allowPassOnNull)
        assertFalse(new StringCoerciveSanityChecker(false).allowPassOnNull)
        assertTrue(new StringCoerciveSanityChecker(true).allowPassOnNull)

        StringCoerciveSanityChecker checker = new StringCoerciveSanityChecker()

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
        StringCoerciveSanityChecker checker = new StringCoerciveSanityChecker()

        checker.check("foo", "hello")
        assertTrue(checker.isNotNull())

        checker.check("foo", null)

        assertFalse(checker.isNotNull())
        assertFalse(checker.allowPassOnNull().isNotNull())
    }

    void testIsNotEmptyOnIncompatibleEntity() {
        assertFalse(new StringCoerciveSanityChecker().check("foo", 12345).isNotEmpty())
    }

    void testIsNotEmptyPassIfNullCases() {
        StringCoerciveSanityChecker checker = new StringCoerciveSanityChecker()

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
        assertTrue(new StringCoerciveSanityChecker().check("foo", " ").isNotEmpty())
        assertTrue(new StringCoerciveSanityChecker().check("foo", ' ').isNotEmpty())

        assertTrue(new StringCoerciveSanityChecker().check("foo", """ """).isNotEmpty())
        assertTrue(new StringCoerciveSanityChecker().check("foo", ''' ''').isNotEmpty())

        assertTrue(new StringCoerciveSanityChecker().check("foo", [null]).isNotEmpty())
        assertTrue(new StringCoerciveSanityChecker().check("foo", ["a"]).isNotEmpty())

        assertTrue(new StringCoerciveSanityChecker().check("foo", [a: null]).isNotEmpty())
        assertTrue(new StringCoerciveSanityChecker().check("foo", [a: "a"]).isNotEmpty())

        assertFalse(new StringCoerciveSanityChecker().check("foo", null).isNotEmpty())
        assertFalse(new StringCoerciveSanityChecker().check("foo", "").isNotEmpty())
        assertFalse(new StringCoerciveSanityChecker().check("foo", '').isNotEmpty())
        assertFalse(new StringCoerciveSanityChecker().check("foo", """""").isNotEmpty())
        assertFalse(new StringCoerciveSanityChecker().check("foo", '''''').isNotEmpty())
        assertFalse(new StringCoerciveSanityChecker().check("foo", []).isNotEmpty())
        assertFalse(new StringCoerciveSanityChecker().check("foo", [:]).isNotEmpty())
    }

    void testIsStringPassIfNullCases() {
        StringCoerciveSanityChecker checker = new StringCoerciveSanityChecker()

        checker.check("foo", null)
        checker.allowPassOnNull().isString()
        checker.disallowPassOnNull().isString(true)

        assertFalse(checker.hasFailures())
    }

    void testIsString() {
        StringCoerciveSanityChecker checker = new StringCoerciveSanityChecker()

        assertEquals('', checker.check("foo", '').isString())
        assertEquals("", checker.check("foo", "").isString())
        assertEquals('''''', checker.check("foo", '''''').isString())
        assertEquals("""""", checker.check("foo", """""").isString())
        assertEquals("1", checker.check("foo", 1 as String).isString())
        assertEquals("1", checker.check("foo", "${1}" as String).isString())
        assertEquals("null", checker.check("foo", "${null}" as String).isString())
        assertNull(checker.check("foo", null).isString())
        assertEquals(1, checker.check("foo", 1).isString())
        assertEquals("${1}", checker.check("foo", "${1}").isString())
        assertEquals("${null}", checker.check("foo", "${null}").isString())
        assertEquals("${}", checker.check("foo", "${}").isString())
    }

    void testIsIntegerPassIfNullCases() {
        StringCoerciveSanityChecker checker = new StringCoerciveSanityChecker()

        checker.check("foo", null)
        checker.allowPassOnNull().isInteger()
        checker.disallowPassOnNull().isInteger(true)

        assertFalse(checker.hasFailures())
    }

    void testIsInteger() {
        StringCoerciveSanityChecker checker = new StringCoerciveSanityChecker()

        assertEquals(1, checker.check("foo", 1).isInteger())
        assertEquals(1, checker.check("foo", 1I).isInteger())
        assertEquals(1, checker.check("foo", 01).isInteger())
        assertEquals(1, checker.check("foo", 01I).isInteger())
        assertEquals(1, checker.check("foo", 0x1).isInteger())
        assertEquals(1, checker.check("foo", 0x1I).isInteger())
        assertEquals(1, checker.check("foo", 1 as String).isInteger())
        assertEquals(1, checker.check("foo", 1I as String).isInteger())
        assertEquals(1, checker.check("foo", 01 as String).isInteger())
        assertEquals(1, checker.check("foo", 01I as String).isInteger())
        assertEquals(1, checker.check("foo", 0x1 as String).isInteger())
        assertEquals(1, checker.check("foo", 0x1I as String).isInteger())
        assertEquals(1, checker.check("foo", "1").isInteger())
        assertEquals(1, checker.check("foo", "01").isInteger())
        assertEquals(1, checker.check("foo", "${1}").isInteger())
        assertEquals(1, checker.check("foo", "${01}").isInteger())
        assertEquals(1, checker.check("foo", "${1I}").isInteger())
        assertEquals(1, checker.check("foo", "${01I}").isInteger())
        assertEquals(1, checker.check("foo", "${0x1}").isInteger())
        assertEquals(1, checker.check("foo", "${0x1I}").isInteger())

        assertEquals("1I", checker.check("foo", "1I").isInteger())
        assertEquals("01I", checker.check("foo", "01I").isInteger())
        assertEquals("0x1", checker.check("foo", "0x1").isInteger())
        assertEquals("0x1I", checker.check("foo", "0x1I").isInteger())
        assertEquals(null, checker.check("foo", null).isInteger())
        assertTrue(checker.check("foo", 1L).isLong().getClass() == Long)
        assertTrue(checker.check("foo", 1.0).isLong().getClass() == BigDecimal)
        assertEquals(Long.MAX_VALUE, checker.check("foo", Long.MAX_VALUE).isInteger())
        assertTrue(checker.check("foo", 9223372036854775807).isInteger().getClass() == Long)
    }

    void testIsLongPassIfNullCases() {
        StringCoerciveSanityChecker checker = new StringCoerciveSanityChecker()

        checker.check("foo", null)
        checker.allowPassOnNull().isLong()
        checker.disallowPassOnNull().isLong(true)

        assertFalse(checker.hasFailures())
    }

    void testIsLong() {
        StringCoerciveSanityChecker checker = new StringCoerciveSanityChecker()

        assertEquals(1, checker.check("foo", 1).isLong())
        assertEquals(1, checker.check("foo", 1L).isLong())
        assertEquals(1, checker.check("foo", 01).isLong())
        assertEquals(1, checker.check("foo", 01L).isLong())
        assertEquals(1, checker.check("foo", 0x1).isLong())
        assertEquals(1, checker.check("foo", 0x1L).isLong())
        assertEquals(1, checker.check("foo", 1 as String).isLong())
        assertEquals(1, checker.check("foo", 1L as String).isLong())
        assertEquals(1, checker.check("foo", 01 as String).isLong())
        assertEquals(1, checker.check("foo", 01L as String).isLong())
        assertEquals(1, checker.check("foo", 0x1 as String).isLong())
        assertEquals(1, checker.check("foo", 0x1L as String).isLong())
        assertEquals(1, checker.check("foo", "1").isLong())
        assertEquals(1, checker.check("foo", "01").isLong())
        assertEquals(1, checker.check("foo", "${1}").isLong())
        assertEquals(1, checker.check("foo", "${01}").isLong())
        assertEquals(1, checker.check("foo", "${1L}").isLong())
        assertEquals(1, checker.check("foo", "${01L}").isLong())
        assertEquals(1, checker.check("foo", "${0x1}").isLong())
        assertEquals(1, checker.check("foo", "${0x1L}").isLong())

        assertEquals("1L", checker.check("foo", "1L").isLong())
        assertEquals("01L", checker.check("foo", "01L").isLong())
        assertEquals("0x1", checker.check("foo", "0x1").isLong())
        assertEquals("0x1L", checker.check("foo", "0x1L").isLong())
        assertEquals(null, checker.check("foo", null).isLong())
        assertTrue(checker.check("foo", 1I).isLong().getClass() == Integer)
        assertTrue(checker.check("foo", 1.0).isLong().getClass() == BigDecimal)
    }

    void testIsBigDecimalPassIfNullCases() {
        StringCoerciveSanityChecker checker = new StringCoerciveSanityChecker()

        checker.check("foo", null)
        checker.allowPassOnNull().isBigDecimal()
        checker.disallowPassOnNull().isBigDecimal(true)

        assertFalse(checker.hasFailures())
    }


    void testIsBigDecimal() {
        StringCoerciveSanityChecker checker = new StringCoerciveSanityChecker()

        assertEquals(1.0, checker.check("foo", 1.0).isBigDecimal())
        assertEquals(1.0, checker.check("foo", 01.0).isBigDecimal())
        assertEquals(1.0, checker.check("foo", 1.0G).isBigDecimal())
        assertEquals(1.0, checker.check("foo", 01.0G).isBigDecimal())
        assertEquals(1.0, checker.check("foo", 1.0 as String).isBigDecimal())
        assertEquals(1.0, checker.check("foo", 01.0 as String).isBigDecimal())
        assertEquals(1.0, checker.check("foo", 1.0G as String).isBigDecimal())
        assertEquals(1.0, checker.check("foo", 01.0G as String).isBigDecimal())
        assertEquals(1.0, checker.check("foo", "1.0").isBigDecimal())
        assertEquals(1.0, checker.check("foo", "01.0").isBigDecimal())
        assertEquals(1.0, checker.check("foo", "${1.0}").isBigDecimal())
        assertEquals(1.0, checker.check("foo", "${01.0}").isBigDecimal())
        assertEquals(1.0, checker.check("foo", "${1.0G}").isBigDecimal())
        assertEquals(1.0, checker.check("foo", "${01.0G}").isBigDecimal())

        assertEquals("1.0G", checker.check("foo", "1.0G").isBigDecimal())
        assertEquals("01.0G", checker.check("foo", "01.0G").isBigDecimal())
        assertEquals(null, checker.check("foo", null).isBigDecimal())
        assertTrue(checker.check("foo", 1G).isBigDecimal().getClass() == BigInteger)
        assertTrue(checker.check("foo", 1L).isLong().getClass() == Long)
    }

    void testIsDoublePassIfNullCases() {
        StringCoerciveSanityChecker checker = new StringCoerciveSanityChecker()

        checker.check("foo", null)
        checker.allowPassOnNull().isDouble()
        checker.disallowPassOnNull().isDouble(true)

        assertFalse(checker.hasFailures())
    }

    void testIsDouble() {

        StringCoerciveSanityChecker checker = new StringCoerciveSanityChecker()

        assertEquals(Double.doubleToLongBits(1D), Double.doubleToLongBits(checker.check("foo", 1D).isDouble()))
        assertEquals(Double.doubleToLongBits(1D), Double.doubleToLongBits(checker.check("foo", 01D).isDouble()))
        assertEquals(Double.doubleToLongBits(1.0D), Double.doubleToLongBits(checker.check("foo", 1.0D).isDouble()))
        assertEquals(Double.doubleToLongBits(1.0D), Double.doubleToLongBits(checker.check("foo", 01.0D).isDouble()))
        assertEquals(Double.doubleToLongBits(1.0D), Double.doubleToLongBits(checker.check("foo", 1.0D as String).isDouble()))
        assertEquals(Double.doubleToLongBits(1.0D), Double.doubleToLongBits(checker.check("foo", 01.0D as String).isDouble()))
        assertEquals(Double.doubleToLongBits(1.0D), Double.doubleToLongBits(checker.check("foo", "1.0").isDouble()))
        assertEquals(Double.doubleToLongBits(1.0D), Double.doubleToLongBits(checker.check("foo", "01.0").isDouble()))
        assertEquals(Double.doubleToLongBits(1.0D), Double.doubleToLongBits(checker.check("foo", "1.0D").isDouble()))
        assertEquals(Double.doubleToLongBits(1.0D), Double.doubleToLongBits(checker.check("foo", "01.0D").isDouble()))
        assertEquals(Double.doubleToLongBits(1.0D), Double.doubleToLongBits(checker.check("foo", "${1.0D}").isDouble()))
        assertEquals(Double.doubleToLongBits(1.0D), Double.doubleToLongBits(checker.check("foo", "${01.0D}").isDouble()))

        assertEquals(null, checker.check("foo", null).isDouble())
        assertTrue(checker.check("foo", 1.0).isDouble().getClass() == BigDecimal)
        assertTrue(checker.check("foo", 1L).isLong().getClass() == Long)
    }

    void testRunChecks() {
        Map normalizedInput = [
            name: "",
            age: "42",
            height: '172.72',
            heightMetric: 'cm',
            maxLong: Long.MAX_VALUE as String
        ]

        StringCoerciveSanityChecker checker = new StringCoerciveSanityChecker()

        assertFalse(checker.hasPasses())
        assertFalse(checker.hasFailures())

        assertEquals(0, checker.countPasses())
        assertEquals(0, checker.countFailures())

        normalizedInput.with {
            checker.runChecks {

                // Implicit isNotNull
                name = check('name', name).isString()

                // Implicit isNotNull
                // Implicit respondsTo
                isNotEmpty()

                // Implicit isNotNull
                age = check('age', age).isInteger()

                // Implicit isNotNull
                height = check('height', height).isDouble()

                // Implicit isNotNull
                maxLong = check('maxLong', maxLong).isInteger()
            }
        }

        assertTrue(checker.hasPasses())
        assertTrue(checker.hasFailures())

        assertEquals(10, checker.countPasses())
        assertEquals(3, checker.countFailures())

        assertEquals("", normalizedInput.name)
        assertEquals(42, normalizedInput.age)

        assertTrue(normalizedInput.height.getClass() == Double)

        assertEquals(Long.MAX_VALUE as String, normalizedInput.maxLong)
        assertTrue(normalizedInput.maxLong.getClass() == String)
    }

    void testCheckYourselfBeforeYouWreckYourself() {
        def fail

        fail = shouldFail(IllegalStateException) {
            new StringCoerciveSanityChecker().isNotNull()
        }

        fail = shouldFail(IllegalStateException) {
            new StringCoerciveSanityChecker().isNotEmpty()
        }

        fail = shouldFail(IllegalStateException) {
            new StringCoerciveSanityChecker().isNotEmpty(false)
        }

        fail = shouldFail(IllegalStateException) {
            new StringCoerciveSanityChecker().isString()
        }

        fail = shouldFail(IllegalStateException) {
            new StringCoerciveSanityChecker().isString(false)
        }

        fail = shouldFail(IllegalStateException) {
            new StringCoerciveSanityChecker().isInteger()
        }

        fail = shouldFail(IllegalStateException) {
            new StringCoerciveSanityChecker().isInteger(false)
        }

        fail = shouldFail(IllegalStateException) {
            new StringCoerciveSanityChecker().isLong()
        }

        fail = shouldFail(IllegalStateException) {
            new StringCoerciveSanityChecker().isLong(false)
        }

        fail = shouldFail(IllegalStateException) {
            new StringCoerciveSanityChecker().isBigDecimal()
        }

        fail = shouldFail(IllegalStateException) {
            new StringCoerciveSanityChecker().isBigDecimal(false)
        }

        fail = shouldFail(IllegalStateException) {
            new StringCoerciveSanityChecker().isDouble()
        }

        fail = shouldFail(IllegalStateException) {
            new StringCoerciveSanityChecker().isDouble(false)
        }

        fail = shouldFail(IllegalStateException) {
            new StringCoerciveSanityChecker().respondsTo('foo')
        }

        fail = shouldFail(IllegalStateException) {
            new StringCoerciveSanityChecker().respondsTo('foo', false)
        }

        fail = shouldFail(IllegalStateException) {
            new StringCoerciveSanityChecker().exactClassMatch(String)
        }

        fail = shouldFail(IllegalStateException) {
            new StringCoerciveSanityChecker().exactClassMatch(String, false)
        }

        fail = shouldFail(IllegalStateException) {
            new StringCoerciveSanityChecker().classMatch(Number)
        }

        fail = shouldFail(IllegalStateException) {
            new StringCoerciveSanityChecker().classMatch(Number, false)
        }
    }
}
