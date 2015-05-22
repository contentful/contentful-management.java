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

import com.contentful.java.cma.Constants.CMAFieldType
import com.squareup.okhttp.mockwebserver.MockResponse
import com.contentful.java.cma.lib.*
import org.junit.Test as test
import kotlin.test.*
import retrofit.RetrofitError
import java.io.IOException
import com.contentful.java.cma.model.CMAContentType
import com.contentful.java.cma.model.CMAField
import com.contentful.java.cma.model.CMAEntry

class ContentTypeTests : BaseTest() {
    test fun testCreate() {
        val requestBody = TestUtils.fileToString("content_type_create_request.json")
        val responseBody = TestUtils.fileToString("content_type_create_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.contentTypes().async().create(
                "spaceid",
                CMAContentType()
                        .setName("whatever1")
                        .setDisplayField("df")
                        .addField(CMAField().setId("f1")
                                .setName("field1")
                                .setType(CMAFieldType.Text)
                                .setRequired(true))
                        .addField(CMAField().setId("f2")
                                .setName("field2")
                                .setType(CMAFieldType.Number)),
                TestCallback()) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/content_types", recordedRequest.getPath())
        assertJsonEquals(requestBody, recordedRequest.getBodyAsString())
        assertEquals(2, result.getFields().size())
        assertTrue(result.getFields()[0].isRequired())
    }

    test fun testCreateWithId() {
        val requestBody = TestUtils.fileToString("content_type_create_request.json")
        val responseBody = TestUtils.fileToString("content_type_create_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.contentTypes().async().create(
                "spaceid",
                CMAContentType()
                        .setId("contenttypeid")
                        .setName("whatever1")
                        .setDisplayField("df")
                        .setFields(listOf(
                                CMAField().setId("f1")
                                        .setName("field1")
                                        .setType(CMAFieldType.Text)
                                        .setRequired(true),
                                CMAField().setId("f2")
                                        .setName("field2")
                                        .setType(CMAFieldType.Number))
                        ), TestCallback()) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/content_types/contenttypeid", recordedRequest.getPath())
        assertJsonEquals(requestBody, recordedRequest.getBodyAsString())
        assertEquals(2, result.getFields().size())
        assertEquals("df", result.getDisplayField())
        assertTrue(result.getFields()[0].isRequired())
    }

    test fun testCreateWithLink() {
        val requestBody = TestUtils.fileToString("content_type_create_with_link.json")
        server!!.enqueue(MockResponse().setResponseCode(200))

        client!!.contentTypes().create("spaceid", CMAContentType()
                .setName("whatever1")
                .addField(CMAField()
                        .setId("f1")
                        .setName("field1")
                        .setType(CMAFieldType.Link)
                        .setLinkType("Entry")))

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/content_types", recordedRequest.getPath())
        assertJsonEquals(requestBody, recordedRequest.getBodyAsString())
    }

    test fun testUpdate() {
        val requestBody = TestUtils.fileToString("content_type_update_request.json")
        val responseBody = TestUtils.fileToString("content_type_update_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        var contentType = gson!!.fromJson(
                TestUtils.fileToString("content_type_update_object.json"),
                javaClass<CMAContentType>())

        assertEquals(1.toDouble(), contentType.getSys()["version"])

        contentType.addField(CMAField().setId("f3")
                .setName("field3")
                .setType(CMAFieldType.Text)
                .setValidations(listOf(mapOf(Pair("size", mapOf(
                        Pair("min", 1),
                        Pair("max", 5)))))))

        contentType = assertTestCallback(client!!.contentTypes().async().update(
                contentType, TestCallback()) as TestCallback)

        assertEquals(3, contentType.getFields().size())
        assertNotNull(contentType.getFields()[0].getValidations())

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/content_types/contenttypeid", recordedRequest.getPath())
        assertJsonEquals(requestBody, recordedRequest.getBodyAsString())
        assertNotNull(recordedRequest.getHeader("X-Contentful-Version"))
        assertEquals(2.toDouble(), contentType.getSys()["version"])
    }

    test fun testDelete() {
        server!!.enqueue(MockResponse().setResponseCode(200))

        assertTestCallback(client!!.contentTypes().async().delete(
                "spaceid", "contenttypeid", TestCallback()) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("DELETE", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/content_types/contenttypeid", recordedRequest.getPath())
    }

    test fun testFetchAll() {
        val responseBody = TestUtils.fileToString("content_type_fetch_all_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.contentTypes().async().fetchAll(
                "spaceid", TestCallback()) as TestCallback)

        val items = result.getItems()
        assertEquals(2, items.size())
        assertEquals("Array", result.getSys()["type"])
        assertEquals("Blog Post", items[0].getName())
        assertEquals(2, items[0].getFields().size())
        assertEquals(2, result.getTotal())
        assertEquals(0, result.getSkip())
        assertEquals(100, result.getLimit())

        // Assert first item
        var field = items[0].getFields()[0]
        assertEquals("titleid", field.getId())
        assertEquals("titlename", field.getName())
        assertEquals(CMAFieldType.Text, field.getType())

        field = items[0].getFields()[1]
        assertEquals("bodyid", field.getId())
        assertEquals("bodyname", field.getName())
        assertEquals(CMAFieldType.Text, field.getType())

        // Assert second item
        field = items[1].getFields()[0]
        assertEquals("a", field.getId())
        assertEquals("b", field.getName())
        assertEquals(CMAFieldType.Text, field.getType())

        field = items[1].getFields()[1]
        assertEquals("c", field.getId())
        assertEquals("d", field.getName())
        assertEquals(CMAFieldType.Text, field.getType())

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/content_types", recordedRequest.getPath())
    }

    test fun testFetchWithId() {
        val responseBody = TestUtils.fileToString("content_type_fetch_one_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.contentTypes().async().fetchOne(
                "spaceid", "contenttypeid", TestCallback()) as TestCallback)

        assertEquals("Blog Post", result.getName())

        // Fields
        val fields = result.getFields()
        assertEquals(2, fields.size())

        assertEquals("titleid", fields[0].getId())
        assertEquals("titlename", fields[0].getName())
        assertEquals(CMAFieldType.Text, fields[0].getType())

        assertEquals("bodyid", fields[1].getId())
        assertEquals("bodyname", fields[1].getName())
        assertEquals(CMAFieldType.Text, fields[1].getType())

        // System Attributes
        val sys = result.getSys()
        assertEquals("contenttypeid", sys["id"])
        assertEquals("ContentType", sys["type"])
        assertEquals("2014-11-05T09:19:49.489Z", sys["createdAt"])
        assertEquals("2014-11-05T09:20:41.770Z", sys["firstPublishedAt"])
        assertEquals(1.toDouble(), sys["publishedCounter"])
        assertEquals(3.toDouble(), sys["version"])
        assertEquals("2014-11-05T09:44:09.857Z", sys["updatedAt"])

        // Created By
        var map = (sys["createdBy"] as Map<*, *>)["sys"] as Map<*, *>
        assertEquals("Link", map["type"])
        assertEquals("User", map["linkType"])
        assertEquals("cuid", map["id"])

        // Updated By
        map = (sys["updatedBy"] as Map<*, *>)["sys"] as Map<*, *>
        assertEquals("Link", map["type"])
        assertEquals("User", map["linkType"])
        assertEquals("uuid", map["id"])

        // Space
        map = (sys["space"] as Map<*, *>)["sys"] as Map<*, *>
        assertEquals("Link", map["type"])
        assertEquals("Space", map["linkType"])
        assertEquals("spaceid", map["id"])

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/content_types/contenttypeid", recordedRequest.getPath())
    }

    test fun testPublish() {
        val responseBody = TestUtils.fileToString("content_type_publish_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.contentTypes().async().publish(
                CMAContentType()
                        .setId("ctid")
                        .setSpaceId("spaceid")
                        .setVersion(1.0),
                TestCallback()) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/content_types/ctid/published", recordedRequest.getPath())
        assertTrue(result.isPublished())
        assertNotNull(recordedRequest.getHeader("X-Contentful-Version"))
    }

    test fun testUnPublish() {
        val responseBody = TestUtils.fileToString("content_type_unpublish_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.contentTypes().async().unPublish(
                CMAContentType()
                        .setId("contenttypeid")
                        .setSpaceId("spaceid")
                        .setName("whatever"),
                TestCallback()) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("DELETE", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/content_types/contenttypeid/published", recordedRequest.getPath())
        assertFalse(result.isPublished())
    }

    test fun testCMAField() {
        val field = CMAField().setId("id")
                .setName("name")
                .setType(CMAFieldType.Link)
                .setLinkType("Entry")

        assertEquals("id", field.getId())
        assertEquals("name", field.getName())
        assertEquals(CMAFieldType.Link, field.getType())
        assertEquals("Entry", field.getLinkType())
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

    test(expected = javaClass<Exception>())
    fun testUpdateFailsWithoutVersion() {
        ModuleTestUtils.assertUpdateWithoutVersion {
            client!!.contentTypes().update(CMAContentType().setId("ctid")
                    .setName("name")
                    .setSpaceId("spaceid"))
        }
    }
}