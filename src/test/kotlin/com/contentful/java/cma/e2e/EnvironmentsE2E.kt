package com.contentful.java.cma.e2e

import com.contentful.java.cma.CMAClient
import com.contentful.java.cma.model.CMAEnvironment
import com.contentful.java.cma.model.CMAEnvironmentStatus
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

private val ACCESS_TOKEN = System.getenv("CMA_TOKEN")
private val SPACE_ID = System.getenv("SPACE_ID")

class EnvironmentsE2E {
    private lateinit var client: CMAClient

    @Before
    fun setup() {
        client = CMAClient.Builder().apply {
            setAccessToken(ACCESS_TOKEN)
            setSpaceId(SPACE_ID)
        }.build()
    }

    @Test
    fun testCreationOfEnvironment() {
        var created: CMAEnvironment? = null
        try {
            val environment = CMAEnvironment().setName("new_environment")

            created = client.environments().create(environment)
            while (created!!.status != CMAEnvironmentStatus.Ready) {
                created = client.environments().fetchOne(created.id)
            }

            assertEquals("new_environment", created.name)
            assertEquals(numberOfEntriesOnEnvironment("master"), numberOfEntriesOnEnvironment(created.id))

        } finally {
            if (created != null) {
                assertEquals(204, client.environments().delete(created))
            }
        }
    }

    @Test
    fun testCloningOfEnvironments() {
        var created: CMAEnvironment? = null
        try {
            val sourceEnvironment = client.environments().fetchOne(SPACE_ID, "empty")
            val newEnvironment = CMAEnvironment().setName("cloned_from_empty")

            created = client.environments().clone(sourceEnvironment, newEnvironment)
            while (created!!.status != CMAEnvironmentStatus.Ready) {
                created = client.environments().fetchOne(created.id)
            }

            assertEquals("cloned_from_empty", created.name)
            assertEquals(numberOfEntriesOnEnvironment("empty"), numberOfEntriesOnEnvironment(created.id))
        } finally {
            if (created != null) {
                assertEquals(204, client.environments().delete(created))
            }
        }
    }

    private fun numberOfEntriesOnEnvironment(environmentId: String) =
            client.entries().fetchAll(SPACE_ID, environmentId).total
}