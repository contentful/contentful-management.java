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

import com.contentful.java.cma.lib.TestCallback
import com.contentful.java.cma.lib.TestUtils
import com.contentful.java.cma.model.CMAWebhook
import com.contentful.java.cma.model.CMAWebhookTopic
import okhttp3.mockwebserver.MockResponse
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import org.junit.Test as test

class WebhookTests : BaseTest() {
    @test
    fun testCreate() {
        val requestBody = TestUtils.fileToString("webhook_create_request.json")
        val responseBody = TestUtils.fileToString("webhook_create_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val webhook = CMAWebhook()
                .setName("cma-created")
                .setUrl("http://lorempixel.com/200/200/cats")
                .addTopic(CMAWebhookTopic.All)
                .addHeader("key", "value")
                .setBasicAuthorization("user", "password")

        assertTestCallback(client!!.webhooks().async().create(
                "spaceid", webhook, TestCallback()) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces/spaceid/webhook_definitions", recordedRequest.path)
        assertEquals(requestBody, recordedRequest.utf8Body)
    }

    @test
    fun testCreateWithId() {
        val requestBody = TestUtils.fileToString("webhook_create_with_id_request.json")
        val responseBody = TestUtils.fileToString("webhook_create_with_id_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val webhook = CMAWebhook()
                .setId<CMAWebhook>("webhookid")
                .setName("cma-created")
                .setUrl("http://lorempixel.com/200/200/cats")
                .addTopic(CMAWebhookTopic.EntrySave)
                .addHeader("key", "value")
                .setBasicAuthorization("user", "password")

        assertTestCallback(client!!.webhooks().async().create(
                "spaceid", "webhookid", webhook, TestCallback()) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.method)
        assertEquals("/spaces/spaceid/webhook_definitions/webhookid", recordedRequest.path)
        assertEquals(requestBody, recordedRequest.utf8Body)
    }

    @test
    fun testDelete() {

        server!!.enqueue(MockResponse().setResponseCode(204))

        assertTestCallback(client!!.webhooks().async().delete(
                "spaceid", "webhookid", TestCallback(true)) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("DELETE", recordedRequest.method)
        assertEquals("/spaces/spaceid/webhook_definitions/webhookid", recordedRequest.path)
    }

    @test
    fun testFetchAll() {
        val responseBody = TestUtils.fileToString("webhook_fetch_all_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.webhooks().async().fetchAll(
                "spaceid", TestCallback()) as TestCallback)!!

        assertEquals("Array", result.sys["type"])
        assertEquals(3, result.total)
        val items = result.items
        assertEquals(3, items.size)
        assertEquals("cma-created", items[0].name)
        assertEquals("http://foo.bar/", items[0].url)
        assertEquals(4, items[0].topics.size)
        assertEquals(CMAWebhookTopic.AssetAll, items[0].topics[0])
        assertEquals(CMAWebhookTopic.ContentTypeCreate, items[0].topics[1])
        assertEquals(CMAWebhookTopic.ContentTypeSave, items[0].topics[2])
        assertEquals(CMAWebhookTopic.EntryAll, items[0].topics[3])

        assertEquals(1, items[0].headers.size)
        assertEquals("user", items[0].user)

        val request = server!!.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/spaces/spaceid/webhook_definitions", request.path)
    }

    @test
    fun testFetchWithId() {
        val responseBody = TestUtils.fileToString("webhook_fetch_id_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.webhooks().async().fetchOne(
                "spaceid", "webhookid",
                TestCallback()) as TestCallback)

        // Request
        val request = server!!.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/spaces/spaceid/webhook_definitions/webhookid", request.path)

        assertEquals("WebhookDefinition", result!!.sys["type"])
        assertNotNull(result)
        assertEquals("cma-created", result.name)
        assertEquals("http://foo.bar/", result.url)
        assertEquals(4, result.topics.size)
        assertEquals(1, result.headers.size)
        assertEquals("user", result.user)
    }

    @test
    fun testUpdate() {
        val requestBody = TestUtils.fileToString("webhook_update_request.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(requestBody))

        assertTestCallback(client!!.webhooks().async().update(CMAWebhook()
                .setId<CMAWebhook>("webhookid")
                .setSpaceId("spaceid")
                .setVersion(5.0)
                .addHeader("my_new_header_name", "header_value")
                , TestCallback(true)) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.method)
        assertEquals("/spaces/spaceid/webhook_definitions/webhookid", recordedRequest.path)
        assertEquals(recordedRequest.getHeader("X-Contentful-Version"), "5")
    }

    @org.junit.Test(expected = RuntimeException::class)
    fun testRetainsSysOnNetworkError() {
        val badClient = CMAClient.Builder()
                .setAccessToken("accesstoken")
                .setCoreCallFactory { throw IOException(it.url().toString(), IOException()) }
                .build()

        val webhook = CMAWebhook().setVersion(31337.0)
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
                "spaceid", "webhookid",
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

        assertTestCallback(client!!.webhooks().async().callDetails(
                "spaceid", "webhookid", "callid",
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
                "spaceid", "webhookid",
                TestCallback(true)) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/spaceid/webhooks/webhookid/health", recordedRequest.path)
    }

}