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

import com.contentful.java.cma.lib.TestCallback
import com.contentful.java.cma.lib.TestUtils
import com.contentful.java.cma.model.CMAEditorInterface
import com.contentful.java.cma.model.CMAEditorInterface.Control
import com.contentful.java.cma.model.CMALink
import com.contentful.java.cma.model.CMASystem
import com.contentful.java.cma.model.CMAType
import okhttp3.mockwebserver.MockResponse
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.junit.Test as test

class EditorInterfacesTests : BaseTest() {

    @test
    fun testFetchOne() {
        val responseBody = TestUtils.fileToString("editor_interfaces_get.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.editorInterfaces().async()
                .fetchOne("spaceId", "master", "contentTypeId", TestCallback()) as TestCallback)!!

        assertEditorInterface(result)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/spaceId/environments/master/content_types/contentTypeId/editor_interface",
                recordedRequest.path)
    }

    @test
    fun testFetchOneWithConfiguredSpaceAndEnvironment() {
        val responseBody = TestUtils.fileToString("editor_interfaces_get.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        assertTestCallback(client!!.editorInterfaces().async()
                .fetchOne("contentTypeId", TestCallback()) as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/configuredSpaceId/environments/configuredEnvironmentId/content_types"
                + "/contentTypeId/editor_interface",
                recordedRequest.path)
    }

    @test
    fun testFetchOneFromEnvironment() {
        val responseBody = TestUtils.fileToString("editor_interfaces_get_from_environment.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.editorInterfaces().async()
                .fetchOne("spaceId", "staging", "contentTypeId", TestCallback()) as TestCallback)!!

        assertEquals("staging", result.environmentId)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/spaceId/environments/staging/content_types/contentTypeId/editor_interface",
                recordedRequest.path)
    }

    @test
    fun testUpdate() {
        val responseBody = TestUtils.fileToString("editor_interfaces_update.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        // ALWAYS DO AN UPDATE ON FETCHED entries. THIS IS JUST FOR TESTING!

        val payload = CMAEditorInterface()

        payload
                .setSystem<CMAEditorInterface>(
                        CMASystem()
                                .setContentType(
                                        CMALink(
                                                CMAType.ContentType
                                        )
                                                .setId("contentTypeId")
                                )
                )


        payload
                .setSpaceId<CMAEditorInterface>("spaceId")
                .setVersion<CMAEditorInterface>(1243)
                .addControl(
                        Control()
                                .setFieldId("name")
                                .setBuildInWidgetId(Control.BuildInWidgetId.SingleLine)
                )
                .addControl(
                        Control()
                                .setFieldId("media")
                                .setBuildInWidgetId(Control.BuildInWidgetId.AssetLinkEditor)
                )
                .addControl(
                        Control()
                                .setFieldId("link")
                                .setBuildInWidgetId(Control.BuildInWidgetId.EntryLinkEditor)
                )
                .addControl(
                        Control()
                                .setFieldId("fields")
                                .setBuildInWidgetId(Control.BuildInWidgetId.ListInput)
                )
                .addControl(
                        Control()
                                .setFieldId("sys")
                                .setBuildInWidgetId(Control.BuildInWidgetId.ListInput)
                )
                .addControl(
                        Control()
                                .setFieldId("date")
                                .setBuildInWidgetId(Control.BuildInWidgetId.DatePicker)
                                .addSetting("format", "timeZ")
                                .addSetting("ampm", "24")
                )
                .addControl(
                        Control()
                                .setFieldId("location")
                                .setBuildInWidgetId(Control.BuildInWidgetId.LocationEditor)
                )
                .addControl(
                        Control()
                                .setFieldId("slug")
                                .setBuildInWidgetId(Control.BuildInWidgetId.SingleLine)
                )
                .addControl(
                        Control()
                                .setFieldId("custom")
                                .setWidgetId("custom_ui_extension")
                                .addHelpText("Help!")
                )

        assertEditorInterface(payload)

        val result = assertTestCallback(client!!.editorInterfaces().async()
                .update(payload, TestCallback()) as TestCallback)!!

        assertEditorInterface(result)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.method)
        assertEquals("/spaces/spaceId/environments/master/content_types/contentTypeId/editor_interface",
                recordedRequest.path)
    }

    @test
    fun testUpdateFromEnvironments() {
        val responseBody = TestUtils.fileToString("editor_interfaces_update_in_environment.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val payload = CMAEditorInterface()
        payload
                .setEnvironmentId<CMAEditorInterface>("staging")
                .setSpaceId<CMAEditorInterface>("spaceId")
                .setVersion<CMAEditorInterface>(123)
                .system.contentType = CMALink(CMAType.ContentType).setId("contentTypeId")

        val result = assertTestCallback(client!!.editorInterfaces().async()
                .update(payload, TestCallback()) as TestCallback)!!

        assertEquals("staging", result.environmentId)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.method)
        assertEquals(
                "/spaces/spaceId/environments/staging/content_types/contentTypeId/editor_interface",
                recordedRequest.path)
    }

    private fun assertEditorInterface(result: CMAEditorInterface) {
        assertEquals(9, result.controls.size)

        var i = 0
        assertEquals("name", result.controls[i].fieldId)
        assertEquals(Control.BuildInWidgetId.SingleLine, result.controls[i].buildInWidgetId)
        assertNull(result.controls[i].settings)
        i++

        assertEquals("media", result.controls[i].fieldId)
        assertEquals(Control.BuildInWidgetId.AssetLinkEditor, result.controls[i].buildInWidgetId)
        assertNull(result.controls[i].settings)
        i++

        assertEquals("link", result.controls[i].fieldId)
        assertEquals(Control.BuildInWidgetId.EntryLinkEditor, result.controls[i].buildInWidgetId)
        assertNull(result.controls[i].settings)
        i++

        assertEquals("fields", result.controls[i].fieldId)
        assertEquals(Control.BuildInWidgetId.ListInput, result.controls[i].buildInWidgetId)
        assertNull(result.controls[i].settings)
        i++

        assertEquals("sys", result.controls[i].fieldId)
        assertEquals(Control.BuildInWidgetId.ListInput, result.controls[i].buildInWidgetId)
        assertNull(result.controls[i].settings)
        i++

        assertEquals("date", result.controls[i].fieldId)
        assertEquals(Control.BuildInWidgetId.DatePicker, result.controls[i].buildInWidgetId)
        assertNotNull(result.controls[i].settings)
        assertEquals("timeZ", result.controls[i].settings["format"])
        assertEquals("24", result.controls[i].settings["ampm"])
        i++

        assertEquals("location", result.controls[i].fieldId)
        assertEquals(Control.BuildInWidgetId.LocationEditor, result.controls[i].buildInWidgetId)
        assertNull(result.controls[i].settings)
        i++

        assertEquals("slug", result.controls[i].fieldId)
        assertEquals(Control.BuildInWidgetId.SingleLine, result.controls[i].buildInWidgetId)
        assertNull(result.controls[i].settings)
        i++

        assertEquals("custom", result.controls[i].fieldId)
        assertNull(result.controls[i].buildInWidgetId)
        assertEquals("custom_ui_extension", result.controls[i].widgetId)
        assertNotNull(result.controls[i].settings)
        assertEquals("Help!", result.controls[i].helpText)

        val resultString = result.toString()
        assertTrue(resultString.contains("CMAEditorInterface"))
        assertTrue(resultString.contains("Control"))
        assertTrue(resultString.contains("Help!"))
    }
}