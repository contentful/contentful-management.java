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

package com.contentful.java.cma;
import com.contentful.java.cma.lib.TestCallback
import com.contentful.java.cma.lib.TestUtils
import com.contentful.java.cma.model.CMABulkAction
import com.contentful.java.cma.model.CMAEntities
import com.contentful.java.cma.model.CMAPayload
import com.google.gson.Gson
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.logging.LogManager
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class BulkActionsTests {
    private lateinit var server: MockWebServer
    private lateinit var client: CMAClient
    private lateinit var gson: Gson

    @Before
    fun setUp() {
        LogManager.getLogManager().reset()
        server = MockWebServer()
        server.start()

        client = CMAClient.Builder()
            .setAccessToken("token")
            .setCoreEndpoint(server.url("/").toString())
            .setUploadEndpoint(server.url("/").toString())
            .setSpaceId("configuredSpaceId")
            .setEnvironmentId("configuredEnvironmentId")
            .build()

        gson = CMAClient.createGson()
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun testPublishBulkAction() {
        val responseBody = TestUtils.fileToString("bulk_actions_fetch_response.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val payload = CMAPayload().setEntities(CMAEntities().setItems(emptyList())) // Setup payload as needed

        val result = TestCallback<CMABulkAction>().also { callback ->
            client.bulkActions().async().publish("configuredSpaceId", "configuredEnvironmentId", payload, callback)
        }

        assertNotNull(result)
        assertEquals("BulkActionId", result.toString())

        val recordedRequest = server.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces/configuredSpaceId/environments/configuredEnvironmentId/bulk_actions/publish", recordedRequest.path)
    }

    @Test
    fun testUnpublishBulkAction() {
        val responseBody = TestUtils.fileToString("bulk_actions_fetch_response.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val payload = CMAPayload().setEntities(CMAEntities().setItems(emptyList())) // Setup payload as needed
        val result = TestCallback<CMABulkAction>().also { callback ->
            client.bulkActions().async().unpublish("configuredSpaceId", "configuredEnvironmentId", payload, callback)
        }

        assertNotNull(result)
        assertEquals("BulkActionId", result.toString())

        val recordedRequest = server.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces/configuredSpaceId/environments/configuredEnvironmentId/bulk_actions/unpublish", recordedRequest.path)
    }

    @Test
    fun testFetchBulkAction() {
        val responseBody = TestUtils.fileToString("bulk_actions_fetch_response.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client.bulkActions().async()
                .fetch(
                    "configuredSpaceId",
                    "configuredEnvironmentId",
                    "bulkActionId", TestCallback()) as TestCallback)!!


        assertNotNull(result)
        assertEquals("bulk_action", result.action)

        val recordedRequest = server.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/configuredSpaceId/environments/configuredEnvironmentId/bulk_actions/actions/bulkActionId", recordedRequest.path)
    }

    @Test
    fun testValidateBulkAction() {
        val responseBody = TestUtils.fileToString("bulk_actions_fetch_response.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val payload = CMAPayload() // Setup payload as needed
        val result = TestCallback<CMABulkAction>().also { callback ->
            client.bulkActions().async().validate("configuredSpaceId", "configuredEnvironmentId", payload, callback)
        }.value

        assertNotNull(result)
        assertEquals("BulkActionId", result.id)

        val recordedRequest = server.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces/configuredSpaceId/environments/configuredEnvironmentId/bulk_actions/validate", recordedRequest.path)
    }
}