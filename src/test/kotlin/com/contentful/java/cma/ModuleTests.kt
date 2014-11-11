package com.contentful.java.cma

import org.junit.Test as test
import org.junit.Before as before
import kotlin.test.assertEquals

/**
 * Created by tomxor on 10/11/14.
 */
class ModuleTests : BaseTest() {
    var module: AbsModule<Any>? = null

    before fun setup() {
        super<BaseTest>.setUp()
        module = object : AbsModule<Any>(null) {}
    }

    test(expected = javaClass<IllegalArgumentException>())
    fun testNotNull() {
        try {
            module!!.assertNotNull(null, "parameter")
        } catch (e: IllegalArgumentException) {
            assertEquals("parameter may not be null.", e.getMessage())
            throw e
        }
    }

    test(expected = javaClass<IllegalArgumentException>())
    fun testResourceId() {
        try {
            module!!.getResourceIdOrThrow(CMAResource(), "parameter")
        } catch (e: IllegalArgumentException) {
            assertEquals("parameter.setId() was not called.", e.getMessage())
            throw e
        }
    }

    test(expected = javaClass<IllegalArgumentException>())
    fun testSpaceId() {
        try {
            module!!.getSpaceIdOrThrow(CMAResource(), "parameter")
        } catch (e: IllegalArgumentException) {
            assertEquals("parameter must have a space associated.", e.getMessage())
            throw e
        }
    }
}