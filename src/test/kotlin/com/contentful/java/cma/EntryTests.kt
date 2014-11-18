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
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import com.contentful.java.cma.lib.getBodyAsString
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import retrofit.RetrofitError
import java.io.IOException
import com.contentful.java.cma.lib.TestCallback
import kotlin.test.assertNull
import com.contentful.java.cma.model.CMAEntry

/**
 * Entry Tests.
 */
class EntryTests : BaseTest() {
    test fun testArchive() {
        val responseBody = TestUtils.fileToString("entry_archive_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))
        val entry = CMAEntry().setId("entryid").setSpaceId("spaceid")
        assertFalse(entry.isArchived())
        val result = assertTestCallback(
                client!!.entries().async().archive(entry, TestCallback()) as TestCallback)
        assertTrue(result.isArchived())

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/entries/entryid/archived", recordedRequest.getPath())
    }

    test fun testCreate() {
        val requestBody = TestUtils.fileToString("entry_create_request.json")
        val responseBody = TestUtils.fileToString("entry_create_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val entry = CMAEntry()
                .setField("fid1", "value1", "en-US")
                .setField("fid2", "value2", "en-US")

        val result = assertTestCallback(client!!.entries()
                .async()
                .create("spaceid", entry, TestCallback()) as TestCallback)

        assertEquals(2, result.getFields().size)

        val entries = result.getFields().entrySet().toList()
        assertEquals("id1", entries[0].key)
        assertEquals("id2", entries[1].key)

        assertEquals("value1", entries[0].value["en-US"])
        assertEquals("value2", entries[1].value["en-US"])

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/entries", recordedRequest.getPath())
        assertEquals(requestBody, recordedRequest.getBodyAsString())
    }

    test fun testCreateWithId() {
        val requestBody = TestUtils.fileToString("entry_create_request.json")
        val responseBody = TestUtils.fileToString("entry_create_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val entry = CMAEntry()
                .setId("entryid")
                .setField("fid1", "value1", "en-US")
                .setField("fid2", "value2", "en-US")

        assertTestCallback(client!!.entries().async().create(
                "spaceid", entry, TestCallback()) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/entries/entryid", recordedRequest.getPath())
        assertEquals(requestBody, recordedRequest.getBodyAsString())
    }

    test fun testDelete() {
        server!!.enqueue(MockResponse().setResponseCode(200))
        assertTestCallback(client!!.entries().async().delete(
                "spaceid", "entryid", TestCallback()) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("DELETE", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/entries/entryid", recordedRequest.getPath())
    }

    test fun testFetchAll() {
        val responseBody = TestUtils.fileToString("entry_fetch_all_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.entries().async().fetchAll(
                "spaceid", TestCallback()) as TestCallback)

        assertEquals("Array", result.getSys()["type"])
        assertEquals(1, result.getTotal())
        assertEquals(1, result.getItems().size)

        val item = result.getItems()[0]
        assertEquals(2, item.getFields().size)
        assertNotNull(item.getFields()["name"])
        assertNotNull(item.getFields()["type"])
        assertNull(result.getIncludes())

        // Request
        val request = server!!.takeRequest()
        assertEquals("GET", request.getMethod())
        assertEquals("/spaces/spaceid/entries", request.getPath())
    }

    test fun testFetchWithId() {
        val responseBody = TestUtils.fileToString("entry_fetch_one_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.entries().async().fetchOne(
                "space", "entry", TestCallback()) as TestCallback)

        val sys = result.getSys()
        val fields = result.getFields()

        assertEquals("Entry", sys["type"])
        assertEquals("entryid", sys["id"])
        assertEquals(2, fields.size)
        assertEquals("http://www.url.com", fields["url"]["en-US"])
        assertEquals("value", fields["key"]["en-US"])

        // Request
        val request = server!!.takeRequest()
        assertEquals("GET", request.getMethod())
        assertEquals("/spaces/space/entries/entry", request.getPath())
    }

    test fun testParseEntryWithList() {
        gson!!.fromJson(
                TestUtils.fileToString("entry_with_list_object.json"),
                javaClass<CMAEntry>())
    }

    test fun testUpdate() {
        val requestBody = TestUtils.fileToString("entry_update_request.json")
        val responseBody = TestUtils.fileToString("entry_update_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.entries().async().update(CMAEntry()
                .setId("entryid")
                .setSpaceId("spaceid")
                .setVersion(1.0)
                .setField("fid1", "newvalue1", "en-US")
                .setField("fid2", "newvalue2", "en-US"), TestCallback()) as TestCallback)

        val fields = result.getFields().entrySet().toList()

        assertEquals("entryid", result.getSys()["id"])
        assertEquals("Entry", result.getSys()["type"])
        assertEquals(2, fields.size)
        assertEquals("fid1", fields[0].key)
        assertEquals("newvalue1", fields[0].value["en-US"])
        assertEquals("fid2", fields[1].key)
        assertEquals("newvalue2", fields[1].value["en-US"])

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/entries/entryid", recordedRequest.getPath())
        assertNotNull(recordedRequest.getHeader("X-Contentful-Version"))
        assertEquals(requestBody, recordedRequest.getBodyAsString())
    }

    test fun testPublish() {
        server!!.enqueue(MockResponse().setResponseCode(200))

        assertTestCallback(client!!.entries().async().publish(CMAEntry()
                .setId("entryid")
                .setSpaceId("spaceid")
                .setVersion(1.0), TestCallback(true)) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/entries/entryid/published", recordedRequest.getPath())
        assertNotNull(recordedRequest.getHeader("X-Contentful-Version"))
    }

    test fun testUnArchive() {
        server!!.enqueue(MockResponse().setResponseCode(200))

        assertTestCallback(client!!.entries().async().unArchive(
                CMAEntry().setId("entryid").setSpaceId("spaceid"),
                TestCallback(true)) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("DELETE", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/entries/entryid/archived", recordedRequest.getPath())
    }

    test fun testUnPublish() {
        server!!.enqueue(MockResponse().setResponseCode(200))

        assertTestCallback(client!!.entries().async().unPublish(
                CMAEntry().setId("entryid").setSpaceId("spaceid"),
                TestCallback(true)) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("DELETE", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/entries/entryid/published", recordedRequest.getPath())
    }

    test(expected = javaClass<RetrofitError>())
    fun testRetainsSysOnNetworkError() {
        val badClient = CMAClient.Builder()
                .setAccessToken("accesstoken")
                .setClient { throw RetrofitError.unexpectedError(it.getUrl(), IOException()) }
                .build()

        val entry = CMAEntry().setVersion(31337.0)
        try {
            badClient.entries().create("spaceid", entry)
        } catch (e: RetrofitError) {
            assertEquals(31337, entry.getVersion())
            throw e
        }
    }
}
