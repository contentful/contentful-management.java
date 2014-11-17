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
import org.junit.Before as before
import kotlin.test.assertEquals
import com.contentful.java.cma.model.CMAResource
import retrofit.RestAdapter

/**
 * Module Tests.
 */
class ModuleTests : BaseTest() {
    var module: AbsModule<Any>? = null

    before fun setup() {
        super<BaseTest>.setUp()
        module = object : AbsModule<Any>(null) {
            override fun createService(restAdapter: RestAdapter?): Any? {
                return null
            }
        }
    }

    test(expected = javaClass<IllegalArgumentException>())
    fun testNotNull() {
        try {
            module!!.assertNotNull(null, "parameter")
        } catch (e: IllegalArgumentException) {
            assertEquals("parameter may not be null.", e.getMessage())
            throw e
        }
    }

    test(expected = javaClass<IllegalArgumentException>())
    fun testResourceId() {
        try {
            module!!.getResourceIdOrThrow(CMAResource(), "parameter")
        } catch (e: IllegalArgumentException) {
            assertEquals("parameter.setId() was not called.", e.getMessage())
            throw e
        }
    }

    test(expected = javaClass<IllegalArgumentException>())
    fun testSpaceId() {
        try {
            module!!.getSpaceIdOrThrow(CMAResource(), "parameter")
        } catch (e: IllegalArgumentException) {
            assertEquals("parameter must have a space associated.", e.getMessage())
            throw e
        }
    }
}