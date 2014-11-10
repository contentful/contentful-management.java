package com.contentful.java.cma

import org.junit.Test as test
import kotlin.test.assertEquals

/**
 * Client Tests.
 */
class ClientTests : BaseTest() {
    test(expected = javaClass<IllegalArgumentException>())
    fun failsNoAccessToken() {
        try {
            CMAClient.Builder().build()
        } catch (e: IllegalArgumentException) {
            assertEquals("No access token was set.", e.getMessage())
            throw e
        }
    }

    test(expected = javaClass<IllegalArgumentException>())
    fun failsSetNullAccessToken() {
        try {
            CMAClient.Builder().setAccessToken(null)
        } catch (e: IllegalArgumentException) {
            assertEquals("Cannot call setAccessToken() with null value.", e.getMessage())
            throw e
        }
    }

    test(expected = javaClass<IllegalArgumentException>())
    fun failsSetNullClient() {
        try {
            CMAClient.Builder().setClient(null)
        } catch (e: IllegalArgumentException) {
            assertEquals("Cannot call setClient() with null value.", e.getMessage())
            throw e
        }
    }
}