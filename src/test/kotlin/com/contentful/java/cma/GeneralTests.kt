package com.contentful.java.cma

import com.contentful.java.cma.Constants.CMAFieldType
import com.contentful.java.cma.lib.TestUtils
import com.contentful.java.cma.model.CMAField
import java.lang.reflect.InvocationTargetException
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import org.junit.Test as test

class GeneralTests : BaseTest() {
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
                .setArrayItems(hashMapOf(Pair("type", CMAFieldType.Symbol.toString())))

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
