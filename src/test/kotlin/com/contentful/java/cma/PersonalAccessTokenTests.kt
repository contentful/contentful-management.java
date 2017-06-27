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
import com.contentful.java.cma.model.CMAHttpException
import com.contentful.java.cma.model.CMAPersonalAccessToken
import com.contentful.java.cma.model.CMAPersonalAccessToken.Scope.Manage
import com.contentful.java.cma.model.CMAPersonalAccessToken.Scope.Read
import okhttp3.mockwebserver.MockResponse
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import org.junit.Test as test

class PersonalAccessTokenTests : BaseTest() {
    @test fun testFetchAll() {
        val responseBody = TestUtils.fileToString("personal_access_token_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.personalAccessTokens().async()
                .fetchAll(TestCallback()) as TestCallback)!!

        assertEquals(2, result.total)
        assertEquals(25, result.limit)
        assertEquals(0, result.skip)

        val first = result.items[0]
        assertEquals("Test Token", first.name)
        assertEquals(1, first.scopes.size)
        assertEquals(Manage, first.scopes[0])
        assertNull(first.token)
        assertNull(first.revokedAt)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/users/me/access_tokens", recordedRequest.path)
    }

    @test fun testFetchOne() {
        val responseBody = TestUtils.fileToString("personal_access_token_get_one.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.personalAccessTokens().async()
                .fetchOne("id", TestCallback()) as TestCallback)!!

        assertEquals("Test Token", result.name)
        assertEquals(1, result.scopes.size)
        assertEquals(Read, result.scopes[0])
        assertNull(result.token)
        assertNull(result.revokedAt)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/users/me/access_tokens/id", recordedRequest.path)
    }

    @test fun testCreate() {
        val responseBody = TestUtils.fileToString("personal_access_token_create.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val token = CMAPersonalAccessToken()
                .setName("Test Token")
                .addScope(Read)

        val result = assertTestCallback(client!!.personalAccessTokens().async()
                .create(token, TestCallback()) as TestCallback)!!

        assertEquals("Test Token", result.name)
        assertEquals(1, result.scopes.size)
        assertEquals(Read, result.scopes[0])
        assertEquals("CFPAT-TOKEN", result.token)
        assertNull(result.revokedAt)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/users/me/access_tokens", recordedRequest.path)
    }

    @test(expected = CMAHttpException::class)
    fun testCreateError() {
        val responseBody = TestUtils.fileToString("personal_access_token_create_error.json")
        server!!.enqueue(MockResponse().setResponseCode(400).setBody(responseBody))

        val token = CMAPersonalAccessToken()
                .setName("Test Token")
                .addScope(null)

        client!!.personalAccessTokens().create(token);
    }

    @test fun testRevoke() {
        val responseBody = TestUtils.fileToString("personal_access_token_revoke.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val token = CMAPersonalAccessToken()
        token.system.id = "tokenId"

        val result = assertTestCallback(client!!.personalAccessTokens().async()
                .revoke(token, TestCallback()) as TestCallback)!!

        assertNotNull(result.revokedAt)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.method)
        assertEquals("/users/me/access_tokens/tokenId/revoked", recordedRequest.path)
    }
}