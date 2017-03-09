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

import com.contentful.java.cma.Constants.CMAFieldType
import com.contentful.java.cma.lib.ModuleTestUtils
import com.contentful.java.cma.lib.TestCallback
import com.contentful.java.cma.lib.TestUtils
import com.contentful.java.cma.model.CMAContentType
import com.contentful.java.cma.model.CMAField
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import org.junit.Test as test

class ContentTypeTests : BaseTest() {
    @test fun testCreate() {
        val requestBody = TestUtils.fileToString("content_type_create_request.json")
        val responseBody = TestUtils.fileToString("content_type_create_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.contentTypes().async().create(
                "spaceid",
                CMAContentType()
                        .setName("whatever1")
                        .setDescription("desc1")
                        .setDisplayField("df")
                        .addField(CMAField().setId("f1")
                                .setName("field1")
                                .setType(CMAFieldType.Text)
                                .setRequired(true))
                        .addField(CMAField().setId("f2")
                                .setName("field2")
                                .setType(CMAFieldType.Number)),
                TestCallback()) as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces/spaceid/content_types", recordedRequest.path)
        assertJsonEquals(requestBody, recordedRequest.utf8Body)
        assertEquals(2, result.fields.size)
        assertTrue(result.fields[0].isRequired)
    }

    @test fun testCreateWithId() {
        val requestBody = TestUtils.fileToString("content_type_create_request.json")
        val responseBody = TestUtils.fileToString("content_type_create_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.contentTypes().async().create(
                "spaceid",
                CMAContentType()
                        .setId("contenttypeid")
                        .setName("whatever1")
                        .setDescription("desc1")
                        .setDisplayField("df")
                        .setFields(listOf(
                                CMAField().setId("f1")
                                        .setName("field1")
                                        .setType(CMAFieldType.Text)
                                        .setRequired(true),
                                CMAField().setId("f2")
                                        .setName("field2")
                                        .setType(CMAFieldType.Number))
                        ), TestCallback()) as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.method)
        assertEquals("/spaces/spaceid/content_types/contenttypeid", recordedRequest.path)
        assertJsonEquals(requestBody, recordedRequest.utf8Body)
        assertEquals(2, result.fields.size)
        assertEquals("df", result.displayField)
        assertTrue(result.fields[0].isRequired)
    }

    @test fun testCreateWithLink() {
        val responseBody = TestUtils.fileToString("content_type_create_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        client!!.contentTypes().create("spaceid", CMAContentType()
                .setName("whatever1")
                .addField(CMAField()
                        .setId("f1")
                        .setName("field1")
                        .setType(CMAFieldType.Link)
                        .setLinkType("Entry")))

        // Request
        val requestBody = TestUtils.fileToString("content_type_create_with_link.json")
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces/spaceid/content_types", recordedRequest.path)
        assertJsonEquals(requestBody, recordedRequest.utf8Body)
    }

    @test fun testUpdate() {
        val requestBody = TestUtils.fileToString("content_type_update_request.json")
        val responseBody = TestUtils.fileToString("content_type_update_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        var contentType = gson!!.fromJson(
                TestUtils.fileToString("content_type_update_object.json"),
                CMAContentType::class.java)

        assertEquals(1.toDouble(), contentType.sys["version"])

        contentType.addField(CMAField().setId("f3")
                .setName("field3")
                .setType(CMAFieldType.Text)
                .setValidations(listOf(mapOf(Pair("size", mapOf(
                        Pair("min", 1),
                        Pair("max", 5)))))))

        contentType = assertTestCallback(client!!.contentTypes().async().update(
                contentType, TestCallback()) as TestCallback)

        assertEquals(3, contentType.fields.size)
        assertNotNull(contentType.fields[0].validations)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.method)
        assertEquals("/spaces/spaceid/content_types/contenttypeid", recordedRequest.path)
        assertJsonEquals(requestBody, recordedRequest.utf8Body)
        assertNotNull(recordedRequest.getHeader("X-Contentful-Version"))
        assertEquals(2.toDouble(), contentType.sys["version"])
    }

    @test fun testDelete() {
        val requestBody = "203"
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(requestBody))

        assertTestCallback(client!!.contentTypes().async().delete(
                "spaceid", "contenttypeid", TestCallback()) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("DELETE", recordedRequest.method)
        assertEquals("/spaces/spaceid/content_types/contenttypeid", recordedRequest.path)
    }

    @test fun testFetchAll() {
        val responseBody = TestUtils.fileToString("content_type_fetch_all_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.contentTypes().async().fetchAll(
                "spaceid", TestCallback()) as TestCallback)!!

        val items = result.items
        assertEquals(2, items.size)
        assertEquals("Array", result.sys["type"])
        assertEquals("Blog Post", items[0].name)
        assertEquals(2, items[0].fields.size)
        assertEquals(2, result.total)
        assertEquals(0, result.skip)
        assertEquals(100, result.limit)

        // Assert first item
        var field = items[0].fields[0]
        assertEquals("titleid", field.id)
        assertEquals("titlename", field.name)
        assertTrue(field.isLocalized)
        assertEquals(CMAFieldType.Text, field.type)

        field = items[0].fields[1]
        assertEquals("bodyid", field.id)
        assertEquals("bodyname", field.name)
        assertFalse(field.isLocalized)
        assertEquals(CMAFieldType.Text, field.type)

        // Assert second item
        field = items[1].fields[0]
        assertEquals("a", field.id)
        assertEquals("b", field.name)
        assertFalse(field.isLocalized)
        assertEquals(CMAFieldType.Text, field.type)

        field = items[1].fields[1]
        assertEquals("c", field.id)
        assertEquals("d", field.name)
        assertEquals(CMAFieldType.Text, field.type)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/spaceid/content_types?limit=100", recordedRequest.path)
    }

    @test fun testFetchAllWithQuery() {
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(
                TestUtils.fileToString("content_type_fetch_all_response.json")))

        val query = hashMapOf(Pair("skip", "1"), Pair("limit", "2"), Pair("foo", "bar"))

        assertTestCallback(client!!.contentTypes().async().fetchAll(
                "spaceid", query, TestCallback()) as TestCallback)

        // Request
        val request = server!!.takeRequest()
        val url = HttpUrl.parse(server!!.url(request.path).toString())
        assertEquals("1", url.queryParameter("skip"))
        assertEquals("2", url.queryParameter("limit"))
        assertEquals("bar", url.queryParameter("foo"))
    }

    @test fun testFetchWithId() {
        val responseBody = TestUtils.fileToString("content_type_fetch_one_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.contentTypes().async().fetchOne(
                "spaceid", "contenttypeid", TestCallback()) as TestCallback)!!

        assertEquals("Blog Post", result.name)
        assertEquals("desc1", result.description)

        // Fields
        val fields = result.fields
        assertEquals(2, fields.size)

        assertEquals("titleid", fields[0].id)
        assertEquals("titlename", fields[0].name)
        assertEquals(CMAFieldType.Text, fields[0].type)

        assertEquals("bodyid", fields[1].id)
        assertEquals("bodyname", fields[1].name)
        assertEquals(CMAFieldType.Text, fields[1].type)

        // System Attributes
        val sys = result.sys
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
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/spaceid/content_types/contenttypeid", recordedRequest.path)
    }

    @test fun testPublish() {
        val responseBody = TestUtils.fileToString("content_type_publish_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.contentTypes().async().publish(
                CMAContentType()
                        .setId("ctid")
                        .setSpaceId("spaceid")
                        .setVersion(1.0),
                TestCallback()) as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.method)
        assertEquals("/spaces/spaceid/content_types/ctid/published", recordedRequest.path)
        assertTrue(result.isPublished)
        assertNotNull(recordedRequest.getHeader("X-Contentful-Version"))
    }

    @test fun testUnPublish() {
        val responseBody = TestUtils.fileToString("content_type_unpublish_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.contentTypes().async().unPublish(
                CMAContentType()
                        .setId("contenttypeid")
                        .setSpaceId("spaceid")
                        .setName("whatever"),
                TestCallback()) as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("DELETE", recordedRequest.method)
        assertEquals("/spaces/spaceid/content_types/contenttypeid/published", recordedRequest.path)
        assertFalse(result.isPublished)
    }

    @test fun testCMAField() {
        val field = CMAField().setId("id")
                .setName("name")
                .setType(CMAFieldType.Link)
                .setLinkType("Entry")

        assertEquals("id", field.id)
        assertEquals("name", field.name)
        assertEquals(CMAFieldType.Link, field.type)
        assertEquals("Entry", field.linkType)
    }

    @test(expected = RuntimeException::class)
    fun testRetainsSysOnNetworkError() {
        val badClient = CMAClient.Builder()
                .setAccessToken("accesstoken")
                .setCoreCallFactory { throw RuntimeException(it.url().toString(), IOException()) }
                .build()

        val contentType = CMAContentType().setVersion(31337.0)
        try {
            badClient.contentTypes().create("spaceid", contentType)
        } catch (e: RuntimeException) {
            assertEquals(31337, contentType.version)
            throw e
        }
    }

    @test(expected = Exception::class)
    fun testUpdateFailsWithoutVersion() {
        ModuleTestUtils.assertUpdateWithoutVersion {
            client!!.contentTypes().update(CMAContentType().setId("ctid")
                    .setName("name")
                    .setSpaceId("spaceid"))
        }
    }
}