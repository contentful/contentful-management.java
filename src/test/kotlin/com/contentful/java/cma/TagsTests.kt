package com.contentful.java.cma

import com.contentful.java.cma.lib.TestCallback
import com.contentful.java.cma.lib.TestUtils
import com.contentful.java.cma.model.CMAVisibility
import com.google.gson.Gson
import java.util.logging.LogManager
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class TagsTests{
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
    fun testFetchAll() {
        val responseBody = TestUtils.fileToString("tag_fetch_all_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.tags().async()
                .fetchAll("spaceId", "master", TestCallback()) as TestCallback)!!

        assertEquals(2, result.total)
        assertEquals(100, result.limit)
        assertEquals(0, result.skip)
        assertEquals(2, result.items.size)

        assertEquals("<tag1_id>", result.items[0].id)
        assertEquals("Tag Name 1", result.items[0].name)
        assertEquals("<space_id>", result.items[0].spaceId)
        assertEquals(CMAVisibility.Public, result.items[0].visibility)
        assertEquals("master", result.items[0].system.environment.environmentId)

        assertEquals("<tag2_id>", result.items[1].id)
        assertEquals("Tag Name 2", result.items[1].name)
        assertEquals("<space_id>", result.items[1].spaceId)
        assertEquals(CMAVisibility.Private, result.items[1].visibility)
        assertEquals("master", result.items[1].system.environment.environmentId)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/spaceId/environments/master/tags", recordedRequest.path)
    }

    @Test
    fun testFetchAllWithConfiguredSpaceAndEnvironment() {
        val responseBody = TestUtils.fileToString("tag_fetch_all_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        assertTestCallback(client!!.tags().async().fetchAll(TestCallback()) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/configuredSpaceId/environments/configuredEnvironmentId/tags",
                recordedRequest.path)
    }

    @Test
    fun testFetchAllWithQuery() {
        val responseBody = TestUtils.fileToString("tag_fetch_all_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val query = mutableMapOf("skip" to "1", "limit" to "2")

        assertTestCallback(client!!.tags().async()
            .fetchAll("spaceId", "master", query, TestCallback()) as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        val url = HttpUrl.parse(server!!.url(recordedRequest.path).toString())!!
        assertEquals("1", url.queryParameter("skip"))
        assertEquals("2", url.queryParameter("limit"))
    }

    @Test
    fun testFetchAllWithQueryWithConfiguredSpaceAndEnvironment() {
        val responseBody = TestUtils.fileToString("tag_fetch_all_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val query = mutableMapOf("skip" to "1", "limit" to "2")

        assertTestCallback(client!!.tags().async()
            .fetchAll(query, TestCallback()) as TestCallback)!!

        // Request
        val request = server!!.takeRequest()
        val url = HttpUrl.parse(server!!.url(request.path).toString())!!
        assertEquals("1", url.queryParameter("skip"))
        assertEquals("2", url.queryParameter("limit"))
        assertEquals(
            "/spaces/configuredSpaceId/environments/configuredEnvironmentId/"
                    + "tags?skip=1&limit=2",
            request.path)
    }
}