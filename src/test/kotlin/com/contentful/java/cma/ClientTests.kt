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

import com.contentful.java.cma.build.GeneratedBuildParameters
import com.contentful.java.cma.interceptor.AuthorizationHeaderInterceptor
import com.contentful.java.cma.lib.TestCallback
import com.contentful.java.cma.lib.TestUtils
import com.contentful.java.cma.model.CMAArray
import com.contentful.java.cma.model.CMASpace
import com.contentful.java.cma.model.CMAUpload
import io.reactivex.Observable
import okhttp3.mockwebserver.MockResponse
import java.io.IOException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.test.*
import org.junit.Test as test

class ClientTests : BaseTest() {
    @test
    fun testCancelledCallback() {
        val responseBody = TestUtils.fileToString("space_fetch_one_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val cdl = CountDownLatch(1)
        var called = false

        val cb = object : CMACallback<CMASpace>() {
            override fun onSuccess(result: CMASpace?) {
                called = true
                cdl.countDown()
            }

            override fun onFailure(exception: RuntimeException?) {
                called = true
                cdl.countDown()
            }
        }

        cb.cancel()
        client!!.spaces().async().fetchOne("whatever", cb)
        cdl.await(3, TimeUnit.SECONDS)

        assertFalse(called)
    }

    @test
    fun testCoreCallbackRetrofitError() {
        val badClient = CMAClient.Builder()
                .setAccessToken("accesstoken")
                .setCoreCallFactory { throw RuntimeException(it.url().toString(), IOException()) }
                .build()

        val cb = TestCallback<CMAArray<CMASpace>>()
        badClient.spaces().async().fetchAll(cb)
        cb.await()
        assertNotNull(cb.error)
    }

    @test(expected = RuntimeException::class)
    fun testUploadCallbackRetrofitError() {
        val badClient = CMAClient.Builder()
                .setAccessToken("accesstoken")
                .setUploadCallFactory { throw RuntimeException(it.url().toString(), IOException()) }
                .build()

        val cb = TestCallback<CMAUpload>()
        badClient.uploads().async().fetchOne("spaceid", "uploadid", cb)
        cb.await()

        assertNotNull(cb.error)
        throw cb.error!!
    }

    @test(expected = IllegalArgumentException::class)
    fun testUploadCallbackWithNull() {
        CMAClient.Builder()
                .setAccessToken("accesstoken")
                .setUploadCallFactory(null)
                .build()
    }

    @test
    fun testCallbackGeneralError() {
        var error: Throwable? = null

        val cb = object : CMACallback<CMASpace>() {
            override fun onSuccess(result: CMASpace?) {
            }

            override fun onFailure(exception: RuntimeException?) {
                super.onFailure(exception)
                error = exception
            }
        }

        Observable.defer {
            Observable.just(CMASpace())
        }.doOnEach {
            throw RuntimeException()
        }.subscribe(
                RxExtensions.ActionSuccess<CMASpace>(client!!.callbackExecutor, cb),
                RxExtensions.ActionError(client!!.callbackExecutor, cb))

        assertTrue(error is RuntimeException)
    }

    @test
    fun testAccessToken() {
        val responseBody = TestUtils.fileToString("space_fetch_all_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))
        client!!.spaces().fetchAll()

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("Bearer token",
                recordedRequest.getHeader(AuthorizationHeaderInterceptor.HEADER_NAME))
    }

    @test
    fun testUserAgent() {
        val responseBody = TestUtils.fileToString("space_fetch_all_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))
        client!!.spaces().fetchAll()

        val versionName = GeneratedBuildParameters.PROJECT_VERSION;

        // Request
        val recordedRequest = server!!.takeRequest()

        assertTrue(recordedRequest.getHeader("User-Agent").contains(versionName))
    }

    @test
    fun testCustomUserAgentHeader() {
        val responseBody = TestUtils.fileToString("space_fetch_all_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))
        client!!.spaces().fetchAll()

        // Request
        val recordedRequest = server!!.takeRequest()

        val actual = recordedRequest.getHeader("X-Contentful-User-Agent")
        assertTrue(actual.contains("sdk contentful-management.java/"))
        assertTrue(actual.contains("platform java/"))
        assertTrue(actual.contains("os"))
    }

    @test(expected = IllegalArgumentException::class)
    fun failsNoAccessToken() {
        try {
            CMAClient.Builder().build()
        } catch (e: IllegalArgumentException) {
            assertEquals("No access token was set.", e.message)
            throw e
        }
    }

    @test(expected = IllegalArgumentException::class)
    fun failsSetNullAccessToken() {
        try {
            CMAClient.Builder().setAccessToken(null)
        } catch (e: IllegalArgumentException) {
            assertEquals("Cannot call setAccessToken() with null.", e.message)
            throw e
        }
    }

    @test(expected = IllegalArgumentException::class)
    fun failsSetNullCallFactory() {
        try {
            CMAClient.Builder().setCoreCallFactory(null)
        } catch (e: IllegalArgumentException) {
            assertEquals("Cannot call setCallFactory() with null.", e.message)
            throw e
        }
    }

    @test(expected = IllegalArgumentException::class)
    fun failsSetNullLogLevel() {
        try {
            CMAClient.Builder().setLogLevel(null)
        } catch (e: IllegalArgumentException) {
            assertEquals("Cannot call setLogLevel() with null.", e.message)
            throw e
        }
    }

    @test(expected = IllegalArgumentException::class)
    fun failsSetNullEndPoint() {
        try {
            CMAClient.Builder().setCoreEndpoint(null)
        } catch (e: IllegalArgumentException) {
            assertEquals("Cannot call setCoreEndpoint() with null.", e.message)
            throw e
        }
    }

    @test(expected = IllegalArgumentException::class)
    fun failsSetCallbackExecutor() {
        try {
            CMAClient.Builder().setCallbackExecutor(null)
        } catch (e: IllegalArgumentException) {
            assertEquals("Cannot call setCallbackExecutor() with null.", e.message)
            throw e
        }
    }

    @test
    fun testSetBasicLogger() {
        val responseBody = TestUtils.fileToString("space_fetch_one_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val builder = StringBuilder()
        val client = CMAClient
                .Builder()
                .setAccessToken("token")
                .setCoreEndpoint(server!!.url("/").toString())
                .setUploadEndpoint(server!!.url("/").toString())
                .setLogLevel(Logger.Level.BASIC)
                .setLogger {
                    builder.append(it)
                }
                .build()

        val cb = TestCallback<CMASpace>()
        client.spaces().async().fetchOne("spaceid", cb)

        assertNull(cb.error)

        // Request
        server!!.takeRequest()
        assertTrue(builder.startsWith("Sending request http://localhost:"))
    }

    @test
    fun testSetNoneLogger() {
        val responseBody = TestUtils.fileToString("space_fetch_one_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val builder = StringBuilder()
        val client = CMAClient
                .Builder()
                .setAccessToken("token")
                .setCoreEndpoint(server!!.url("/").toString())
                .setUploadEndpoint(server!!.url("/").toString())
                .setLogLevel(Logger.Level.NONE)
                .setLogger {
                    builder.append(it)
                }
                .build()

        val cb = TestCallback<CMASpace>()
        client.spaces().async().fetchOne("spaceid", cb)

        assertNull(cb.error)

        // Request
        server!!.takeRequest()
        assertTrue(builder.isEmpty())
    }

    @test
    fun testSetNetworkLogger() {
        val responseBody = TestUtils.fileToString("space_fetch_one_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val builder = StringBuilder()
        val client = CMAClient
                .Builder()
                .setAccessToken("token")
                .setCoreEndpoint(server!!.url("/").toString())
                .setUploadEndpoint(server!!.url("/").toString())
                .setLogLevel(Logger.Level.FULL)
                .setLogger {
                    builder.append(it)
                }
                .build()

        val cb = TestCallback<CMASpace>()
        client.spaces().async().fetchOne("spaceid", cb)

        assertNull(cb.error)

        // Request
        server!!.takeRequest()
        assertTrue(builder.contains("Accept-Encoding: gzip"))
    }

    @test
    fun testAddContentfulApplicationHeader() {
        val responseBody = TestUtils.fileToString("space_fetch_one_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val client = CMAClient
                .Builder()
                .setAccessToken("token")
                .setApplication("UNIT_TEST", "0.0.1-PATCH")
                .setCoreEndpoint(server!!.url("/").toString())
                .setUploadEndpoint(server!!.url("/").toString())
                .build()

        val cb = TestCallback<CMASpace>()
        client.spaces().async().fetchOne("spaceid", cb)

        assertNull(cb.error)

        // Request
        val customUserHeader = server!!.takeRequest().headers.get("X-Contentful-User-Agent")!!
        assertTrue(customUserHeader.contains("app unit_test/0.0.1-PATCH"))
    }

    @test
    fun testAddContentfulIntegrationHeader() {
        val responseBody = TestUtils.fileToString("space_fetch_one_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val client = CMAClient
                .Builder()
                .setAccessToken("token")
                .setIntegration("UNIT_TEST", "0.0.1-PATCH")
                .setCoreEndpoint(server!!.url("/").toString())
                .setUploadEndpoint(server!!.url("/").toString())
                .build()

        val cb = TestCallback<CMASpace>()
        client.spaces().async().fetchOne("spaceid", cb)

        assertNull(cb.error)

        // Request
        val customUserHeader = server!!.takeRequest().headers.get("X-Contentful-User-Agent")!!
        assertTrue(customUserHeader.contains("integration UNIT_TEST/0.0.1-PATCH"))
    }

    @test
    fun testAddRateLimitListener() {
        val responseBody = TestUtils.fileToString("space_fetch_one_response.json")
        server!!
                .enqueue(
                        MockResponse()
                                .setResponseCode(200)
                                .setBody(responseBody)
                                .setHeader("X-Contentful-RateLimit-Hour-Limit", "123")
                )

        val client = CMAClient
                .Builder()
                .setCoreEndpoint(server!!.url("/").toString())
                .setUploadEndpoint(server!!.url("/").toString())
                .setAccessToken("token")
                .setRateLimitListener { limits ->
                    assertEquals(123, limits.hourLimit)
                }
                .build()

        val cb = TestCallback<CMASpace>()
        client.spaces().async().fetchOne("spaceid", cb)
        cb.await()

        assertNull(cb.error)
    }
}