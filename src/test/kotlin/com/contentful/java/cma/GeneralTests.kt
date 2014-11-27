package com.contentful.java.cma

import org.junit.Test as test
import org.mockito.Mockito
import java.io.IOException
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import java.lang.reflect.InvocationTargetException

/**
 * General Tests.
 */
class GeneralTests : BaseTest() {
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
