package com.contentful.java.cma

import com.contentful.java.cma.lib.TestCallback
import com.contentful.java.cma.lib.TestUtils
import com.contentful.java.cma.model.CMALocale
import com.google.gson.Gson
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.logging.LogManager
import kotlin.test.assertEquals

class LocalesTests{
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

    @Test
    fun testFetchOne() {
        val responseBody = TestUtils.fileToString("locales_get_one.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.locales().async()
                .fetchOne("spaceId", "master", "localeId", TestCallback()) as TestCallback)!!

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
        assertEquals("/spaces/spaceId/environments/master/locales/localeId", recordedRequest.path)
    }

    @Test
    fun testFetchOneWithConfiguredSpaceAndEnvironment() {
        val responseBody = TestUtils.fileToString("locales_get_one.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        assertTestCallback(client!!.locales().async().fetchOne("localeId", TestCallback())
                as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/configuredSpaceId/environments/configuredEnvironmentId"
                + "/locales/localeId",
                recordedRequest.path)
    }

    @Test
    fun testFetchAll() {
        val responseBody = TestUtils.fileToString("locales_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.locales().async()
                .fetchAll("spaceId", "master", TestCallback()) as TestCallback)!!

        assertEquals(2, result.total)
        assertEquals(100, result.limit)
        assertEquals(0, result.skip)
        assertEquals(2, result.items.size)

        assertEquals("U.S. English", result.items[0].name)

        assertEquals("English (British)", result.items[1].name)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/spaceId/environments/master/locales", recordedRequest.path)
    }

    @Test
    fun testFetchAllWithConfiguredSpaceAndEnvironment() {
        val responseBody = TestUtils.fileToString("locales_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        assertTestCallback(client!!.locales().async().fetchAll(TestCallback()) as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/configuredSpaceId/environments/configuredEnvironmentId/locales",
                recordedRequest.path)
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
                .create("spaceId", "master", locale, TestCallback()) as TestCallback)!!

        assertEquals("English (British)", result.name)
        assertEquals("en-UK", result.code)
        assertEquals("en-US", result.fallbackCode)
        assertEquals(false, result.isOptional)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces/spaceId/environments/master/locales/", recordedRequest.path)
    }

    @Test
    fun testCreateNewWithConfiguredSpaceAndEnvironment() {
        val responseBody = TestUtils.fileToString("locales_create.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val locale = CMALocale()
                .setName("English (British)")
                .setCode("en-UK")
                .setFallbackCode("en-US")
                .setOptional(false)

        assertTestCallback(client!!.locales().async().create(locale, TestCallback())
                as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces/configuredSpaceId/environments/configuredEnvironmentId/locales/",
                recordedRequest.path)
    }

    @Test
    fun testUpdate() {
        val responseBody = TestUtils.fileToString("locales_update.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        // DO NOT USE IN PRODUCTION: USE A FETCH FIRST!
        val locale = CMALocale()
                .setId("sampleId")
                .setSpaceId("spaceId")
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
        assertEquals("/spaces/spaceId/environments/master/locales/sampleId", recordedRequest.path)
    }

    @Test
    fun testDeleteOne() {
        val responseBody = TestUtils.fileToString("locales_delete.json")
        server!!.enqueue(MockResponse().setResponseCode(204).setBody(responseBody))

        assertTestCallback(client!!.locales().async()
                .delete(
                        CMALocale().setId("localeId").setSpaceId("spaceId"),
                        TestCallback()
                ) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("DELETE", recordedRequest.method)
        assertEquals("/spaces/spaceId/environments/master/locales/localeId", recordedRequest.path)
    }

    @Test
    fun testFetchAllFromEnvironment() {
        val responseBody = TestUtils.fileToString("locales_get_all_form_environment.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.locales().async()
                .fetchAll("spaceId", "staging", TestCallback()) as TestCallback)!!

        assertEquals(8, result.total)
        assertEquals(100, result.limit)
        assertEquals(0, result.skip)
        assertEquals(8, result.items.size)

        assertEquals("U.S. English", result.items[0].name)
        assertEquals("English (British)", result.items[1].name)

        assertEquals("staging", result.items[0].environmentId)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/spaceId/environments/staging/locales", recordedRequest.path)
    }

    @Test
    fun testFetchOneFromEnvironment() {
        val responseBody = TestUtils.fileToString("locales_get_one_form_environment.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.locales().async()
                .fetchOne("spaceId", "staging", "7lTcrh2SzR626t7fR8rIPD", TestCallback()) as TestCallback)!!

        assertEquals("U.S. English", result.name)
        assertEquals("staging", result.environmentId)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/spaceId/environments/staging/locales/7lTcrh2SzR626t7fR8rIPD", recordedRequest.path)
    }

    @Test
    fun testCreateInEnvironment() {
        val responseBody = TestUtils.fileToString("locales_create_in_environment.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val locale = CMALocale().apply {
            name = "Chinese"
            code = "cn"
        }

        val result = assertTestCallback(client!!.locales().async()
                .create("spaceId", "staging", locale, TestCallback()) as TestCallback)!!

        assertEquals("Chinese", result.name)
        assertEquals("cn", result.code)
        assertEquals("staging", result.environmentId)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces/spaceId/environments/staging/locales/", recordedRequest.path)
    }
}