package com.contentful.java.cma

import com.squareup.okhttp.mockwebserver.MockResponse
import com.contentful.java.cma.lib.*
import org.junit.Test as test
import kotlin.test.*
import retrofit.RetrofitError
import java.io.IOException

/**
 * Content Type Tests.
 */
class ContentTypeTests : BaseTest() {
    test fun testCreate() {
        val requestBody = Utils.fileToString("content_type_create_request.json")
        val responseBody = Utils.fileToString("content_type_create_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = client!!.contentTypes().create(
                "spaceid",
                CMAContentType()
                        .setName("whatever1")
                        .addField(CMAField().setId("f1").setName("field1").setType("Text"))
                        .addField(CMAField().setId("f2").setName("field2").setType("Number")))

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/content_types", recordedRequest.getPath())
        Utils.assertJsonEquals(requestBody, recordedRequest.getBodyAsString())
        assertEquals(2, result.fields.size)
    }

    test fun testCreateWithId() {
        val requestBody = Utils.fileToString("content_type_create_request.json")
        val responseBody = Utils.fileToString("content_type_create_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = client!!.contentTypes().create(
                "spaceid",
                CMAContentType()
                        .setId("contenttypeid")
                        .setName("whatever1")
                        .addField(CMAField().setId("f1").setName("field1").setType("Text"))
                        .addField(CMAField().setId("f2").setName("field2").setType("Number")))

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/content_types/contenttypeid", recordedRequest.getPath())
        Utils.assertJsonEquals(requestBody, recordedRequest.getBodyAsString())
        assertEquals(2, result.fields.size)
    }

    test fun testCreateWithLink() {
        val requestBody = Utils.fileToString("content_type_create_with_link.json")
        server!!.enqueue(MockResponse().setResponseCode(200))

        client!!.contentTypes().create("spaceid", CMAContentType()
                .setName("whatever1")
                .addField(CMAField()
                        .setId("f1")
                        .setName("field1")
                        .setType("Link")
                        .setLinkType("Entry")))

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/content_types", recordedRequest.getPath())
        Utils.assertJsonEquals(requestBody, recordedRequest.getBodyAsString())
    }

    test fun testUpdate() {
        val requestBody = Utils.fileToString("content_type_update_request.json")
        val responseBody = Utils.fileToString("content_type_update_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        var contentType = gson!!.fromJson(
                Utils.fileToString("content_type_update_object.json"),
                javaClass<CMAContentType>())

        assertEquals(1.toDouble(), contentType.sys["version"])

        contentType.addField(CMAField().setId("f3").setName("field3").setType("Text"))
        contentType = client!!.contentTypes().update(contentType)
        assertEquals(3, contentType.fields.size)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/content_types/contenttypeid", recordedRequest.getPath())
        Utils.assertJsonEquals(requestBody, recordedRequest.getBodyAsString())
        assertNotNull(recordedRequest.getHeader("X-Contentful-Version"))
        assertEquals(2.toDouble(), contentType.sys["version"])
    }

    test fun testDelete() {
        server!!.enqueue(MockResponse().setResponseCode(200))
        client!!.contentTypes().delete("spaceid", "contenttypeid")

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("DELETE", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/content_types/contenttypeid", recordedRequest.getPath())
    }

    test fun testFetchAll() {
        val responseBody = Utils.fileToString("content_type_fetch_all_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = client!!.contentTypes().fetchAll("spaceid")
        assertEquals(2, result.items.size)
        assertEquals("Array", result.sys["type"])
        assertEquals("Blog Post", result.items[0].name)
        assertEquals(2, result.items[0].fields.size)
        assertEquals(2, result.total)
        assertEquals(0, result.skip)
        assertEquals(100, result.limit)

        // Assert first item
        assertEquals("titleid", result.items[0].fields[0].id)
        assertEquals("titlename", result.items[0].fields[0].name)
        assertEquals("Text", result.items[0].fields[0].type)

        assertEquals("bodyid", result.items[0].fields[1].id)
        assertEquals("bodyname", result.items[0].fields[1].name)
        assertEquals("Text", result.items[0].fields[1].type)

        // Assert second item
        assertEquals("titleid", result.items[0].fields[0].id)
        assertEquals("titlename", result.items[0].fields[0].name)
        assertEquals("Text", result.items[0].fields[0].type)

        assertEquals("bodyid", result.items[0].fields[1].id)
        assertEquals("bodyname", result.items[0].fields[1].name)
        assertEquals("Text", result.items[0].fields[1].type)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/content_types", recordedRequest.getPath())
    }

    test fun testFetchWithId() {
        val responseBody = Utils.fileToString("content_type_fetch_one_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = client!!.contentTypes().fetchOne("spaceid", "contenttypeid")
        assertEquals("Blog Post", result.name)

        // Fields
        assertEquals(2, result.fields.size)

        assertEquals("titleid", result.fields[0].id)
        assertEquals("titlename", result.fields[0].name)
        assertEquals("Text", result.fields[0].type)

        assertEquals("bodyid", result.fields[1].id)
        assertEquals("bodyname", result.fields[1].name)
        assertEquals("Text", result.fields[1].type)

        // System Attributes
        assertEquals("contenttypeid", result.sys["id"])
        assertEquals("ContentType", result.sys["type"])
        assertEquals("2014-11-05T09:19:49.489Z", result.sys["createdAt"])
        assertEquals("2014-11-05T09:20:41.770Z", result.sys["firstPublishedAt"])
        assertEquals(1.toDouble(), result.sys["publishedCounter"])
        assertEquals(3.toDouble(), result.sys["version"])
        assertEquals("2014-11-05T09:44:09.857Z", result.sys["updatedAt"])

        // Created By
        var map = (result.sys["createdBy"] as Map<*, *>)["sys"] as Map<*, *>
        assertEquals("Link", map["type"])
        assertEquals("User", map["linkType"])
        assertEquals("cuid", map["id"])

        // Updated By
        map = (result.sys["updatedBy"] as Map<*, *>)["sys"] as Map<*, *>
        assertEquals("Link", map["type"])
        assertEquals("User", map["linkType"])
        assertEquals("uuid", map["id"])

        // Space
        map = (result.sys["space"] as Map<*, *>)["sys"] as Map<*, *>
        assertEquals("Link", map["type"])
        assertEquals("Space", map["linkType"])
        assertEquals("spaceid", map["id"])

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/content_types/contenttypeid", recordedRequest.getPath())
    }

    test fun testPublish() {
        val responseBody = Utils.fileToString("content_type_publish_response.json")

        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))
        val result = client!!.contentTypes().publish(
                CMAContentType()
                        .setId("ctid")
                        .setSpaceId("spaceid")
                        .setVersion(1.0))

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/content_types/ctid/published", recordedRequest.getPath())
        assertTrue(result.isPublished())
        assertNotNull(recordedRequest.getHeader("X-Contentful-Version"))
    }

    test fun testUnPublish() {
        val responseBody = Utils.fileToString("content_type_unpublish_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))
        val result = client!!.contentTypes().unPublish(
                CMAContentType()
                        .setId("contenttypeid")
                        .setSpaceId("spaceid"))
                        .setName("whatever")

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("DELETE", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/content_types/contenttypeid/published", recordedRequest.getPath())
        assertFalse(result.isPublished())
    }

    test(expected = javaClass<RetrofitError>())
    fun testRetainsSysOnNetworkError() {
        val badClient = CMAClient.Builder()
                .setAccessToken("accesstoken")
                .setClient { throw RetrofitError.unexpectedError(it.getUrl(), IOException()) }
                .build()

        val contentType = CMAContentType().setVersion(31337.0)
        try {
            badClient.contentTypes().create("spaceid", contentType)
        } catch (e: RetrofitError) {
            assertEquals(31337, contentType.getVersion())
            throw e
        }
    }
}