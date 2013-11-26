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
        SanityChecker checker = new SanityChecker("foo", "bar")

        assertEquals("foo", checker.name)
        assertEquals("bar", checker.entity)
        assertEquals("entity", checker.classification)
        assertFalse(checker.allowPassOnNull)
        assertTrue(checker.suppressPasses)
    }

    void testConstructor02() {
        SanityChecker checker = new SanityChecker("foo", "bar", "baz")

        assertEquals("foo", checker.name)
        assertEquals("bar", checker.entity)
        assertEquals("baz", checker.classification)
        assertFalse(checker.allowPassOnNull)
        assertTrue(checker.suppressPasses)
    }

    void testCheckOn01() {
        SanityChecker checker = new SanityChecker("foo", "bar")

        UUID uuid = checker.currentReport

        assertEquals("foo", checker.name)
        assertEquals("bar", checker.entity)
        assertEquals("entity", checker.classification)
        assertFalse(checker.allowPassOnNull)
        assertTrue(checker.suppressPasses)

        checker.checkOn("bar", "baz")
        assertEquals("bar", checker.name)
        assertEquals("baz", checker.entity)
        assertEquals("entity", checker.classification)
        assertFalse(uuid == checker.currentReport)
    }

    void testCheckOn02() {
        SanityChecker checker = new SanityChecker("foo", "bar", "baz")

        UUID uuid = checker.currentReport

        assertEquals("foo", checker.name)
        assertEquals("bar", checker.entity)
        assertEquals("baz", checker.classification)
        assertFalse(checker.allowPassOnNull)
        assertTrue(checker.suppressPasses)

        checker.checkOn("apple", "banana", "cucumber")
        assertEquals("apple", checker.name)
        assertEquals("banana", checker.entity)
        assertEquals("cucumber", checker.classification)
        assertFalse(uuid == checker.currentReport)
    }

    void testUnmodifiableSetters() {
        SanityChecker checker = new SanityChecker("foo", "bar")

        checker.name = "apple"
        checker.entity = "banana"
        checker.classification = "cucumber"
        checker.reports = []

        UUID uuid = UUID.randomUUID()

        checker.currentReport = uuid

        assertEquals("foo", checker.name)
        assertEquals("bar", checker.entity)
        assertEquals("entity", checker.classification)
        assertFalse(checker.reports.isEmpty())
        assertFalse(checker.currentReport == uuid)
    }

    void testIsNotNull() {
        assertTrue(new SanityChecker("foo", "hello").isNotNull())

        assertFalse(new SanityChecker("foo", String string).isNotNull())

        SanityChecker checker = new SanityChecker("foo", null)

        assertFalse(checker.isNotNull())

        checker.allowPassOnNull = true

        assertFalse(checker.isNotNull())
    }

    void testIsNotEmptyOnIncompatibleEntity() {
        assertFalse(new SanityChecker("foo", 12345).isNotEmpty())
    }

    void testIsNotEmptyPassIfNullCases() {
        SanityChecker checker

        checker = new SanityChecker("foo", null)
        checker.allowPassOnNull = true

        assertTrue(checker.isNotEmpty())
        assertTrue(checker.isNotEmpty(true))

        checker.checkOn("foo", "")
        assertFalse(checker.isNotEmpty())
        assertFalse(checker.isNotEmpty(true))

        checker.checkOn("foo", '')
        assertFalse(checker.isNotEmpty())
        assertFalse(checker.isNotEmpty(true))

        checker.checkOn("foo", """""")
        assertFalse(checker.isNotEmpty())
        assertFalse(checker.isNotEmpty(true))

        checker.checkOn("foo", '''''')
        assertFalse(checker.isNotEmpty())
        assertFalse(checker.isNotEmpty(true))

        checker.checkOn("foo", [])
        assertFalse(checker.isNotEmpty())
        assertFalse(checker.isNotEmpty(true))

        checker.checkOn("foo", [:])
        assertFalse(checker.isNotEmpty())
        assertFalse(checker.isNotEmpty(true))
    }

    void testIsNotEmpty() {
        assertTrue(new SanityChecker("foo", " ").isNotEmpty())
        assertTrue(new SanityChecker("foo", ' ').isNotEmpty())

        assertTrue(new SanityChecker("foo", """ """).isNotEmpty())
        assertTrue(new SanityChecker("foo", ''' ''').isNotEmpty())

        assertTrue(new SanityChecker("foo", [null]).isNotEmpty())
        assertTrue(new SanityChecker("foo", ["a"]).isNotEmpty())

        assertTrue(new SanityChecker("foo", [a: null]).isNotEmpty())
        assertTrue(new SanityChecker("foo", [a: "a"]).isNotEmpty())

        assertFalse(new SanityChecker("foo", null).isNotEmpty())
        assertFalse(new SanityChecker("foo", "").isNotEmpty())
        assertFalse(new SanityChecker("foo", '').isNotEmpty())
        assertFalse(new SanityChecker("foo", """""").isNotEmpty())
        assertFalse(new SanityChecker("foo", '''''').isNotEmpty())
        assertFalse(new SanityChecker("foo", []).isNotEmpty())
        assertFalse(new SanityChecker("foo", [:]).isNotEmpty())
    }

    void testIsBooleanPassIfNullCases() {
        SanityChecker checker = new SanityChecker("foo", null)
        checker.allowPassOnNull = true

        assertTrue(checker.isBoolean())
        assertTrue(checker.isBoolean(true))

        checker.checkOn("foo", 1)
        assertFalse(checker.isBoolean())
        assertFalse(checker.isBoolean(true))
    }

    void testIsBoolean() {

        assertTrue(new SanityChecker("foo", true).isBoolean())
        assertTrue(new SanityChecker("foo", false).isBoolean())
        assertTrue(new SanityChecker("foo", 1.asBoolean()).isBoolean())
        assertTrue(new SanityChecker("foo", 0.asBoolean()).isBoolean())

        assertFalse(new SanityChecker("foo", null).isBoolean())

        assertFalse(new SanityChecker("foo", 0).isBoolean())
        assertFalse(new SanityChecker("foo", 1).isBoolean())

        assertFalse(new SanityChecker("foo", "true").isBoolean())
        assertFalse(new SanityChecker("foo", "false").isBoolean())

        assertFalse(new SanityChecker("foo", "${true}").isBoolean())
        assertFalse(new SanityChecker("foo", "${false}").isBoolean())
    }

    void testIsStringPassIfNullCases() {
        SanityChecker checker = new SanityChecker("foo", null)
        checker.allowPassOnNull = true

        assertTrue(checker.isString())
        assertTrue(checker.isString(true))

        checker.checkOn("foo", "${null}")

        assertFalse(checker.isString())
        assertFalse(checker.isString(true))

        checker.checkOn("foo", "${}")

        assertFalse(checker.isString())
        assertFalse(checker.isString(true))
    }

    void testIsString() {

        assertTrue(new SanityChecker("foo", '').isString())
        assertTrue(new SanityChecker("foo", "").isString())
        assertTrue(new SanityChecker("foo", '''''').isString())
        assertTrue(new SanityChecker("foo", """""").isString())
        assertTrue(new SanityChecker("foo", 1 as String).isString())
        assertTrue(new SanityChecker("foo", "${1}" as String).isString())
        assertTrue(new SanityChecker("foo", "${null}" as String).isString())

        assertFalse(new SanityChecker("foo", null).isString())
        assertFalse(new SanityChecker("foo", 1).isString())
        assertFalse(new SanityChecker("foo", "${1}").isString())
        assertFalse(new SanityChecker("foo", "${null}").isString())
        assertFalse(new SanityChecker("foo", "${}").isString())
    }

    void testIsNumberPassIfNullCases() {
        SanityChecker checker = new SanityChecker("foo", null)
        checker.allowPassOnNull = true

        assertTrue(checker.isNumber())
        assertTrue(checker.isNumber(true))

        checker.checkOn("foo", "1")

        assertFalse(checker.isNumber())
        assertFalse(checker.isNumber(true))
    }

    void testIsNumber() {
        assertTrue(new SanityChecker("foo", 1).isNumber())
        assertTrue(new SanityChecker("foo", 1I).isNumber())
        assertTrue(new SanityChecker("foo", 1L).isNumber())
        assertTrue(new SanityChecker("foo", 1G).isNumber())
        assertTrue(new SanityChecker("foo", 1D).isNumber())
        assertTrue(new SanityChecker("foo", 1F).isNumber())
        assertTrue(new SanityChecker("foo", 1.0).isNumber())
        assertTrue(new SanityChecker("foo", 1.0G).isNumber())
        assertTrue(new SanityChecker("foo", 1.0D).isNumber())
        assertTrue(new SanityChecker("foo", 1.0F).isNumber())
        assertTrue(new SanityChecker("foo", 1E10).isNumber())
        assertTrue(new SanityChecker("foo", 1E10G).isNumber())
        assertTrue(new SanityChecker("foo", 1E10D).isNumber())
        assertTrue(new SanityChecker("foo", 1E10F).isNumber())
        assertTrue(new SanityChecker("foo", 1.0E10).isNumber())
        assertTrue(new SanityChecker("foo", 1.0E10G).isNumber())
        assertTrue(new SanityChecker("foo", 1.0E10D).isNumber())
        assertTrue(new SanityChecker("foo", 1.0E10F).isNumber())
        assertTrue(new SanityChecker("foo", 01).isNumber())
        assertTrue(new SanityChecker("foo", 01I).isNumber())
        assertTrue(new SanityChecker("foo", 01L).isNumber())
        assertTrue(new SanityChecker("foo", 01G).isNumber())
        assertTrue(new SanityChecker("foo", 0x1).isNumber())
        assertTrue(new SanityChecker("foo", 0x1I).isNumber())
        assertTrue(new SanityChecker("foo", 0x1L).isNumber())
        assertTrue(new SanityChecker("foo", 0x1G).isNumber())

        assertFalse(new SanityChecker("foo", null).isNumber())
        assertFalse(new SanityChecker("foo", "1").isNumber())
        assertFalse(new SanityChecker("foo", "${1}").isNumber())
    }

    void testIsIntegerPassIfNullCases() {
        SanityChecker checker = new SanityChecker("foo", null)
        checker.allowPassOnNull = true

        assertTrue(checker.isInteger())
        assertTrue(checker.isInteger(true))

        checker.checkOn("foo", "1")

        assertFalse(checker.isInteger())
        assertFalse(checker.isInteger(true))
    }

    void testIsInteger() {
        assertTrue(new SanityChecker("foo", 1).isInteger())
        assertTrue(new SanityChecker("foo", 1I).isInteger())
        assertTrue(new SanityChecker("foo", 01).isInteger())
        assertTrue(new SanityChecker("foo", 01I).isInteger())
        assertTrue(new SanityChecker("foo", 0x1).isInteger())
        assertTrue(new SanityChecker("foo", 0x1I).isInteger())

        assertFalse(new SanityChecker("foo", null).isInteger())
        assertFalse(new SanityChecker("foo", 1L).isInteger())
        assertFalse(new SanityChecker("foo", 1.0).isInteger())
        assertFalse(new SanityChecker("foo", Long.MAX_VALUE).isInteger())
        assertFalse(new SanityChecker("foo", 9223372036854775807).isInteger())
        assertFalse(new SanityChecker("foo", "1").isInteger())
        assertFalse(new SanityChecker("foo", "${1}").isInteger())
    }

    void testIsLongPassIfNullCases() {
        SanityChecker checker = new SanityChecker("foo", null)
        checker.allowPassOnNull = true

        assertTrue(checker.isLong())
        assertTrue(checker.isLong(true))

        checker.checkOn("foo", "1")

        assertFalse(checker.isLong())
        assertFalse(checker.isLong(true))
    }

    void testIsLong() {

        assertTrue(new SanityChecker("foo", 1L).isLong())
        assertTrue(new SanityChecker("foo", 01L).isLong())
        assertTrue(new SanityChecker("foo", 0x1L).isLong())

        assertFalse(new SanityChecker("foo", null).isLong())
        assertFalse(new SanityChecker("foo", 1).isLong())
        assertFalse(new SanityChecker("foo", 1.0).isLong())
        assertFalse(new SanityChecker("foo", Integer.MAX_VALUE).isLong())
        assertFalse(new SanityChecker("foo", "1L").isLong())
        assertFalse(new SanityChecker("foo", "${1L}").isLong())
    }

    void testIsBigDecimalPassIfNullCases() {
        SanityChecker checker = new SanityChecker("foo", null)
        checker.allowPassOnNull = true

        assertTrue(checker.isBigDecimal())
        assertTrue(checker.isBigDecimal(true))

        checker.checkOn("foo", "1")

        assertFalse(checker.isBigDecimal())
        assertFalse(checker.isBigDecimal(true))
    }

    void testIsBigDecimal() {

        assertTrue(new SanityChecker("foo", 1.0).isBigDecimal())
        assertTrue(new SanityChecker("foo", 1.0G).isBigDecimal())

        assertFalse(new SanityChecker("foo", null).isBigDecimal())
        assertFalse(new SanityChecker("foo", 1).isBigDecimal())
        assertFalse(new SanityChecker("foo", 1G).isBigDecimal())
        assertFalse(new SanityChecker("foo", Double.MAX_VALUE).isBigDecimal())
        assertFalse(new SanityChecker("foo", "1.0G").isBigDecimal())
        assertFalse(new SanityChecker("foo", "${1.0G}").isBigDecimal())
    }

    void testIsDoublePassIfNullCases() {
        SanityChecker checker = new SanityChecker("foo", null)
        checker.allowPassOnNull = true

        assertTrue(checker.isDouble())
        assertTrue(checker.isDouble(true))

        checker.checkOn("foo", "1")

        assertFalse(checker.isDouble())
        assertFalse(checker.isDouble(true))
    }

    void testIsDouble() {

        assertTrue(new SanityChecker("foo", 1D).isDouble())
        assertTrue(new SanityChecker("foo", 1.0D).isDouble())

        assertFalse(new SanityChecker("foo", null).isDouble())
        assertFalse(new SanityChecker("foo", 1).isDouble())
        assertFalse(new SanityChecker("foo", 1.0).isDouble())
        assertFalse(new SanityChecker("foo", "1D").isDouble())
        assertFalse(new SanityChecker("foo", "1.0D").isDouble())
        assertFalse(new SanityChecker("foo", "${1D}").isDouble())
        assertFalse(new SanityChecker("foo", "${1.0D}").isDouble())
    }

    void testIsListPassIfNullCases() {
        SanityChecker checker = new SanityChecker("foo", null)
        checker.allowPassOnNull = true

        assertTrue(checker.isList())
        assertTrue(checker.isList(true))

        checker.checkOn("foo", "1")

        assertFalse(checker.isList())
        assertFalse(checker.isList(true))
    }

    void testIsList() {

        assertTrue(new SanityChecker("foo", []).isList())
        assertTrue(new SanityChecker("foo", [1, 2, 3, "blue", "moon"]).isList())
        assertTrue(new SanityChecker("foo", 1..10).isList())
        assertTrue(new SanityChecker("foo", 'a'..'z').isList())
        assertTrue(new SanityChecker("foo", [[a: 'apple'],[a: 'apricot']].a).isList())

        assertFalse(new SanityChecker("foo", null).isList())
        assertFalse(new SanityChecker("foo", 1..10 as int[]).isList())
        assertFalse(new SanityChecker("foo", 'a'..'z' as String[]).isList())
        assertFalse(new SanityChecker("foo", 1).isList())
        assertFalse(new SanityChecker("foo", [:]).isList())
    }

    void testIsMapPassIfNullCases() {
        SanityChecker checker = new SanityChecker("foo", null)
        checker.allowPassOnNull = true

        assertTrue(checker.isMap())
        assertTrue(checker.isMap(true))

        checker.checkOn("foo", "1")

        assertFalse(checker.isMap())
        assertFalse(checker.isMap(true))
    }

    void testIsMap() {

        assertTrue(new SanityChecker("foo", [:]).isMap())
        assertTrue(new SanityChecker("foo", [a: 'apple', b: 'banana']).isMap())

        assertFalse(new SanityChecker("foo", null).isMap())
        assertFalse(new SanityChecker("foo", 1).isMap())
        assertFalse(new SanityChecker("foo", 1..10 as int[]).isMap())
        assertFalse(new SanityChecker("foo", []).isMap())
    }

    void testExactClassMatchPassIfNullCases() {
        SanityChecker checker = new SanityChecker("foo", null)
        checker.allowPassOnNull = true

        assertTrue(checker.exactClassMatch(Class))
        assertTrue(checker.exactClassMatch(Class, true))

        checker.checkOn("foo", 1)
        assertFalse(checker.exactClassMatch(String))
        assertFalse(checker.exactClassMatch(String, true))
    }

    void testExactClassMatch() {

        assertTrue(new SanityChecker("foo", SampleUserClass.SAMPLE_USER_CLASS).exactClassMatch(SampleUserClass))

        assertFalse(new SanityChecker("foo", null).exactClassMatch(SampleUserClass))

        assertFalse(new SanityChecker("foo", 1).exactClassMatch(SampleUserClass))
    }
}
