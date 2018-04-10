/*
 * Copyright (C) 2018 Contentful GmbH
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
import com.contentful.java.cma.model.CMAApiKey
import com.contentful.java.cma.model.CMAType
import okhttp3.mockwebserver.MockResponse
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import org.junit.Test as test

class ApiKeysTests : BaseTest() {
    override fun setUp() {
        super.setUp()

        // overwrite client to not use environments
        client = CMAClient.Builder().apply {
            accessToken = "token"
            coreEndpoint = server!!.url("/").toString()
            uploadEndpoint = server!!.url("/").toString()
            spaceId = "configuredSpaceId"
        }.build()
    }

    @test
    fun testFetchAll() {
        val responseBody = TestUtils.fileToString("apikeys_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.apiKeys().async()
                .fetchAll("spaceid", TestCallback()) as TestCallback)!!

        assertEquals(4, result.total)
        assertEquals(25, result.limit)
        assertEquals(0, result.skip)

        val first = result.items[0]
        assertEquals("Website Key", first.name)
        assertEquals("Use this key for your website. Create separate keys for your other apps.",
                first.description)
        assertEquals("<token>", first.accessToken)
        assertNull(first.previewApiKey)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/spaceid/api_keys", recordedRequest.path)
    }

    @test
    fun testFetchOne() {
        val responseBody = TestUtils.fileToString("apikeys_get_one.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.apiKeys().async()
                .fetchOne("spaceid", "keyid", TestCallback()) as TestCallback)!!

        assertEquals("Test", result.name)
        assertEquals("Some Description",
                result.description)
        assertEquals("<token>", result.accessToken)

        assertNotNull(result.previewApiKey)
        assertEquals(CMAType.Link, result.previewApiKey.system.type)
        assertNull(result.previewApiKey.system.linkType)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/spaceid/api_keys/keyid", recordedRequest.path)
    }

    @test
    fun testCreate() {
        val responseBody = TestUtils.fileToString("apikeys_create.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val apiKey = CMAApiKey()
                .setName("Test")
                .setDescription("Some Description.")

        val result = assertTestCallback(client!!.apiKeys().async()
                .create("spaceid", apiKey, TestCallback()) as TestCallback)!!

        assertEquals("Test", result.name)
        assertEquals("some Description",
                result.description)
        assertEquals("<token>", result.accessToken)

        assertNotNull(result.previewApiKey)
        assertEquals(CMAType.Link, result.previewApiKey.system.type)
        assertNull(result.previewApiKey.system.linkType)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces/spaceid/api_keys", recordedRequest.path)
    }

    @test
    fun testQueryForAll() {
        val responseBody = TestUtils.fileToString("apikeys_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val query = hashMapOf("skip" to "6")
        assertTestCallback(client!!.apiKeys().async()
                .fetchAll("spaceid", query, TestCallback()) as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/spaceid/api_keys?skip=6", recordedRequest.path)
    }

    @test
    fun testCreateOneWithConfiguredSpaceAndEnvironment() {
        val responseBody = TestUtils.fileToString("apikeys_create.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        assertTestCallback(client!!.apiKeys().async()
                .create(CMAApiKey(), TestCallback()) as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces/configuredSpaceId/api_keys", recordedRequest.path)
    }

    @test
    fun testFetchOneWithConfiguredSpaceAndEnvironment() {
        val responseBody = TestUtils.fileToString("apikeys_get_one.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        assertTestCallback(client!!.apiKeys().async()
                .fetchOne("keyId", TestCallback()) as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/configuredSpaceId/api_keys/keyId", recordedRequest.path)
    }

    @test
    fun testFetchAllWithConfiguredSpaceAndEnvironment() {
        val responseBody = TestUtils.fileToString("apikeys_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        assertTestCallback(client!!.apiKeys().async().fetchAll(TestCallback()) as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/configuredSpaceId/api_keys", recordedRequest.path)
    }

    @test
    fun testQueryAllWithConfiguredSpaceAndEnvironment() {
        val responseBody = TestUtils.fileToString("apikeys_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val query = hashMapOf("skip" to "6")
        assertTestCallback(client!!.apiKeys().async()
                .fetchAll(query, TestCallback()) as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/configuredSpaceId/api_keys?skip=6", recordedRequest.path)
    }

}