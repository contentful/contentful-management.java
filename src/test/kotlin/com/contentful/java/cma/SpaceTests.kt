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

import com.contentful.java.cma.lib.*
import com.squareup.okhttp.mockwebserver.MockResponse
import org.junit.Test as test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import com.contentful.java.cma.model.CMASpace
import com.contentful.java.cma.model.CMAEntry

class SpaceTests : BaseTest() {
    test fun testCreate() {
        val requestBody = TestUtils.fileToString("space_create_request.json")
        val responseBody = TestUtils.fileToString("space_create_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.spaces().async().create(
                "xxx", TestCallback()) as TestCallback)

        assertEquals("spaceid", result.getSys()["id"])
        assertEquals("xxx", result.getName())

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.getMethod())
        assertEquals("/spaces", recordedRequest.getPath())
        assertEquals(requestBody, recordedRequest.getUtf8Body())
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
        val responseBody = TestUtils.fileToString("space_fetch_all_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.spaces().async()
                .fetchAll(TestCallback()) as TestCallback)

        val items = result.getItems()
        assertEquals("Array", result.getSys()["type"])
        assertEquals(2, items.size())
        assertEquals(2, result.getTotal())
        assertEquals(0, result.getSkip())
        assertEquals(25, result.getLimit())

        // Space #1
        var sys = items[0].getSys()
        assertEquals("Space", sys["type"])
        assertEquals("id1", sys["id"])
        assertEquals(1.toDouble(), sys["version"])
        assertEquals("2014-03-21T08:43:52Z", sys["createdAt"])
        assertEquals("2014-04-27T18:16:10Z", sys["updatedAt"])
        assertEquals("space1", items[0].getName())

        // Created By
        var map = (items[0].getSys()["createdBy"] as Map<*, *>)["sys"] as Map<*, *>
        assertEquals("Link", map["type"])
        assertEquals("User", map["linkType"])
        assertEquals("user1", map["id"])

        // Updated By
        map = (items[0].getSys()["updatedBy"] as Map<*, *>)["sys"] as Map<*, *>
        assertEquals("Link", map["type"])
        assertEquals("User", map["linkType"])
        assertEquals("user1", map["id"])

        // Space #2
        sys = items[1].getSys()
        assertEquals("Space", sys["type"])
        assertEquals("id2", sys["id"])
        assertEquals(2.toDouble(), sys["version"])
        assertEquals("2014-05-19T09:00:27Z", sys["createdAt"])
        assertEquals("2014-07-09T07:48:24Z", sys["updatedAt"])
        assertEquals("space2", items[1].getName())

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
        assertEquals("GET", request.getMethod())
        assertEquals("/spaces", request.getPath())
    }

    test fun testFetchWithId() {
        val responseBody = TestUtils.fileToString("space_fetch_one_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.spaces().async().fetchOne(
                "spaceid", TestCallback()) as TestCallback)

        val sys = result.getSys()
        assertEquals("Space", sys["type"])
        assertEquals("id1", sys["id"])
        assertEquals(1.toDouble(), sys["version"])
        assertEquals("2014-03-21T08:43:52Z", sys["createdAt"])
        assertEquals("2014-04-27T18:16:10Z", sys["updatedAt"])
        assertEquals("space1", result.getName())

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
        assertEquals("GET", request.getMethod())
        assertEquals("/spaces/spaceid", request.getPath())
    }

    test fun testFetchLocales() {
        val responseBody = TestUtils.fileToString("space_fetch_locales_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.spaces().async().fetchLocales(
                "spaceid", TestCallback()) as TestCallback)

        val item = result.getItems()[0]
        assertEquals("U.S. English", item.getName())
        assertEquals("en-US", item.getCode())
        assertTrue(item.isDefault())
        assertTrue(item.isPublished())

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid/locales", recordedRequest.getPath())
    }

    test fun testUpdate() {
        val requestBody = TestUtils.fileToString("space_update_request.json")
        val responseBody = TestUtils.fileToString("space_update_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        var space = gson!!.fromJson(
                TestUtils.fileToString("space_update_object.json"),
                javaClass<CMASpace>())

        space.setName("newname")

        space = assertTestCallback(client!!.spaces().async().update(
                space, TestCallback()) as TestCallback)

        assertEquals(2.toDouble(), space.getSys()["version"])
        assertEquals("newname", space.getName())

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.getMethod())
        assertEquals("/spaces/spaceid", recordedRequest.getPath())
        assertNotNull(recordedRequest.getHeader("X-Contentful-Version"))
        assertEquals(requestBody, recordedRequest.getUtf8Body())
    }

    test(expected = javaClass<Exception>())
    fun testUpdateFailsWithoutVersion() {
        ModuleTestUtils.assertUpdateWithoutVersion {
            val space: CMASpace = CMASpace().setName("name").setId("id")
            client!!.spaces().update(space)
        }
    }
}