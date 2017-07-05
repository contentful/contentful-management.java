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

import com.contentful.java.cma.lib.TestCallback
import com.contentful.java.cma.lib.TestUtils
import okhttp3.mockwebserver.MockResponse
import kotlin.test.assertEquals
import org.junit.Test as test

class OrganizationTests : BaseTest() {
    @test fun testFetchAll() {
        val responseBody = TestUtils.fileToString("organizations_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.organizations().async()
                .fetchAll(TestCallback()) as TestCallback)!!

        assertEquals(4, result.total)
        assertEquals(25, result.limit)
        assertEquals(0, result.skip)

        assertEquals("Contentful", result.items[0].name)
        assertEquals("781CC83AZAPo72ADSs1BR1", result.items[0].id)

        assertEquals("Space Templates", result.items[1].name)
        assertEquals("781CC83AZAPo72ADSs1BR2", result.items[1].id)

        assertEquals("Marios Org", result.items[2].name)
        assertEquals("781CC83AZAPo72ADSs1BR3", result.items[2].id)

        assertEquals("Contentful-SDK-Tests", result.items[3].name)
        assertEquals("781CC83AZAPo72ADSs1BR4", result.items[3].id)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/organizations", recordedRequest.path)
    }

    @test fun testFetchAllWithQuery() {
        val responseBody = TestUtils.fileToString("organizations_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        assertTestCallback(client!!.organizations().async()
                .fetchAll(hashMapOf("skip" to "4"),
                        TestCallback()) as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/organizations?skip=4", recordedRequest.path)
    }
}