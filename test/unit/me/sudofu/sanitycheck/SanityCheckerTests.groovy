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
import me.sudofu.sanitycheck.SanityCheckException

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
        SanityChecker checker = new SanityChecker("foo", "bar")

        assertEquals("foo", checker.name)
        assertEquals("bar", checker.entity)
        assertEquals("parameter", checker.classification)
        assertFalse(checker.allowPassOnNull)
    }

    void testConstructor02() {
        SanityChecker checker = new SanityChecker("foo", "bar", "baz")

        assertEquals("foo", checker.name)
        assertEquals("bar", checker.entity)
        assertEquals("baz", checker.classification)
        assertFalse(checker.allowPassOnNull)
    }

    void testConstructor03() {
        SanityChecker checker = new SanityChecker("foo", "bar", true)

        assertEquals("foo", checker.name)
        assertEquals("bar", checker.entity)
        assertEquals("parameter", checker.classification)
        assertTrue(checker.allowPassOnNull)
    }

    void testConstructor04() {
        SanityChecker checker = new SanityChecker("foo", "bar", "baz", true)

        assertEquals("foo", checker.name)
        assertEquals("bar", checker.entity)
        assertEquals("baz", checker.classification)
        assertTrue(checker.allowPassOnNull)
    }

    void testIsNotNull() {

        new SanityChecker("foo", "hello").isNotNull()

        String failure

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", null).isNotNull()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", null, true).isNotNull()
        }

        String string

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", string).isNotNull()
        }
    }

    void testIsNotEmptyOnIncompatibleEntity() {
        String failure

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", 12345).isNotEmpty()
        }
    }

    void testIsNotEmptyPassIfNullCases() {
        new SanityChecker("foo", null, true).isNotEmpty()
        new SanityChecker("foo", null).isNotEmpty(true)

        String fail

        fail = shouldFail(SanityCheckException) {
            new SanityChecker("foo", "", true).isNotEmpty()
        }

        fail = shouldFail(SanityCheckException) {
            new SanityChecker("foo", "").isNotEmpty(true)
        }

        fail = shouldFail(SanityCheckException) {
            new SanityChecker("foo", '', true).isNotEmpty()
        }

        fail = shouldFail(SanityCheckException) {
            new SanityChecker("foo", '').isNotEmpty(true)
        }

        fail = shouldFail(SanityCheckException) {
            new SanityChecker("foo", """""", true).isNotEmpty()
        }

        fail = shouldFail(SanityCheckException) {
            new SanityChecker("foo", """""").isNotEmpty(true)
        }

        fail = shouldFail(SanityCheckException) {
            new SanityChecker("foo", '''''', true).isNotEmpty()
        }

        fail = shouldFail(SanityCheckException) {
            new SanityChecker("foo", '''''').isNotEmpty(true)
        }

        fail = shouldFail(SanityCheckException) {
            new SanityChecker("foo", [], true).isNotEmpty()
        }

        fail = shouldFail(SanityCheckException) {
            new SanityChecker("foo", []).isNotEmpty(true)
        }

        fail = shouldFail(SanityCheckException) {
            new SanityChecker("foo", [:], true).isNotEmpty()
        }

        fail = shouldFail(SanityCheckException) {
            new SanityChecker("foo", [:]).isNotEmpty(true)
        }
    }

    void testIsNotEmptyOnString() {

        new SanityChecker("foo", " ").isNotEmpty()
        new SanityChecker("foo", ' ').isNotEmpty()
        new SanityChecker("foo", """ """).isNotEmpty()
        new SanityChecker("foo", ''' ''').isNotEmpty()

        String failure

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", null).isNotEmpty()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", "").isNotEmpty()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", '').isNotEmpty()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", """""").isNotEmpty()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", '''''').isNotEmpty()
        }
    }

    void testIsNotEmptyOnList() {
        new SanityChecker("foo", [1, 2, 3, "blue", "moon"]).isNotEmpty()

        String failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", []).isNotEmpty()
        }
    }

    void testIsNotEmptyOnMap() {

        new SanityChecker("foo", [one: 1, two: 2, three: 3, blue: "moon"]).isNotEmpty()

        String failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", [:]).isNotEmpty()
        }
    }

    void testIsBooleanPassIfNullCases() {
        new SanityChecker("foo", null, true).isBoolean()
        new SanityChecker("foo", null).isBoolean(true)

        String fail

        fail = shouldFail(SanityCheckException) {
            new SanityChecker("foo", 1, true).isBoolean()
        }

        fail = shouldFail(SanityCheckException) {
            new SanityChecker("foo", 1).isBoolean(true)
        }
    }

    void testIsBoolean() {

        new SanityChecker("foo", true).isBoolean()
        new SanityChecker("foo", false).isBoolean()
        new SanityChecker("foo", 1.asBoolean()).isBoolean()
        new SanityChecker("foo", 0.asBoolean()).isBoolean()

        String failure

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", null).isBoolean()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", 0).isBoolean()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", 1).isBoolean()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", "true").isBoolean()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", "false").isBoolean()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", "${true}").isBoolean()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", "${false}").isBoolean()
        }
    }

    void testIsStringPassIfNullCases() {
        new SanityChecker("foo", null, true).isString()
        new SanityChecker("foo", null).isString(true)

        String fail

        fail = shouldFail(SanityCheckException) {
            new SanityChecker("foo", "${null}", true).isString()
        }

        fail = shouldFail(SanityCheckException) {
            new SanityChecker("foo", "${null}").isString(true)
        }

        fail = shouldFail(SanityCheckException) {
            new SanityChecker("foo", "${}", true).isString()
        }

        fail = shouldFail(SanityCheckException) {
            new SanityChecker("foo", "${}").isString(true)
        }
    }

    void testIsString() {

        new SanityChecker("foo", '').isString()
        new SanityChecker("foo", "").isString()
        new SanityChecker("foo", '''''').isString()
        new SanityChecker("foo", """""").isString()
        new SanityChecker("foo", 1 as String).isString()
        new SanityChecker("foo", "${1}" as String).isString()
        new SanityChecker("foo", "${null}" as String).isString()

        String failure

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", null).isString()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", 1).isString()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", "${1}").isString()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", "${null}").isString()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", "${}").isString()
        }
    }

    void testIsNumberPassIfNullCases() {
        new SanityChecker("foo", null, true).isNumber()
        new SanityChecker("foo", null).isNumber(true)

        String fail

        fail = shouldFail(SanityCheckException) {
            new SanityChecker("foo", "1", true).isNumber()
        }

        fail = shouldFail(SanityCheckException) {
            new SanityChecker("foo", "1").isNumber(true)
        }
    }

    void testIsNumber() {

        new SanityChecker("foo", 1).isNumber()
        new SanityChecker("foo", 1I).isNumber()
        new SanityChecker("foo", 1L).isNumber()
        new SanityChecker("foo", 1G).isNumber()
        new SanityChecker("foo", 1D).isNumber()
        new SanityChecker("foo", 1F).isNumber()
        new SanityChecker("foo", 1.0).isNumber()
        new SanityChecker("foo", 1.0G).isNumber()
        new SanityChecker("foo", 1.0D).isNumber()
        new SanityChecker("foo", 1.0F).isNumber()
        new SanityChecker("foo", 1E10).isNumber()
        new SanityChecker("foo", 1E10G).isNumber()
        new SanityChecker("foo", 1E10D).isNumber()
        new SanityChecker("foo", 1E10F).isNumber()
        new SanityChecker("foo", 1.0E10).isNumber()
        new SanityChecker("foo", 1.0E10G).isNumber()
        new SanityChecker("foo", 1.0E10D).isNumber()
        new SanityChecker("foo", 1.0E10F).isNumber()
        new SanityChecker("foo", 01).isNumber()
        new SanityChecker("foo", 01I).isNumber()
        new SanityChecker("foo", 01L).isNumber()
        new SanityChecker("foo", 01G).isNumber()
        new SanityChecker("foo", 0x1).isNumber()
        new SanityChecker("foo", 0x1I).isNumber()
        new SanityChecker("foo", 0x1L).isNumber()
        new SanityChecker("foo", 0x1G).isNumber()

        String failure

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", null).isNumber()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", "1").isNumber()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", "${1}").isNumber()
        }
    }

    void testIsIntegerPassIfNullCases() {
        new SanityChecker("foo", null, true).isInteger()
        new SanityChecker("foo", null).isInteger(true)

        String fail

        fail = shouldFail(SanityCheckException) {
            new SanityChecker("foo", "1", true).isInteger()
        }

        fail = shouldFail(SanityCheckException) {
            new SanityChecker("foo", "1").isInteger(true)
        }
    }

    void testIsInteger() {

        new SanityChecker("foo", 1).isInteger()
        new SanityChecker("foo", 1I).isInteger()
        new SanityChecker("foo", 01).isInteger()
        new SanityChecker("foo", 01I).isInteger()
        new SanityChecker("foo", 0x1).isInteger()
        new SanityChecker("foo", 0x1I).isInteger()

        String failure

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", null).isInteger()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", 1L).isInteger()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", 1.0).isInteger()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", Long.MAX_VALUE).isInteger()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", 9223372036854775807).isInteger()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", "1").isInteger()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", "${1}").isInteger()
        }
    }

    void testIsLongPassIfNullCases() {
        new SanityChecker("foo", null, true).isLong()
        new SanityChecker("foo", null).isLong(true)

        String fail

        fail = shouldFail(SanityCheckException) {
            new SanityChecker("foo", "1", true).isLong()
        }

        fail = shouldFail(SanityCheckException) {
            new SanityChecker("foo", "1").isLong(true)
        }
    }

    void testIsLong() {

        new SanityChecker("foo", 1L).isLong()
        new SanityChecker("foo", 01L).isLong()
        new SanityChecker("foo", 0x1L).isLong()

        String failure

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", null).isLong()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", 1).isLong()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", 1.0).isLong()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", Integer.MAX_VALUE).isLong()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", "1L").isLong()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", "${1L}").isLong()
        }
    }

    void testIsBigDecimalPassIfNullCases() {
        new SanityChecker("foo", null, true).isBigDecimal()
        new SanityChecker("foo", null).isBigDecimal(true)

        String fail

        fail = shouldFail(SanityCheckException) {
            new SanityChecker("foo", "1", true).isBigDecimal()
        }

        fail = shouldFail(SanityCheckException) {
            new SanityChecker("foo", "1").isBigDecimal(true)
        }
    }

    void testIsBigDecimal() {

        new SanityChecker("foo", 1.0).isBigDecimal()
        new SanityChecker("foo", 1.0G).isBigDecimal()

        String failure

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", null).isBigDecimal()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", 1).isBigDecimal()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", 1G).isBigDecimal()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", Double.MAX_VALUE).isBigDecimal()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", "1.0G").isBigDecimal()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", "${1.0G}").isBigDecimal()
        }
    }

    void testIsDoublePassIfNullCases() {
        new SanityChecker("foo", null, true).isDouble()
        new SanityChecker("foo", null).isDouble(true)

        String fail

        fail = shouldFail(SanityCheckException) {
            new SanityChecker("foo", "1", true).isDouble()
        }

        fail = shouldFail(SanityCheckException) {
            new SanityChecker("foo", "1").isDouble(true)
        }
    }

    void testIsDouble() {

        new SanityChecker("foo", 1D).isDouble()
        new SanityChecker("foo", 1.0D).isDouble()

        String failure

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", null).isDouble()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", 1).isDouble()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", 1.0).isDouble()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", "1D").isDouble()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", "1.0D").isDouble()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", "${1D}").isDouble()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", "${1.0D}").isDouble()
        }
    }

    void testIsListPassIfNullCases() {
        new SanityChecker("foo", null, true).isList()
        new SanityChecker("foo", null).isList(true)

        String fail

        fail = shouldFail(SanityCheckException) {
            new SanityChecker("foo", 1, true).isList()
        }

        fail = shouldFail(SanityCheckException) {
            new SanityChecker("foo", 1).isList(true)
        }
    }

    void testIsList() {

        new SanityChecker("foo", []).isList()
        new SanityChecker("foo", [1, 2, 3, "blue", "moon"]).isList()
        new SanityChecker("foo", 1..10).isList()
        new SanityChecker("foo", 'a'..'z').isList()
        new SanityChecker("foo", [[a: 'apple'],[a: 'apricot']].a).isList()

        String failure

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", null).isList()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", 1..10 as int[]).isList()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", 'a'..'z' as String[]).isList()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", 1).isList()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", [:]).isList()
        }
    }

    void testIsMapPassIfNullCases() {
        new SanityChecker("foo", null, true).isMap()
        new SanityChecker("foo", null).isMap(true)

        String fail

        fail = shouldFail(SanityCheckException) {
            new SanityChecker("foo", 1, true).isMap()
        }

        fail = shouldFail(SanityCheckException) {
            new SanityChecker("foo", 1).isMap(true)
        }
    }

    void testIsMap() {

        new SanityChecker("foo", [:]).isMap()
        new SanityChecker("foo", [a: 'apple', b: 'banana']).isMap()

        String failure

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", null).isMap()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", 1).isMap()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", 1..10 as int[]).isMap()
        }

        failure = shouldFail(SanityCheckException) {
            new SanityChecker("foo", []).isMap()
        }
    }

    void testCheckFor01() {
        SanityChecker checker = SanityChecker.checkFor("foo", [a: "apple", b: "banana"]) {
            isMap()
            isNotEmpty()
        }

        assertEquals("foo", checker.name)
        assertEquals([a: "apple", b: "banana"], checker.entity)
        assertEquals("parameter", checker.classification)
        assertFalse(checker.allowPassOnNull)

        String fail
        fail = shouldFail(SanityCheckException) {
            SanityChecker.checkFor("foo", [:]) {
                isMap()
                isNotEmpty()
            }
        }

        fail = shouldFail(SanityCheckException) {
            SanityChecker.checkFor("foo", null) {
                isMap()
                isNotEmpty()
            }
        }
    }

    void testCheckFor02() {
        SanityChecker checker = SanityChecker.checkFor("foo", [a: "apple", b: "banana"], "bar") {
            isMap()
            isNotEmpty()
        }

        assertEquals("foo", checker.name)
        assertEquals([a: "apple", b: "banana"], checker.entity)
        assertEquals("bar", checker.classification)
        assertFalse(checker.allowPassOnNull)

        String fail
        fail = shouldFail(SanityCheckException) {
            SanityChecker.checkFor("foo", [:], "bar") {
                isMap()
                isNotEmpty()
            }
        }

        fail = shouldFail(SanityCheckException) {
            SanityChecker.checkFor("foo", null, "bar") {
                isMap()
                isNotEmpty()
            }
        }
    }

    void testCheckFor03() {
        SanityChecker checker = SanityChecker.checkFor("foo", null, true) {
            isMap()
            isNotEmpty()
        }

        assertEquals("foo", checker.name)
        assertEquals(null, checker.entity)
        assertEquals("parameter", checker.classification)
        assertTrue(checker.allowPassOnNull)

        String fail
        fail = shouldFail(SanityCheckException) {
            SanityChecker.checkFor("foo", [:], true) {
                isMap()
                isNotEmpty()
            }
        }

        fail = shouldFail(SanityCheckException) {
            SanityChecker.checkFor("foo", null, true) {
                isNotNull()
                isMap()
                isNotEmpty()
            }
        }
    }

    void testCheckFor04() {
        SanityChecker checker = SanityChecker.checkFor("foo", null, "bar", true) {
            isMap()
            isNotEmpty()
        }

        assertEquals("foo", checker.name)
        assertEquals(null, checker.entity)
        assertEquals("bar", checker.classification)
        assertTrue(checker.allowPassOnNull)

        String fail
        fail = shouldFail(SanityCheckException) {
            SanityChecker.checkFor("foo", [:], "bar", true) {
                isMap()
                isNotEmpty()
            }
        }

        fail = shouldFail(SanityCheckException) {
            SanityChecker.checkFor("foo", null, "bar", true) {
                isNotNull()
                isMap()
                isNotEmpty()
            }
        }
    }
}
