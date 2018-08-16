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
import com.contentful.java.cma.model.CMARole
import org.junit.BeforeClass
import org.junit.Test
import kotlin.test.assertEquals

open class RolesE2E : Base() {
    companion object {
        @BeforeClass
        @JvmStatic
        fun setUpRolesSuite() {
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
    fun testRoles() {
        val roles = client.roles().fetchAll().items
        assertEquals(0, roles.size)

//        var role = client.roles().fetchOne(roles.first().id)
//        assertEquals("test role", role.name)
//        assertEquals("test description", role.description)
//
//        role.name = "test role 2"
//        role = client.roles().update(role)
//        assertEquals("test role 2", role.name)
//
//        val result = client.roles().delete(role)
//        assertEquals(204, result)

    }
}