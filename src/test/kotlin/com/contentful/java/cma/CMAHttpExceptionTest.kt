package com.contentful.java.cma

import com.contentful.java.cma.lib.TestUtils
import com.contentful.java.cma.model.CMAHttpException
import com.google.gson.Gson
import okhttp3.*
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import java.util.logging.LogManager
import kotlin.test.assertEquals
import org.junit.Test as test

class CMAHttpExceptionTest {
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

    val HEADER_RATE_LIMIT_HOUR_LIMIT = "X-Contentful-RateLimit-Hour-Limit"
    val HEADER_RATE_LIMIT_HOUR_REMAINING = "X-Contentful-RateLimit-Hour-Remaining"
    val HEADER_RATE_LIMIT_SECOND_LIMIT = "X-Contentful-RateLimit-Second-Limit"
    val HEADER_RATE_LIMIT_SECOND_REMAINING = "X-Contentful-RateLimit-Second-Remaining"
    val HEADER_RATE_LIMIT_RESET = "X-Contentful-RateLimit-Reset"

    @test
    fun testHeadersParsing() {
        val request = Request.Builder().url("https://example.com/foo").build()

        val response = buildMockedResponse(request, "{\"sys\":{\"id\":\"manualid\"}}", 500, "message")

        val e = CMAHttpException(request, response)

        assertEquals("message", e.responseMessage())

        assertEquals(1, e.rateLimitHourLimit())
        assertEquals(2, e.rateLimitHourRemaining())
        assertEquals(3, e.rateLimitSecondLimit())
        assertEquals(4, e.rateLimitSecondRemaining())

        assertEquals(-1, e.rateLimitReset(), "Error while parsing number.")

        assertEquals(500, e.responseCode())

        assertEquals("FAILED ErrorBody { sys = Sys { id = manualid, } }\n" +
                "\tRequest{method=GET, url=https://example.com/foo, tag=null}\n" +
                "\t↳ Header{}\n" +
                "\tResponse{" +
                "protocol=http/1.1, code=500, message=message, url=https://example.com/foo}\n" +
                "\t↳ Header{X-Contentful-RateLimit-Hour-Limit: 1, " +
                "X-Contentful-RateLimit-Hour-Remaining: 2, X-Contentful-RateLimit-Reset: 5k, " +
                "X-Contentful-RateLimit-Second-Limit: 3, " +
                "X-Contentful-RateLimit-Second-Remaining: 4}",
                e.toString())
    }

    @test
    fun testUnknownPath() {
        val request = Request.Builder().url("https://example.com/foo").build()

        val response = buildMockedResponse(request, TestUtils.fileToString("error_unknown_path.json"), 200, "message")

        val e = CMAHttpException(request, response).errorBody

        assertEquals(
                "The query you sent was invalid. Probably a filter or ordering specification is not applicable to the type of a field.",
                e.message)

        assertEquals("InvalidQuery", e.sys.id)
        assertEquals("f5841eb3c46dc4a2c4f67c2d5a027f1c", e.requestId)

        assertEquals(1, e.details.errors.size)
        assertEquals("unknown", e.details.errors[0].name)
        val path = e.details.errors[0].path as List<*>
        assertEquals("sys", path[0])
        assertEquals("foo", path[1])
        assertEquals("The path \"sys.foo\" is not recognized", e.details.errors[0].details)
        assertEquals("f5841eb3c46dc4a2c4f67c2d5a027f1c", e.requestId)
    }

    @test
    fun testWrongTypeForLimit() {
        val request = Request.Builder().url("https://example.com/foo").build()

        val response = buildMockedResponse(request, TestUtils.fileToString("error_wrong_type_for_limit.json"), 200, "message")

        val e = CMAHttpException(request, response).errorBody

        assertEquals("InvalidQuery", e.sys.id)
        assertEquals("20add8f6cab860a61752c2e0b00966f1", e.requestId)

        assertEquals("The value provided for \"limit\" is invalid. Please provide a valid number value", e.message)
    }

    @test
    fun testWrongTypeForDate() {
        val request = Request.Builder().url("https://example.com/foo").build()

        val response = buildMockedResponse(request, TestUtils.fileToString("error_wrong_type_for_date.json"), 200, "message")

        val e = CMAHttpException(request, response).errorBody

        assertEquals("InvalidQuery", e.sys.id)
        assertEquals("4dfcab3bd85b753033280b1fa38d7717", e.requestId)

        assertEquals(
                "The query you sent was invalid. Probably a filter or ordering specification is not applicable to the type of a field.",
                e.message)

        assertEquals(1, e.details.errors.size)

        assertEquals("value", e.details.errors[0].name)
        val path = e.details.errors[0].path as List<*>
        assertEquals("sys", path[0])
        assertEquals("archivedAt", path[1])
        assertEquals("Date", e.details.errors[0].type)
        assertEquals("equals", e.details.errors[0].filter)
        assertEquals("false used on sys.archivedAt is not a valid value for type Date.", e.details.errors[0].details)
    }

    @test
    fun testPostWithWrongData() {
        val request = Request.Builder().url("https://example.com/foo").build()

        val response = buildMockedResponse(request, TestUtils.fileToString("error_post_wrong_data.json"), 200, "message")

        val e = CMAHttpException(request, response).errorBody

        assertEquals("InvalidEntry", e.sys.id)
        assertEquals("0cb77b0bf64d814ed182a668be4dc0eb", e.requestId)

        assertEquals("Validation error", e.message)

        assertEquals(1, e.details.errors.size)

        assertEquals("unknown", e.details.errors[0].name)
        val path = e.details.errors[0].path as List<*>
        assertEquals("fields", path[0])
        assertEquals("test", path[1])
        assertEquals("bar", e.details.errors[0].value)
        assertEquals("The property \"test\" is not allowed here.", e.details.errors[0].details)
    }

    @test
    fun testInvalidJson() {
        val request = Request.Builder().url("https://example.com/foo").build()

        val response = buildMockedResponse(request, TestUtils.fileToString("error_invalid_json.json"), 200, "message")

        val e = CMAHttpException(request, response).errorBody

        assertEquals("BadRequest", e.sys.id)
        assertEquals("8467e3d93f5650aefc596367370b6fa3", e.requestId)

        assertEquals(
                "Invalid JSON in request body: {\n\t\"fields\": {\n\t\t\"test\": {\n\t\t\t\"en-US: \"bar\"\n\t\t}\n\t}\n}",
                e.message)
    }

    @test
    fun testWrongType() {
        val request = Request.Builder().url("https://example.com/foo").build()

        val response = buildMockedResponse(request, TestUtils.fileToString("error_wrong_type.json"), 200, "message")

        val e = CMAHttpException(request, response).errorBody

        assertEquals("InvalidEntry", e.sys.id)
        assertEquals("8cd7ef1751dd283ee230e6a58efef8ec", e.requestId)

        assertEquals("Validation error", e.message)

        assertEquals(1, e.details.errors.size)

        assertEquals("type", e.details.errors[0].name)
        val path = e.details.errors[0].path as List<*>
        assertEquals("fields", path[0])
        assertEquals("xqZFfK7YdzEfhhJg", path[1])
        assertEquals("en-US", path[2])
        assertEquals("bar", e.details.errors[0].value)
        assertEquals("Object", e.details.errors[0].type)
        assertEquals("The type of \"value\" is incorrect, expected type: Object", e.details.errors[0].details)
    }

    @test
    fun testWrongField() {
        val request = Request.Builder().url("https://example.com/foo").build()

        val response = buildMockedResponse(request, TestUtils.fileToString("error_wrong_field.json"), 200, "message")

        val e = CMAHttpException(request, response).errorBody

        assertEquals("InvalidEntry", e.sys.id)
        assertEquals("fieldsRequestId", e.requestId)

        assertEquals("The body you sent contains an unknown key.", e.message)

        assertEquals(1, e.details.keys.size)
        assertEquals("a_field_that_does_not_exist", e.details.keys[0])
    }

    @test
    fun testToString() {
        val request = Request.Builder().url("https://example.com/foo").build()

        val response = buildMockedResponse(
                request,
                TestUtils.fileToString("error_wrong_type.json"), 500, "message"
        )

        val e = CMAHttpException(request, response)

        assertEquals("FAILED ErrorBody { details = Details { errors = [Error " +
                "{ details = The type of \"value\" is incorrect, expected type: Object," +
                " name = type, path = [fields, xqZFfK7YdzEfhhJg, en-US], type = Object, " +
                "value = bar }], }, message = Validation error, " +
                "requestId = 8cd7ef1751dd283ee230e6a58efef8ec, sys = Sys { id = InvalidEntry, " +
                "type = Error } }\n" +
                "\tRequest{method=GET, url=https://example.com/foo, tag=null}\n" +
                "\t↳ Header{}\n" +
                "\tResponse{protocol=http/1.1, code=500, message=message, " +
                "url=https://example.com/foo}\n" +
                "\t↳ Header{X-Contentful-RateLimit-Hour-Limit: 1, " +
                "X-Contentful-RateLimit-Hour-Remaining: 2, X-Contentful-RateLimit-Reset: 5k, " +
                "X-Contentful-RateLimit-Second-Limit: 3, " +
                "X-Contentful-RateLimit-Second-Remaining: 4}", e.toString())
    }

    private fun buildMockedResponse(request: Request, body: String, code: Int, message: String): Response? {
        return Response.Builder()
                .request(request)
                .body(
                        ResponseBody.create(MediaType.parse("application/json"), body)
                )
                .header(HEADER_RATE_LIMIT_HOUR_LIMIT, "1")
                .header(HEADER_RATE_LIMIT_HOUR_REMAINING, "2")
                .header(HEADER_RATE_LIMIT_SECOND_LIMIT, "3")
                .header(HEADER_RATE_LIMIT_SECOND_REMAINING, "4")
                .header(HEADER_RATE_LIMIT_RESET, "5k")
                .protocol(Protocol.HTTP_1_1)
                .code(code)
                .message(message)
                .build()
    }
}
