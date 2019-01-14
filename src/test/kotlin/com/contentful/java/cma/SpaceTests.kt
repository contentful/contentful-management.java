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

import com.contentful.java.cma.lib.ModuleTestUtils
import com.contentful.java.cma.lib.TestCallback
import com.contentful.java.cma.lib.TestUtils
import com.contentful.java.cma.model.CMASpace
import com.contentful.java.cma.model.CMAType
import com.google.gson.Gson
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import java.util.logging.LogManager
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import org.junit.Test as test

class SpaceTests {
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
                .setEnvironmentId("configuredEnvironmentId")
                .build()

        gson = CMAClient.createGson()
    }

    @After
    fun tearDown() {
        server!!.shutdown()
    }

    @test
    fun testCreate() {
        val requestBody = TestUtils.fileToString("space_create_request.json")
        val responseBody = TestUtils.fileToString("space_create_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.spaces().async().create(
                "xxx", TestCallback()) as TestCallback)!!

        assertEquals("spaceid", result.id)
        assertEquals("xxx", result.name)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces", recordedRequest.path)
        assertEquals(requestBody, recordedRequest.body.readUtf8())
    }

    @test
    fun testCreateWithDefaultLocale() {
        val requestBody = TestUtils.fileToString("space_create_with_locale_request.json")
        val responseBody = TestUtils.fileToString("space_create_with_locale_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val space: CMASpace = CMASpace().setDefaultLocale("de-DE").setName("foo")

        val result = assertTestCallback(client!!.spaces().async().create(
                space, TestCallback()) as TestCallback)!!

        assertEquals("spaceid", result.id)
        assertEquals("foo", result.name)
        assertNull(result.defaultLocale, "default locale will not be returned.")

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces", recordedRequest.path)
        assertEquals(requestBody, recordedRequest.body.readUtf8())
    }

    @test
    fun testCreateInOrg() {
        server!!.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        assertTestCallback(client!!.spaces().async().create(
                "whatever", "org", TestCallback()) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces", recordedRequest.path)
        assertEquals("org", recordedRequest.getHeader("X-Contentful-Organization"))
    }

    @test
    fun testCreateInOrgWithObject() {
        server!!.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        assertTestCallback(client!!.spaces().async().create(
                CMASpace().setName("foo"), "org", TestCallback()) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces", recordedRequest.path)
        assertEquals("org", recordedRequest.getHeader("X-Contentful-Organization"))
    }

    @test
    fun testCreateWithObject() {
        server!!.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        assertTestCallback(client!!.spaces().async().create(
                CMASpace().setName("name").setDefaultLocale("my locale"),
                "org", TestCallback()) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces", recordedRequest.path)
        assertEquals("org", recordedRequest.getHeader("X-Contentful-Organization"))
        assertEquals(32, recordedRequest.body.readUtf8().indexOf("my locale"))
    }

    @test
    fun testDelete() {
        val requestBody = ""
        server!!.enqueue(MockResponse().setResponseCode(204).setBody(requestBody))

        assertTestCallback(client!!.spaces().async().delete(
                "spaceid", TestCallback()) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("DELETE", recordedRequest.method)
        assertEquals("/spaces/spaceid", recordedRequest.path)
    }

    @test
    fun testDeleteWithObject() {
        val requestBody = ""
        server!!.enqueue(MockResponse().setResponseCode(204).setBody(requestBody))

        assertTestCallback(client!!.spaces().async().delete(
                CMASpace().setId("spaceid"),
                TestCallback()
        ) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("DELETE", recordedRequest.method)
        assertEquals("/spaces/spaceid", recordedRequest.path)
    }

    @test
    fun testFetchAll() {
        val responseBody = TestUtils.fileToString("space_fetch_all_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.spaces().async()
                .fetchAll(TestCallback()) as TestCallback)!!

        val items = result.items
        assertEquals(CMAType.Array, result.system.type)
        assertEquals(2, items.size)
        assertEquals(2, result.total)
        assertEquals(0, result.skip)
        assertEquals(25, result.limit)

        // Space #1
        var sys = items[0].system
        assertEquals(CMAType.Space, sys.type)
        assertEquals("id1", sys.id)
        assertEquals(1, sys.version)
        assertEquals("2014-03-21T08:43:52Z", sys.createdAt)
        assertEquals("2014-04-27T18:16:10Z", sys.updatedAt)
        assertEquals("space1", items[0].name)

        // Created By
        assertEquals(CMAType.Link, sys.createdBy.system.type)
        assertEquals(CMAType.User, sys.createdBy.system.linkType)
        assertEquals("user1", sys.createdBy.system.id)

        // Updated By
        assertEquals(CMAType.Link, sys.updatedBy.system.type)
        assertEquals(CMAType.User, sys.updatedBy.system.linkType)
        assertEquals("user1", sys.updatedBy.system.id)

        // Space #2
        sys = items[1].system
        assertEquals(CMAType.Space, sys.type)
        assertEquals("id2", sys.id)
        assertEquals(2, sys.version)
        assertEquals("2014-05-19T09:00:27Z", sys.createdAt)
        assertEquals("2014-07-09T07:48:24Z", sys.updatedAt)
        assertEquals("space2", items[1].name)

        // Created By
        assertEquals(CMAType.Link, sys.createdBy.system.type)
        assertEquals(CMAType.User, sys.createdBy.system.linkType)
        assertEquals("user2", sys.createdBy.system.id)

        // Updated By
        assertEquals(CMAType.Link, sys.updatedBy.system.type)
        assertEquals(CMAType.User, sys.updatedBy.system.linkType)
        assertEquals("user2", sys.updatedBy.system.id)

        // Request
        val request = server!!.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/spaces?limit=100", request.path)
    }

    @test
    fun testFetchWithId() {
        val responseBody = TestUtils.fileToString("space_fetch_one_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.spaces().async().fetchOne(
                "spaceid", TestCallback()) as TestCallback)!!

        val sys = result.system
        assertEquals(CMAType.Space, sys.type)
        assertEquals("id1", sys.id)
        assertEquals(1, sys.version)
        assertEquals("2014-03-21T08:43:52Z", sys.createdAt)
        assertEquals("2014-04-27T18:16:10Z", sys.updatedAt)
        assertEquals("space1", result.name)

        // Created By
        assertEquals(CMAType.Link, sys.createdBy.system.type)
        assertEquals(CMAType.User, sys.createdBy.system.linkType)
        assertEquals("user1", sys.createdBy.system.id)

        // Updated By
        assertEquals(CMAType.Link, sys.updatedBy.system.type)
        assertEquals(CMAType.User, sys.updatedBy.system.linkType)
        assertEquals("user1", sys.updatedBy.system.id)

        // Request
        val request = server!!.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/spaces/spaceid", request.path)
    }

    @test
    fun testUpdate() {
        val requestBody = TestUtils.fileToString("space_update_request.json")
        val responseBody = TestUtils.fileToString("space_update_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        var space = gson!!.fromJson(
                TestUtils.fileToString("space_update_object.json"),
                CMASpace::class.java)

        space.name = "newname"

        space = assertTestCallback(client!!.spaces().async().update(
                space, TestCallback()) as TestCallback)

        assertEquals(2, space.system.version)
        assertEquals("newname", space.name)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.method)
        assertEquals("/spaces/spaceid", recordedRequest.path)
        assertNotNull(recordedRequest.getHeader("X-Contentful-Version"))
        assertEquals(requestBody, recordedRequest.body.readUtf8())
    }

    @test(expected = Exception::class)
    fun testUpdateFailsWithoutVersion() {
        ModuleTestUtils.assertUpdateWithoutVersion {
            val space: CMASpace = CMASpace().setName("name").setId("id")
            client!!.spaces().update(space)
        }
    }
}