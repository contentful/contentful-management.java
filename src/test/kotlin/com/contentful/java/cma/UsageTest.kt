package com.contentful.java.cma

import com.contentful.java.cma.lib.TestCallback
import com.contentful.java.cma.lib.TestUtils
import com.contentful.java.cma.model.CMAUsage
import com.google.gson.Gson
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.logging.LogManager
import kotlin.test.assertEquals


class UsageTest {
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
                .build()

        gson = CMAClient.createGson()
    }

    @After
    fun tearDown() {
        server!!.shutdown()
    }

    @Test
    fun testFetchOrganizationUsageAll() {
        val responseBody = TestUtils.fileToString("organizations_usage_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val organizationId = "0ar3TVMKEKfNfi1zRoENff"
        val result = assertTestCallback(client!!.organizationUsage().async()
                .fetchAll(organizationId,null, TestCallback()) as TestCallback)!!

        assertEquals(4, result.total)
        assertEquals(100, result.limit)
        assertEquals(0, result.skip)

        assertEquals(CMAUsage.UsageMetric.cma, result.items[0].metric)
        assertEquals(1224, result.items[0].usage)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/organizations/$organizationId/organization_periodic_usages", recordedRequest.path)
    }

    @Test
    fun testFetchSpaceUsageAll() {
        val responseBody = TestUtils.fileToString("organization_space_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val organizationId = "0ar3TVMKEKfNfi1zRoENff"
        val result = assertTestCallback(client!!.spaceUsage().async()
                .fetchAll(organizationId,null, TestCallback()) as TestCallback)!!

        assertEquals(4, result.total)
        assertEquals(100, result.limit)
        assertEquals(0, result.skip)

        assertEquals(CMAUsage.UsageMetric.cma, result.items[0].metric)
        assertEquals(1224, result.items[0].usage)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/organizations/$organizationId/space_periodic_usages", recordedRequest.path)
    }

    @Test
    fun testFetchAllOrganizationUsageWithQuery() {
        val responseBody = TestUtils.fileToString("organizations_usage_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))
        val organizationId = "0ar3TVMKEKfNfi1zRoENff"
        assertTestCallback(client!!.organizationUsage().async()
                .fetchAll(organizationId, hashMapOf("metric[in]" to CMAUsage.UsageMetric.cma.toString()),
                        TestCallback()) as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/organizations/$organizationId/organization_periodic_usages?metric%5Bin%5D=cma", recordedRequest.path)
    }

    @Test
    fun testFetchAllSpaceUsageWithQuery() {
        val responseBody = TestUtils.fileToString("organization_space_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))
        val organizationId = "0ar3TVMKEKfNfi1zRoENff"
        assertTestCallback(client!!.spaceUsage().async()
                .fetchAll(organizationId, hashMapOf("metric[in]" to CMAUsage.UsageMetric.cma.toString()),
                        TestCallback()) as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/organizations/$organizationId/space_periodic_usages?metric%5Bin%5D=cma", recordedRequest.path)
    }
}
