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
import com.contentful.java.cma.model.*
import com.google.gson.Gson
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import java.util.logging.LogManager
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import org.junit.Test as test

class ContentTagsTests {
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
        // overwrite client to not use environments
        client = CMAClient.Builder()
                .setAccessToken("token")
                .setCoreEndpoint(server!!.url("/").toString())
                .setUploadEndpoint(server!!.url("/").toString())
                .setSpaceId("configuredSpaceId")
                .setEnvironmentId("configuredEnvironmentId")
                .build()

        gson = CMAClient.createGson()
    }

    @After
    fun tearDown() {
        server!!.shutdown()
    }

    @test
    fun testFetchOne() {
        val responseBody = TestUtils.fileToString("content_tags_one_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.tags().async()
                .fetchOne("nyCampaign", TestCallback()) as TestCallback)!!

        assertEquals("NY Campaign", result.name)

        assertNotNull(result.system.id)
        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/configuredSpaceId/environments/configuredEnvironmentId/tags/nyCampaign", recordedRequest.path)
    }

    @test
    fun testFetchOneWithConfiguredSpaceAndEnvironment() {
        val responseBody = TestUtils.fileToString("content_tags_one_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))
        assertTestCallback(client!!.tags().async()
                .fetchOne("customSpaceId","customEnvironmentId", "nyCampaign", TestCallback()) as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/customSpaceId/environments/customEnvironmentId/tags/nyCampaign", recordedRequest.path)
    }

    @test
    fun testFetchAll() {
        val responseBody = TestUtils.fileToString("content_tags_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.tags().async()
                .fetchAll(TestCallback()) as TestCallback)!!

        assertEquals(1, result.total)
        assertEquals(100, result.limit)
        assertEquals(0, result.skip)
        assertEquals(1, result.items.size)

        assertEquals("NY Campaign", result.items[0].name)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/configuredSpaceId/environments/configuredEnvironmentId/tags", recordedRequest.path)
    }

    @test
    fun testFetchAllWithConfiguredSpaceAndEnvironment() {
        val responseBody = TestUtils.fileToString("content_tags_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))
        val query = mutableMapOf("skip" to "0", "limit" to "100")
        val result = assertTestCallback(client!!.tags().async()
                .fetchAll("customSpaceId","customEnvironmentId", query, TestCallback()) as TestCallback)!!

        assertEquals(1, result.total)
        assertEquals(100, result.limit)
        assertEquals(0, result.skip)
        assertEquals(1, result.items.size)

        assertEquals("NY Campaign", result.items[0].name)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/customSpaceId/environments/customEnvironmentId/tags?skip=0&limit=100", recordedRequest.path)
    }

    @test
    fun testCreateNew() {
        val responseBody = TestUtils.fileToString("content_tags_one_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.tags().async()
                .create("configuredSpaceId",
                        "configuredEnvironmentId",
                        "nyCampaign",
                        "NY Campaign",
                        CMAVisibility.publicVisibility, TestCallback()) as TestCallback)!!

        assertEquals("NY Campaign", result.name)
        assertEquals(CMAVisibility.publicVisibility, result.system.visibility)
    }


    @test
    fun testDeleteOne() {
        val responseBody = TestUtils.fileToString("content_tags_delete.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        assertTestCallback(client!!.tags().async()
                .delete("spaceId","environmentId","tagId",
                        TestCallback()
                ) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("DELETE", recordedRequest.method)
        assertEquals("/spaces/spaceId/environments/environmentId/tags/tagId", recordedRequest.path)
    }
}