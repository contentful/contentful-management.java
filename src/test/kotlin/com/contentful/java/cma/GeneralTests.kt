package com.contentful.java.cma

import com.contentful.java.cma.Constants.CMAFieldType
import com.contentful.java.cma.lib.TestUtils
import com.contentful.java.cma.model.CMAField
import com.google.gson.Gson
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import java.lang.reflect.InvocationTargetException
import java.util.logging.LogManager
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import org.junit.Test as test

class GeneralTests{
    var server: MockWebServer? = null
    var client: CMAClient? = null
    var gson: Gson? = null

    @Before
    fun setUp() {
        LogManager.getLogManager().reset()
        // MockWebServer
        server = MockWebServer()
        server!!.start()

        // Client
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
    fun testGsonInstanceRetained() {
        assertTrue(CMAClient.createGson() === CMAClient.createGson())
    }

    @test
    fun testFieldSerialization() {
        val field = gson!!.fromJson(
                TestUtils.fileToString("field_object.json"),
                CMAField::class.java)

        assertTrue(field.isRequired)
        assertTrue(field.isDisabled)
        assertTrue(field.isOmitted)

        // True
        var json = gson!!.toJsonTree(field, CMAField::class.java).asJsonObject
        assertTrue(json.has("required"))
        assertTrue(json.has("disabled"))
        assertTrue(json.has("omitted"))

        // False
        field.isRequired = false
        field.isDisabled = false
        field.isOmitted = false

        // General attributes
        json = gson!!.toJsonTree(field, CMAField::class.java).asJsonObject
        assertEquals("fieldname", json.get("name").asString)
        assertEquals("fieldid", json.get("id").asString)
        assertEquals("Text", json.get("type").asString)
    }

    @test
    fun testFieldArraySerialization() {
        val field = CMAField().setType(CMAFieldType.Array)
                .setArrayItems(mapOf(Pair("type", CMAFieldType.Symbol.toString())))

        val json = gson!!.toJsonTree(field, CMAField::class.java).asJsonObject
        assertEquals("Array", json.get("type").asString)
        val items = json.get("items").asJsonObject
        assertEquals("Symbol", items.get("type").asString)
    }

    @test
    fun testConstantsThrowsUnsupportedException() {
        assertPrivateConstructor(Constants::class.java)
    }

    @test
    fun testRxExtensionsThrowsUnsupportedException() {
        assertPrivateConstructor(RxExtensions::class.java)
    }

    fun assertPrivateConstructor(clazz: Class<out Any>) {
        val constructor = clazz.getDeclaredConstructor()
        constructor.isAccessible = true
        val exception = try {
            constructor.newInstance()
        } catch (e: Exception) {
            e
        }

        assertNotNull(exception)
        assertTrue(exception is InvocationTargetException)
        assertTrue((exception as InvocationTargetException).cause is
                UnsupportedOperationException)
    }
}
