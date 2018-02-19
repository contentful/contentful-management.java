package com.contentful.java.cma

import com.contentful.java.cma.lib.TestCallback
import com.contentful.java.cma.lib.TestUtils
import com.contentful.java.cma.model.CMALocale
import okhttp3.mockwebserver.MockResponse
import org.junit.Test
import kotlin.test.assertEquals

class LocalesTests : BaseTest() {
    @Test
    fun testFetchOne() {
        val responseBody = TestUtils.fileToString("locales_get_one.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.locales().async()
                .fetchOne("SPACE_ID", "LOCALE_ID", TestCallback()) as TestCallback)!!

        assertEquals("U.S. English", result.name)
        assertEquals("en-US", result.code)
        assertEquals(null, result.fallbackCode)
        assertEquals(true, result.isDefault)
        assertEquals(true, result.isContentManagementApi)
        assertEquals(true, result.isContentDeliveryApi)
        assertEquals(false, result.isOptional)

        assertEquals("7lTcrh2SzR626t7fR8rIPD", result.id)
        assertEquals("Locale", result.system.type.name)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/SPACE_ID/locales/LOCALE_ID", recordedRequest.path)
    }

    @Test
    fun testFetchAll() {
        val responseBody = TestUtils.fileToString("locales_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.locales().async()
                .fetchAll("SPACE_ID", TestCallback()) as TestCallback)!!

        assertEquals(2, result.total)
        assertEquals(100, result.limit)
        assertEquals(0, result.skip)
        assertEquals(2, result.items.size)

        assertEquals("U.S. English", result.items[0].name)

        assertEquals("English (British)", result.items[1].name)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/SPACE_ID/locales", recordedRequest.path)
    }

    @Test
    fun testFetchAllWithQuery() {
        val responseBody = TestUtils.fileToString("locales_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        assertTestCallback(client!!.locales().async()
                .fetchAll("SPACE_ID",
                        hashMapOf("skip" to "3"),
                        TestCallback()) as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/SPACE_ID/locales?skip=3", recordedRequest.path)
    }

    @Test
    fun testCreateNew() {
        val responseBody = TestUtils.fileToString("locales_create.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val locale = CMALocale()
                .setName("English (British)")
                .setCode("en-UK")
                .setFallbackCode("en-US")
                .setOptional(false)

        val result = assertTestCallback(client!!.locales().async()
                .create("SPACE_ID", locale, TestCallback()) as TestCallback)!!

        assertEquals("English (British)", result.name)
        assertEquals("en-UK", result.code)
        assertEquals("en-US", result.fallbackCode)
        assertEquals(false, result.isOptional)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces/SPACE_ID/locales/", recordedRequest.path)
    }

    @Test
    fun testUpdate() {
        val responseBody = TestUtils.fileToString("locales_update.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        // DO NOT USE IN PRODUCTION: USE A FETCH FIRST!
        val locale = CMALocale()
                .setId("sampleId")
                .setSpaceId("SPACE_ID")
                .setVersion(3)
                .setName("U.S. English")
                .setCode("en-US")
                .setFallbackCode(null)
                .setOptional(false)

        val result = assertTestCallback(client!!.locales().async()
                .update(locale, TestCallback()) as TestCallback)!!

        assertEquals("U.S. English", result.name)
        assertEquals("en-US", result.code)
        assertEquals(null, result.fallbackCode)
        assertEquals(true, result.isDefault)
        assertEquals(true, result.isContentManagementApi)
        assertEquals(true, result.isContentDeliveryApi)
        assertEquals(false, result.isOptional)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.method)
        assertEquals("/spaces/SPACE_ID/locales/sampleId", recordedRequest.path)
    }

    @Test
    fun testDeleteOne() {
        val responseBody = TestUtils.fileToString("locales_delete.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        assertTestCallback(client!!.locales().async()
                .delete(
                        "SPACE_ID",
                        CMALocale().setId("LOCALE_ID"),
                        TestCallback()
                ) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("DELETE", recordedRequest.method)
        assertEquals("/spaces/SPACE_ID/locales/LOCALE_ID", recordedRequest.path)
    }
}