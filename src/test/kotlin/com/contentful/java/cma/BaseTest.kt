/*
 * Copyright (C) 2014 Contentful GmbH
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

import com.squareup.okhttp.mockwebserver.MockWebServer
import com.google.gson.Gson
import org.junit.Before as before
import org.junit.After as after
import com.google.gson.GsonBuilder
import com.contentful.java.cma.lib.TestCallback
import kotlin.test.assertNull
import kotlin.test.assertNotNull
import com.google.gson.JsonParser
import kotlin.test.assertTrue
import retrofit.RestAdapter
import com.contentful.java.cma.model.CMAResource
import java.util.HashMap

/**
 * BaseTest.
 */
open class BaseTest {
    var server: MockWebServer? = null
    var client: CMAClient? = null
    var gson: Gson? = null

    before fun setUp() {
        // MockWebServer
        server = MockWebServer()
        server!!.play()

        // Client
        client = CMAClient.Builder()
                .setAccessToken("token")
                .setEndpoint(server!!.getUrl("/").toString())
                .build()

        gson = CMAClient.createGson()
    }

    after fun tearDown() {
        server!!.shutdown()
    }

    fun <T> assertTestCallback(cb: TestCallback<T>): T {
        cb.await()
        assertNull(cb.error)
        if (cb.allowEmpty) {
            return null
        }
        assertNotNull(cb.value)
        return cb.value!!
    }

    fun assertJsonEquals(json1: String, json2: String) {
        val parser = JsonParser()
        assertTrue(parser.parse(json1).equals(parser.parse(json2)),
                "Expected:\n${json1}\nActual:\n${json2}\n")
    }
}

// Extensions
fun <T : CMAResource> T.setSpaceId(spaceId: String): T {
    var sys = getSys()

    if (sys == null) {
        sys = hashMapOf()
        setSys(sys)
    }

    sys.put("space", hashMapOf(Pair("sys", hashMapOf(Pair("id", spaceId)))))

    return this@setSpaceId
}

fun <T : CMAResource> T.setVersion(version: Double): T {
    var sys = getSys()
    if (sys == null) {
        sys = hashMapOf()
        setSys(sys)
    }

    sys.put("version", version);
    return this@setVersion
}