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

class UserTests : BaseTest() {
    @test fun testFetchMe() {
        val responseBody = TestUtils.fileToString("user_fetch_me.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.users().async()
                .fetchMe(TestCallback()) as TestCallback)!!

        assertEquals("Mario", result.firstName)
        assertEquals("Bodemann", result.lastName)
        assertEquals("https://avatars.githubusercontent.com/u/1162562?v=3", result.avatarUrl)
        assertEquals("mario@contentful.com", result.email)
        assertEquals(true, result.activated)
        assertEquals(345, result.signInCount)
        assertEquals(true, result.confirmed)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/users/me", recordedRequest.path)
    }
}