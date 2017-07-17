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

import com.contentful.java.cma.lib.ModuleTestUtils
import com.contentful.java.cma.lib.TestCallback
import com.contentful.java.cma.lib.TestUtils
import com.contentful.java.cma.model.CMAAsset
import com.contentful.java.cma.model.CMAAssetFile
import com.contentful.java.cma.model.CMALink
import com.contentful.java.cma.model.CMAType
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import java.io.IOException
import kotlin.test.*
import org.junit.Test as test

class AssetTests : BaseTest() {
    @test
    fun testArchive() {
        val cli = CMAClient.Builder()
                .setAccessToken("token")
                .setCoreEndpoint(server!!.url("/").toString())
                .setCallbackExecutor { it.run() }
                .build()

        val responseBody = TestUtils.fileToString("asset_archive_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val asset = CMAAsset()
                .setSpaceId("spaceid")
                .setId("assetid")
        assertFalse(asset.isArchived)

        val result = assertTestCallback(cli.assets().async().archive(
                asset, TestCallback()) as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.method)
        assertEquals("/spaces/spaceid/assets/assetid/archived", recordedRequest.path)
        assertTrue(result.isArchived)
    }

    @test
    fun testCreate() {
        val requestBody = TestUtils.fileToString("asset_create_request.json")
        val responseBody = TestUtils.fileToString("asset_create_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val asset = CMAAsset()
        asset.fields.localize("en-US")
                .setTitle("title")
                .setDescription("description")

        assertTestCallback(client!!.assets().async().create(
                "spaceid", asset, TestCallback()) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces/spaceid/assets", recordedRequest.path)
        assertEquals(requestBody, recordedRequest.body.readUtf8())
    }

    @test
    fun testCreateWithId() {
        val requestBody = TestUtils.fileToString("asset_create_request.json")
        val responseBody = TestUtils.fileToString("asset_create_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val asset = CMAAsset()
                .setId("assetid")
        asset.fields.localize("en-US")
                .setTitle("title")
                .setDescription("description")

        assertTestCallback(client!!.assets().async().create(
                "spaceid", asset, TestCallback()) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.method)
        assertEquals("/spaces/spaceid/assets/assetid", recordedRequest.path)
        assertEquals(requestBody, recordedRequest.body.readUtf8())
    }

    @test
    fun testDelete() {
        val responseBody = ""
        server!!.enqueue(MockResponse().setResponseCode(204).setBody(responseBody))
        assertTestCallback(client!!.assets().async().delete(
                "spaceid", "assetid", TestCallback()) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("DELETE", recordedRequest.method)
        assertEquals("/spaces/spaceid/assets/assetid", recordedRequest.path)
    }

    @test
    fun testFetchAll() {
        val responseBody = TestUtils.fileToString("asset_fetch_all_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.assets().async().fetchAll(
                "spaceid", TestCallback()) as TestCallback)!!

        assertEquals(CMAType.Array, result.system.type)
        assertEquals(1, result.total)
        val items = result.items
        assertEquals(1, items.size)
        assertNull(items[0].fields.localize("en-US").description)
        assertNotNull(items[0].fields.localize("en-US").file)
        assertNotNull(items[0].fields.localize("en-US").title)

        // Request
        val request = server!!.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/spaces/spaceid/assets?limit=100", request.path)
    }

    @test
    fun testFetchAllWithQuery() {
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(
                TestUtils.fileToString("asset_fetch_all_response.json")))

        val query = hashMapOf(Pair("skip", "1"), Pair("limit", "2"), Pair("content_type", "foo"))

        assertTestCallback(client!!.assets().async().fetchAll(
                "spaceid", query, TestCallback()) as TestCallback)

        // Request
        val request = server!!.takeRequest()
        val url = HttpUrl.parse(server!!.url(request.path).toString())!!
        assertEquals("1", url.queryParameter("skip"))
        assertEquals("2", url.queryParameter("limit"))
        assertEquals("foo", url.queryParameter("content_type"))
    }

    @test
    fun testFetchWithId() {
        val responseBody = TestUtils.fileToString("asset_publish_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        assertTestCallback(client!!.assets().async().fetchOne(
                "spaceid", "assetid",
                TestCallback(true)) as TestCallback)

        // Request
        val request = server!!.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/spaces/spaceid/assets/assetid", request.path)
    }

    @test
    fun testProcess() {
        val responseBody = ""
        server!!.enqueue(MockResponse().setResponseCode(204).setBody(responseBody))

        val asset = CMAAsset()
                .setId("assetid")
                .setSpaceId("spaceid")

        assertTestCallback(client!!.assets().async().process(asset,
                "locale", TestCallback()) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.method)
        assertEquals("/spaces/spaceid/assets/assetid/files/locale/process", recordedRequest.path)
    }

    @test
    fun testPublish() {
        val responseBody = TestUtils.fileToString("asset_publish_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val asset = CMAAsset()
                .setId("assetid")
                .setSpaceId("spaceid")
                .setVersion(1)
        assertFalse(asset.isPublished)

        val result = assertTestCallback(client!!.assets().async().publish(
                asset, TestCallback()) as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.method)
        assertEquals("/spaces/spaceid/assets/assetid/published", recordedRequest.path)
        assertNotNull(recordedRequest.getHeader("X-Contentful-Version"))
        assertTrue(result.isPublished)
    }

    @test
    fun testUnArchive() {
        val responseBody = TestUtils.fileToString("asset_publish_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val asset = CMAAsset()
                .setId("assetid")
                .setSpaceId("spaceid")

        assertTestCallback(client!!.assets().async().unArchive(
                asset,
                TestCallback(true)) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("DELETE", recordedRequest.method)
        assertEquals("/spaces/spaceid/assets/assetid/archived", recordedRequest.path)
    }

    @test
    fun testUnPublish() {
        val responseBody = TestUtils.fileToString("asset_publish_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        assertTestCallback(client!!.assets().async().unPublish(
                CMAAsset().setId("assetid").setSpaceId("spaceid"),
                TestCallback(true)) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("DELETE", recordedRequest.method)
        assertEquals("/spaces/spaceid/assets/assetid/published", recordedRequest.path)
    }

    @test
    fun testUpdate() {
        val requestBody = TestUtils.fileToString("asset_update_request.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(requestBody))

        val asset = CMAAsset()
        asset.setId("assetid")
                .setSpaceId("spaceid")
                .setVersion(1)
        asset.fields.localize("en-US")
                .file = CMAAssetFile()
                .setContentType("image/jpeg")
                .setUploadUrl("https://www.nowhere.com/image.jpg")
                .setFileName("example.jpg")

        assertTestCallback(client!!.assets().async().update(asset,
                TestCallback(true)) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.method)
        assertEquals("/spaces/spaceid/assets/assetid", recordedRequest.path)
        assertNotNull(recordedRequest.getHeader("X-Contentful-Version"))
        assertEquals(requestBody, recordedRequest.body.readUtf8())
    }

    @test
    fun testUpdateWithFluidInterface() {
        val requestBody = TestUtils.fileToString("asset_update_request.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(requestBody))

        val asset = CMAAsset()
                .setId("assetid")
                .setSpaceId("spaceid")
                .setVersion(1)

        asset.fields.localize("en-US")
                .file = CMAAssetFile()
                .setContentType("image/jpeg")
                .setUploadUrl("https://www.nowhere.com/image.jpg")
                .setFileName("example.jpg")

        assertTestCallback(client!!.assets().async().update(asset, TestCallback(true)) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.method)
        assertEquals("/spaces/spaceid/assets/assetid", recordedRequest.path)
        assertNotNull(recordedRequest.getHeader("X-Contentful-Version"))
        assertEquals(requestBody, recordedRequest.body.readUtf8())
    }

    @org.junit.Test(expected = RuntimeException::class)
    fun testRetainsSysOnNetworkError() {
        val badClient = CMAClient.Builder()
                .setAccessToken("accesstoken")
                .setCoreCallFactory { throw IOException(it.url().toString(), IOException()) }
                .build()

        val asset = CMAAsset().setVersion(31337)
        try {
            badClient.assets().create("spaceid", asset)
        } catch (e: RuntimeException) {
            assertEquals(31337, asset.version)
            throw e
        }
    }

    @test
    fun testNullVersionDoesNotThrow() {
        assertNull(CMAAsset().version)
    }

    @org.junit.Test(expected = Exception::class)
    fun testUpdateFailsWithoutVersion() {
        ModuleTestUtils.assertUpdateWithoutVersion {
            client!!.assets().update(CMAAsset().setId("aid").setSpaceId("spaceid"))
        }
    }

    @org.junit.Test
    fun testDoNotChangeSysOnException() {
        val asset = CMAAsset().setId("aid").setSpaceId("spaceid")
        val system = asset.system

        try {
            ModuleTestUtils.assertUpdateWithoutVersion {
                client!!.assets().update(asset)
            }
        } catch (e: Exception) {
            assertEquals(system, asset.system)
        }
    }

    @test
    fun testCreateAssetWithUploadId() {
        val client = CMAClient.Builder()
                .setAccessToken("token")
                .setCoreEndpoint(server!!.url("/").toString())
                .setCallbackExecutor { it.run() }
                .build()

        val responseBody = TestUtils.fileToString("asset_create_with_upload_id_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val asset = CMAAsset()
                .setSpaceId("space_id")
                .setVersion(1)

        asset.fields.localize("en-US").file = CMAAssetFile()
                .setContentType("image/jpeg")
                .setUploadFrom(CMALink().setId("some_secret_keys"))
                .setFileName("example.jpg")

        assertTestCallback(client.assets().async()
                .create(
                        "space_id",
                        asset
                        , TestCallback()) as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces/space_id/assets", recordedRequest.path)
    }

}