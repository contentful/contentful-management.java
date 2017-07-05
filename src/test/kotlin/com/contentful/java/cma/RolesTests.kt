/*
 * Copyright (C) 2017 Contentful GmbH
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

import com.contentful.java.cma.lib.TestCallback
import com.contentful.java.cma.lib.TestUtils
import com.contentful.java.cma.model.CMAConstraint
import com.contentful.java.cma.model.CMAConstraint.Equals
import com.contentful.java.cma.model.CMAConstraint.FieldKeyPath
import com.contentful.java.cma.model.CMAPermissions
import com.contentful.java.cma.model.CMAPolicy
import com.contentful.java.cma.model.CMAPolicy.ALLOW
import com.contentful.java.cma.model.CMARole
import okhttp3.mockwebserver.MockResponse
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.junit.Test as test

class RolesTests : BaseTest() {
    @test fun testFetchOne() {
        val responseBody = TestUtils.fileToString("roles_get_one.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.roles().async()
                .fetchOne("SPACE_ID", "ROLE_ID", TestCallback()) as TestCallback)!!

        assertEquals("DELETE ME!!", result.name)
        assertEquals("Test role", result.description)

        assertNotNull(result.policies)
        assertEquals(9, result.policies.size)
        val policy = result.policies[0]
        assertEquals("allow", policy.effect)
        assertEquals(1, (policy.actions as List<String>).size)
        assertEquals("read", (policy.actions as List<String>)[0])

        assertNotNull(policy.constraint)
        assertNull(policy.constraint.or)
        assertNull(policy.constraint.equals)
        assertNull(policy.constraint.not)
        assertNotNull(policy.constraint.and)
        assertEquals("sys.type", policy.constraint.and[0].equals.path.doc)
        assertEquals("Entry", policy.constraint.and[0].equals.value)

        assertNotNull(result.permissions)
        assertNotNull(result.permissions.contentModel)
        assertEquals(1, (result.permissions.contentModel as List<String>).size)
        assertNotNull(result.permissions.settings)
        assertEquals(0, (result.permissions.settings as List<String>).size)
        assertNotNull(result.permissions.contentDelivery)
        assertNotNull("all", result.permissions.contentDelivery as String?)

        assertEquals("1sJzssG9PfxOKGVr2ePpXm", result.id)
        assertEquals("Role", result.system.type.name)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/SPACE_ID/roles/ROLE_ID", recordedRequest.path)
    }

    @test fun testFetchAll() {
        val responseBody = TestUtils.fileToString("roles_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.roles().async()
                .fetchAll("SPACE_ID", TestCallback()) as TestCallback)!!

        assertEquals(7, result.total)
        assertEquals(25, result.limit)
        assertEquals(0, result.skip)
        assertEquals(7, result.items.size)

        assertEquals("Editor", result.items[0].name)
        assertEquals(2, result.items[0].policies.size)

        assertEquals("DELETE ME!!", result.items[1].name)
        assertEquals(1, result.items[1].policies.size)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/SPACE_ID/roles/", recordedRequest.path)
    }

    @test fun testFetchAllWithQuery() {
        val responseBody = TestUtils.fileToString("roles_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        assertTestCallback(client!!.roles().async()
                .fetchAll("SPACE_ID",
                        hashMapOf("skip" to "3"),
                        TestCallback()) as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/SPACE_ID/roles?skip=3", recordedRequest.path)
    }

    @test fun testCreateNew() {
        val responseBody = TestUtils.fileToString("roles_create.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val role = CMARole()
                .setName("Manage Settings Role is actually all")
                .setDescription("Test role")
                .setPermissions(
                        CMAPermissions().setSettings("all")
                )

        val result = assertTestCallback(client!!.roles().async()
                .create("SPACE_ID", role, TestCallback()) as TestCallback)!!

        assertEquals("Manage Settings Role is actually all", result.name)
        assertEquals("Test role", result.description)
        assertEquals("0g3FcTwbFicqZuoaY288Qe", result.id)
        assertNotNull(result.permissions.settings)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces/SPACE_ID/roles/", recordedRequest.path)
    }

    @test fun testUpdate() {
        val responseBody = TestUtils.fileToString("roles_update.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        // DO NOT USE IN PRODUCTION: USE A FETCH FIRST!

        val role = CMARole()
                .setId("sampleId")
                .setSpaceId("SPACE_ID")
                .setVersion(3)
                .setName("DELETE ME!!")
                .setDescription("Test role")
                .addPolicy(
                        CMAPolicy()
                                .allow()
                                .read()
                                .create()
                                .update()
                                .delete()
                                .publish()
                                .unpublish()
                                .archive()
                                .unarchive()
                                .setConstraint(
                                        CMAConstraint()
                                                .setAnd(
                                                        CMAConstraint()
                                                                .setEquals(
                                                                        Equals()
                                                                                .setPath(FieldKeyPath()
                                                                                        .setDoc("fields.foo"))
                                                                                .setValue("something")
                                                                )
                                                )
                                )
                )
                .setPermissions(
                        CMAPermissions()
                                .setContentModel(arrayListOf("read")
                                )
                )

        val result = assertTestCallback(client!!.roles().async()
                .update(role, TestCallback()) as TestCallback)!!

        assertEquals("3h44ENEEpAA9XNx52dRDs0", result.id)
        assertEquals("DELETE ME!!", result.name)
        assertEquals("Test role", result.description)
        assertEquals(1, result.policies.size)
        assertEquals(ALLOW, result.policies[0].effect)
        val actions = result.policies[0].actions as List<String>
        assertTrue(actions.contains("read"))
        assertTrue(actions.contains("create"))
        assertTrue(actions.contains("update"))
        assertTrue(actions.contains("delete"))
        assertTrue(actions.contains("publish"))
        assertTrue(actions.contains("unpublish"))
        assertTrue(actions.contains("archive"))
        assertTrue(actions.contains("unarchive"))
        assertEquals("[read]", result.permissions.contentModel.toString())

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.method)
        assertEquals("/spaces/SPACE_ID/roles/sampleId", recordedRequest.path)
    }

    @test fun testDeleteOne() {
        val responseBody = TestUtils.fileToString("roles_delete.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        assertTestCallback(client!!.roles().async()
                .delete(
                        "SPACE_ID",
                        CMARole().setId("ROLE_ID"),
                        TestCallback()
                ) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("DELETE", recordedRequest.method)
        assertEquals("/spaces/SPACE_ID/roles/ROLE_ID", recordedRequest.path)
    }
}