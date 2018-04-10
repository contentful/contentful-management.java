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

import com.contentful.java.cma.lib.TestUtils
import com.contentful.java.cma.model.CMAEntry
import com.contentful.java.cma.model.CMALink
import com.contentful.java.cma.model.CMAType
import okhttp3.mockwebserver.MockResponse
import kotlin.test.assertTrue
import org.junit.Test as test

class LinkTests : BaseTest() {
    @test
    fun testCreateLink() {
        val requestBody = TestUtils.fileToString("asset_update_request.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(requestBody))
        val cmaLink = CMALink(CMAType.Asset).setId("linkedTargetId")

        val cmaEntry = CMAEntry()
        cmaEntry.localize("en-US").setField("link", cmaLink)

        client!!.entries().create("spaceId", "master", "contentTypeId", cmaEntry)

        val cmaRequest = server!!.takeRequest()

        val body = cmaRequest.body.readUtf8()
        assertTrue(body.contains("\"id\":\"linkedTargetId\""))
        assertTrue(body.contains("\"linkType\":\"Asset\""))
        assertTrue(body.contains("\"type\":\"Link\""))
    }
}