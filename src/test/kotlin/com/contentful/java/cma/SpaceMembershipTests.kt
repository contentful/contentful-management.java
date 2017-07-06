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
import com.contentful.java.cma.model.CMALink
import com.contentful.java.cma.model.CMASpaceMembership
import okhttp3.mockwebserver.MockResponse
import kotlin.test.assertEquals
import org.junit.Test as test

class SpaceMembershipTests : BaseTest() {
    @test fun testFetchAll() {
        val responseBody = TestUtils.fileToString("space_memberships_fetch_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.spaceMemberships().async()
                .fetchAll("SPACE_ID", TestCallback()) as TestCallback)!!

        assertEquals(2, result.items.size)
        assertEquals(true, result.items[0].isAdmin)
        assertEquals("7uJNojWP0gbuP7Pplz7Syo", result.items[0].user.id)
        assertEquals(0, result.items[0].roles.size)

        assertEquals(false, result.items[1].isAdmin)
        assertEquals("0PpV3cQM3fI5WKAZvFkTWu", result.items[1].user.id)
        assertEquals(1, result.items[1].roles.size)
        assertEquals("7mdjKnRonMN0e2lLVooOjT", result.items[1].roles[0].id)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/SPACE_ID/space_memberships", recordedRequest.path)
    }

    @test fun testFetchAllWithQuery() {
        val responseBody = TestUtils.fileToString("space_memberships_fetch_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        assertTestCallback(client!!.spaceMemberships().async()
                .fetchAll(
                        "SPACE_ID",
                        hashMapOf("limit" to "foo"),
                        TestCallback()) as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/SPACE_ID/space_memberships?limit=foo", recordedRequest.path)
    }

    @test fun testFetchOne() {
        val responseBody = TestUtils.fileToString("space_memberships_fetch_one.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.spaceMemberships().async()
                .fetchOne("SPACE_ID", "MEMBERSHIP_ID", TestCallback()) as TestCallback)!!

        assertEquals("MEMBERSHIP_ID", result.id)
        assertEquals("SpaceMembership", result.system.type.name)
        assertEquals(true, result.isAdmin)
        assertEquals("7uJNojWP0gbuP7Pplz7Syo", result.user.id)
        assertEquals(0, result.roles.size)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/SPACE_ID/space_memberships/MEMBERSHIP_ID", recordedRequest.path)
    }

    @test fun testCreateNew() {
        val responseBody = TestUtils.fileToString("space_memberships_create.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val membership = CMASpaceMembership()
                .setEmail("mario@contentful.com")
                .setIsAdmin(true)

        val result = assertTestCallback(client!!.spaceMemberships().async()
                .create("SPACE_ID", membership, TestCallback()) as TestCallback)!!

        assertEquals(true, result.isAdmin)
        assertEquals("7CgvT6bSNvpojgcdETZvL0", result.id)
        assertEquals("7CgWzjaOaefGbPBv5EfTBI", result.user.id)
        assertEquals(0, result.roles.size)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces/SPACE_ID/space_memberships", recordedRequest.path)
    }

    @test fun testUpdate() {
        val responseBody = TestUtils.fileToString("space_memberships_update.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        // DO NOT USE THIS IN PRODUCTION: FETCH TO UPDATE FIRST!

        val membership = CMASpaceMembership()
                .setSpaceId("SPACE_ID")
                .setEmail("👸@contentful.com")
                .setIsAdmin(false)
                .setVersion(123)
                .setId("sampleid")

        val result = assertTestCallback(client!!.spaceMemberships().async()
                .update(membership, TestCallback()) as TestCallback)!!

        assertEquals(false, result.isAdmin)
        assertEquals("sampleid", result.id)
        assertEquals("0PpV3cQM3fI5WKAZvFkTWu", result.user.id)
        assertEquals(0, result.roles.size)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.method)
        assertEquals("/spaces/SPACE_ID/space_memberships/sampleid", recordedRequest.path)
    }

    @test(expected = IllegalArgumentException::class)
    fun testUpdateThrowsOnNullEmail() {
        CMASpaceMembership().email = null
    }

    @test(expected = IllegalArgumentException::class)
    fun testUpdateThrowsOnEmailNotContainingAtSymbol() {
        CMASpaceMembership().email = "NoOneAtSomewhere"
    }

    @test(expected = IllegalArgumentException::class)
    fun testThrowIfNotAdminAndNoRole() {
        val responseBody = TestUtils.fileToString("space_memberships_update.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val membership = CMASpaceMembership()
                .setSpaceId("SPACE_ID")
                .setEmail("luigi")
                .setIsAdmin(false)

        assertTestCallback(client!!.spaceMemberships().async()
                .update(membership, TestCallback()) as TestCallback)!!
    }

    @test(expected = IllegalArgumentException::class)
    fun testThrowIfRoleIsNull() {
        CMASpaceMembership().addRole(null)
    }

    @test
    fun testAddRoles() {
        val membership = CMASpaceMembership()
                .addRole(CMALink())
                .addRole(CMALink())
                .addRole(CMALink())
                .addRole(CMALink())

        assertEquals(4, membership.roles.size)
    }

    @test fun testIfNotAdminNeedsRoles() {
        val responseBody = TestUtils.fileToString("space_memberships_no_admin_needs_roles.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val membership = CMASpaceMembership()
                .setId("sampleid")
                .setSpaceId("SPACE_ID")
                .setEmail("luigi@contentful.com")
                .setIsAdmin(false)
                .setVersion(3)
                .setRoles(CMALink())

        val result = assertTestCallback(client!!.spaceMemberships().async()
                .update(membership, TestCallback()) as TestCallback)!!

        assertEquals(false, result.isAdmin)
        assertEquals("sampleid", result.id)
        assertEquals("0PpV3cQM3fI5WKAZvFkTWu", result.user.id)
        assertEquals(1, result.roles.size)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.method)
        assertEquals("/spaces/SPACE_ID/space_memberships/sampleid", recordedRequest.path)
    }

    @test fun testDeleteOne() {
        val responseBody = TestUtils.fileToString("space_memberships_delete_one.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        assertTestCallback(client!!.spaceMemberships().async()
                .delete(
                        "SPACE_ID",
                        CMASpaceMembership().setId("MEMBERSHIP_ID"),
                        TestCallback()
                ) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("DELETE", recordedRequest.method)
        assertEquals("/spaces/SPACE_ID/space_memberships/MEMBERSHIP_ID", recordedRequest.path)
    }

}