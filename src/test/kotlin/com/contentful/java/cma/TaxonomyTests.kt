package com.contentful.java.cma

import com.contentful.java.cma.lib.TestCallback
import com.contentful.java.cma.lib.TestUtils
import com.contentful.java.cma.model.CMAConcept
import com.contentful.java.cma.model.CMALink
import com.contentful.java.cma.model.CMAType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import kotlin.test.assertEquals
import org.junit.Test
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TaxonomyTests {
    private var server: MockWebServer? = null
    private var client: CMAClient? = null

    @Before
    fun setUp() {
        server = MockWebServer()
        server!!.start()

        client = CMAClient.Builder()
            .setAccessToken("token")
            .setCoreEndpoint(server!!.url("/").toString())
            .build()
    }

    @After
    fun tearDown() {
        server!!.shutdown()
    }

    @Test
    fun `test fetch all concept schemes`() {
        val responseBody = TestUtils.fileToString("taxonomy_concept_scheme_get_all_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = client!!.taxonomy().fetchConceptSchemes("orgId", emptyMap())

        assertEquals(2, result.items.size)
        assertEquals("3S9wFqkGUtAOCr6IUjpI3F", result.items[0].system.id)

        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/organizations/orgId/taxonomy/concept-schemes", recordedRequest.path)
    }

    @Test
    fun `test fetch single concept scheme`() {
        val responseBody = TestUtils.fileToString("taxonomy_concept_scheme_get_one_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = client!!.taxonomy().fetchConceptScheme("orgId", "schemeId")


        assertEquals("3ObFCyQRmzXGNrTNf7ejn7", result.system.id)
        assertEquals(CMAType.TaxonomyConceptScheme, result.system.type)
        assertEquals(4, result.system.version)
        assertEquals("2025-01-22T14:14:33.149Z", result.system.createdAt)
        assertEquals("2025-01-22T14:19:19.508Z", result.system.updatedAt)

        assertEquals("0ar3TVMKEKfNfi1zRoENff", result.system.organization.system.id)
        assertEquals("0aq1aR1pMpgsM7j6LLjRRP", result.system.createdBy.system.id)
        assertEquals("0aq1aR1pMpgsM7j6LLjRRP", result.system.updatedBy.system.id)

        assertNull(result.uri)
        assertEquals("ExampleSchema2", result.prefLabel["en-US"])
        assertEquals("This is example schema 2", result.definition["en-US"])

        assertEquals(1, result.topConcepts.size)
        assertEquals("6mGQ3L1vSc9Ejh93vdgjiv", result.topConcepts[0].system.id)

        assertEquals(3, result.concepts.size)
        assertEquals("6mGQ3L1vSc9Ejh93vdgjiv", result.concepts[0].system.id)
        assertEquals("5EzTDFmXoH774nO5uVDkRR", result.concepts[1].system.id)
        assertEquals("uTpwzHqhWHva3Cv8MtJtf", result.concepts[2].system.id)

        assertEquals(3, result.totalConcepts)

        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/organizations/orgId/taxonomy/concept-schemes/schemeId", recordedRequest.path)
    }

    @Test
    fun `test fetch all concepts`() {
        val responseBody = TestUtils.fileToString("taxonomy_concepts_get_all_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = client!!.taxonomy().fetchConcepts("orgId", emptyMap())

        assertEquals(4, result.items.size)
        assertEquals("5yddoLMAMMPcsFqEQAqvd3", result.items[0].system.id)

        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/organizations/orgId/taxonomy/concepts", recordedRequest.path)
    }

    @Test
    fun `test fetch single concept`() {
        val responseBody = TestUtils.fileToString("taxonomy_concepts_get_one_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = client!!.taxonomy().fetchConcept("orgId", "conceptId")

        assertEquals("example concept 2", result.prefLabel["en-US"])

        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/organizations/orgId/taxonomy/concepts/conceptId", recordedRequest.path)
    }

    @Test
    fun `test create concept`() {
        val responseBody = TestUtils.fileToString("taxonomy_concepts_create_one_response.json")
        server!!.enqueue(MockResponse().setResponseCode(201).setBody(responseBody))
        val payload = CMAConcept().apply {
            uri = null
            prefLabel = linkedMapOf("en-US" to "cddcdc")
            altLabels = linkedMapOf("en-US" to listOf("cddcdc", "cddc"))
            hiddenLabels = linkedMapOf("en-US" to listOf("dcdc", "cdcd"))
            note = linkedMapOf("en-US" to "cdcd")
            changeNote = null
            definition = linkedMapOf("en-US" to "dccdcddc")
            editorialNote = linkedMapOf("en-US" to "cdcd")
            example = linkedMapOf("en-US" to "dcdc")
            historyNote = linkedMapOf("en-US" to "dccd")
            scopeNote = linkedMapOf("en-US" to "cdcd")
            notations = listOf("dcdc")
            broader = listOf(CMALink(CMAType.TaxonomyConcept).setId("6mGQ3L1vSc9Ejh93vdgjiv"))
            related = listOf(CMALink(CMAType.TaxonomyConcept).setId("uTpwzHqhWHva3Cv8MtJtf"))
        }

        // Call the method
        val result = assertTestCallback(
            client!!.taxonomy().async()
                .createConcept("organizationId", payload, TestCallback()) as TestCallback
        )!!

        // Assertions for the response
        assertEquals("6BGzvRdljNJ58FN1uPtfQW", result.system.id)
        assertEquals(CMAType.TaxonomyConcept, result.system.type)
        assertEquals("2025-01-23T08:57:33.468Z", result.system.createdAt)
        assertEquals("2025-01-23T08:57:33.468Z", result.system.updatedAt)

        assertEquals("0ar3TVMKEKfNfi1zRoENff", result.system.organization.system.id)
        assertEquals("0aq1aR1pMpgsM7j6LLjRRP", result.system.createdBy.system.id)
        assertEquals("0aq1aR1pMpgsM7j6LLjRRP", result.system.updatedBy.system.id)
        assertEquals(1, result.system.version)

        assertNull(result.uri)
        assertEquals("cddcdc", result.prefLabel["en-US"])
        assertEquals(listOf("cddcdc", "cddc"), result.altLabels["en-US"])
        assertEquals(listOf("dcdc", "cdcd"), result.hiddenLabels["en-US"])
        assertEquals("cdcd", result.note["en-US"])
        assertNull(result.changeNote)
        assertEquals("dccdcddc", result.definition["en-US"])
        assertEquals("cdcd", result.editorialNote["en-US"])
        assertEquals("dcdc", result.example["en-US"])
        assertEquals("dccd", result.historyNote["en-US"])
        assertEquals("cdcd", result.scopeNote["en-US"])
        assertEquals(listOf("dcdc"), result.notations)

        assertEquals(1, result.broader.size)
        assertEquals("6mGQ3L1vSc9Ejh93vdgjiv", result.broader[0].system.id)

        assertEquals(1, result.related.size)
        assertEquals("uTpwzHqhWHva3Cv8MtJtf", result.related[0].system.id)

        // Verify the request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/organizations/organizationId/taxonomy/concepts", recordedRequest.path)

        val requestBody = recordedRequest.body.readUtf8()
        assertTrue(requestBody.contains("\"prefLabel\":{\"en-US\":\"cddcdc\"}"))
        assertTrue(requestBody.contains("\"altLabels\":{\"en-US\":[\"cddcdc\",\"cddc\"]}"))
        assertTrue(requestBody.contains("\"hiddenLabels\":{\"en-US\":[\"dcdc\",\"cdcd\"]}"))
        assertTrue(requestBody.contains("\"broader\":[{\"sys\":{\"id\":\"6mGQ3L1vSc9Ejh93vdgjiv\",\"linkType\":\"TaxonomyConcept\",\"type\":\"Link\"}}]"))
        assertTrue(requestBody.contains("\"related\":[{\"sys\":{\"id\":\"uTpwzHqhWHva3Cv8MtJtf\",\"linkType\":\"TaxonomyConcept\",\"type\":\"Link\"}}]"))
    }
}
