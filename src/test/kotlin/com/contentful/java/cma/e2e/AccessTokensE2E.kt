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

import com.contentful.java.cma.CMAClient
import com.contentful.java.cma.model.CMAPersonalAccessToken
import org.junit.BeforeClass
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

open class AccessTokensE2E : Base() {
    companion object {
        @BeforeClass
        @JvmStatic
        fun setUpTokenSuite() {
            client = CMAClient.Builder().apply {
                if (!(PROXY.isNullOrEmpty())) {
                    setCoreEndpoint(PROXY)
                    setUploadEndpoint(PROXY)
                }
                setAccessToken(ACCESS_TOKEN)
                setSpaceId(SPACE_ID)
                setCoreCallFactory(createCallFactory())
                setUploadCallFactory(createCallFactory())
            }.build()
        }
    }

    @Test
    fun testToken() {
        var token = CMAPersonalAccessToken().apply {
            name = "test token"
            addScope(CMAPersonalAccessToken.Scope.Read)
        }

        token = client.personalAccessTokens().create(token)
        assertEquals("test token", token.name)

        token = client.personalAccessTokens().fetchOne(token.id)
        assertEquals("test token", token.name)

        val result = client.personalAccessTokens().revoke(token)
        assertNotNull(result.revokedAt)

        val personalAccessTokens = client.personalAccessTokens().fetchAll().items
        assertEquals(1, personalAccessTokens.size)
    }
}