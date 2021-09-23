package com.contentful.java.cma

import com.contentful.java.cma.lib.TestCallback
import com.contentful.java.cma.lib.TestUtils
import com.google.gson.Gson
import java.util.logging.LogManager
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PreviewEnvironmentsTests{
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
    fun testFetchAllWithConfiguredSpace() {
        val responseBody = TestUtils.fileToString("preview_environment_fetch_all_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        assertTestCallback(client!!.previewEnvironments().async().fetchAll(TestCallback()) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/configuredSpaceId/preview_environments",
                recordedRequest.path)
    }

    @Test
    fun testFetchAllWithQuery() {
        val responseBody = TestUtils.fileToString("preview_environment_fetch_all_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val query = mutableMapOf("skip" to "1", "limit" to "2")

        val result = assertTestCallback(client!!.previewEnvironments().async()
            .fetchAll("spaceId", query, TestCallback()) as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        val url = HttpUrl.parse(server!!.url(recordedRequest.path).toString())!!
        assertEquals("1", url.queryParameter("skip"))
        assertEquals("2", url.queryParameter("limit"))

        assertEquals(2, result.total)
        assertEquals(100, result.limit)
        assertEquals(0, result.skip)
        assertEquals(2, result.items.size)

        assertEquals("<web_preview_id>", result.items[0].id)
        assertEquals("web preview", result.items[0].name)
        assertEquals("web preview description", result.items[0].description)
        assertEquals("<space_id>", result.items[0].spaceId)
        assertEquals(1, result.items[0].configurations.size)
        assertEquals("https://endeza22it7y5.x.pipedream.net/web_preview", result.items[0].configurations[0].url)
        assertEquals("testEntry", result.items[0].configurations[0].contentType)
        assertTrue(result.items[0].configurations[0].isEnabled)
        assertFalse(result.items[0].configurations[0].isExample)

        assertEquals("<preview_config_id>", result.items[1].id)
        assertEquals("preview config", result.items[1].name)
        assertEquals("preview config description", result.items[1].description)
        assertEquals("<space_id>", result.items[1].spaceId)
        assertEquals(1, result.items[1].configurations.size)
        assertEquals("https://endeza22it7y5.x.pipedream.net/test", result.items[1].configurations[0].url)
        assertEquals("testEntry1", result.items[1].configurations[0].contentType)
        assertTrue(result.items[1].configurations[0].isEnabled)
        assertFalse(result.items[1].configurations[0].isExample)
    }
}