package com.contentful.java.cma

import com.contentful.java.cma.lib.TestCallback
import com.contentful.java.cma.lib.TestUtils
import com.contentful.java.cma.model.CMALink
import com.contentful.java.cma.model.CMAScheduledAction
import com.contentful.java.cma.model.CMAScheduledFor
import com.google.gson.Gson
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.logging.LogManager
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ScheduledActionsTests {
    private lateinit var server: MockWebServer
    private lateinit var client: CMAClient
    private lateinit var gson: Gson

    @Before
    @Throws(Exception::class)
    fun setUp() {
        LogManager.getLogManager().reset()
        server = MockWebServer()
        server.start()

        client = CMAClient.Builder()
            .setAccessToken("token")
            .setCoreEndpoint(server.url("/").toString())
            .setUploadEndpoint(server.url("/").toString())
            .setSpaceId("configuredSpaceId")
            .setEnvironmentId("configuredEnvironmentId")
            .build()

        gson = CMAClient.createGson()
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        server.shutdown()
    }

    @Test
    @Throws(Exception::class)
    fun testCreateScheduledAction() {
        val responseBody = TestUtils.fileToString("scheduled_action_create_response.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val scheduledAction = CMAScheduledAction().apply {
            entity = CMALink().setId("entitiyId")
            action = "publish"
            environment = CMALink().setId("enivronmentId")
            scheduledFor = CMAScheduledFor().apply {
                datetime = "2022-01-01T12:00:00.000Z"
                timezone = "Europe/Berlin"
            }
        }

        val result = assertTestCallback(client.scheduledActions().async()
            .create(scheduledAction, TestCallback()) as TestCallback)

        assertNotNull(result)
        assertEquals("scheduled", result.system.scheduledActionStatus.status)

        // Check request details
        val recordedRequest = server.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces/configuredSpaceId/scheduled_actions", recordedRequest.path)
    }

    @Test
    @Throws(Exception::class)
    fun testFetchScheduledAction() {
        val responseBody = TestUtils.fileToString("scheduled_action_fetch_response.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client.scheduledActions().async()
            .fetchOne("3A13SXSDwO8c46NrjigFYT", "5KsDBWseXY6QegucYAoacS", TestCallback()) as TestCallback)

        assertNotNull(result)
        assertEquals("scheduled", result.system.scheduledActionStatus.status)
        assertEquals("publish", result.action)

        // Check request details
        val recordedRequest = server.takeRequest()
        val expectedPath = "/spaces/configuredSpaceId/scheduled_actions/3A13SXSDwO8c46NrjigFYT?entity.sys.id=5KsDBWseXY6QegucYAoacS&environment.sys.id=configuredEnvironmentId"
        assertEquals("GET", recordedRequest.method)
        assertEquals(expectedPath, recordedRequest.path)
    }

    @Test
    @Throws(Exception::class)
    fun testUpdateScheduledAction() {
        val responseBody = TestUtils.fileToString("scheduled_action_update_response.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val scheduledAction = CMAScheduledAction().apply {
            entity = CMALink().setId("entitiyId")
            action = "publish"
            environment = CMALink().setId("enivronmentId")
            scheduledFor = CMAScheduledFor().apply {
                datetime = "2022-01-01T12:00:00.000Z"
                timezone = "Europe/Berlin"
            }
        }

        val result = assertTestCallback(client.scheduledActions().async()
            .update("3A13SXSDwO8c46NrjigFYT", scheduledAction, TestCallback()) as TestCallback)

        assertNotNull(result)
        assertEquals("scheduled", result.system.scheduledActionStatus.status)
        assertEquals("publish", result.action)

        // Check request details
        val recordedRequest = server.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces/configuredSpaceId/scheduled_actions/3A13SXSDwO8c46NrjigFYT", recordedRequest.path)
    }

    @Test
    @Throws(Exception::class)
    fun testCancelScheduledAction() {
        val responseBody = TestUtils.fileToString("scheduled_action_cancel_response.json")
        server.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client.scheduledActions().async()
            .cancel("3A13SXSDwO8c46NrjigFYT", TestCallback()) as TestCallback)

        assertNotNull(result)
        assertEquals("canceled", result.system.scheduledActionStatus.status)
        assertEquals("publish", result.action)
        assertEquals("5KsDBWseXY6QegucYAoacS", result.entity.id)

        // Check request details
        val recordedRequest = server.takeRequest()
        val expectedPath = "/spaces/configuredSpaceId/scheduled_actions/3A13SXSDwO8c46NrjigFYT?environment.sys.id=configuredEnvironmentId"
        assertEquals("DELETE", recordedRequest.method)
        assertEquals(expectedPath, recordedRequest.path)
    }
}
