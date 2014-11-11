package com.contentful.java.cma

import com.contentful.java.cma.lib.*
import com.squareup.okhttp.mockwebserver.MockResponse
import org.junit.Test as test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Space Tests.
 */
class SpaceTests : BaseTest() {
    test fun testCreate() {
        val requestBody = Utils.fileToString("space_create_request.json")
        val responseBody = Utils.fileToString("space_create_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.spaces().async().create(
                "xxx", TestCallback()) as TestCallback)

        assertEquals("spaceid", result.sys["id"])
        assertEquals("xxx", result.name)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.getMethod())
        assertEquals("/spaces", recordedRequest.getPath())
        assertEquals(requestBody, recordedRequest.getBodyAsString())
    }

    test fun testCreateInOrg() {
        server!!.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        assertTestCallback(client!!.spaces().async().create(
                "whatever", "org", TestCallback()) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.getMethod())
        assertEquals("/spaces", recordedRequest.getPath())
        assertEquals("org", recordedRequest.getHeader("X-Contentful-Organization"))
    }

    test fun testDelete() {
        server!!.enqueue(MockResponse().setResponseCode(200))

        assertTestCallback(client!!.spaces().async().delete(
                "spaceid", TestCallback()) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("DELETE", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid", recordedRequest.getPath())
    }

    test fun testFetchAll() {
        val responseBody = Utils.fileToString("space_fetch_all_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.spaces().async()
                .fetchAll(TestCallback()) as TestCallback)

        assertEquals("Array", result.sys["type"])
        assertEquals(2, result.items.size)
        assertEquals(2, result.total)
        assertEquals(0, result.skip)
        assertEquals(25, result.limit)

        // Space #1
        assertEquals("Space", result.items[0].sys["type"])
        assertEquals("id1", result.items[0].sys["id"])
        assertEquals(1.toDouble(), result.items[0].sys["version"])
        assertEquals("2014-03-21T08:43:52Z", result.items[0].sys["createdAt"])
        assertEquals("2014-04-27T18:16:10Z", result.items[0].sys["updatedAt"])
        assertEquals("space1", result.items[0].name)

        // Created By
        var map = (result.items[0].sys["createdBy"] as Map<*, *>)["sys"] as Map<*, *>
        assertEquals("Link", map["type"])
        assertEquals("User", map["linkType"])
        assertEquals("user1", map["id"])

        // Updated By
        map = (result.items[0].sys["updatedBy"] as Map<*, *>)["sys"] as Map<*, *>
        assertEquals("Link", map["type"])
        assertEquals("User", map["linkType"])
        assertEquals("user1", map["id"])

        // Space #2
        assertEquals("Space", result.items[1].sys["type"])
        assertEquals("id2", result.items[1].sys["id"])
        assertEquals(2.toDouble(), result.items[1].sys["version"])
        assertEquals("2014-05-19T09:00:27Z", result.items[1].sys["createdAt"])
        assertEquals("2014-07-09T07:48:24Z", result.items[1].sys["updatedAt"])
        assertEquals("space2", result.items[1].name)

        // Created By
        map = (result.items[1].sys["createdBy"] as Map<*, *>)["sys"] as Map<*, *>
        assertEquals("Link", map["type"])
        assertEquals("User", map["linkType"])
        assertEquals("user2", map["id"])

        // Updated By
        map = (result.items[1].sys["updatedBy"] as Map<*, *>)["sys"] as Map<*, *>
        assertEquals("Link", map["type"])
        assertEquals("User", map["linkType"])
        assertEquals("user2", map["id"])

        // Request
        val request = server!!.takeRequest()
        assertEquals("GET", request.getMethod())
        assertEquals("/spaces", request.getPath())
    }

    test fun testFetchWithIdentifier() {
        val responseBody = Utils.fileToString("space_fetch_one_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.spaces().async().fetchOne(
                "spaceid", TestCallback()) as TestCallback)

        assertEquals("Space", result.sys["type"])
        assertEquals("id1", result.sys["id"])
        assertEquals(1.toDouble(), result.sys["version"])
        assertEquals("2014-03-21T08:43:52Z", result.sys["createdAt"])
        assertEquals("2014-04-27T18:16:10Z", result.sys["updatedAt"])
        assertEquals("space1", result.name)

        // Created By
        var map = (result.sys["createdBy"] as Map<*, *>)["sys"] as Map<*, *>
        assertEquals("Link", map["type"])
        assertEquals("User", map["linkType"])
        assertEquals("user1", map["id"])

        // Updated By
        map = (result.sys["updatedBy"] as Map<*, *>)["sys"] as Map<*, *>
        assertEquals("Link", map["type"])
        assertEquals("User", map["linkType"])
        assertEquals("user1", map["id"])

        // Request
        val request = server!!.takeRequest()
        assertEquals("GET", request.getMethod())
        assertEquals("/spaces/spaceid", request.getPath())
    }

    test fun testUpdate() {
        val requestBody = Utils.fileToString("space_update_request.json")
        val responseBody = Utils.fileToString("space_update_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        var space = gson!!.fromJson(
                Utils.fileToString("space_update_object.json"),
                javaClass<CMASpace>())

        space.name = "newname"

        space = assertTestCallback(client!!.spaces().async().update(
                space, TestCallback()) as TestCallback)

        assertEquals(2.toDouble(), space.sys["version"])
        assertEquals("newname", space.name)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid", recordedRequest.getPath())
        assertNotNull(recordedRequest.getHeader("X-Contentful-Version"))
        assertEquals(requestBody, recordedRequest.getBodyAsString())
    }
}