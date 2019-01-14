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

import com.contentful.java.cma.CMAClient
import com.contentful.java.cma.model.CMASpaceMembership
import org.junit.BeforeClass
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

open class SpaceMembershipsE2E : Base() {
    companion object {
        @BeforeClass
        @JvmStatic
        fun setUpSpaceMembershipSuite() {
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

            client
                    .spaceMemberships()
                    .fetchAll()
                    .items
                    .forEach {
                        if (it.id != "2tb1QWsh8J49B1enRpfVYe"
                                && it.id != "5DJ1zhqoIGiTmIUxkb8ITU") {
                            assertEquals(204, client.spaceMemberships().delete(it))
                        }
                    }
        }
    }

    @Test
    fun testCreateSpaceMembership() {
        var spaceMembership = CMASpaceMembership().apply {
            email = "name@example.com"
            isAdmin = true
        }

        spaceMembership = client.spaceMemberships().create(spaceMembership)
        assertTrue(spaceMembership.isAdmin)
        assertNull(spaceMembership.email) // security: Do not show email

        spaceMembership = client.spaceMemberships().fetchOne(spaceMembership.id)
        assertTrue(spaceMembership.isAdmin)
        assertNull(spaceMembership.email) // security: Do not show email

        assertEquals(204, client.spaceMemberships().delete(spaceMembership))
    }
}