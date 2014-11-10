package com.contentful.java.cma

import org.junit.Test as test
import com.contentful.java.cma.lib.Utils
import com.squareup.okhttp.mockwebserver.MockResponse
import com.contentful.java.cma.lib.getBodyAsString
import kotlin.test.*
import retrofit.RetrofitError
import java.io.IOException

/**
 * Asset Tests.
 */
class AssetTests : BaseTest() {
    test fun testArchive() {
        val responseBody = Utils.fileToString("asset_archive_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))
        val asset = CMAAsset().setId("assetid").setSpaceId("spaceid")
        assertFalse(asset.isArchived())
        val result = client!!.assets().archive(asset)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/assets/assetid/archived", recordedRequest.getPath())
        assertTrue(result.isArchived())
    }

    test fun testCreate() {
        val requestBody = Utils.fileToString("asset_create_request.json")
        val responseBody = Utils.fileToString("asset_create_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val asset = CMAAsset()
                .addField("title", "title", "en-US")
                .addField("description", "description", "en-US")

        client!!.assets().create("spaceid", asset)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/assets", recordedRequest.getPath())
        assertEquals(requestBody, recordedRequest.getBodyAsString())
    }

    test fun testCreateWithId() {
        val requestBody = Utils.fileToString("asset_create_request.json")
        val responseBody = Utils.fileToString("asset_create_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val asset = CMAAsset()
                .setId("assetid")
                .addField("title", "title", "en-US")
                .addField("description", "description", "en-US")

        client!!.assets().create("spaceid", asset)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/assets/assetid", recordedRequest.getPath())
        assertEquals(requestBody, recordedRequest.getBodyAsString())
    }

    test fun testDelete() {
        server!!.enqueue(MockResponse().setResponseCode(200))
        client!!.assets().delete("spaceid", "assetid")

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("DELETE", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/assets/assetid", recordedRequest.getPath())
    }

    test fun testFetchAll() {
        val responseBody = Utils.fileToString("asset_fetch_all_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = client!!.assets().fetchAll("spaceid")
        assertEquals("Array", result.sys["type"])
        assertEquals(1, result.total)
        assertEquals(1, result.items.size)
        assertEquals(2, result.items[0].fields.size)
        assertNotNull(result.items[0].fields["file"])
        assertNotNull(result.items[0].fields["title"])

        // Request
        val request = server!!.takeRequest()
        assertEquals("GET", request.getMethod())
        assertEquals("/spaces/spaceid/assets", request.getPath())
    }

    test fun testFetchWithId() {
        server!!.enqueue(MockResponse().setResponseCode(200))

        client!!.assets().fetchOne("spaceid", "assetid")

        // Request
        val request = server!!.takeRequest()
        assertEquals("GET", request.getMethod())
        assertEquals("/spaces/spaceid/assets/assetid", request.getPath())
    }

    test fun testProcess() {
        server!!.enqueue(MockResponse().setResponseCode(200))

        client!!.assets().process(CMAAsset()
                .setId("assetid")
                .setSpaceId("spaceid"), "locale")

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/assets/assetid/files/locale/process", recordedRequest.getPath())
    }

    test fun testPublish() {
        val responseBody = Utils.fileToString("asset_publish_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val asset = CMAAsset().setId("assetid").setSpaceId("spaceid").setVersion(1.0)
        assertFalse(asset.isPublished())
        val result = client!!.assets().publish(asset)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/assets/assetid/published", recordedRequest.getPath())
        assertNotNull(recordedRequest.getHeader("X-Contentful-Version"))
        assertTrue(result.isPublished())
    }

    test fun testUnArchive() {
        server!!.enqueue(MockResponse().setResponseCode(200))
        client!!.assets().unArchive(CMAAsset().setId("assetid").setSpaceId("spaceid"))

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("DELETE", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/assets/assetid/archived", recordedRequest.getPath())
    }

    test fun testUnPublish() {
        server!!.enqueue(MockResponse().setResponseCode(200))
        client!!.assets().unPublish(CMAAsset().setId("assetid").setSpaceId("spaceid"))

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("DELETE", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/assets/assetid/published", recordedRequest.getPath())
    }

    test fun testUpdate() {
        val requestBody = Utils.fileToString("asset_update_request.json")
        server!!.enqueue(MockResponse().setResponseCode(200))

        client!!.assets().update(CMAAsset()
                .setId("assetid")
                .setSpaceId("spaceid")
                .setVersion(1.0)
                .addField("file", linkedMapOf(
                        Pair("content_type", "image/jpeg"),
                        Pair("upload", "https://www.nowhere.com/image.jpg"),
                        Pair("fileName", "example.jpg")
                ), "en-US"))

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
}