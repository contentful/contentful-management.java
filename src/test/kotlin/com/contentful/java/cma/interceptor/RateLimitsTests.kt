package com.contentful.java.cma.interceptor

import com.contentful.java.cma.model.RateLimits
import org.junit.Test
import kotlin.test.assertEquals

class RateLimitsTests {
    @Test fun testNoMatchWillResultInZerosEverywhere() {
        val headers = hashMapOf("foo" to listOf("123"))
        val limits = RateLimits.DefaultParser().parse(headers)

        assertEquals(0, limits.hourLimit)
        assertEquals(0, limits.hourRemaining)
        assertEquals(0, limits.secondLimit)
        assertEquals(0, limits.secondRemaining)
        assertEquals(0, limits.reset)
    }

    @Test fun testValidHeaders() {
        val headers = hashMapOf(
                "X-Contentful-RateLimit-Hour-Limit" to listOf("1"),
                "X-Contentful-RateLimit-Hour-Remaining" to listOf("2"),
                "X-Contentful-RateLimit-Second-Limit" to listOf("3"),
                "X-Contentful-RateLimit-Second-Remaining" to listOf("4"),
                "X-Contentful-RateLimit-Reset" to listOf("5")
        )

        val limits = RateLimits.DefaultParser().parse(headers)

        assertEquals(1, limits.hourLimit)
        assertEquals(2, limits.hourRemaining)
        assertEquals(3, limits.secondLimit)
        assertEquals(4, limits.secondRemaining)
        assertEquals(5, limits.reset)
    }

    @Test fun testInvalidTypeWillResultInMinusOne() {
        val headers = hashMapOf(
                "X-Contentful-RateLimit-Hour-Limit" to listOf("11da")
        )

        val limits = RateLimits.DefaultParser().parse(headers)

        assertEquals(-1, limits.hourLimit)
    }

}