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

package com.contentful.java.cma

import com.contentful.java.cma.Constants.CMAFieldType.Symbol
import com.contentful.java.cma.Constants.CMAFieldType.Text
import com.contentful.java.cma.lib.TestCallback
import com.contentful.java.cma.lib.TestUtils
import com.contentful.java.cma.model.CMAUiExtension
import com.contentful.java.cma.model.CMAUiExtensionParameter
import com.contentful.java.cma.model.CMAUiExtensionParameterType
import com.contentful.java.cma.model.CMAUiExtensionParameters
import com.google.gson.Gson
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import java.util.logging.LogManager
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.junit.Test as test

class UiExtensionsTests {
    var server: MockWebServer? = null
    var client: CMAClient? = null
    var gson: Gson? = null

    @Before
    fun setUp() {
        LogManager.getLogManager().reset()
        // MockWebServer
        server = MockWebServer()
        server!!.start()

        // overwrite client to not use environments
        client = CMAClient.Builder()
                .setAccessToken("token")
                .setCoreEndpoint(server!!.url("/").toString())
                .setUploadEndpoint(server!!.url("/").toString())
                .setSpaceId("configuredSpaceId")
                .setEnvironmentId("configuredEnvironmentId")
                .build()

        gson = CMAClient.createGson()
    }

    @After
    fun tearDown() {
        server!!.shutdown()
    }

    @test
    fun testFetchAll() {
        val responseBody = TestUtils.fileToString("ui_extensions_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.uiExtensions().async()
                .fetchAll("spaceId", "environmentId", TestCallback()) as TestCallback)!!

        assertEquals(6, result.total)

        val third = result.items[3].extension
        assertNull(third.sourceContent)
        assertEquals("http://localhost:3000/", third.sourceUrl)
        assertEquals("Hello World Editor", third.name)
        assertEquals(1, third.fieldTypes.size)
        assertEquals(Text, third.fieldTypes[0].type)
        assertEquals(false, third.isOnSidebar)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/spaceId/environments/environmentId/extensions",
                recordedRequest.path)
    }

    @test
    fun testFetchAllWithConfiguredSpaceAndEnvironment() {
        val responseBody = TestUtils.fileToString("ui_extensions_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        assertTestCallback(client!!.uiExtensions().async().fetchAll(TestCallback())
                as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/configuredSpaceId/environments/configuredEnvironmentId/extensions",
                recordedRequest.path)
    }

    @test
    fun testFetchAllWithQuery() {
        val responseBody = TestUtils.fileToString("ui_extensions_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        assertTestCallback(client!!.uiExtensions().async()
                .fetchAll(
                        "spaceId",
                        "environmentId",
                        hashMapOf("skip" to "3"),
                        TestCallback()) as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/spaceId/environments/environmentId/extensions?skip=3",
                recordedRequest.path)
    }

    @test
    fun testFetchAllWithQueryWithConfiguredSpaceAndEnvironment() {
        val responseBody = TestUtils.fileToString("ui_extensions_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        assertTestCallback(client!!.uiExtensions().async()
                .fetchAll(mapOf("skip" to "3"), TestCallback()) as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/configuredSpaceId/environments/configuredEnvironmentId/extensions?skip=3",
                recordedRequest.path)
    }

    @test
    fun testFetchOne() {
        val responseBody = TestUtils.fileToString("ui_extensions_get_one.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val intermediate = assertTestCallback(client!!.uiExtensions().async()
                .fetchOne("spaceId", "environmentId", "extensionId", TestCallback()) as TestCallback)!!

        val result = intermediate.extension

        assertNull(result.sourceUrl)
        assertNotNull(result.sourceContent)
        assertEquals(2, result.fieldTypes.size)
        assertEquals(Symbol, result.fieldTypes[0].type)
        assertEquals(Text, result.fieldTypes[1].type)
        assertEquals(false, result.isOnSidebar)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/spaceId/environments/environmentId/extensions/extensionId",
                recordedRequest.path)
    }

    @test
    fun testFetchOneWithConfiguredSpaceAndEnvironment() {
        val responseBody = TestUtils.fileToString("ui_extensions_get_one.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        assertTestCallback(client!!.uiExtensions().async().fetchOne("extensionId", TestCallback())
                as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/configuredSpaceId/environments/configuredEnvironmentId/extensions/extensionId",
                recordedRequest.path)
    }

    @test
    fun testCreateWithSourceContent() {
        val responseBody = TestUtils.fileToString("ui_extensions_create_with_html_data.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val uiExtension = CMAUiExtension()
        uiExtension
                .extension
                .setSourceContent("<html>")
                .setName("My awesome extensions by srcDoc")
                .addFieldType(Symbol)
                .addFieldType(Text)
                .setIsOnSidebar(false)

        val intermediate = assertTestCallback(client!!.uiExtensions().async()
                .create("spaceId", "environmentId", uiExtension, TestCallback()) as TestCallback)!!
        val result = intermediate.extension

        assertNotNull(result.sourceContent)
        assertNull(result.sourceUrl)
        assertEquals("My awesome extension by srcDoc", result.name)
        assertEquals(2, result.fieldTypes.size)
        assertEquals(Symbol, result.fieldTypes[0].type)
        assertEquals(Text, result.fieldTypes[1].type)
        assertEquals(false, result.isOnSidebar)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces/spaceId/environments/environmentId/extensions",
                recordedRequest.path)
    }

    @test
    fun testCreateWithSourceContentWithConfiguredSpaceAndEnvironment() {
        val responseBody = TestUtils.fileToString("ui_extensions_create_with_html_data.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val uiExtension = CMAUiExtension()
        uiExtension
                .extension
                .setSourceContent("<html>")
                .setName("My awesome extensions by srcDoc")
                .addFieldType(Symbol)
                .addFieldType(Text)
                .setIsOnSidebar(false)

        assertTestCallback(client!!.uiExtensions().async().create(uiExtension, TestCallback())
                as TestCallback)!!

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces/configuredSpaceId/environments/configuredEnvironmentId/extensions", recordedRequest.path)
    }

    @test
    fun testCreateWithSourceLink() {
        val responseBody = TestUtils.fileToString("ui_extensions_create_with_url.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val uiExtension = CMAUiExtension()
        uiExtension
                .extension
                .setSourceUrl("https://example.com/my")
                .setName("My awesome extensions")
                .addFieldType(Symbol)
                .addFieldType(Text)
                .setIsOnSidebar(false)

        val intermediate = assertTestCallback(client!!.uiExtensions().async()
                .create("spaceId", "environmentId", uiExtension, TestCallback()) as TestCallback)!!

        val result = intermediate.extension

        assertNull(result.sourceContent)
        assertEquals("https://example.com/my", result.sourceUrl)
        assertEquals("My awesome extension", result.name)
        assertEquals(2, result.fieldTypes.size)
        assertEquals(Symbol, result.fieldTypes[0].type)
        assertEquals(Text, result.fieldTypes[1].type)
        assertEquals(false, result.isOnSidebar)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces/spaceId/environments/environmentId/extensions",
                recordedRequest.path)
    }

    @test
    fun testCreateWithId() {
        val responseBody = TestUtils.fileToString("ui_extensions_create_with_url.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val uiExtension = CMAUiExtension()
        uiExtension
                .setId("newId")
                .extension
                .setSourceUrl("https://example.com/my")
                .setName("My awesome extensions")
                .addFieldType(Symbol)
                .addFieldType(Text)
                .setIsOnSidebar(false)

        assertEquals("newId", uiExtension.id)

        val intermediate = assertTestCallback(client!!.uiExtensions().async()
                .create("spaceId", "environmentId", uiExtension, TestCallback()) as TestCallback)!!

        assertEquals("newId", intermediate.id)

        val result = intermediate.extension

        assertNull(result.sourceContent)
        assertEquals("https://example.com/my", result.sourceUrl)
        assertEquals("My awesome extension", result.name)
        assertEquals(2, result.fieldTypes.size)
        assertEquals(Symbol, result.fieldTypes[0].type)
        assertEquals(Text, result.fieldTypes[1].type)
        assertEquals(false, result.isOnSidebar)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.method)
        assertEquals("/spaces/spaceId/environments/environmentId/extensions/newId",
                recordedRequest.path)
    }

    @test(expected = IllegalArgumentException::class)
    fun testCreateWithHtmlSourceContentToBig() {
        val toLongSource = String(ByteArray(200 * 1024, Int::toByte))

        CMAUiExtension()
                .extension
                .setSourceContent(toLongSource)
                .setName("My awesome extensions by srcDoc")
                .addFieldType(Symbol)
                .addFieldType(Text)
                .setIsOnSidebar(false)
    }

    @test
    fun testCreateWithInstallationParameters() {
        val responseBody = TestUtils.fileToString("ui_extensions_create_with_parameters.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val parameters = CMAUiExtensionParameters()

        val installationParameter0 = CMAUiExtensionParameter()
                .setId("devMode")
                .setType(CMAUiExtensionParameterType.Boolean)
                .setName("Run in development mode")
                .setDescription("Help description")
        parameters.addInstallationParameters(installationParameter0)

        assertEquals("devMode", installationParameter0.id)
        assertEquals(CMAUiExtensionParameterType.Boolean, installationParameter0.type)
        assertEquals("Run in development mode", installationParameter0.name)
        assertEquals("Help description", installationParameter0.description)

        val installationParameter1 = CMAUiExtensionParameter()
                .setId("retries")
                .setType(CMAUiExtensionParameterType.Number)
                .setName("Number of retries for API calls")
                .setRequired(true).setDefaultValue("3")
                .addOption("one")
                .addOption("one")
        parameters.addInstallationParameters(installationParameter1)

        assertEquals("retries", installationParameter1.id)
        assertEquals(CMAUiExtensionParameterType.Number, installationParameter1.type)
        assertEquals("Number of retries for API calls", installationParameter1.name)
        assertEquals("one", installationParameter1.getOptions().get(0));
        assertEquals("one", installationParameter1.getOptions().get(1));

        val uiExtension = CMAUiExtension()
        uiExtension
                .extension
                .setSourceUrl("https://example.com/my")
                .setName("My awesome extensions")
                .addFieldType(Symbol)
                .addFieldType(Text)
                .setIsOnSidebar(false)
                .setParameters(parameters)

        val intermediate = assertTestCallback(client!!.uiExtensions().async()
                .create("spaceId", "environmentId", uiExtension, TestCallback()) as TestCallback)!!

        val result = intermediate.extension.parameters.getInstallation()

        assertEquals(2, result.size)
        assertEquals("devMode", result[0].id);
        assertEquals(CMAUiExtensionParameterType.Boolean, result[0].type);
        assertEquals("Run in development mode", result[0].name);
        assertEquals("Help description", result[0].description);
        assertEquals("retries", result[1].id);
        assertEquals(CMAUiExtensionParameterType.Enum, result[1].type);
        assertEquals("Number of retries for API calls", result[1].name);
        assertEquals("one", result[1].getOptions()[0]);
        assertEquals("one", result[1].getOptions()[1]);

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces/spaceId/environments/environmentId/extensions",
                recordedRequest.path)
    }

    @test
    fun testCreateWithInstanceParameters() {
        val responseBody = TestUtils.fileToString("ui_extensions_create_with_parameters.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val parameters = CMAUiExtensionParameters()

        val instanceParameter0 = CMAUiExtensionParameter()
                .setId("helpText")
                .setType(CMAUiExtensionParameterType.Symbol)
                .setName("Help text")
                .setDescription("Help text for a user to help them understand the editor")
        parameters.addInstanceParameters(instanceParameter0)

        assertEquals("helpText", instanceParameter0.id)
        assertEquals(CMAUiExtensionParameterType.Symbol, instanceParameter0.type)
        assertEquals("Help text", instanceParameter0.name)
        assertEquals("Help text for a user to help them understand the editor", instanceParameter0.description)

        val instanceParameter1 = CMAUiExtensionParameter()
                .setName("Theme")
                .setType(CMAUiExtensionParameterType.Enum)
                .setDefaultValue("light")
                .setRequired(true)
                .addLabel("empty", "Choose a value")
                .addLabel("value", "Other")
                .addOption("one")
                .addOption("one")
        parameters.addInstanceParameters(instanceParameter1)

        assertEquals("Theme", instanceParameter1.name)
        assertEquals(CMAUiExtensionParameterType.Enum, instanceParameter1.type)
        assertEquals("Choose a value", instanceParameter1.getLabels().get(0).get("empty"))
        assertEquals("one", instanceParameter1.getOptions().get(0));
        assertEquals("one", instanceParameter1.getOptions().get(1));

        val uiExtension = CMAUiExtension()
        uiExtension
                .extension
                .setSourceUrl("https://example.com/my")
                .setName("My awesome extensions")
                .addFieldType(Symbol)
                .addFieldType(Text)
                .setIsOnSidebar(false)
                .setParameters(parameters)

        val intermediate = assertTestCallback(client!!.uiExtensions().async()
                .create("spaceId", "environmentId", uiExtension, TestCallback()) as TestCallback)!!

        val result = intermediate.extension.parameters.getInstance()


        assertEquals(2, result.size)
        assertEquals("Theme", result[1].name);
        assertEquals(true, result[1].isRequired());
        assertEquals("light", result[1].getDefaultValue());
        assertEquals("Choose a value", result[1].getLabels()[0]["empty"])
        assertEquals(hashMapOf("one" to "one"), result[1].getOptions()[0]);
        assertEquals(hashMapOf("two" to "two"), result[1].getOptions()[1]);

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces/spaceId/environments/environmentId/extensions",
                recordedRequest.path)
    }

    @test
    fun testUpdate() {
        val responseBody = TestUtils.fileToString("ui_extensions_update.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        // usually you would fetch this from Contentful

        val old = CMAUiExtension()
        old
                .setId("extensionId")
                .setVersion<CMAUiExtension>(1213)
                .setSpaceId<CMAUiExtension>("spaceId")
                .setEnvironmentId<CMAUiExtension>("environmentId")
                .extension
                .setSourceUrl("http://example.com/my")
                .setName("My awesome extension by srcUrl")
                .addFieldType(Symbol)
                .addFieldType(Text)

        val intermediate = assertTestCallback(client!!.uiExtensions().async()
                .update(old, TestCallback()) as TestCallback)!!

        val updated = intermediate.extension

        assertEquals("http://example.com/my", updated.sourceUrl)
        assertEquals("My awesome extension by srcUrl", updated.name)
        assertEquals(2, updated.fieldTypes.size)
        assertEquals(Symbol, updated.fieldTypes[0].type)
        assertEquals(Text, updated.fieldTypes[1].type)
        assertEquals(false, updated.isOnSidebar)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.method)
        assertEquals("/spaces/spaceId/environments/environmentId/extensions/extensionId",
                recordedRequest.path)
    }

    @test
    fun testDelete() {
        server!!.enqueue(MockResponse().setResponseCode(204))

        // usually you would fetch this from Contentful

        val old = CMAUiExtension()
        old
                .setId("extensionId")
                .setVersion<CMAUiExtension>(1)
                .setSpaceId<CMAUiExtension>("spaceId")
                .setEnvironmentId<CMAUiExtension>("environmentId")
                .extension
                .setSourceUrl("http://example.com/my")
                .setName("My awesome extension")
                .addFieldType(Symbol)
                .addFieldType(Text)


        val result = assertTestCallback(client!!.uiExtensions().async()
                .delete(old, TestCallback()) as TestCallback)!!

        assertEquals(204, result)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("DELETE", recordedRequest.method)
        assertEquals("/spaces/spaceId/environments/environmentId/extensions/extensionId",
                recordedRequest.path)
    }

    @test
    fun testToStringWithParameters() {
        val parameters = CMAUiExtensionParameters()

        val instanceParameter0 = CMAUiExtensionParameter()
                .setId("helpText")
                .setType(CMAUiExtensionParameterType.Symbol)
                .setName("Help text")
        parameters.addInstanceParameters(instanceParameter0)

        assertTrue(parameters.toString().contains("id = helpText"))
        assertTrue(instanceParameter0.toString().contains("id = helpText"))
    }
}
