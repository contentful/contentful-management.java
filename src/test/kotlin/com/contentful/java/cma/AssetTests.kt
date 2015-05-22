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

import org.junit.Test as test
import com.contentful.java.cma.lib.TestUtils
import com.squareup.okhttp.mockwebserver.MockResponse
import com.contentful.java.cma.lib.getBodyAsString
import kotlin.test.*
import retrofit.RetrofitError
import java.io.IOException
import com.contentful.java.cma.lib.TestCallback
import org.junit.Test
import com.contentful.java.cma.model.CMAAsset
import retrofit.RestAdapter
import com.contentful.java.cma.lib.ModuleTestUtils
import com.contentful.java.cma.model.CMAEntry

class AssetTests : BaseTest() {
    test fun testArchive() {
        val cli = CMAClient.Builder()
                .setAccessToken("token")
                .setEndpoint(server!!.getUrl("/").toString())
                .setCallbackExecutor { it.run() }
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build()

        val responseBody = TestUtils.fileToString("asset_archive_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))
        val asset = CMAAsset().setId("assetid").setSpaceId("spaceid")
        assertFalse(asset.isArchived())

        val result = assertTestCallback(cli.assets().async().archive(
                asset, TestCallback()) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/assets/assetid/archived", recordedRequest.getPath())
        assertTrue(result.isArchived())
    }

    test fun testCreate() {
        val requestBody = TestUtils.fileToString("asset_create_request.json")
        val responseBody = TestUtils.fileToString("asset_create_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val asset = CMAAsset()
                .setField("title", "title", "en-US")
                .setField("description", "description", "en-US")

        assertTestCallback(client!!.assets().async().create(
                "spaceid", asset, TestCallback()) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/assets", recordedRequest.getPath())
        assertEquals(requestBody, recordedRequest.getBodyAsString())
    }

    test fun testCreateWithId() {
        val requestBody = TestUtils.fileToString("asset_create_request.json")
        val responseBody = TestUtils.fileToString("asset_create_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val asset = CMAAsset()
                .setId("assetid")
                .setField("title", "title", "en-US")
                .setField("description", "description", "en-US")

        assertTestCallback(client!!.assets().async().create(
                "spaceid", asset, TestCallback()) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/assets/assetid", recordedRequest.getPath())
        assertEquals(requestBody, recordedRequest.getBodyAsString())
    }

    test fun testDelete() {
        server!!.enqueue(MockResponse().setResponseCode(200))
        assertTestCallback(client!!.assets().async().delete(
                "spaceid", "assetid", TestCallback()) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("DELETE", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/assets/assetid", recordedRequest.getPath())
    }

    test fun testFetchAll() {
        val responseBody = TestUtils.fileToString("asset_fetch_all_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.assets().async().fetchAll(
                "spaceid", TestCallback()) as TestCallback)

        assertEquals("Array", result.getSys()["type"])
        assertEquals(1, result.getTotal())
        val items = result.getItems()
        assertEquals(1, items.size())
        assertEquals(2, items[0].getFields().size())
        assertNotNull(items[0].getFields()["file"])
        assertNotNull(items[0].getFields()["title"])

        // Request
        val request = server!!.takeRequest()
        assertEquals("GET", request.getMethod())
        assertEquals("/spaces/spaceid/assets", request.getPath())
    }

    test fun testFetchWithId() {
        server!!.enqueue(MockResponse().setResponseCode(200))

        assertTestCallback(client!!.assets().async().fetchOne(
                "spaceid", "assetid",
                TestCallback(true)) as TestCallback)

        // Request
        val request = server!!.takeRequest()
        assertEquals("GET", request.getMethod())
        assertEquals("/spaces/spaceid/assets/assetid", request.getPath())
    }

    test fun testProcess() {
        server!!.enqueue(MockResponse().setResponseCode(200))

        assertTestCallback(client!!.assets().async().process(CMAAsset()
                .setId("assetid")
                .setSpaceId("spaceid"), "locale", TestCallback()) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/assets/assetid/files/locale/process", recordedRequest.getPath())
    }

    test fun testPublish() {
        val responseBody = TestUtils.fileToString("asset_publish_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val asset = CMAAsset().setId("assetid").setSpaceId("spaceid").setVersion(1.0)
        assertFalse(asset.isPublished())

        val result = assertTestCallback(client!!.assets().async().publish(
                asset, TestCallback()) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/assets/assetid/published", recordedRequest.getPath())
        assertNotNull(recordedRequest.getHeader("X-Contentful-Version"))
        assertTrue(result.isPublished())
    }

    test fun testUnArchive() {
        server!!.enqueue(MockResponse().setResponseCode(200))

        assertTestCallback(client!!.assets().async().unArchive(
                CMAAsset().setId("assetid").setSpaceId("spaceid"),
                TestCallback(true)) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("DELETE", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/assets/assetid/archived", recordedRequest.getPath())
    }

    test fun testUnPublish() {
        server!!.enqueue(MockResponse().setResponseCode(200))

        assertTestCallback(client!!.assets().async().unPublish(
                CMAAsset().setId("assetid").setSpaceId("spaceid"),
                TestCallback(true)) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("DELETE", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/assets/assetid/published", recordedRequest.getPath())
    }

    test fun testUpdate() {
        val requestBody = TestUtils.fileToString("asset_update_request.json")
        server!!.enqueue(MockResponse().setResponseCode(200))

        assertTestCallback(client!!.assets().async().update(CMAAsset()
                .setId("assetid")
                .setSpaceId("spaceid")
                .setVersion(1.0)
                .setField("file", linkedMapOf(
                        Pair("content_type", "image/jpeg"),
                        Pair("upload", "https://www.nowhere.com/image.jpg"),
                        Pair("fileName", "example.jpg")
                ), "en-US"), TestCallback(true)) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/assets/assetid", recordedRequest.getPath())
        assertNotNull(recordedRequest.getHeader("X-Contentful-Version"))
        assertEquals(requestBody, recordedRequest.getBodyAsString())
    }

    test(expected = javaClass<RetrofitError>())
    fun testRetainsSysOnNetworkError() {
        val badClient = CMAClient.Builder()
                .setAccessToken("accesstoken")
                .setClient { throw RetrofitError.unexpectedError(it.getUrl(), IOException()) }
                .build()

        val asset = CMAAsset().setVersion(31337.0)
        try {
            badClient.assets().create("spaceid", asset)
        } catch (e: RetrofitError) {
            assertEquals(31337, asset.getVersion())
            throw e
        }
    }

    test fun testNullVersionDoesNotThrow() {
        assertNull(CMAAsset().getVersion())
    }

    test(expected = javaClass<Exception>())
    fun testUpdateFailsWithoutVersion() {
        ModuleTestUtils.assertUpdateWithoutVersion {
            client!!.assets().update(CMAAsset().setId("aid").setSpaceId("spaceid"))
        }
    }
}