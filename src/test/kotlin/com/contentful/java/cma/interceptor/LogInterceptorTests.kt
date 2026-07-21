package com.contentful.java.cma.interceptor

import com.contentful.java.cma.Logger
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LogInterceptorTests {
    private lateinit var server: MockWebServer

    @Before fun setUp() {
        server = MockWebServer()
        server.start()
    }

    @After fun tearDown() {
        server.shutdown()
    }

    @Test fun testAuthorizationHeaderIsRedacted() {
        val token = "super-secret-token"
        val logs = mutableListOf<String>()
        val logger = Logger { message -> logs.add(message) }

        val client = OkHttpClient.Builder()
                .addInterceptor(LogInterceptor(logger))
                .build()

        server.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        val request = Request.Builder()
                .url(server.url("/"))
                .header(AuthorizationHeaderInterceptor.HEADER_NAME, "Bearer $token")
                .build()

        client.newCall(request).execute().use { it.body?.string() }

        val requestLog = logs.first { it.startsWith("Sending request") }

        assertFalse(requestLog.contains(token), "Raw token must not appear in logs")
        assertTrue(requestLog.contains("Bearer [REDACTED]"), "Redacted marker expected")
    }

    @Test fun testOtherHeadersAreStillLogged() {
        val logs = mutableListOf<String>()
        val logger = Logger { message -> logs.add(message) }

        val client = OkHttpClient.Builder()
                .addInterceptor(LogInterceptor(logger))
                .build()

        server.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        val request = Request.Builder()
                .url(server.url("/"))
                .header(AuthorizationHeaderInterceptor.HEADER_NAME, "Bearer secret")
                .header("X-Custom-Header", "custom-value")
                .build()

        client.newCall(request).execute().use { it.body?.string() }

        val requestLog = logs.first { it.startsWith("Sending request") }

        assertTrue(requestLog.contains("X-Custom-Header: custom-value"),
                "Non-authorization headers must still be logged")
    }
}
