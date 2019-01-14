/*
 * Copyright (C) 2019 Contentful GmbH
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
import com.contentful.java.cma.model.CMALink
import com.contentful.java.cma.model.CMANotWithEnvironmentsException
import com.contentful.java.cma.model.CMAType
import com.google.gson.Gson
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import java.util.logging.LogManager
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import org.junit.Test as test

class ApiKeysTests {
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
        assertEquals("Master Key - Do not share", first.name)
        assertEquals("", first.description)
        assertEquals("<token>", first.accessToken)
        assertEquals("5szEuycyXqACwhWgwNwX2m", first.previewApiKey.id)
        assertEquals(CMAType.Link, first.previewApiKey.system.type)
        assertEquals(CMAType.PreviewApiKey, first.previewApiKey.system.linkType)

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

        assertEquals("Master Key - Do not share", result.name)
        assertEquals("", result.description)
        assertEquals("<token>", result.accessToken)

        assertNotNull(result.previewApiKey)
        assertEquals(CMAType.Link, result.previewApiKey.system.type)
        assertEquals(CMAType.PreviewApiKey, result.previewApiKey.system.linkType)
        assertEquals("5szEuycyXqACwhWgwNwX2m", result.previewApiKey.id)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/spaceid/api_keys/keyid", recordedRequest.path)
    }

    @test
    fun testUpdate() {
        val responseBody = TestUtils.fileToString("apikeys_update.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val key = CMAApiKey().apply {
            setId<CMAApiKey>("keyid")
            setSpaceId<CMAApiKey>("spaceid")
            setVersion<CMAApiKey>(4)
            name = "Master Key - Do not update"
            addEnvironment("develop")
        }

        val result = assertTestCallback(client!!.apiKeys().async()
                .update(key, TestCallback()) as TestCallback)!!

        assertEquals("First API Key Name", result.name)
        assertEquals("Updated API Key Description", result.description)
        assertEquals("<access_token>", result.accessToken)
        assertEquals("master", result.environments[0].id)
        assertEquals("java_e2e", result.environments[1].id)

        assertNotNull(result.previewApiKey)
        assertEquals(CMAType.Link, result.previewApiKey.system.type)
        assertEquals(CMAType.PreviewApiKey, result.previewApiKey.system.linkType)
        assertEquals("5szEuycyXqACwhWgwNwX2m", result.previewApiKey.id)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.method)
        assertEquals("/spaces/spaceid/api_keys/keyid", recordedRequest.path)
    }

    @test
    fun testCreate() {
        val responseBody = TestUtils.fileToString("apikeys_create.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val apiKey = CMAApiKey()
                .setName("Test")
                .setDescription("Some Description.")
                .addEnvironment("master")
                .addEnvironment("staging")

        val result = assertTestCallback(client!!.apiKeys().async()
                .create("spaceid", apiKey, TestCallback()) as TestCallback)!!

        assertEquals("Test", result.name)
        assertEquals("some Description",
                result.description)
        assertEquals("<token>", result.accessToken)
        assertEquals("master", result.environments.first().id)
        assertEquals("staging", result.environments.last().id)

        assertNotNull(result.previewApiKey)
        assertEquals(CMAType.Link, result.previewApiKey.system.type)
        assertEquals(CMAType.PreviewApiKey, result.previewApiKey.system.linkType)
        assertEquals("2AQ2dSH1WrSrI9G2MNA8cW", result.previewApiKey.id)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces/spaceid/api_keys", recordedRequest.path)
    }

    @test
    fun testDelete() {
        server!!.enqueue(MockResponse().setResponseCode(204).setBody(""))

        val apiKey = CMAApiKey()
                .setId<CMAApiKey>("id")
                .setSpaceId<CMAApiKey>("spaceid")

        val result = assertTestCallback(client!!.apiKeys().async()
                .delete(apiKey, TestCallback()) as TestCallback)!!

        assertEquals(204, result)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("DELETE", recordedRequest.method)
        assertEquals("/spaces/spaceid/api_keys/id", recordedRequest.path)
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

    @test(expected = CMANotWithEnvironmentsException::class)
    fun testThrowsIfConfiguredEnvironmentIsUsed() {
        client = CMAClient.Builder()
                .setAccessToken("token")
                .setCoreEndpoint(server!!.url("/").toString())
                .setUploadEndpoint(server!!.url("/").toString())
                .setSpaceId("configuredSpaceId")
                .setEnvironmentId("configuredEnvironmentId")
                .build()

        client!!.apiKeys().fetchAll()
    }

    @test
    fun testOverrideConfigurationDoesNotThrow() {
        client = CMAClient.Builder()
                .setAccessToken("token")
                .setCoreEndpoint(server!!.url("/").toString())
                .setUploadEndpoint(server!!.url("/").toString())
                .setSpaceId("configuredSpaceId")
                .setEnvironmentId("configuredEnvironmentId")
                .build()

        val responseBody = TestUtils.fileToString("apikeys_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        client!!.apiKeys().fetchAll("overwrittenSpaceId")

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/overwrittenSpaceId/api_keys", recordedRequest.path)
    }

}