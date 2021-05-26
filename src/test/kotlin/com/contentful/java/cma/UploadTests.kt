/*
 * Copyright (C) 2019 Contentful GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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
import com.contentful.java.cma.model.CMAType
import com.contentful.java.cma.model.CMAUpload
import com.google.gson.Gson
import junit.framework.TestCase.assertEquals
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import java.util.logging.LogManager
import org.junit.Test as test

class UploadTests {
    var server: MockWebServer? = null
    var client: CMAClient? = null
    var gson: Gson? = null

    @Before
    fun setUp() {
        LogManager.getLogManager().reset()
        // MockWebServer
        server = MockWebServer()
        server!!.start()

        // overwrite client to not use environments
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
    fun testPostBinaryToUpload() {
        val responseBody = TestUtils.fileToString("upload_post_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val uploadStream = TestUtils.fileToInputStream("upload_post_payload.jpg")

        val result = assertTestCallback(client!!.uploads().async()
                .create("space_id", uploadStream, TestCallback()) as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces/space_id/uploads", recordedRequest.path)
        assertEquals("upload_id", result.id)
    }

    @test
    fun testPostBinaryToUploadWithConfiguredSpaceAndEnvironment() {
        val responseBody = TestUtils.fileToString("upload_post_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val uploadStream = TestUtils.fileToInputStream("upload_post_payload.jpg")

        assertTestCallback(client!!.uploads().async().create(uploadStream, TestCallback())
                as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces/configuredSpaceId/uploads", recordedRequest.path)
    }

    @test
    fun testGetUploadWithId() {
        val responseBody = TestUtils.fileToString("upload_get_upload_with_id.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.uploads().async()
                .fetchOne("space_id", "upload_id", TestCallback()) as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/space_id/uploads/upload_id", recordedRequest.path)

        assertEquals("upload_id", result.system.id)
        assertEquals(CMAType.Upload, result.system.type)
    }

    @test
    fun testGetUploadWithIdWithConfiguredSpaceAndEnvironment() {
        val responseBody = TestUtils.fileToString("upload_get_upload_with_id.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        assertTestCallback(client!!.uploads().async().fetchOne("upload_id", TestCallback())
                as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/configuredSpaceId/uploads/upload_id", recordedRequest.path)
    }

    @test
    fun testDeleteUpload() {
        server!!.enqueue(MockResponse().setResponseCode(204))

        val result = assertTestCallback(client!!.uploads().async()
                .delete(CMAUpload().apply {
                    spaceId = "space_id"
                    id = "upload_id"
                }, TestCallback()) as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("DELETE", recordedRequest.method)
        assertEquals("/spaces/space_id/uploads/upload_id", recordedRequest.path)
        assertEquals(204, result)
    }

    @test
    fun testDeleteUploadWithObject() {
        server!!.enqueue(MockResponse().setResponseCode(204))

        val result = assertTestCallback(client!!.uploads().async()
                .delete(
                        CMAUpload().setId("upload_id").setSpaceId("space_id"),
                        TestCallback()
                ) as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("DELETE", recordedRequest.method)
        assertEquals("/spaces/space_id/uploads/upload_id", recordedRequest.path)
        assertEquals(204, result)
    }
}
