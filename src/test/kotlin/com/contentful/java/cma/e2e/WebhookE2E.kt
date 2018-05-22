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

package com.contentful.java.cma.e2e

import com.contentful.java.cma.CMAClient
import com.contentful.java.cma.assertTestCallback
import com.contentful.java.cma.lib.TestCallback
import com.contentful.java.cma.model.*
import org.junit.AfterClass
import org.junit.Test
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

open class WebhookE2E : Base() {
    companion object {
        protected const val WEBHOOK_NAME = "deleteMeAfterTest"

        @AfterClass
        @JvmStatic
        fun tearDownWebhooks() {
            val hooks = client.webhooks().fetchAll(SPACE_ID)
            for (hook in hooks.items) {
                if (hook.name.startsWith(WEBHOOK_NAME, ignoreCase = true)) {
                    client.webhooks().delete(hook)
                }
            }
        }
    }

    @Test
    fun testCreateWebhook() {
        val requestedWebhook = CMAWebhook()
                .setName(WEBHOOK_NAME + Math.random())
                .setUrl("http://example.com/${Math.random()}/foo")
                .addTopic(CMAWebhookTopic.All)
                .addHeader("key", "value")
                .setBasicAuthorization("user", "password")

        val callback = TestCallback<CMAWebhook>()
        client.webhooks().async().create(SPACE_ID, requestedWebhook, callback)
        assertTestCallback(callback)

        // Request
        val createdWebhook = callback.value!!

        assertEquals(requestedWebhook.name, createdWebhook.name)
        assertEquals(requestedWebhook.url, createdWebhook.url)
        assertEquals(requestedWebhook.topics, createdWebhook.topics)
        assertEquals(requestedWebhook.headers.size, createdWebhook.headers.size)
        assertEquals(requestedWebhook.user, createdWebhook.user)
        assertNotNull(createdWebhook.id)
    }

    @Test
    fun testCreateWithIdWebhook() {
        val methodName = Throwable().stackTrace.first().methodName
        val webhookId = "temporaryId"

        val requestedWebhook = CMAWebhook()
                .setId(webhookId)
                .setName(WEBHOOK_NAME)
                .setUrl("http://example.com/$methodName/${Math.random()}/foo")
                .addTopic(CMAWebhookTopic.EntrySave)
                .addHeader("key", "value")
                .setBasicAuthorization("user", "password")

        val callback = client.webhooks().async().create(
                SPACE_ID, requestedWebhook, TestCallback()) as TestCallback<CMAWebhook>
        assertTestCallback(callback)

        // Request
        val createdWebhook = callback.value!!

        assertEquals(requestedWebhook.name, createdWebhook.name)
        assertEquals(requestedWebhook.url, createdWebhook.url)
        assertEquals(requestedWebhook.topics, createdWebhook.topics)
        assertEquals(requestedWebhook.headers.size, createdWebhook.headers.size)
        assertEquals(1, createdWebhook.headers.size)
        assertEquals(requestedWebhook.user, createdWebhook.user)
        assertEquals(webhookId, createdWebhook.id)
    }

    @Test
    fun testDelete() {
        val methodName = Throwable().stackTrace.first().methodName

        val webhook = client.webhooks().create(
                SPACE_ID,
                CMAWebhook()
                        .setName(WEBHOOK_NAME + Math.random())
                        .setUrl("https://www.example.com/$methodName/${Math.random()}")
                        .addTopic(CMAWebhookTopic.All)
        )


        val result = client.webhooks().delete(webhook)
        assertEquals(204, result)
    }

    @Test
    fun testFetchAllWebhook() {
        val methodName = Throwable().stackTrace.first().methodName

        client.webhooks().create(
                SPACE_ID,
                CMAWebhook()
                        .setName(WEBHOOK_NAME + Math.random())
                        .setUrl("https://www.example.com/$methodName/${Math.random()}")
                        .addTopic(CMAWebhookTopic.All)
        )
        client.webhooks().create(
                SPACE_ID,
                CMAWebhook()
                        .setName(WEBHOOK_NAME + Math.random())
                        .setUrl("https://www.example.com/$methodName/${Math.random()}")
                        .addTopic(CMAWebhookTopic.All)
        )

        client.webhooks().create(
                SPACE_ID,
                CMAWebhook()
                        .setName(WEBHOOK_NAME + Math.random())
                        .setUrl("https://www.example.com/$methodName/${Math.random()}")
                        .addTopic(CMAWebhookTopic.All)
        )

        val result = assertTestCallback(client.webhooks().async().fetchAll(
                SPACE_ID, TestCallback()) as TestCallback<CMAArray<CMAWebhook>>)!!

        assertEquals(CMAType.Array, result.system.type)
        assertEquals(10, result.total)
        val items = result.items
        assertEquals(10, items.size)
        assertTrue { items[0].name.startsWith(WEBHOOK_NAME) }
    }

    @Test
    fun testFetchWithIdWebhook() {
        val methodName = Throwable().stackTrace.first().methodName

        val webhook = client.webhooks().create(
                SPACE_ID,
                CMAWebhook()
                        .setName(WEBHOOK_NAME + Math.random())
                        .setUrl("https://www.example.com/$methodName/${Math.random()}")
                        .addTopic(CMAWebhookTopic.All)
        )

        val result = assertTestCallback(client.webhooks().async().fetchOne(
                SPACE_ID, webhook.id,
                TestCallback()) as TestCallback<CMAWebhook>)

        // Request
        assertEquals(CMAType.WebhookDefinition, result!!.system.type)
        assertNotNull(result)
        assertTrue { result.name.startsWith(WEBHOOK_NAME) }
    }

    @Test
    fun testUpdateWebhook() {
        val methodName = Throwable().stackTrace.first().methodName

        val webhook = client.webhooks().create(
                SPACE_ID,
                CMAWebhook()
                        .setName(WEBHOOK_NAME + Math.random())
                        .setUrl("https://www.example.com/$methodName/${Math.random()}")
                        .addTopic(CMAWebhookTopic.All)
        )

        webhook.addHeader("yet-another-name", "header_value")
        assertTestCallback(client.webhooks().async().update(webhook
                , TestCallback(true)) as TestCallback<CMAWebhook>)

        webhook.addHeader("header-key", "header_value")
        webhook.setVersion(1)
        assertTestCallback(client.webhooks().async().update(webhook
                , TestCallback(true)) as TestCallback<CMAWebhook>)
    }

    @Test(expected = RuntimeException::class)
    fun testRetainsSysOnNetworkErrorWebhook() {
        val badClient = CMAClient.Builder()
                .setAccessToken("accesstoken")
                .setCoreCallFactory { throw IOException(it.url().toString(), IOException()) }
                .build()

        val webhook = CMAWebhook().setVersion(31337)
        try {
            badClient.webhooks().create(SPACE_ID, webhook)
        } catch (e: RuntimeException) {
            assertEquals(31337, webhook.version)
            throw e
        }
    }

    @Test
    fun testCallsOverviewWebhook() {
        val methodName = Throwable().stackTrace.first().methodName

        val webhook = client.webhooks().create(
                SPACE_ID,
                CMAWebhook()
                        .setName(WEBHOOK_NAME + Math.random())
                        .setUrl("https://www.example.com/$methodName/${Math.random()}")
                        .addTopic(CMAWebhookTopic.All)
        )

        val callback = client.webhooks().async().calls(
                webhook,
                TestCallback(true)) as TestCallback<CMAArray<CMAWebhookCall>>
        assertTestCallback(callback)

        val overview = callback.value!!
        assertEquals(0, overview.items.size)
    }

    @Test
    fun testCallDetailsWebhook() {
        val methodName = Throwable().stackTrace.first().methodName

        val webhook = client.webhooks().create(
                SPACE_ID,
                CMAWebhook()
                        .setName(WEBHOOK_NAME + Math.random())
                        .setUrl("https://www.example.com/$methodName/${Math.random()}")
                        .addTopic(CMAWebhookTopic.All)
        )

        // generate webhook calls
        val type = client.contentTypes().create(SPACE_ID, "master", CMAContentType().setName("Delete me CT"))
        client.contentTypes().delete(type)

        val calls = client.webhooks().calls(webhook)

        assertEquals(2, calls.items.size)
        assertEquals(404, calls.items[0].statusCode)
        assertEquals(404, calls.items[1].statusCode)

        val detail = client.webhooks().callDetails(calls.items[0])

        assertTrue { detail.url.startsWith("https://www.example.com/$methodName") }

        // health
        val callback = client.webhooks().async().health(
                webhook,
                TestCallback(true)) as TestCallback<CMAWebhookHealth>

        assertTestCallback(callback)
        assertNotNull(callback.value)

        val value = callback.value!!
        assertEquals(2, value.calls.total)
        assertEquals(0, value.calls.healthy)
    }

    @Test
    fun testInitialCallHealthWebhook() {
        val methodName = Throwable().stackTrace.first().methodName

        val webhook = client.webhooks().create(
                SPACE_ID,
                CMAWebhook()
                        .setName(WEBHOOK_NAME + Math.random())
                        .setUrl("https://www.example.com/$methodName/${Math.random()}")
                        .addTopic(CMAWebhookTopic.All)
        )

        val callback = client.webhooks().async().health(
                webhook,
                TestCallback(true)) as TestCallback<CMAWebhookHealth>

        assertTestCallback(callback)
        assertNotNull(callback.value)

        val value = callback.value!!
        assertEquals(0, value.calls.total)
        assertEquals(0, value.calls.healthy)
    }

}