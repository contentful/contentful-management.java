package com.contentful.java.cma

import org.junit.Test as test
import org.mockito.Mockito
import java.io.IOException
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import java.lang.reflect.InvocationTargetException
import com.contentful.java.cma.lib.TestUtils
import com.contentful.java.cma.model.CMAContentType
import kotlin.test.assertFalse
import com.contentful.java.cma.model.CMAField
import kotlin.test.assertEquals
import com.contentful.java.cma.Constants.CMAFieldType

/**
 * General Tests.
 */
class GeneralTests : BaseTest() {
    test fun testFieldSerialization() {
        val field = gson!!.fromJson(
                TestUtils.fileToString("field_object.json"),
                javaClass<CMAField>())

        assertTrue(field.isRequired())
        assertTrue(field.isDisabled())

        // True
        var json = gson!!.toJsonTree(field, javaClass<CMAField>()).getAsJsonObject()
        assertTrue(json.has("required"))
        assertTrue(json.has("disabled"))

        // False
        field.setRequired(false)
        field.setDisabled(false)

        // General attributes
        json = gson!!.toJsonTree(field, javaClass<CMAField>()).getAsJsonObject()
        assertEquals("fieldname", json.get("name").getAsString())
        assertEquals("fieldid", json.get("id").getAsString())
        assertEquals("Text", json.get("type").getAsString())
    }

    test fun testFieldArraySerialization() {
        val field = CMAField().setType(CMAFieldType.Array)
                .setArrayItems(hashMapOf(Pair("type", CMAFieldType.Symbol.toString())))

        val json = gson!!.toJsonTree(field, javaClass<CMAField>()).getAsJsonObject()
        assertEquals("Array", json.get("type").getAsString())
        val items = json.get("items").getAsJsonObject()
        assertEquals("Symbol", items.get("type").getAsString())
    }

    test fun testConstantsThrowsUnsupportedException() {
        assertPrivateConstructor(javaClass<Constants>())
    }

    test fun testRxExtensionsThrowsUnsupportedException() {
        assertPrivateConstructor(javaClass<RxExtensions>())
    }

    fun assertPrivateConstructor(clazz: Class<out Any>) {
        var ctor = clazz.getDeclaredConstructor()
        ctor.setAccessible(true)
        var exception = try {
            ctor.newInstance()
        } catch(e: Exception) {
            e
        }

        assertNotNull(exception)
        assertTrue(exception is InvocationTargetException)
        assertTrue((exception as InvocationTargetException).getCause() is
                UnsupportedOperationException)
    }
}
