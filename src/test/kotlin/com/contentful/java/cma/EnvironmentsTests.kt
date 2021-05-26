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
import com.contentful.java.cma.model.CMAEnvironment
import com.contentful.java.cma.model.CMAEnvironmentStatus
import com.contentful.java.cma.model.CMAType
import com.google.gson.Gson
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import java.util.logging.LogManager
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.junit.Test as test

class EnvironmentsTests {
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
        val responseBody = TestUtils.fileToString("environments_create.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val environment = CMAEnvironment().setName("environment_name")

        val result = assertTestCallback(
                client!!
                        .environments()
                        .async()
                        .create(
                                "<space_id>",
                                environment,
                                TestCallback()
                        ) as TestCallback)!!

        assertEquals("d35qsfsq3ctp", result.id)
        assertEquals("<space_id>", result.spaceId)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces/%3Cspace_id%3E/environments", recordedRequest.path)
    }

    @test
    fun testCreateWithConfiguredSpaceAndEnvironment() {
        val responseBody = TestUtils.fileToString("environments_create.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val environment = CMAEnvironment().setName("environment_name")

        assertTestCallback(client!!
                .environments()
                .async()
                .create(
                        environment,
                        TestCallback()
                ) as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces/configuredSpaceId/environments", recordedRequest.path)
    }

    @test
    fun testCreateWithId() {
        val responseBody = TestUtils.fileToString("environments_create_with_id.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val environment = CMAEnvironment().apply {
            name = "environment_with_id"
            id = "new_id"
        }

        val result = assertTestCallback(
                client!!
                        .environments()
                        .async()
                        .create(
                                "<space_id>",
                                environment,
                                TestCallback()
                        ) as TestCallback)!!

        assertEquals("new_id", result.id)
        assertEquals("<space_id>", result.spaceId)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.method)
        assertEquals("/spaces/%3Cspace_id%3E/environments/new_id", recordedRequest.path)
    }

    @test
    fun testCreateFromOther() {
        val responseBody = TestUtils.fileToString("environments_create_from_id.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        // should be fetched from Contentful.
        val sourceEnvironment = CMAEnvironment().apply {
            id = "source"
            name = "source"
            setSpaceId<CMAEnvironment>("my_space")
        }

        val newEnvironment = CMAEnvironment().apply {
            name = "environment_from_id"
            id = "cloned_from_io"
        }

        val result = assertTestCallback(
                client!!
                        .environments()
                        .async()
                        .clone(
                                sourceEnvironment,
                                newEnvironment,
                                TestCallback()
                        ) as TestCallback)!!

        assertEquals("cloned_from_io", result.id)
        assertEquals("<space_id>", result.spaceId)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.method)
        assertTrue(recordedRequest.headers.names().contains("X-Contentful-Source-Environment"))
        assertEquals("source", recordedRequest.headers["X-Contentful-Source-Environment"])
        assertEquals("/spaces/configuredSpaceId/environments/cloned_from_io", recordedRequest.path)
    }

    @test
    fun testCreateFromOtherUsingBranch() {
        val responseBody = TestUtils.fileToString("environments_create_from_id.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        // should be fetched from Contentful.
        val sourceEnvironment = CMAEnvironment().apply {
            id = "source"
            name = "source"
            setSpaceId<CMAEnvironment>("my_space")
        }

        val newEnvironment = CMAEnvironment().apply {
            name = "environment_from_id"
            id = "cloned_from_io"
        }

        val result = assertTestCallback(
                client!!
                        .environments()
                        .async()
                        .branch(
                                sourceEnvironment,
                                newEnvironment,
                                TestCallback()
                        ) as TestCallback)!!

        assertEquals("cloned_from_io", result.id)
        assertEquals("<space_id>", result.spaceId)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.method)
        assertTrue(recordedRequest.headers.names().contains("X-Contentful-Source-Environment"))
        assertEquals("source", recordedRequest.headers["X-Contentful-Source-Environment"])
        assertEquals("/spaces/configuredSpaceId/environments/cloned_from_io", recordedRequest.path)
    }

    @test
    fun testCreateFromOtherWithoutTargetId() {
        val responseBody = TestUtils.fileToString("environments_create_from_id.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))


        // should be fetched from Contentful.
        val sourceEnvironment = CMAEnvironment().apply {
            id = "source"
            name = "source"
            setSpaceId<CMAEnvironment>("my_space")
        }

        val newEnvironment = CMAEnvironment().apply {
            name = "environment_from_id"
        }

        val result = assertTestCallback(
                client!!
                        .environments()
                        .async()
                        .clone(
                                sourceEnvironment,
                                newEnvironment,
                                TestCallback()
                        ) as TestCallback)!!

        assertEquals("cloned_from_io", result.id)
        assertEquals("<space_id>", result.spaceId)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertTrue(recordedRequest.headers.names().contains("X-Contentful-Source-Environment"))
        assertEquals("source", recordedRequest.headers["X-Contentful-Source-Environment"])
        assertEquals("/spaces/configuredSpaceId/environments", recordedRequest.path)
    }

    @test
    fun testDelete() {
        val fetchBody = TestUtils.fileToString("environments_get_one.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(fetchBody))

        val requestBody = ""
        server!!.enqueue(MockResponse().setResponseCode(204).setBody(requestBody))

        val environment = assertTestCallback(
                client!!
                        .environments()
                        .async()
                        .fetchOne(
                                "<space_id>",
                                "staging",
                                TestCallback()
                        ) as TestCallback)

        server!!.takeRequest()

        assertTestCallback(
                client!!
                        .environments()
                        .async()
                        .delete(
                                environment,
                                TestCallback()
                        ) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("DELETE", recordedRequest.method)
        assertEquals("/spaces/%3Cspace_id%3E/environments/staging", recordedRequest.path)
    }

    @test
    fun testFetchAll() {
        val responseBody = TestUtils.fileToString("environments_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(
                client!!
                        .environments()
                        .async()
                        .fetchAll(
                                "<space_id>",
                                TestCallback()
                        ) as TestCallback)!!

        val items = result.items
        assertEquals(CMAType.Array, result.system.type)
        assertEquals(6, items.size)
        assertEquals(6, result.total)
        assertEquals(0, result.skip)
        assertEquals(25, result.limit)

        // Environment #1
        var sys = items[0].system
        assertEquals(CMAType.Environment, sys.type)
        assertEquals("master", sys.id)
        assertEquals(1, sys.version)
        assertEquals("2017-12-07T10:24:16Z", sys.createdAt)
        assertEquals("2017-12-07T10:24:16Z", sys.updatedAt)
        assertEquals("master", items[0].name)
        assertEquals(CMAEnvironmentStatus.Ready, items[0].status)

        // Created By
        assertEquals(CMAType.Link, sys.createdBy.system.type)
        assertEquals(CMAType.User, sys.createdBy.system.linkType)
        assertEquals("<user_id>", sys.createdBy.system.id)

        // Updated By
        assertEquals(CMAType.Link, sys.updatedBy.system.type)
        assertEquals(CMAType.User, sys.updatedBy.system.linkType)
        assertEquals("<user_id>", sys.updatedBy.system.id)

        // Environment #2
        sys = items[1].system
        assertEquals(CMAType.Environment, sys.type)
        assertEquals("staging", sys.id)
        assertEquals(6, sys.version)
        assertEquals("2018-02-27T13:25:13Z", sys.createdAt)
        assertEquals("2018-02-27T13:25:37Z", sys.updatedAt)
        assertEquals("staging", items[1].name)
        assertEquals(CMAEnvironmentStatus.Queued, items[1].status)

        // Created By
        assertEquals(CMAType.Link, sys.createdBy.system.type)
        assertEquals(CMAType.User, sys.createdBy.system.linkType)
        assertEquals("<user_id>", sys.createdBy.system.id)

        // Updated By
        assertEquals(CMAType.Link, sys.updatedBy.system.type)
        assertEquals(CMAType.User, sys.updatedBy.system.linkType)
        assertEquals("<user_id>", sys.updatedBy.system.id)

        assertEquals(CMAEnvironmentStatus.Creating, items[2].status)
        assertEquals(CMAEnvironmentStatus.Failed, items[3].status)

        // Request
        val request = server!!.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/spaces/%3Cspace_id%3E/environments", request.path)
    }

    @test
    fun testFetchAllWithConfiguredSpaceAndEnvironment() {
        val responseBody = TestUtils.fileToString("environments_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        assertTestCallback(
                client!!
                        .environments()
                        .async()
                        .fetchAll(
                                TestCallback()
                        ) as TestCallback)!!

        // Request
        val request = server!!.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/spaces/configuredSpaceId/environments", request.path)
    }

    @test
    fun testFetchWithId() {
        val responseBody = TestUtils.fileToString("environments_get_one.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(
                client!!
                        .environments()
                        .async()
                        .fetchOne(
                                "<space_id>",
                                "staging",
                                TestCallback()
                        ) as TestCallback)!!

        val sys = result.system
        assertEquals(CMAType.Environment, sys.type)
        assertEquals("staging", result.id)
        assertEquals("staging", result.environmentId)
        assertEquals("staging", result.name)
        assertEquals(6, result.version)
        assertEquals("2018-02-27T13:25:13Z", sys.createdAt)
        assertEquals("2018-02-27T13:25:37Z", sys.updatedAt)

        // Created By
        assertEquals(CMAType.Link, sys.createdBy.system.type)
        assertEquals(CMAType.User, sys.createdBy.system.linkType)
        assertEquals("<user_id>", sys.createdBy.system.id)

        // Updated By
        assertEquals(CMAType.Link, sys.updatedBy.system.type)
        assertEquals(CMAType.User, sys.updatedBy.system.linkType)
        assertEquals("<user_id>", sys.updatedBy.system.id)

        // Request
        val request = server!!.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/spaces/%3Cspace_id%3E/environments/staging", request.path)
    }

    @test
    fun testFetchWithIdWithConfiguredSpaceAndEnvironment() {
        val responseBody = TestUtils.fileToString("environments_get_one.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        assertTestCallback(
                client!!
                        .environments()
                        .async()
                        .fetchOne(
                                "staging",
                                TestCallback()
                        ) as TestCallback)!!

        // Request
        val request = server!!.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/spaces/configuredSpaceId/environments/staging", request.path)
    }

    @test
    fun testUpdate() {
        val fetchBody = TestUtils.fileToString("environments_get_one.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(fetchBody))

        val requestBody = TestUtils.fileToString("environments_update.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(requestBody))

        val environment = assertTestCallback(
                client!!
                        .environments()
                        .async()
                        .fetchOne(
                                "spaceid",
                                "environment_id",
                                TestCallback()
                        ) as TestCallback)!!

        server!!.takeRequest()

        environment.name = "environment_new_name"

        val newEnvironment = assertTestCallback(
                client!!
                        .environments()
                        .async()
                        .update(
                                environment,
                                TestCallback()
                        ) as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.method)
        assertEquals("/spaces/%3Cspace_id%3E/environments/staging", recordedRequest.path)

        assertEquals("environment_new_name", newEnvironment.name)
    }

    @test(expected = Exception::class)
    fun testUpdateFailsWithoutVersion() {
        ModuleTestUtils.assertUpdateWithoutVersion {
            val environment = CMAEnvironment().setName("name").setId("id")
            client!!
                    .environments()
                    .update(environment)
        }
    }

    @test
    fun testNotPushedEnvironmentReturnsNullStatus() {
        assertNull(CMAEnvironment().setName("name").setId("id").status)
    }
}