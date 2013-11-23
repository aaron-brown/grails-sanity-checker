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

        failure = shouldFail(IllegalArgumentException) {
            new SanityChecker("foo", null).isNotNull()
        }

        failure = shouldFail(IllegalArgumentException) {
            new SanityChecker("foo", null, true).isNotNull()
        }

        String string

        failure = shouldFail(IllegalArgumentException) {
            new SanityChecker("foo", string).isNotNull()
        }
    }

    void testIsNotEmptyOnIncompatibleEntity() {
        String failure

        failure = shouldFail(IllegalArgumentException) {
            new SanityChecker("foo", 12345).isNotEmpty()
        }
    }

    void testIsNotEmptyNullPreconditionCases() {
        new SanityChecker("foo", null, true).isNotEmpty()
        new SanityChecker("foo", null).isNotEmpty(true)

        String failure = shouldFail(IllegalArgumentException) {
            new SanityChecker("foo", null).isNotEmpty()
        }
    }

    void testIsNotEmptyOnString() {

        new SanityChecker("foo", " ").isNotEmpty()
        new SanityChecker("foo", ' ').isNotEmpty()
        new SanityChecker("foo", """ """).isNotEmpty()
        new SanityChecker("foo", ''' ''').isNotEmpty()

        String failure

        failure = shouldFail(IllegalArgumentException) {
            new SanityChecker("foo", "").isNotEmpty()
        }

        failure = shouldFail(IllegalArgumentException) {
            new SanityChecker("foo", '').isNotEmpty()
        }

        failure = shouldFail(IllegalArgumentException) {
            new SanityChecker("foo", """""").isNotEmpty()
        }

        failure = shouldFail(IllegalArgumentException) {
            new SanityChecker("foo", '''''').isNotEmpty()
        }
    }

    void testIsNotEmptyOnList() {
        new SanityChecker("foo", [1, 2, 3, "blue", "moon"]).isNotEmpty()

        String failure = shouldFail(IllegalArgumentException) {
            new SanityChecker("foo", []).isNotEmpty()
        }
    }

    void testIsNotEmptyOnMap() {

        new SanityChecker("foo", [one: 1, two: 2, three: 3, blue: "moon"]).isNotEmpty()

        String failure = shouldFail(IllegalArgumentException) {
            new SanityChecker("foo", [:]).isNotEmpty()
        }
    }

    void testIsStringNullPreconditionCases() {
        new SanityChecker("foo", null, true).isString()
        new SanityChecker("foo", null).isString(true)

        new SanityChecker("foo", "${null}", true).isString()
        new SanityChecker("foo", "${null}").isString(true)

        new SanityChecker("foo", "${}", true).isString()
        new SanityChecker("foo", "${}").isString(true)
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

        failure = shouldFail(IllegalArgumentException) {
            new SanityChecker("foo", null).isString()
        }

        failure = shouldFail(IllegalArgumentException) {
            new SanityChecker("foo", 1).isString()
        }

        failure = shouldFail(IllegalArgumentException) {
            new SanityChecker("foo", "${1}").isString()
        }

        failure = shouldFail(IllegalArgumentException) {
            new SanityChecker("foo", "${null}").isString()
        }

        failure = shouldFail(IllegalArgumentException) {
            new SanityChecker("foo", "${}").isString()
        }
    }
}
