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
import com.contentful.java.cma.model.CMAAsset
import com.contentful.java.cma.model.CMAAssetFile
import com.contentful.java.cma.model.CMALink
import com.contentful.java.cma.model.CMAType
import com.google.gson.Gson
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import java.io.IOException
import java.util.logging.LogManager
import kotlin.test.*
import org.junit.Test as test

class AssetTests {
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
        assertEquals("/spaces/spaceid/environments/master/assets/assetid/archived",
                recordedRequest.path)
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
                "spaceid", "master", asset, TestCallback()) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces/spaceid/environments/master/assets", recordedRequest.path)
        assertEquals(requestBody, recordedRequest.body.readUtf8())
    }

    @test
    fun testCreateWithConfiguredSpaceAndEnvironment() {
        val requestBody = TestUtils.fileToString("asset_create_request.json")
        val responseBody = TestUtils.fileToString("asset_create_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val asset = CMAAsset()
        asset.fields.localize("en-US").apply {
            title = "title"
            description = "description"
        }

        assertTestCallback(client!!.assets().async().create(
                asset, TestCallback()) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces/configuredSpaceId/environments/configuredEnvironmentId/assets",
                recordedRequest.path)
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
                "spaceid", "master", asset, TestCallback()) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.method)
        assertEquals("/spaces/spaceid/environments/master/assets/assetid",
                recordedRequest.path)
        assertEquals(requestBody, recordedRequest.body.readUtf8())
    }

    @test
    fun testDeleteWithObject() {
        val responseBody = ""
        server!!.enqueue(MockResponse().setResponseCode(204).setBody(responseBody))
        assertTestCallback(client!!.assets().async().delete(
                CMAAsset().setSpaceId("spaceid").setId("assetid"),
                TestCallback()
        ) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("DELETE", recordedRequest.method)
        assertEquals("/spaces/spaceid/environments/master/assets/assetid",
                recordedRequest.path)
    }

    @test
    fun testFetchAll() {
        val responseBody = TestUtils.fileToString("asset_fetch_all_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.assets().async().fetchAll(
                "spaceid", "master", TestCallback()) as TestCallback)!!

        assertEquals(CMAType.Array, result.system.type)
        assertEquals(1, result.total)
        val items = result.items
        assertEquals(1, items.size)
        val fields = items[0].fields.localize("en-US")
        assertNull(fields.description)
        assertEquals("Bonanza Coffee Heroes", fields.title)

        assertNotNull(fields.file)
        assertEquals("hash.jpg", fields.file.fileName)
        assertEquals("image/jpeg", fields.file.contentType)
        assertEquals("//images.contentful.com/spaceid/a/b/c.jpg", fields.file.url)
        assertNull(fields.file.uploadFrom)
        assertNull(fields.file.uploadUrl)
        assertNotNull(fields.file.details)

        assertEquals(101905, fields.file.details.size)
        assertEquals(960, fields.file.details.imageMeta.width)
        assertEquals(720, fields.file.details.imageMeta.height)

        // Request
        val request = server!!.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/spaces/spaceid/environments/master/assets?limit=100", request.path)
    }

    @test
    fun testFetchAllWithConfiguredSpaceAndEnvironment() {
        val responseBody = TestUtils.fileToString("asset_fetch_all_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        assertTestCallback(client!!.assets().async().fetchAll(TestCallback()) as TestCallback)!!

        // Request
        val request = server!!.takeRequest()
        assertEquals("GET", request.method)
        assertEquals(
                "/spaces/configuredSpaceId/environments/configuredEnvironmentId/assets?limit=100",
                request.path)
    }

    @test
    fun testFetchAllWithQuery() {
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(
                TestUtils.fileToString("asset_fetch_all_response.json")))

        val query = hashMapOf(Pair("skip", "1"), Pair("limit", "2"), Pair("content_type", "foo"))

        assertTestCallback(client!!.assets().async().fetchAll(
                "spaceid", "environmentId", query, TestCallback()) as TestCallback)

        // Request
        val request = server!!.takeRequest()
        val url = HttpUrl.parse(server!!.url(request.path).toString())!!
        assertEquals("1", url.queryParameter("skip"))
        assertEquals("2", url.queryParameter("limit"))
        assertEquals("foo", url.queryParameter("content_type"))
    }

    @test
    fun testFetchAllWithQueryWithConfiguredSpaceAndEnvironment() {
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(
                TestUtils.fileToString("asset_fetch_all_response.json")))

        val query = hashMapOf("skip" to "1", "limit" to "2", "content_type" to "foo")

        assertTestCallback(client!!.assets().async().fetchAll(query, TestCallback())
                as TestCallback)

        // Request
        val request = server!!.takeRequest()
        val url = HttpUrl.parse(server!!.url(request.path).toString())!!
        assertEquals("1", url.queryParameter("skip"))
        assertEquals("2", url.queryParameter("limit"))
        assertEquals("foo", url.queryParameter("content_type"))
        assertEquals(
                "/spaces/configuredSpaceId/environments/configuredEnvironmentId/"
                        + "assets?limit=2&content_type=foo&skip=1",
                request.path)
    }

    @test
    fun testFetchWithId() {
        val responseBody = TestUtils.fileToString("asset_publish_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        assertTestCallback(client!!.assets().async().fetchOne(
                "spaceid", "master", "assetid",
                TestCallback(true)) as TestCallback)

        // Request
        val request = server!!.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/spaces/spaceid/environments/master/assets/assetid", request.path)
    }

    @test
    fun testFetchWithIdWithConfiguredSpaceAndEnvironment() {
        val responseBody = TestUtils.fileToString("asset_publish_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        assertTestCallback(client!!.assets().async().fetchOne("assetid",
                TestCallback(true)) as TestCallback)

        // Request
        val request = server!!.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/spaces/configuredSpaceId/environments/configuredEnvironmentId" +
                "/assets/assetid", request.path)
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
        assertEquals(
                "/spaces/spaceid/environments/master/assets/assetid/files/locale/process",
                recordedRequest.path)
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
        assertEquals("/spaces/spaceid/environments/master/assets/assetid/published",
                recordedRequest.path)
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
        assertEquals("/spaces/spaceid/environments/master/assets/assetid/archived",
                recordedRequest.path)
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
        assertEquals("/spaces/spaceid/environments/master/assets/assetid/published",
                recordedRequest.path)
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
        assertEquals("/spaces/spaceid/environments/master/assets/assetid",
                recordedRequest.path)
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

        assertTestCallback(
                client!!
                        .assets()
                        .async()
                        .update(asset, TestCallback(true))
                        as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.method)
        assertEquals("/spaces/spaceid/environments/master/assets/assetid",
                recordedRequest.path)
        assertNotNull(recordedRequest.getHeader("X-Contentful-Version"))
        assertEquals(requestBody, recordedRequest.body.readUtf8())
    }

    @test(expected = RuntimeException::class)
    fun testRetainsSysOnNetworkError() {
        val badClient = CMAClient.Builder()
                .setAccessToken("accesstoken")
                .setCoreCallFactory { throw IOException(it.url().toString(), IOException()) }
                .build()

        val asset = CMAAsset().setVersion(31337)
        try {
            badClient.assets().create("spaceid", "environmentId", asset)
        } catch (e: RuntimeException) {
            assertEquals(31337, asset.version)
            throw e
        }
    }

    @test
    fun testNullVersionDoesNotThrow() {
        assertNull(CMAAsset().version)
    }

    @test(expected = Exception::class)
    fun testUpdateFailsWithoutVersion() {
        ModuleTestUtils.assertUpdateWithoutVersion {
            client!!.assets().update(CMAAsset().setId("aid").setSpaceId("spaceid"))
        }
    }

    @test
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
                .setSpaceId("spaceid")
                .setVersion(1)

        asset.fields.localize("en-US").file = CMAAssetFile()
                .setContentType("image/jpeg")
                .setUploadFrom(CMALink().setId("some_secret_keys"))
                .setFileName("example.jpg")

        assertTestCallback(client.assets().async()
                .create("spaceid", "master", asset, TestCallback()) as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces/spaceid/environments/master/assets", recordedRequest.path)
    }

    @test
    fun testFetchOneFromEnvironment() {
        val responseBody = TestUtils.fileToString("asset_fetch_one_from_environment.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(
                client!!
                        .assets()
                        .async()
                        .fetchOne(
                                "spaceid",
                                "staging",
                                "2ReMHJhXoAcy4AyamgsgwQ",
                                TestCallback()
                        ) as TestCallback)!!

        assertEquals(CMAType.Asset, result.system.type)
        assertEquals("staging", result.environmentId)
        assertEquals("Lewis Carroll", result.fields.getTitle("en-US"))

        // Request
        val request = server!!.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/spaces/spaceid/environments/staging/assets/2ReMHJhXoAcy4AyamgsgwQ",
                request.path)
    }

    @test
    fun testFetchAllFromEnvironment() {
        val responseBody = TestUtils.fileToString("asset_fetch_all_from_environment.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(
                client!!
                        .assets()
                        .async()
                        .fetchAll(
                                "spaceid",
                                "staging",
                                TestCallback()
                        ) as TestCallback)!!

        assertEquals(CMAType.Array, result.system.type)
        assertEquals(47, result.total)
        val items = result.items
        assertEquals(47, items.size)
        val fields = items[0].fields.localize("en-US")
        assertEquals("Lewis Carroll", fields.title)
        assertEquals("staging", items[0].environmentId)

        // Request
        val request = server!!.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/spaces/spaceid/environments/staging/assets?limit=100", request.path)
    }

    @test
    fun testFetchAllFromEnvironmentWithQuery() {
        val responseBody = TestUtils.fileToString(
                "asset_fetch_all_from_environment_with_query.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(
                client!!
                        .assets()
                        .async()
                        .fetchAll(
                                "spaceid",
                                "staging",
                                hashMapOf(Pair("limit", "1")),
                                TestCallback()
                        ) as TestCallback)!!

        assertEquals(CMAType.Array, result.system.type)
        assertEquals(47, result.total)
        val items = result.items
        assertEquals(1, items.size)
        val fields = items[0].fields.localize("en-US")
        assertEquals("budah-small", fields.title)
        assertEquals("staging", items[0].environmentId)

        // Request
        val request = server!!.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/spaces/spaceid/environments/staging/assets?limit=1", request.path)
    }

    @test
    fun testCreateInEnvironment() {
        val responseBody = TestUtils.fileToString("asset_create_in_environment.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val asset = CMAAsset()
        asset.fields.localize("en-US").apply {
            title = "title"
            description = "description"
        }

        val result = assertTestCallback(
                client!!
                        .assets()
                        .async()
                        .create(
                                "spaceid",
                                "staging",
                                asset,
                                TestCallback()
                        ) as TestCallback)!!

        assertEquals(CMAType.Asset, result.system.type)
        assertEquals("staging", result.environmentId)
        assertEquals("title", result.fields.getTitle("en-US"))
        assertEquals("description", result.fields.getDescription("en-US"))

        // Request
        val request = server!!.takeRequest()
        assertEquals("POST", request.method)
        assertEquals("/spaces/spaceid/environments/staging/assets",
                request.path)
    }

    @test
    fun testDeleteFromEnvironment() {
        server!!.enqueue(MockResponse().setResponseCode(204).setBody(""))

        val result = assertTestCallback(
                client!!
                        .assets()
                        .async()
                        .delete(CMAAsset().apply {
                            spaceId = "spaceid"
                            environmentId = "staging"
                            id = "1fgii3GZo4euykA6u6mKmi"
                        }, TestCallback()
                        ) as TestCallback)!!

        assertEquals(204, result)

        // Request
        val request = server!!.takeRequest()
        assertEquals("DELETE", request.method)
        assertEquals("/spaces/spaceid/environments/staging/assets/1fgii3GZo4euykA6u6mKmi",
                request.path)
    }

    @test(expected = IllegalArgumentException::class)
    fun testThrowIfNotConfigured() {
        val client = CMAClient.Builder()
                .setAccessToken("token")
                .setCoreEndpoint(server!!.url("/").toString())
                .setUploadEndpoint(server!!.url("/").toString())
                .build()

        try {
            client.assets().fetchAll()
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("spaceId may not be null"))
            throw e
        }
    }
}