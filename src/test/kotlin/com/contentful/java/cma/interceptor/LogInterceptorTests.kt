package com.contentful.java.cma.interceptor

import com.contentful.java.cma.Logger
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class LogInterceptorTests {
    private lateinit var server: MockWebServer

    @Before fun setUp() {
        server = MockWebServer()
        server.start()
    }

    @After fun tearDown() {
        server.shutdown()
    }

    // redactHeaders is the unit under test: it must replace the Authorization
    // value with the redacted marker and leave everything else untouched. We
    // assert on the header values directly (not Headers.toString(), which
    // okhttp >= 4.10 masks to "██" by name regardless of value).
    @Test fun testRedactHeadersReplacesAuthorizationValue() {
        val redacted = LogInterceptor.redactHeaders(
                Headers.Builder()
                        .add(AuthorizationHeaderInterceptor.HEADER_NAME, "Bearer super-secret-token")
                        .add("X-Custom-Header", "custom-value")
                        .build()
        )

        assertEquals("Bearer [REDACTED]",
                redacted[AuthorizationHeaderInterceptor.HEADER_NAME],
                "Authorization value must be redacted")
        assertEquals("custom-value", redacted["X-Custom-Header"],
                "Non-authorization headers must be left untouched")
    }

    @Test fun testRedactHeadersMatchesHeaderNameCaseInsensitively() {
        val redacted = LogInterceptor.redactHeaders(
                Headers.Builder().add("authorization", "Bearer super-secret-token").build()
        )

        assertEquals("Bearer [REDACTED]", redacted["authorization"])
    }

    // End-to-end: drive a real request through the interceptor and confirm the
    // raw token never reaches the log output.
    @Test fun testTokenNeverAppearsInLog() {
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
                .header("X-Custom-Header", "custom-value")
                .build()

        client.newCall(request).execute().use { it.body?.string() }

        val requestLog = logs.first { it.startsWith("Sending request") }

        assertFalse(requestLog.contains(token), "Raw token must not appear in logs")
    }
}
