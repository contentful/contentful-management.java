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

package com.contentful.java.cma

import com.contentful.java.cma.Constants.CMAFieldType
import com.contentful.java.cma.lib.ModuleTestUtils
import com.contentful.java.cma.lib.TestCallback
import com.contentful.java.cma.lib.TestUtils
import com.contentful.java.cma.model.CMAContentType
import com.contentful.java.cma.model.CMAField
import com.contentful.java.cma.model.CMAHttpException
import com.contentful.java.cma.model.CMAType
import com.google.gson.Gson
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import java.io.IOException
import java.util.*
import java.util.logging.LogManager
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import org.junit.Test as test

class ContentTypeTests{
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
        client = CMAClient.Builder().apply {
            accessToken = "token"
            coreEndpoint = server!!.url("/").toString()
            uploadEndpoint = server!!.url("/").toString()
            spaceId = "configuredSpaceId"
            environmentId = "configuredEnvironmentId"
        }.build()

        gson = CMAClient.createGson()
    }

    @After
    fun tearDown() {
        server!!.shutdown()
    }

    @test
    fun testCreate() {
        val requestBody = TestUtils.fileToString("content_type_create_request.json")
        val responseBody = TestUtils.fileToString("content_type_create_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.contentTypes().async().create(
                "spaceid",
                "master",
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
        assertEquals("/spaces/spaceid/environments/master/content_types", recordedRequest.path)
        assertJsonEquals(requestBody, recordedRequest.body.readUtf8())
        assertEquals(2, result.fields.size)
        assertTrue(result.fields[0].isRequired)
    }

    @test

    fun testCreateWithConfiguredSpaceAndEnvironment() {
        val responseBody = TestUtils.fileToString("content_type_create_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        assertTestCallback(client!!.contentTypes().async().create(
                CMAContentType().apply {
                    name = "whatever1"
                    description = "desc1"
                    displayField = "df"
                    addField(
                            CMAField().apply {
                                id = "f1"
                                name = "field1"
                                type = CMAFieldType.Text
                                isRequired = true
                            })
                    addField(
                            CMAField().apply {
                                id = "f2"
                                name = "field2"
                                type = CMAFieldType.Number
                            })
                },
                TestCallback()) as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces/configuredSpaceId/environments/configuredEnvironmentId/content_types",
                recordedRequest.path)
    }

    @test
    fun testCreateWithId() {
        val requestBody = TestUtils.fileToString("content_type_create_request.json")
        val responseBody = TestUtils.fileToString("content_type_create_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.contentTypes().async().create(
                "spaceid",
                "master",
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
        assertEquals("/spaces/spaceid/environments/master/content_types/contenttypeid",
                recordedRequest.path)
        assertJsonEquals(requestBody, recordedRequest.body.readUtf8())
        assertEquals(2, result.fields.size)
        assertEquals("df", result.displayField)
        assertTrue(result.fields[0].isRequired)
    }

    @test
    fun testCreateWithLink() {
        val responseBody = TestUtils.fileToString("content_type_create_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        client!!.contentTypes().create("spaceid", "environmentId", CMAContentType()
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
        assertEquals("/spaces/spaceid/environments/environmentId/content_types", recordedRequest.path)
        assertJsonEquals(requestBody, recordedRequest.body.readUtf8())
    }

    @test
    fun testCreateLocalized() {
        val responseBody = TestUtils.fileToString("content_type_create_localized_payload.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val response = client!!.contentTypes().create("spaceid", "environmentId", CMAContentType()
                .setName("whatever1")
                .addField(CMAField()
                        .setId("f1")
                        .setName("field1")
                        .setType(CMAFieldType.Link)
                        .setLinkType("Entry")
                        .setLocalized(true)
                )
        )

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces/spaceid/environments/environmentId/content_types", recordedRequest.path)
        assertTrue(recordedRequest.body.readUtf8().contains("\"localized\":true"))

        assertTrue(response.fields[0].isLocalized)
    }

    @test
    fun testUpdate() {
        val requestBody = TestUtils.fileToString("content_type_update_request.json")
        val responseBody = TestUtils.fileToString("content_type_update_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        var contentType = gson!!.fromJson(
                TestUtils.fileToString("content_type_update_object.json"),
                CMAContentType::class.java)

        assertEquals(1, contentType.version)

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
        assertEquals("/spaces/spaceid/environments/master/content_types/contenttypeid",
                recordedRequest.path)
        assertJsonEquals(requestBody, recordedRequest.body.readUtf8())
        assertNotNull(recordedRequest.getHeader("X-Contentful-Version"))
        assertEquals(2, contentType.version)
    }

    @test
    fun testDelete() {
        val requestBody = ""
        server!!.enqueue(MockResponse().setResponseCode(204).setBody(requestBody))

        assertTestCallback(client!!.contentTypes().async().delete(
                CMAContentType().apply {
                    spaceId = "spaceid"
                    environmentId = "master"
                    id = "contenttypeid"
                }, TestCallback()) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("DELETE", recordedRequest.method)
        assertEquals("/spaces/spaceid/environments/master/content_types/contenttypeid",
                recordedRequest.path)
    }

    @test
    fun testFetchAll() {
        val responseBody = TestUtils.fileToString("content_type_fetch_all_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.contentTypes().async().fetchAll(
                "spaceid", "environmentId", TestCallback()) as TestCallback)!!

        val items = result.items
        assertEquals(2, items.size)
        assertEquals(CMAType.Array, result.system.type)
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
        assertEquals("/spaces/spaceid/environments/environmentId/content_types?limit=100",
                recordedRequest.path)
    }

    @test
    fun testFetchAllWithConfiguredSpaceAndEnvironment() {
        val responseBody = TestUtils.fileToString("content_type_fetch_all_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        assertTestCallback(client!!.contentTypes().async().fetchAll(TestCallback())
                as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/configuredSpaceId/environments/configuredEnvironmentId/content_types"
                + "?limit=100",
                recordedRequest.path)
    }

    @test
    fun testFetchAllWithQuery() {
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(
                TestUtils.fileToString("content_type_fetch_all_response.json")))

        val query = hashMapOf(Pair("skip", "1"), Pair("limit", "2"), Pair("foo", "bar"))

        assertTestCallback(client!!.contentTypes().async().fetchAll(
                "spaceid", "environmentId", query, TestCallback()) as TestCallback)

        // Request
        val request = server!!.takeRequest()
        val url = HttpUrl.parse(server!!.url(request.path).toString())!!
        assertEquals("1", url.queryParameter("skip"))
        assertEquals("2", url.queryParameter("limit"))
        assertEquals("bar", url.queryParameter("foo"))
    }

    @test
    fun testFetchAllWithQueryWithConfiguredSpaceAndEnvironment() {
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(
                TestUtils.fileToString("content_type_fetch_all_response.json")))

        val query = hashMapOf("skip" to "1", "limit" to "2", "foo" to "bar")

        assertTestCallback(client!!.contentTypes().async().fetchAll(
                query, TestCallback()) as TestCallback)

        // Request
        val request = server!!.takeRequest()
        assertEquals("/spaces/configuredSpaceId/environments/configuredEnvironmentId/content_types"
                + "?limit=2"
                + "&skip=1"
                + "&foo=bar",
                request.path)
    }

    @test
    fun testFetchWithId() {
        val responseBody = TestUtils.fileToString("content_type_fetch_one_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.contentTypes().async().fetchOne(
                "spaceid", "environmentId", "contenttypeid", TestCallback()) as TestCallback)!!

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
        val sys = result.system
        assertEquals("contenttypeid", sys.id)
        assertEquals(CMAType.ContentType, sys.type)
        assertEquals("2014-11-05T09:19:49.489Z", sys.createdAt)
        assertEquals("2014-11-05T09:20:41.770Z", sys.firstPublishedAt)
        assertEquals(1, sys.publishedCounter)
        assertEquals(3, sys.version)
        assertEquals("2014-11-05T09:44:09.857Z", sys.updatedAt)

        // Created By
        assertEquals(CMAType.Link, sys.createdBy.system.type)
        assertEquals(CMAType.User, sys.createdBy.system.linkType)
        assertEquals("cuid", sys.createdBy.system.id)

        // Updated By
        assertEquals(CMAType.Link, sys.updatedBy.system.type)
        assertEquals(CMAType.User, sys.updatedBy.system.linkType)
        assertEquals("uuid", sys.updatedBy.system.id)

        // Space
        assertEquals(CMAType.Link, sys.space.system.type)
        assertEquals(CMAType.Space, sys.space.system.linkType)
        assertEquals("spaceid", sys.space.system.id)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/spaceid/environments/environmentId/content_types/contenttypeid",
                recordedRequest.path)
    }

    @test
    fun testFetchWithIdWithConfiguredSpaceAndEnvironment() {
        val responseBody = TestUtils.fileToString("content_type_fetch_one_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        assertTestCallback(client!!.contentTypes().async().fetchOne("contenttypeid", TestCallback())
                as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/configuredSpaceId/environments/configuredEnvironmentId/content_types"
                + "/contenttypeid",
                recordedRequest.path)
    }

    @test
    fun testPublish() {
        val responseBody = TestUtils.fileToString("content_type_publish_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.contentTypes().async().publish(
                CMAContentType()
                        .setId("ctid")
                        .setSpaceId("spaceid")
                        .setVersion(1),
                TestCallback()) as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.method)
        assertEquals("/spaces/spaceid/environments/master/content_types/ctid/published",
                recordedRequest.path)
        assertTrue(result.isPublished)
        assertNotNull(recordedRequest.getHeader("X-Contentful-Version"))
    }

    @test
    fun testUnPublish() {
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
        assertEquals("/spaces/spaceid/environments/master/content_types/contenttypeid/published",
                recordedRequest.path)
        assertFalse(result.isPublished)
    }

    @test
    fun testCMAField() {
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

        val contentType = CMAContentType().setVersion(31337)
        try {
            badClient.contentTypes().create("spaceid", "environmentId", contentType)
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

    @test
    fun testFetchAllSnapshots() {
        val responseBody = TestUtils.fileToString("content_type_snapshots_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val contentType = CMAContentType()
                .setId("contentTypeId")
                .setSpaceId("spaceid")

        val result = assertTestCallback(client!!.contentTypes().async().fetchAllSnapshots(
                contentType, TestCallback()) as TestCallback)!!

        val items = result.items
        assertEquals(2, items.size)

        // Assert first item
        val first = items[0]
        assertEquals(CMAType.Snapshot, first.system.type)
        assertEquals(CMAType.ContentType, first.snapshot.system.type)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/spaceid/environments/master/content_types/contentTypeId/snapshots",
                recordedRequest.path)
    }

    @test
    fun testFetchOneSnapshot() {
        val responseBody = TestUtils.fileToString("content_type_snapshots_get_one.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val contentType = CMAContentType()
                .setId("contentTypeId")
                .setSpaceId("spaceid")

        val result = assertTestCallback(client!!.contentTypes().async().fetchOneSnapshot(
                contentType, "snapShotId", TestCallback()) as TestCallback)!!

        assertEquals(CMAType.Snapshot, result.system.type)
        assertEquals(CMAType.ContentType, result.snapshot.system.type)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/spaceid/environments/master/content_types/contentTypeId/snapshots/snapShotId",
                recordedRequest.path)
    }

    @test(expected = CMAHttpException::class)
    fun testFailureOnCreateContenTypeWithWrongFieldType() {
        val responseBody = TestUtils.fileToString("content_type_error_field_type_invalid.json")
        server!!.enqueue(MockResponse().setResponseCode(422).setBody(responseBody))

        val fields = Collections.singletonList(
                CMAField()
                        .setName("name")
                        .setId("testid")
                        .setType(null)
                        .setRequired(true))

        val contentType = CMAContentType()
                .setId("entryId")
                .setSpaceId("spaceId")
                .setFields(fields)

        try {
            client!!.contentTypes().create("spaceID", "master'", contentType)
        } catch (e: CMAHttpException) {
            assertEquals("null", e.errorBody.details.errors[0].value)
            assertEquals("[Array, Boolean, Date, Integer, Number, Object, Symbol, Link, Location, Text]",
                    e.errorBody.details.errors[0].expected.toString())
            throw e
        }
    }

    @test
    fun testCreateInEnvironment() {
        val responseBody = TestUtils.fileToString("content_type_create_in_environment.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.contentTypes().async().create(
                "spaceid",
                "staging",
                CMAContentType()
                        .setName("Post")
                        .addField(CMAField().setId("title")
                                .setName("Title")
                                .setType(CMAFieldType.Text)
                                .setRequired(true)),
                TestCallback()) as TestCallback)!!

        assertEquals(1, result.fields.size)
        assertEquals("Post", result.name)
        assertEquals("title", result.fields[0].id)
        assertEquals("staging", result.environmentId)
        assertTrue(result.fields[0].isRequired)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces/spaceid/environments/staging/content_types", recordedRequest.path)
    }

    @test
    fun testDeleteFromEnvironment() {
        server!!.enqueue(MockResponse().setResponseCode(204).setBody(""))

        val result = assertTestCallback(client!!.contentTypes().async().delete(
                CMAContentType().apply {
                    spaceId = "spaceid"
                    environmentId = "staging"
                    id = "6WIydLkj2ogUWusoQguOki"
                },
                TestCallback()) as TestCallback)!!

        assertEquals(204, result)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("DELETE", recordedRequest.method)
        assertEquals("/spaces/spaceid/environments/staging/content_types/6WIydLkj2ogUWusoQguOki",
                recordedRequest.path)
    }

    @test
    fun testFetchAllFromEnvironment() {
        val responseBody = TestUtils.fileToString("content_type_fetch_all_from_environment.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(
                client!!
                        .contentTypes()
                        .async()
                        .fetchAll(
                                "spaceid",
                                "staging",
                                TestCallback()
                        ) as TestCallback)!!

        val items = result.items
        assertEquals(11, result.total)
        assertEquals(11, items.size)
        assertEquals(CMAType.Array, result.system.type)
        assertEquals("Author", items[0].name)
        assertEquals("staging", items[0].environmentId)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/spaceid/environments/staging/content_types?limit=100",
                recordedRequest.path)
    }

    @test
    fun testFetchOneFromEnvironment() {
        val responseBody = TestUtils.fileToString("content_type_fetch_one_from_environment.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(
                client!!
                        .contentTypes()
                        .async()
                        .fetchOne(
                                "spaceid",
                                "staging",
                                "1kUEViTN4EmGiEaaeC6ouY",
                                TestCallback()
                        ) as TestCallback)!!

        assertEquals(CMAType.ContentType, result.system.type)
        assertEquals("Author", result.name)
        assertEquals("staging", result.environmentId)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/spaceid/environments/staging/content_types/1kUEViTN4EmGiEaaeC6ouY",
                recordedRequest.path)
    }
}