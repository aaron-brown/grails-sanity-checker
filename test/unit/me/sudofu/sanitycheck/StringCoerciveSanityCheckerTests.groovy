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
import me.sudofu.sanitycheck.CoerciveSanityChecker
import me.sudofu.sanitycheck.SanityCheckException

import static org.junit.Assert.*

import grails.test.mixin.*
import grails.test.mixin.support.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class CoerciveSanityCheckerTests {

    void setUp() {
        // Setup logic here
    }

    void tearDown() {
        // Tear down logic here
    }

    void testConstructor01() {
        CoerciveSanityChecker checker = new CoerciveSanityChecker("foo", "bar")

        assertEquals("foo", checker.name)
        assertEquals("bar", checker.entity)
        assertEquals("parameter", checker.classification)
        assertFalse(checker.allowPassOnNull)
    }

    void testConstructor02() {
        CoerciveSanityChecker checker = new CoerciveSanityChecker("foo", "bar", "baz")

        assertEquals("foo", checker.name)
        assertEquals("bar", checker.entity)
        assertEquals("baz", checker.classification)
        assertFalse(checker.allowPassOnNull)
    }

    void testConstructor03() {
        CoerciveSanityChecker checker = new CoerciveSanityChecker("foo", "bar", true)

        assertEquals("foo", checker.name)
        assertEquals("bar", checker.entity)
        assertEquals("parameter", checker.classification)
        assertTrue(checker.allowPassOnNull)
    }

    void testConstructor04() {
        CoerciveSanityChecker checker = new CoerciveSanityChecker("foo", "bar", "baz", true)

        assertEquals("foo", checker.name)
        assertEquals("bar", checker.entity)
        assertEquals("baz", checker.classification)
        assertTrue(checker.allowPassOnNull)
    }

    void testConstructor05() {
        SanityChecker baseChecker = new SanityChecker("foo", "bar", "baz", true)
        CoerciveSanityChecker checker = new CoerciveSanityChecker(baseChecker)

        assertEquals("foo", checker.name)
        assertEquals("bar", checker.entity)
        assertEquals("baz", checker.classification)
        assertTrue(checker.allowPassOnNull)
    }

    void testIsNotNull() {

        new CoerciveSanityChecker("foo", "hello").isNotNull()

        String failure

        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", null).isNotNull()
        }

        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", null, true).isNotNull()
        }

        String string

        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", string).isNotNull()
        }
    }

    void testIsNotEmptyOnIncompatibleEntity() {
        String failure

        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", 12345).isNotEmpty()
        }
    }

    void testIsNotEmptyPassIfNullCases() {
        new CoerciveSanityChecker("foo", null, true).isNotEmpty()
        new CoerciveSanityChecker("foo", null).isNotEmpty(true)

        String fail

        fail = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", "", true).isNotEmpty()
        }

        fail = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", "").isNotEmpty(true)
        }

        fail = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", '', true).isNotEmpty()
        }

        fail = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", '').isNotEmpty(true)
        }

        fail = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", """""", true).isNotEmpty()
        }

        fail = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", """""").isNotEmpty(true)
        }

        fail = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", '''''', true).isNotEmpty()
        }

        fail = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", '''''').isNotEmpty(true)
        }

        fail = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", [], true).isNotEmpty()
        }

        fail = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", []).isNotEmpty(true)
        }

        fail = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", [:], true).isNotEmpty()
        }

        fail = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", [:]).isNotEmpty(true)
        }
    }

    void testIsNotEmptyOnString() {

        new CoerciveSanityChecker("foo", " ").isNotEmpty()
        new CoerciveSanityChecker("foo", ' ').isNotEmpty()
        new CoerciveSanityChecker("foo", """ """).isNotEmpty()
        new CoerciveSanityChecker("foo", ''' ''').isNotEmpty()

        String failure

        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", null).isNotEmpty()
        }

        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", "").isNotEmpty()
        }

        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", '').isNotEmpty()
        }

        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", """""").isNotEmpty()
        }

        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", '''''').isNotEmpty()
        }
    }

    void testIsNotEmptyOnList() {
        new CoerciveSanityChecker("foo", [1, 2, 3, "blue", "moon"]).isNotEmpty()

        String failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", []).isNotEmpty()
        }
    }

    void testIsNotEmptyOnMap() {

        new CoerciveSanityChecker("foo", [one: 1, two: 2, three: 3, blue: "moon"]).isNotEmpty()

        String failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", [:]).isNotEmpty()
        }
    }

    void testIsStringPassIfNullCases() {
        assertEquals(null, new CoerciveSanityChecker("foo", null, true).isString())
        assertEquals(null, new CoerciveSanityChecker("foo", null).isString(true))

        String fail

        fail = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", "${null}", true).isString()
        }

        fail = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", "${null}").isString(true)
        }

        fail = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", "${}", true).isString()
        }

        fail = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", "${}").isString(true)
        }
    }

    void testIsString() {

        assertEquals('', new CoerciveSanityChecker("foo", '').isString())
        assertEquals("", new CoerciveSanityChecker("foo", "").isString())
        assertEquals('''''', new CoerciveSanityChecker("foo", '''''').isString())
        assertEquals("""""", new CoerciveSanityChecker("foo", """""").isString())
        assertEquals("1", new CoerciveSanityChecker("foo", 1 as String).isString())
        assertEquals("1", new CoerciveSanityChecker("foo", "${1}" as String).isString())
        assertEquals("null", new CoerciveSanityChecker("foo", "${null}" as String).isString())

        String failure

        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", null).isString()
        }

        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", 1).isString()
        }

        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", "${1}").isString()
        }

        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", "${null}").isString()
        }

        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", "${}").isString()
        }
    }

    void testIsNumberPassIfNullCases() {
        assertEquals(null, new CoerciveSanityChecker("foo", null, true).isNumber())
        assertEquals(null, new CoerciveSanityChecker("foo", null).isNumber(true))

        String fail

        fail = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", "apple", true).isNumber()
        }

        fail = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", "apple").isNumber(true)
        }
    }

    void testIsNumber() {

        new CoerciveSanityChecker("foo", 1).isNumber()
        new CoerciveSanityChecker("foo", "1").isNumber()
        new CoerciveSanityChecker("foo", "${1}").isNumber()
        new CoerciveSanityChecker("foo", 1I).isNumber()
        new CoerciveSanityChecker("foo", 1L).isNumber()
        new CoerciveSanityChecker("foo", 1G).isNumber()
        new CoerciveSanityChecker("foo", 1D).isNumber()
        new CoerciveSanityChecker("foo", 1F).isNumber()
        new CoerciveSanityChecker("foo", 1.0).isNumber()
        new CoerciveSanityChecker("foo", "1.0").isNumber()
        new CoerciveSanityChecker("foo", 1.0G).isNumber()
        new CoerciveSanityChecker("foo", 1.0D).isNumber()
        new CoerciveSanityChecker("foo", 1.0F).isNumber()
        new CoerciveSanityChecker("foo", 1E10).isNumber()
        new CoerciveSanityChecker("foo", "1E10").isNumber()
        new CoerciveSanityChecker("foo", 1E10G).isNumber()
        new CoerciveSanityChecker("foo", 1E10D).isNumber()
        new CoerciveSanityChecker("foo", 1E10F).isNumber()
        new CoerciveSanityChecker("foo", 1.0E10).isNumber()
        new CoerciveSanityChecker("foo", "1.0E10").isNumber()
        new CoerciveSanityChecker("foo", 1.0E10G).isNumber()
        new CoerciveSanityChecker("foo", 1.0E10D).isNumber()
        new CoerciveSanityChecker("foo", 1.0E10F).isNumber()
        new CoerciveSanityChecker("foo", 01).isNumber()
        new CoerciveSanityChecker("foo", "01").isNumber()
        new CoerciveSanityChecker("foo", 01I).isNumber()
        new CoerciveSanityChecker("foo", 01L).isNumber()
        new CoerciveSanityChecker("foo", 01G).isNumber()
        new CoerciveSanityChecker("foo", 0x1).isNumber()
        new CoerciveSanityChecker("foo", 0x1I).isNumber()
        new CoerciveSanityChecker("foo", 0x1L).isNumber()
        new CoerciveSanityChecker("foo", 0x1G).isNumber()

        String failure

        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", null).isNumber()
        }

        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", "1I").isNumber()
        }

        // See tests for double; this passes in that case.
        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", "1D").isNumber()
        }

        // See tests for double; this passes in that case.
        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", "1.0D").isNumber()
        }

        // See tests for double; this passes in that case.
        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", "1E10D").isNumber()
        }

        // See tests for double; this passes in that case.
        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", "1.0E10D").isNumber()
        }

        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", "0x1").isNumber()
        }
    }

    void testIsIntegerPassIfNullCases() {
        assertEquals(null, new CoerciveSanityChecker("foo", null, true).isInteger())
        assertEquals(null, new CoerciveSanityChecker("foo", null).isInteger(true))

        String fail

        fail = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", "1I", true).isInteger()
        }

        fail = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", "1I").isInteger(true)
        }
    }

    void testIsInteger() {

        assertEquals(1, new CoerciveSanityChecker("foo", 1).isInteger())
        assertEquals(1, new CoerciveSanityChecker("foo", "1").isInteger())
        assertEquals(1I, new CoerciveSanityChecker("foo", 1I).isInteger())
        assertEquals(01, new CoerciveSanityChecker("foo", 01).isInteger())
        assertEquals(01I, new CoerciveSanityChecker("foo", 01I).isInteger())
        assertEquals(0x1, new CoerciveSanityChecker("foo", 0x1).isInteger())
        assertEquals(0x1I, new CoerciveSanityChecker("foo", 0x1I).isInteger())

        String failure

        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", null).isInteger()
        }

        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", "1I").isInteger()
        }

        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", 1L).isInteger()
        }

        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", 1.0).isInteger()
        }

        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", Long.MAX_VALUE).isInteger()
        }

        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", 9223372036854775807).isInteger()
        }

        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", "9223372036854775807").isInteger()
        }

        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", "${9223372036854775807}").isInteger()
        }
    }

    void testIsLongPassIfNullCases() {
        assertEquals(null, new CoerciveSanityChecker("foo", null, true).isLong())
        assertEquals(null, new CoerciveSanityChecker("foo", null).isLong(true))

        String fail

        fail = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", "1L", true).isLong()
        }

        fail = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", "1L").isLong(true)
        }
    }

    void testIsLong() {

        assertEquals(1 as Long, new CoerciveSanityChecker("foo", "1").isLong())
        assertEquals(1 as Long, new CoerciveSanityChecker("foo", 1L).isLong())
        assertEquals(01 as Long, new CoerciveSanityChecker("foo", "01").isLong())
        assertEquals(01L, new CoerciveSanityChecker("foo", 01L).isLong())
        assertEquals(0x1L, new CoerciveSanityChecker("foo", 0x1L).isLong())

        String failure

        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", null).isLong()
        }

        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", 1).isLong()
        }

        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", 1.0).isLong()
        }

        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", Integer.MAX_VALUE).isLong()
        }

        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", "1L").isLong()
        }
    }

    void testIsBigDecimalPassIfNullCases() {
        assertEquals(null, new CoerciveSanityChecker("foo", null, true).isBigDecimal())
        assertEquals(null, new CoerciveSanityChecker("foo", null).isBigDecimal(true))

        String fail

        fail = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", "1.0G", true).isBigDecimal()
        }

        fail = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", "1.0G").isBigDecimal(true)
        }
    }

    void testIsBigDecimal() {

        assertEquals(1 as BigDecimal, new CoerciveSanityChecker("foo", "1").isBigDecimal())
        assertEquals(1.0, new CoerciveSanityChecker("foo", 1.0).isBigDecimal())
        assertEquals(1.0, new CoerciveSanityChecker("foo", "1.0").isBigDecimal())
        assertEquals(1.0G, new CoerciveSanityChecker("foo", "${1.0}").isBigDecimal())
        assertEquals(1.0G, new CoerciveSanityChecker("foo", 1.0G).isBigDecimal())
        assertEquals(1.0G, new CoerciveSanityChecker("foo", "${1.0G}").isBigDecimal())

        String failure

        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", null).isBigDecimal()
        }

        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", 1).isBigDecimal()
        }

        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", 1G).isBigDecimal()
        }

        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", "1.0G").isBigDecimal()
        }

        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", Double.MAX_VALUE).isBigDecimal()
        }

        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", "1.0G").isBigDecimal()
        }
    }

    void testIsDoublePassIfNullCases() {
        assertEquals(null, new CoerciveSanityChecker("foo", null, true).isDouble())
        assertEquals(null, new CoerciveSanityChecker("foo", null).isDouble(true))

        String fail

        fail = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", "apple", true).isDouble()
        }

        fail = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", "apple").isDouble(true)
        }
    }

    void testIsDouble() {

        assertEquals(Double.doubleToLongBits(1D), Double.doubleToLongBits(new CoerciveSanityChecker("foo", "1").isDouble()))
        assertEquals(Double.doubleToLongBits(1D), Double.doubleToLongBits(new CoerciveSanityChecker("foo", "${1}").isDouble()))
        assertEquals(Double.doubleToLongBits(1D), Double.doubleToLongBits(new CoerciveSanityChecker("foo", 1D).isDouble()))
        assertEquals(Double.doubleToLongBits(1D), Double.doubleToLongBits(new CoerciveSanityChecker("foo", "1D").isDouble()))
        assertEquals(Double.doubleToLongBits(1D), Double.doubleToLongBits(new CoerciveSanityChecker("foo", "${1D}").isDouble()))

        assertEquals(Double.doubleToLongBits(1.0D), Double.doubleToLongBits(new CoerciveSanityChecker("foo", 1.0D).isDouble()))
        assertEquals(Double.doubleToLongBits(1.0D), Double.doubleToLongBits(new CoerciveSanityChecker("foo", "1.0D").isDouble()))
        assertEquals(Double.doubleToLongBits(1.0D), Double.doubleToLongBits(new CoerciveSanityChecker("foo", "${1.0D}").isDouble()))

        assertEquals(Double.doubleToLongBits(1E10D), Double.doubleToLongBits(new CoerciveSanityChecker("foo", 1E10D).isDouble()))
        assertEquals(Double.doubleToLongBits(1E10D), Double.doubleToLongBits(new CoerciveSanityChecker("foo", "1E10").isDouble()))
        assertEquals(Double.doubleToLongBits(1E10D), Double.doubleToLongBits(new CoerciveSanityChecker("foo", "${1E10}").isDouble()))
        assertEquals(Double.doubleToLongBits(1E10D), Double.doubleToLongBits(new CoerciveSanityChecker("foo", "1E10D").isDouble()))
        assertEquals(Double.doubleToLongBits(1E10D), Double.doubleToLongBits(new CoerciveSanityChecker("foo", "${1E10D}").isDouble()))

        assertEquals(Double.doubleToLongBits(1.0E10D), Double.doubleToLongBits(new CoerciveSanityChecker("foo", 1.0E10D).isDouble()))
        assertEquals(Double.doubleToLongBits(1.0E10D), Double.doubleToLongBits(new CoerciveSanityChecker("foo", "1.0E10D").isDouble()))
        assertEquals(Double.doubleToLongBits(1.0E10D), Double.doubleToLongBits(new CoerciveSanityChecker("foo", "${1.0E10D}").isDouble()))

        String failure

        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", null).isDouble()
        }

        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", 1).isDouble()
        }

        failure = shouldFail(SanityCheckException) {
            new CoerciveSanityChecker("foo", 1.0).isDouble()
        }
    }

    void testCheckFor01() {
        Integer result

        CoerciveSanityChecker checker = CoerciveSanityChecker.checkFor("foo", "20") {
            result = isInteger()
        }

        assertEquals(20, result)

        assertEquals("foo", checker.name)
        assertEquals("20", checker.entity)
        assertEquals("parameter", checker.classification)
        assertFalse(checker.allowPassOnNull)

        String fail
        fail = shouldFail(SanityCheckException) {
            CoerciveSanityChecker.checkFor("foo", "0x1") {
                isInteger()
            }
        }

        fail = shouldFail(SanityCheckException) {
            CoerciveSanityChecker.checkFor("foo", null) {
                isString()
                isNotEmpty()
            }
        }
    }

    void testCheckFor02() {
        Integer result

        CoerciveSanityChecker checker = CoerciveSanityChecker.checkFor("foo", "20", "bar") {
            result = isInteger()
        }

        assertEquals(20, result)

        assertEquals("foo", checker.name)
        assertEquals("20", checker.entity)
        assertEquals("bar", checker.classification)
        assertFalse(checker.allowPassOnNull)

        String fail
        fail = shouldFail(SanityCheckException) {
            CoerciveSanityChecker.checkFor("foo", "0x1", "bar") {
                isInteger()
            }
        }

        fail = shouldFail(SanityCheckException) {
            CoerciveSanityChecker.checkFor("foo", null, "bar") {
                isString()
                isNotEmpty()
            }
        }
    }

    void testCheckFor03() {
        Integer result

        CoerciveSanityChecker checker = CoerciveSanityChecker.checkFor("foo", null, true) {
            result = isInteger()
        }

        assertEquals(null, result)

        assertEquals("foo", checker.name)
        assertEquals(null, checker.entity)
        assertEquals("parameter", checker.classification)
        assertTrue(checker.allowPassOnNull)

        String fail = shouldFail(SanityCheckException) {
            CoerciveSanityChecker.checkFor("foo", null, true) {
                isNotNull()
                isNotEmpty()
            }
        }
    }

    void testCheckFor04() {
        Integer result

        CoerciveSanityChecker checker = CoerciveSanityChecker.checkFor("foo", null, "bar", true) {
            result = isInteger()
        }

        assertEquals(null, result)

        assertEquals("foo", checker.name)
        assertEquals(null, checker.entity)
        assertEquals("bar", checker.classification)
        assertTrue(checker.allowPassOnNull)

        String fail = shouldFail(SanityCheckException) {
            CoerciveSanityChecker.checkFor("foo", null, "bar", true) {
                isNotNull()
                isNotEmpty()
            }
        }
    }
}
