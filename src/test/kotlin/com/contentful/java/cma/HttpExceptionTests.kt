package com.contentful.java.cma

import com.contentful.java.cma.model.CMAHttpException
import okhttp3.*
import kotlin.test.assertEquals
import org.junit.Test as test

class HttpExceptionTests : BaseTest() {
    val HEADER_RATE_LIMIT_HOUR_LIMIT = "X-Contentful-RateLimit-Hour-Limit"
    val HEADER_RATE_LIMIT_HOUR_REMAINING = "X-Contentful-RateLimit-Hour-Remaining"
    val HEADER_RATE_LIMIT_SECOND_LIMIT = "X-Contentful-RateLimit-Second-Limit"
    val HEADER_RATE_LIMIT_SECOND_REMAINING = "X-Contentful-RateLimit-Second-Remaining"
    val HEADER_RATE_LIMIT_RESET = "X-Contentful-RateLimit-Reset"

    @test fun testHeadersParsing() {
        val request = Request.Builder().url("https://example.com/foo").build()

        val response = Response.Builder()
                .request(request)
                .body(
                        ResponseBody.create(MediaType.parse("application/json"), "{}")
                )
                .header(HEADER_RATE_LIMIT_HOUR_LIMIT, "1")
                .header(HEADER_RATE_LIMIT_HOUR_REMAINING, "2")
                .header(HEADER_RATE_LIMIT_SECOND_LIMIT, "3")
                .header(HEADER_RATE_LIMIT_SECOND_REMAINING, "4")
                .header(HEADER_RATE_LIMIT_RESET, "5k")
                .protocol(Protocol.HTTP_1_1)
                .code(500)
                .message("message")
                .build()

        val e = CMAHttpException(request, response)

        assertEquals("message", e.responseMessage())

        assertEquals(1, e.rateLimitHourLimit())
        assertEquals(2, e.rateLimitHourRemaining())
        assertEquals(3, e.rateLimitSecondLimit())
        assertEquals(4, e.rateLimitSecondRemaining())

        assertEquals(-1, e.rateLimitReset(), "Error while parsing number.")

        assertEquals(500, e.responseCode())

        assertEquals("FAILED REQUEST:\n"
                + "\tRequest{method=GET, url=https://example.com/foo, tag=null}\n"
                + "\t↳ Header{}\n"
                + "\tResponse{protocol=http/1.1, code=500, message=message, "
                + "url=https://example.com/foo}\n"
                + "\t↳ Header{X-Contentful-RateLimit-Hour-Limit: 1, "
                + "X-Contentful-RateLimit-Hour-Remaining: 2, X-Contentful-RateLimit-Reset: 5k, "
                + "X-Contentful-RateLimit-Second-Limit: 3, "
                + "X-Contentful-RateLimit-Second-Remaining: 4}",
                e.toString())
    }
}
