/*
 * Copyright (C) 2017 Contentful GmbH
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
import org.junit.Test as tests

class DefaultQueryParameterTests {
    val MAP_KEY = "some_awesome_key"
    val MAP_VALUE = "value"

    @org.junit.Test fun testEmptyMapWithDefaults() {
        val defaultMap = hashMapOf(Pair(MAP_KEY, MAP_VALUE))
        val targetMap = HashMap<String, String>()

        DefaultQueryParameter.putIfNotSet(targetMap, defaultMap)

        assertEquals(targetMap[MAP_KEY], MAP_VALUE)
    }

    @org.junit.Test fun testDoNotOverwriteValueWithDefaultValue() {
        val defaultMap = hashMapOf(Pair(MAP_KEY, MAP_VALUE))
        val targetMap = hashMapOf(Pair(MAP_KEY, "NON_DEFAULT_VALUE"))

        DefaultQueryParameter.putIfNotSet(targetMap, defaultMap)

        assertEquals(targetMap[MAP_KEY], "NON_DEFAULT_VALUE")
    }

    @org.junit.Test fun testDoNotChangeDifferentValues() {
        val defaultMap = hashMapOf(Pair(MAP_KEY + "_123", "SOMETHING"))
        val targetMap = hashMapOf(Pair(MAP_KEY, MAP_VALUE))

        DefaultQueryParameter.putIfNotSet(targetMap, defaultMap)

        assertEquals(targetMap[MAP_KEY], MAP_VALUE)
        assertEquals(targetMap[MAP_KEY + "_123"], "SOMETHING")
    }
}