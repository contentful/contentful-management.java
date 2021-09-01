/*
 * Copyright (C) 2019 Contentful GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.contentful.java.cma

import java.util.*
import kotlin.test.assertEquals
import org.junit.Test as test

class DefaultQueryParameterTests {
    val MAP_KEY = "some_awesome_key"
    val MAP_VALUE = "value"

    @test
    fun testEmptyMapWithDefaults() {
        val defaultMap = hashMapOf(Pair(MAP_KEY, MAP_VALUE))
        val targetMap = mapOf<String, String>()

        val resultingMap = DefaultQueryParameter.putIfNotSet(targetMap, defaultMap)

        assertEquals(resultingMap[MAP_KEY], MAP_VALUE)
    }

    @test
    fun testDoNotOverwriteValueWithDefaultValue() {
        val defaultMap = hashMapOf(Pair(MAP_KEY, MAP_VALUE))
        val targetMap = mapOf(Pair(MAP_KEY, "NON_DEFAULT_VALUE"))

        val resultingMap = DefaultQueryParameter.putIfNotSet(targetMap, defaultMap)

        assertEquals(resultingMap[MAP_KEY], "NON_DEFAULT_VALUE")
    }

    @test
    fun testDoNotChangeDifferentValues() {
        val defaultMap = hashMapOf(Pair(MAP_KEY + "_123", "SOMETHING"))
        val targetMap = mapOf(Pair(MAP_KEY, MAP_VALUE))

        val resultingMap = DefaultQueryParameter.putIfNotSet(targetMap, defaultMap)

        assertEquals(resultingMap[MAP_KEY], MAP_VALUE)
        assertEquals(resultingMap[MAP_KEY + "_123"], "SOMETHING")
    }
}