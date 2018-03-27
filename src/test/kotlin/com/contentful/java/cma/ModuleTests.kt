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

import com.contentful.java.cma.model.CMALink
import retrofit2.Retrofit
import java.util.concurrent.CountDownLatch
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import org.junit.Before as before
import org.junit.Test as test

class ModuleTests : BaseTest() {
    private var module: AbsModule<Any>? = null

    @before
    fun setup() {
        super.setUp()
        module = object : AbsModule<Any>(null, SynchronousExecutor()) {
            override fun createService(retrofit: Retrofit?): Any? {
                return null
            }
        }
    }

    @test(expected = IllegalArgumentException::class)
    fun testNotNull() {
        try {
            module!!.assertNotNull(null, "parameter")
        } catch (e: IllegalArgumentException) {
            assertEquals("parameter may not be null.", e.message)
            throw e
        }
    }

    @test(expected = IllegalArgumentException::class)
    fun testResourceId() {
        try {
            module!!.getResourceIdOrThrow(CMALink(), "parameter")
        } catch (e: IllegalArgumentException) {
            assertEquals("parameter.setId() was not called.", e.message)
            throw e
        }
    }

    @test(expected = IllegalArgumentException::class)
    fun testSpaceId() {
        try {
            module!!.getSpaceIdOrThrow(CMALink(), "parameter")
        } catch (e: IllegalArgumentException) {
            assertEquals("parameter must have a space associated.", e.message)
            throw e
        }
    }

    @test
    fun testDefersToBackgroundThread() {
        val currentThreadId = Thread.currentThread().id
        var workerThreadId: Long? = null

        val module = object : AbsModule<Any>(null, SynchronousExecutor()) {
            override fun createService(retrofit: Retrofit?): Any? = null

            fun work() {
                val cdl = CountDownLatch(1)

                defer(
                        object : RxExtensions.DefFunc<Long>() {
                            override fun method(): Long? {
                                workerThreadId = Thread.currentThread().id
                                return workerThreadId
                            }
                        },
                        object : CMACallback<Long>() {
                            override fun onSuccess(result: Long?) {
                                cdl.countDown()
                            }
                        })

                cdl.await()
            }
        }

        module.work()
        assertNotEquals(currentThreadId, workerThreadId)
    }
}