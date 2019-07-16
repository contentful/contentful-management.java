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
import java.io.IOException
import java.util.logging.LogManager
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import org.skyscreamer.jsonassert.JSONAssert.assertEquals as assertEqualJsons
import org.junit.Test as test

class WebhookTests {
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
    fun testCreate() {
        val requestBody = TestUtils.fileToString("webhook_create_request.json")
        val responseBody = TestUtils.fileToString("webhook_create.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))
		
        val webhookTransformation = CMAWebhookTransformation()
                .setMethod("PUT")
                .setContentType("application/json; charset=utf-8")
                .setIncludeContentLength(true)
                .setBody("{ /payload/sys/id }: { /payload/fields/title }")

        val webhook = CMAWebhook()
                .setName("cma-created")
                .setUrl("https://contentful.com")
                .addTopic(CMAWebhookTopic.EntryAll)
                .addHeader("key", "value")
                .setBasicAuthorization("user", "password")
                .setTransformation(webhookTransformation)

        assertTestCallback(client!!.webhooks().async().create(
                "spaceid", webhook, TestCallback()) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces/spaceid/webhook_definitions", recordedRequest.path)
        assertEqualJsons(requestBody, recordedRequest.body.readUtf8(), false)
    }

    @test
    fun testCreateWithConfiguredSpaceAndEnvironment() {
        val responseBody = TestUtils.fileToString("webhook_create.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val webhook = CMAWebhook()
                .setName("cma-created")
                .setUrl("http://lorempixel.com/200/200/cats")
                .addTopic(CMAWebhookTopic.All)
                .addHeader("key", "value")
                .setBasicAuthorization("user", "password")

        assertTestCallback(client!!.webhooks().async().create(webhook, TestCallback())
                as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces/configuredSpaceId/webhook_definitions", recordedRequest.path)
    }

    @test
    fun testCreateWithId() {
        val requestBody = TestUtils.fileToString("webhook_create_with_id_request.json")
        val responseBody = TestUtils.fileToString("webhook_create_with_id_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val webhook = CMAWebhook()
                .setId("webhookid")
                .setName("cma-created")
                .setUrl("http://lorempixel.com/200/200/cats")
                .addTopic(CMAWebhookTopic.EntrySave)
                .addHeader("key", "value")
                .setBasicAuthorization("user", "password")

        assertTestCallback(client!!.webhooks().async().create(
                "spaceid", webhook, TestCallback()) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.method)
        assertEquals("/spaces/spaceid/webhook_definitions/webhookid", recordedRequest.path)
        assertEqualJsons(requestBody, recordedRequest.body.readUtf8(), true)
    }

    @test
    fun testDelete() {
    		val responseBody = TestUtils.fileToString("webhook_delete.json")
        server!!.enqueue(MockResponse().setResponseCode(204).setBody(responseBody))

        val webhook = CMAWebhook().apply {
            spaceId = "spaceid"
            id = "webhookid"
        }

        val callback: TestCallback<Int> = TestCallback(true)
        assertTestCallback(client!!.webhooks().async().delete(
                webhook, callback) as TestCallback)

        assertEquals(204, callback.value)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("DELETE", recordedRequest.method)
        assertEquals("/spaces/spaceid/webhook_definitions/webhookid", recordedRequest.path)
    }

    @test
    fun testFetchAll() {
        val responseBody = TestUtils.fileToString("webhook_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.webhooks().async().fetchAll(
                "spaceid", TestCallback()) as TestCallback)!!

        assertEquals(CMAType.Array, result.system.type)
        assertEquals(4, result.total)
        val items = result.items
        assertEquals(4, items.size)
        assertEquals("cma-created", items[0].name)
        assertEquals("https://contentful.com", items[0].url)
        assertEquals(1, items[0].topics.size)
        assertEquals(CMAWebhookTopic.EntryAll, items[0].topics[0])

        assertEquals(1, items[0].headers.size)
        assertEquals("user", items[0].user)

        val request = server!!.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/spaces/spaceid/webhook_definitions", request.path)
    }

    @test
    fun testFetchAllWithConfiguredSpaceAndEnvironment() {
        val responseBody = TestUtils.fileToString("webhook_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        assertTestCallback(client!!.webhooks().async().fetchAll(TestCallback()) as TestCallback)!!

        val request = server!!.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/spaces/configuredSpaceId/webhook_definitions", request.path)
    }

    @test
    fun testFetchAllWithQuery() {
        val responseBody = TestUtils.fileToString("webhook_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        assertTestCallback(client!!.webhooks().async().fetchAll(
                "spaceid",
                hashMapOf("limit" to "4"),
                TestCallback()) as TestCallback)!!

        val request = server!!.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/spaces/spaceid/webhook_definitions?limit=4", request.path)
    }

    @test
    fun testFetchAllWithQueryWithConfiguredSpaceAndEnvironment() {
        val responseBody = TestUtils.fileToString("webhook_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        assertTestCallback(client!!.webhooks().async().fetchAll(
                hashMapOf("limit" to "4"),
                TestCallback()) as TestCallback)!!

        val request = server!!.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/spaces/configuredSpaceId/webhook_definitions?limit=4", request.path)
    }

    @test
    fun testFetchWithId() {
        val responseBody = TestUtils.fileToString("webhook_get_one.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.webhooks().async().fetchOne(
                "spaceid", "webhookid",
                TestCallback()) as TestCallback)

        // Request
        val request = server!!.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/spaces/spaceid/webhook_definitions/webhookid", request.path)

        assertEquals(CMAType.WebhookDefinition, result!!.system.type)
        assertNotNull(result)
        assertEquals("cma-created", result.name)
        assertEquals("https://contentful.com", result.url)
        assertEquals(1, result.topics.size)
        assertEquals(1, result.headers.size)
        assertEquals("user", result.user)
    }

    @test
    fun testFetchWithIdWithConfiguredSpaceAndEnvironment() {
        val responseBody = TestUtils.fileToString("webhook_get_one.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        assertTestCallback(client!!.webhooks().async().fetchOne("webhookid", TestCallback())
                as TestCallback)

        // Request
        val request = server!!.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/spaces/configuredSpaceId/webhook_definitions/webhookid", request.path)
    }

    @test
    fun testUpdate() {
    		val requestBody = TestUtils.fileToString("webhook_update_request.json")
        val responseBody = TestUtils.fileToString("webhook_update.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))
		
        val webhookTransformation = CMAWebhookTransformation()
                .setBody("{ /payload/sys/id }: { /payload/fields/new }")

        assertTestCallback(client!!.webhooks().async().update(CMAWebhook()
                .setId("webhookid")
                .setSpaceId("spaceid")
                .setVersion(3)
                .addHeader("my_new_header_name", "header_value")
                .setTransformation(webhookTransformation)
                , TestCallback(true)) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.method)
        assertEquals("/spaces/spaceid/webhook_definitions/webhookid", recordedRequest.path)
        assertEquals(recordedRequest.getHeader("X-Contentful-Version"), "3")
        assertEqualJsons(requestBody, recordedRequest.body.readUtf8(), false)
    }

    @test(expected = RuntimeException::class)
    fun testRetainsSysOnNetworkError() {
        val badClient = CMAClient.Builder()
                .setAccessToken("accesstoken")
                .setCoreCallFactory { throw IOException(it.url().toString(), IOException()) }
                .build()

        val webhook = CMAWebhook().setVersion(31337)
        try {
            badClient.webhooks().create("spaceid", webhook)
        } catch (e: RuntimeException) {
            assertEquals(31337, webhook.version)
            throw e
        }
    }

    @test
    fun testCallsOverview() {
        val responseBody = TestUtils.fileToString("webhook_calls_overview_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        assertTestCallback(client!!.webhooks().async().calls(
                CMAWebhook().setSpaceId("spaceid").setId("webhookid"),
                TestCallback(true)) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/spaceid/webhooks/webhookid/calls", recordedRequest.path)
    }

    @test
    fun testCallDetails() {
        val responseBody = TestUtils.fileToString("webhook_calls_detail_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val call: CMAWebhookCall = CMAWebhookCall().setSpaceId("spaceid").setId("callid")
        call.system.setCreatedBy(CMALink(CMAType.Webhook).setId("webhookid"))

        assertTestCallback(client!!.webhooks().async().callDetails(
                call,
                TestCallback(true)) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/spaceid/webhooks/webhookid/calls/callid", recordedRequest.path)
    }

    @test
    fun testCallHealth() {
        val responseBody = TestUtils.fileToString("webhook_health_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        assertTestCallback(client!!.webhooks().async().health(
                CMAWebhook().setSpaceId("spaceid").setId("webhookid"),
                TestCallback(true)) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/spaceid/webhooks/webhookid/health", recordedRequest.path)
    }

	@test
    fun testWebhookToString() {
        val webhook = CMAWebhook()
                .setId("helpText")
                .setName("Help text")

        assertTrue(webhook.toString().contains("id = helpText"))
    }

	@test
    fun testWebhookTransformationToString() {
		val webhookTransformation = CMAWebhookTransformation()
                .setMethod("PUT")
                .setContentType("application/json; charset=utf-8")

        assertTrue(webhookTransformation.toString().contains("method=PUT"))
    }

    @test
    fun testWebhookTransformation() {
		val webhookTransformation = CMAWebhookTransformation()
                .setMethod("PUT")
                .setContentType("application/json; charset=utf-8")
                .setIncludeContentLength(true)
                .setBody("{ /payload/test }: { /payload/test }")

        assertEquals("PUT", webhookTransformation.getMethod())
		assertEquals("application/json; charset=utf-8", webhookTransformation.getContentType())
		assertEquals(true, webhookTransformation.getIncludeContentLength())
		assertEquals("{ /payload/test }: { /payload/test }", webhookTransformation.getBody())
    }
}
