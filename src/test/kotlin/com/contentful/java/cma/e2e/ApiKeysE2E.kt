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

package com.contentful.java.cma.e2e

import com.contentful.java.cma.assertTestCallback
import com.contentful.java.cma.lib.TestCallback
import com.contentful.java.cma.model.CMAApiKey
import com.contentful.java.cma.model.CMAType
import org.junit.AfterClass
import org.junit.BeforeClass
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.junit.Test as test

class ApiKeysE2E : Base() {
    companion object {
        var FIRST_ID = "first_e2e_key"
        private const val FIRST_NAME = "First API Key Name"
        private const val FIRST_DESCRIPTION = "First API Key Description"

        @BeforeClass
        @JvmStatic
        fun setUpApiKeys() {
            val key = CMAApiKey().apply {
                name = FIRST_NAME
                description = FIRST_DESCRIPTION
            }

            val result = client.apiKeys().create(SPACE_ID, key)
            FIRST_ID = result.id
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
        val result = assertTestCallback(client.apiKeys().async()
                .fetchAll(SPACE_ID, TestCallback()) as TestCallback)!!

        assertTrue(result.total > 0, "At least one api key should have been created.")
        assertEquals(25, result.limit)
        assertEquals(0, result.skip)

        val first = result.items[0]
        assertEquals(FIRST_NAME, first.name)
        assertEquals(FIRST_DESCRIPTION, first.description)
        assertNotNull(first.previewApiKey)
        assertEquals(CMAType.Link, first.previewApiKey.system.type)
        assertNull(first.previewApiKey.system.linkType)
    }

    @test
    fun testFetchOne() {
        val result = assertTestCallback(client.apiKeys().async()
                .fetchOne(SPACE_ID, FIRST_ID, TestCallback()) as TestCallback)!!

        assertEquals(FIRST_NAME, result.name)
        assertEquals(FIRST_DESCRIPTION, result.description)

        assertNotNull(result.previewApiKey)
        assertEquals(CMAType.Link, result.previewApiKey.system.type)
        assertNull(result.previewApiKey.system.linkType)
    }

    @test
    fun testCreate() {
        val apiKey = CMAApiKey()
                .setName("E2E Test Key")
                .setDescription("Description of the test key.")
                .addEnvironment("master")
                .addEnvironment("staging")

        val result = assertTestCallback(client.apiKeys().async()
                .create(SPACE_ID, apiKey, TestCallback()) as TestCallback)!!

        assertEquals("E2E Test Key", result.name)
        assertEquals("Description of the test key.", result.description)
        assertNotNull(result.accessToken)
        assertTrue(result.accessToken.isNotBlank())

        assertEquals("master", result.environments.first().id)
        assertEquals("staging", result.environments.last().id)

        assertNotNull(result.previewApiKey)
        assertEquals(CMAType.Link, result.previewApiKey.system.type)
        assertNull(result.previewApiKey.system.linkType)
    }

    @test
    fun testQueryForAll() {
        val query = hashMapOf("skip" to "6")
        val result = assertTestCallback(client.apiKeys().async()
                .fetchAll(SPACE_ID, query, TestCallback()) as TestCallback)!!

        assertEquals(6, result.skip)
        assertEquals(0, result.items.size)
    }
}