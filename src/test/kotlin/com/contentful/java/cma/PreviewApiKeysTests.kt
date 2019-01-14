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

import com.contentful.java.cma.lib.TestCallback
import com.contentful.java.cma.lib.TestUtils
import com.contentful.java.cma.model.CMAPreviewApiKey
import com.google.gson.Gson
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import java.util.logging.LogManager
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.Test as test

class PreviewApiKeysTests {
    var server: MockWebServer? = null
    var client: CMAClient? = null
    var gson: Gson? = null

    @Before
    fun setUp() {
        LogManager.getLogManager().reset()
        // MockWebServer
        server = MockWebServer()
        server!!.start()

        // Client
        client = CMAClient.Builder()
                .setAccessToken("token")
                .setCoreEndpoint(server!!.url("/").toString())
                .setUploadEndpoint(server!!.url("/").toString())
                .setSpaceId("configuredSpaceId")
                .build()

        gson = CMAClient.createGson()
    }

    @After
    fun tearDown() {
        server!!.shutdown()
    }

    @test
    fun testFetchAll() {
        val responseBody = TestUtils.fileToString("preview_apikeys_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.previewApiKeys().async()
                .fetchAll(TestCallback()) as TestCallback)!!

        assertEquals(2, result.total)
        assertEquals(25, result.limit)
        assertEquals(0, result.skip)

        val first = result.items[0]
        assertTrue(first is CMAPreviewApiKey)
        assertEquals("<token>", first.accessToken)
        assertEquals("5szEuycyXqACwhWgwNwX2m", first.id)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/configuredSpaceId/preview_api_keys", recordedRequest.path)
    }

    @test
    fun testFetchOne() {
        val responseBody = TestUtils.fileToString("preview_apikeys_get_one.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.previewApiKeys().async()
                .fetchOne("5szEuycyXqACwhWgwNwX2m", TestCallback()) as TestCallback)!!

        assertEquals("<token>", result.accessToken)
        assertEquals("5szEuycyXqACwhWgwNwX2m", result.id)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/configuredSpaceId/preview_api_keys/5szEuycyXqACwhWgwNwX2m",
                recordedRequest.path)
    }
}