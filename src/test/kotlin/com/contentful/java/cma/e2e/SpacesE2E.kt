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

package com.contentful.java.cma.e2e

import com.contentful.java.cma.assertTestCallback
import com.contentful.java.cma.lib.TestCallback
import com.contentful.java.cma.model.CMASpace
import org.junit.AfterClass
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.Test as test

class SpacesE2E : Base() {
    companion object {
        private const val CREATED_SPACE_NAME = "created space name"
        private const val CREATED_SPACE_LOCALE = "en-US"

        private const val FIRST_NAME = "UPDATED"
        private const val FIRST_ID = "ckrcqx0r0ffc"

        private const val UPDATED_NAME = "UPDATED"

        @AfterClass
        @JvmStatic
        fun tearDownSpaces() {
            try {
                client.spaces().fetchAll().items.filter { it.name == CREATED_SPACE_NAME }.forEach {
                    client.spaces().delete(it)
                }
            } catch (throwable: Throwable) {
                // nothing found so no problem
            }
        }
    }

    @test
    fun testFetchAll() {
        val result = assertTestCallback(client.spaces().async()
                .fetchAll(TestCallback()) as TestCallback)!!

        assertTrue(result.total > 0, "At least one space should have been found.")
        assertEquals(100, result.limit)
        assertEquals(0, result.skip)

        val first = result.items.first()
        assertEquals(FIRST_ID, first.id)
        assertEquals(FIRST_NAME, first.name)
    }

    @test
    fun testFetchOne() {
        val result = assertTestCallback(client.spaces().async()
                .fetchOne(FIRST_ID, TestCallback()) as TestCallback)!!

        assertEquals(FIRST_NAME, result.name)
        assertEquals(FIRST_ID, result.id)
        assertEquals(FIRST_NAME, result.name)
    }

    @test
    fun testUpdate() {
        val toBeUpdated = assertTestCallback(client.spaces().async()
                .fetchOne(SPACE_ID, TestCallback()) as TestCallback)!!

        toBeUpdated.name = UPDATED_NAME

        val result = assertTestCallback(client.spaces().async()
                .update(toBeUpdated, TestCallback()) as TestCallback)!!

        assertEquals(UPDATED_NAME, result.name)
    }

    @test
    fun testCreate() {
        val space = CMASpace()
                .setName(CREATED_SPACE_NAME)
                .setDefaultLocale(CREATED_SPACE_LOCALE)

        val result = assertTestCallback(client.spaces().async()
                .create(space, TestCallback()) as TestCallback)!!

        assertEquals(CREATED_SPACE_NAME, result.name)
    }
}