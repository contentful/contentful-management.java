/*
 * Copyright (C) 2018 Contentful GmbH
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

import com.contentful.java.cma.lib.TestCallback
import com.google.gson.JsonParser
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.junit.After as after
import org.junit.Before as before

// Extensions
fun <T : Any> assertTestCallback(cb: TestCallback<T>): T? {
    cb.await()
    assertNull(cb.error)
    if (cb.allowEmpty) {
        return null
    }
    assertNotNull(cb.value)
    return cb.value
}

fun assertJsonEquals(json1: String, json2: String) {
    val parser = JsonParser()
    assertTrue(parser.parse(json1).equals(parser.parse(json2)),
            "Expected:\n$json1\nActual:\n$json2\n")
}
