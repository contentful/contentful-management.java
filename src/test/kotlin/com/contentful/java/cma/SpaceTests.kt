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

import com.contentful.java.cma.lib.ModuleTestUtils
import com.contentful.java.cma.lib.TestCallback
import com.contentful.java.cma.lib.TestUtils
import com.contentful.java.cma.model.CMASpace
import okhttp3.mockwebserver.MockResponse
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import org.junit.Test as tests

class SpaceTests : BaseTest() {
    @org.junit.Test fun testCreate() {
        val requestBody = TestUtils.fileToString("space_create_request.json")
        val responseBody = TestUtils.fileToString("space_create_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.spaces().async().create(
                "xxx", TestCallback()) as TestCallback)!!

        assertEquals("spaceid", result.sys["id"])
        assertEquals("xxx", result.name)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces", recordedRequest.path)
        assertEquals(requestBody, recordedRequest.utf8Body)
    }

    @org.junit.Test fun testCreateInOrg() {
        server!!.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        assertTestCallback(client!!.spaces().async().create(
                "whatever", "org", TestCallback()) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces", recordedRequest.path)
        assertEquals("org", recordedRequest.getHeader("X-Contentful-Organization"))
    }

    @org.junit.Test fun testDelete() {
        val requestBody = "203"
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(requestBody))

        assertTestCallback(client!!.spaces().async().delete(
                "spaceid", TestCallback()) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("DELETE", recordedRequest.method)
        assertEquals("/spaces/spaceid", recordedRequest.path)
    }

    @org.junit.Test fun testFetchAll() {
        val responseBody = TestUtils.fileToString("space_fetch_all_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.spaces().async()
                .fetchAll(TestCallback()) as TestCallback)!!

        val items = result.items
        assertEquals("Array", result.sys["type"])
        assertEquals(2, items.size)
        assertEquals(2, result.total)
        assertEquals(0, result.skip)
        assertEquals(25, result.limit)

        // Space #1
        var sys = items[0].sys
        assertEquals("Space", sys["type"])
        assertEquals("id1", sys["id"])
        assertEquals(1.toDouble(), sys["version"])
        assertEquals("2014-03-21T08:43:52Z", sys["createdAt"])
        assertEquals("2014-04-27T18:16:10Z", sys["updatedAt"])
        assertEquals("space1", items[0].name)

        // Created By
        var map = (items[0].sys["createdBy"] as Map<*, *>)["sys"] as Map<*, *>
        assertEquals("Link", map["type"])
        assertEquals("User", map["linkType"])
        assertEquals("user1", map["id"])

        // Updated By
        map = (items[0].sys["updatedBy"] as Map<*, *>)["sys"] as Map<*, *>
        assertEquals("Link", map["type"])
        assertEquals("User", map["linkType"])
        assertEquals("user1", map["id"])

        // Space #2
        sys = items[1].sys
        assertEquals("Space", sys["type"])
        assertEquals("id2", sys["id"])
        assertEquals(2.toDouble(), sys["version"])
        assertEquals("2014-05-19T09:00:27Z", sys["createdAt"])
        assertEquals("2014-07-09T07:48:24Z", sys["updatedAt"])
        assertEquals("space2", items[1].name)

        // Created By
        map = (sys["createdBy"] as Map<*, *>)["sys"] as Map<*, *>
        assertEquals("Link", map["type"])
        assertEquals("User", map["linkType"])
        assertEquals("user2", map["id"])

        // Updated By
        map = (sys["updatedBy"] as Map<*, *>)["sys"] as Map<*, *>
        assertEquals("Link", map["type"])
        assertEquals("User", map["linkType"])
        assertEquals("user2", map["id"])

        // Request
        val request = server!!.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/spaces?limit=100", request.path)
    }

    @org.junit.Test fun testFetchWithId() {
        val responseBody = TestUtils.fileToString("space_fetch_one_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.spaces().async().fetchOne(
                "spaceid", TestCallback()) as TestCallback)!!

        val sys = result.sys
        assertEquals("Space", sys["type"])
        assertEquals("id1", sys["id"])
        assertEquals(1.toDouble(), sys["version"])
        assertEquals("2014-03-21T08:43:52Z", sys["createdAt"])
        assertEquals("2014-04-27T18:16:10Z", sys["updatedAt"])
        assertEquals("space1", result.name)

        // Created By
        var map = (sys["createdBy"] as Map<*, *>)["sys"] as Map<*, *>
        assertEquals("Link", map["type"])
        assertEquals("User", map["linkType"])
        assertEquals("user1", map["id"])

        // Updated By
        map = (sys["updatedBy"] as Map<*, *>)["sys"] as Map<*, *>
        assertEquals("Link", map["type"])
        assertEquals("User", map["linkType"])
        assertEquals("user1", map["id"])

        // Request
        val request = server!!.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/spaces/spaceid", request.path)
    }

    @org.junit.Test fun testFetchAllLocales() {
        val responseBody = TestUtils.fileToString("space_fetch_locales_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.spaces().async().fetchLocales(
                "spaceid", TestCallback()) as TestCallback)!!

        val item = result.items[0]
        assertEquals("U.S. English", item.name)
        assertEquals("en-US", item.code)
        assertTrue(item.isDefault)
        assertTrue(item.isPublished)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/spaceid/locales?limit=100", recordedRequest.path)
    }

    @org.junit.Test fun testUpdate() {
        val requestBody = TestUtils.fileToString("space_update_request.json")
        val responseBody = TestUtils.fileToString("space_update_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        var space = gson!!.fromJson(
                TestUtils.fileToString("space_update_object.json"),
                CMASpace::class.java)

        space.name = "newname"

        space = assertTestCallback(client!!.spaces().async().update(
                space, TestCallback()) as TestCallback)

        assertEquals(2.toDouble(), space.sys["version"])
        assertEquals("newname", space.name)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.method)
        assertEquals("/spaces/spaceid", recordedRequest.path)
        assertNotNull(recordedRequest.getHeader("X-Contentful-Version"))
        assertEquals(requestBody, recordedRequest.utf8Body)
    }

    @org.junit.Test(expected = Exception::class)
    fun testUpdateFailsWithoutVersion() {
        ModuleTestUtils.assertUpdateWithoutVersion {
            val space: CMASpace = CMASpace().setName("name").setId("id")
            client!!.spaces().update(space)
        }
    }
}