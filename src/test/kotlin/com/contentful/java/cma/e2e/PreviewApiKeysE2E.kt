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
import com.contentful.java.cma.model.CMAApiKey
import com.contentful.java.cma.model.CMAPreviewApiKey
import org.junit.AfterClass
import org.junit.BeforeClass
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.Test as test

class PreviewApiKeysE2E : Base() {
    companion object {
        var FIRST_ID = "first_e2e_key"
        private const val FIRST_NAME = "Preview test key"
        private const val FIRST_DESCRIPTION = "First preview API Key Description"

        @BeforeClass
        @JvmStatic
        fun setUpPreviewKeys() {
            val key = CMAApiKey().apply {
                name = FIRST_NAME
                description = FIRST_DESCRIPTION
            }

            val result = client.apiKeys().create(SPACE_ID, key)
            FIRST_ID = result.previewApiKey.id
        }

        @AfterClass
        @JvmStatic
        fun tearDownApiKeys() {
            client.apiKeys().fetchAll(SPACE_ID).items.forEach {
                client.apiKeys().delete(it)
            }
        }
    }

    @test
    fun testFetchAll() {
        val result = assertTestCallback(client.previewApiKeys().async()
                .fetchAll(SPACE_ID, TestCallback()) as TestCallback)!!

        assertTrue(result.total > 0, "At least one api key should have been created.")
        assertEquals(25, result.limit)
        assertEquals(0, result.skip)

        val first = result.items[0]
        assertTrue(first is CMAPreviewApiKey)
        assertTrue(first.accessToken.isNotBlank())
    }

    @test
    fun testFetchOne() {
        val result = assertTestCallback(client.previewApiKeys().async()
                .fetchOne(SPACE_ID, FIRST_ID, TestCallback()) as TestCallback)!!

        assertTrue(result.accessToken.isNotBlank())
    }
}