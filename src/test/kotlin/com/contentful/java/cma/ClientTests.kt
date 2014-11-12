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

import org.junit.Test as test
import kotlin.test.assertEquals
import com.contentful.java.cma.lib.TestCallback
import retrofit.RetrofitError
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import com.contentful.java.cma.lib.Utils
import com.squareup.okhttp.mockwebserver.MockResponse
import kotlin.test.assertFalse
import java.io.IOException
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import rx.Observable

/**
 * Client Tests.
 */
class ClientTests : BaseTest() {
    test(expected = javaClass<IllegalArgumentException>())
    fun failsNoAccessToken() {
        try {
            CMAClient.Builder().build()
        } catch (e: IllegalArgumentException) {
            assertEquals("No access token was set.", e.getMessage())
            throw e
        }
    }

    test(expected = javaClass<IllegalArgumentException>())
    fun failsSetNullAccessToken() {
        try {
            CMAClient.Builder().setAccessToken(null)
        } catch (e: IllegalArgumentException) {
            assertEquals("Cannot call setAccessToken() with null value.", e.getMessage())
            throw e
        }
    }

    test(expected = javaClass<IllegalArgumentException>())
    fun failsSetNullClient() {
        try {
            CMAClient.Builder().setClient(null)
        } catch (e: IllegalArgumentException) {
            assertEquals("Cannot call setClient() with null value.", e.getMessage())
            throw e
        }
    }

    test fun testCancelledCallback() {
        val responseBody = Utils.fileToString("space_fetch_one_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val cdl = CountDownLatch(1)
        var called = false

        val cb = object : CMACallback<CMASpace>() {
            override fun onSuccess(result: CMASpace?) {
                called = true
                cdl.countDown()
            }

            override fun onFailure(retrofitError: RetrofitError?) {
                called = true
                cdl.countDown()
            }
        }

        cb.cancel()
        client!!.spaces().async().fetchOne("whatever", cb)
        cdl.await(3, TimeUnit.SECONDS)

        assertFalse(called)
    }

    test fun testCallbackRetrofitError() {
        val badClient = CMAClient.Builder()
                .setAccessToken("accesstoken")
                .setClient { throw RetrofitError.unexpectedError(it.getUrl(), IOException()) }
                .build()

        val cb = TestCallback<CMAArray<CMASpace>>()
        badClient.spaces().async().fetchAll(cb)
        cb.await()
        assertNotNull(cb.error)
    }

    test fun testCallbackGeneralError() {
        var error: Throwable? = null

        val cb = object : CMACallback<CMASpace>() {
            override fun onSuccess(result: CMASpace?) {
            }

            override fun onFailure(retrofitError: RetrofitError?) {
                super<CMACallback>.onFailure(retrofitError)
                error = retrofitError
            }
        }

        Observable.defer {
            Observable.just(CMASpace())
        }.doOnEach {
            throw RuntimeException()
        }.subscribe(
                RxExtensions.ActionSuccess<CMASpace>(cb),
                RxExtensions.ActionError(cb))

        assertTrue(error is RetrofitError)
    }
}