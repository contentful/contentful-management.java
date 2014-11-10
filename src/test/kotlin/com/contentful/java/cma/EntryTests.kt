package com.contentful.java.cma

import org.junit.Test as test
import com.contentful.java.cma.lib.Utils
import com.squareup.okhttp.mockwebserver.MockResponse
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import com.contentful.java.cma.lib.getBodyAsString
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import retrofit.RetrofitError
import java.io.IOException

/**
 * Entry Tests.
 */
class EntryTests : BaseTest() {
    test fun testArchive() {
        val responseBody = Utils.fileToString("entry_archive_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))
        val entry = CMAEntry().setId("entryid").setSpaceId("spaceid")
        assertFalse(entry.isArchived())
        val result = client!!.entries().archive(entry)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/entries/entryid/archived", recordedRequest.getPath())
        assertTrue(result.isArchived())
    }

    test fun testCreate() {
        val requestBody = Utils.fileToString("entry_create_request.json")
        val responseBody = Utils.fileToString("entry_create_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val entry = CMAEntry()
                .addField("fid1", "value1", "en-US")
                .addField("fid2", "value2", "en-US")

        val result = client!!.entries().create("spaceid", entry)
        assertEquals(2, result.fields.size)

        val entries = result.fields.entrySet().toList()
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
        val requestBody = Utils.fileToString("entry_create_request.json")
        val responseBody = Utils.fileToString("entry_create_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val entry = CMAEntry()
                .setId("entryid")
                .addField("fid1", "value1", "en-US")
                .addField("fid2", "value2", "en-US")

        client!!.entries().create("spaceid", entry)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/entries/entryid", recordedRequest.getPath())
        assertEquals(requestBody, recordedRequest.getBodyAsString())
    }

    test fun testDelete() {
        server!!.enqueue(MockResponse().setResponseCode(200))
        client!!.entries().delete("spaceid", "entryid")

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("DELETE", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/entries/entryid", recordedRequest.getPath())
    }

    test fun testFetchAll() {
        val responseBody = Utils.fileToString("entry_fetch_all_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = client!!.entries().fetchAll("spaceid")
        assertEquals("Array", result.sys["type"])
        assertEquals(1, result.total)
        assertEquals(1, result.items.size)
        assertEquals(2, result.items[0].fields.size)
        assertNotNull(result.items[0].fields["name"])
        assertNotNull(result.items[0].fields["type"])

        // Request
        val request = server!!.takeRequest()
        assertEquals("GET", request.getMethod())
        assertEquals("/spaces/spaceid/entries", request.getPath())
    }

    test fun testFetchWithId() {
        val responseBody = Utils.fileToString("entry_fetch_one_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = client!!.entries().fetchOne("space", "entry")
        assertEquals("Entry", result.sys["type"])
        assertEquals("entryid", result.sys["id"])
        assertEquals(2, result.fields.size)
        assertEquals("http://www.url.com", result.fields["url"]["en-US"])
        assertEquals("value", result.fields["key"]["en-US"])

        // Request
        val request = server!!.takeRequest()
        assertEquals("GET", request.getMethod())
        assertEquals("/spaces/space/entries/entry", request.getPath())
    }

    test fun testUpdate() {
        val requestBody = Utils.fileToString("entry_update_request.json")
        val responseBody = Utils.fileToString("entry_update_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = client!!.entries().update(CMAEntry()
                .setId("entryid")
                .setSpaceId("spaceid")
                .setVersion(1.0)
                .addField("fid1", "newvalue1", "en-US")
                .addField("fid2", "newvalue2", "en-US"))

        val fields = result.fields.entrySet().toList()

        assertEquals("entryid", result.sys["id"])
        assertEquals("Entry", result.sys["type"])
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
        client!!.entries().publish(CMAEntry()
                .setId("entryid")
                .setSpaceId("spaceid")
                .setVersion(1.0))

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/entries/entryid/published", recordedRequest.getPath())
        assertNotNull(recordedRequest.getHeader("X-Contentful-Version"))
    }

    test fun testUnArchive() {
        server!!.enqueue(MockResponse().setResponseCode(200))
        client!!.entries().unArchive(CMAEntry().setId("entryid").setSpaceId("spaceid"))

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("DELETE", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/entries/entryid/archived", recordedRequest.getPath())
    }

    test fun testUnPublish() {
        server!!.enqueue(MockResponse().setResponseCode(200))
        client!!.entries().unPublish(CMAEntry().setId("entryid").setSpaceId("spaceid"))

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
