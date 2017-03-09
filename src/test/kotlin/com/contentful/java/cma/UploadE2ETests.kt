package com.contentful.java.cma

import com.contentful.java.cma.lib.TestUtils
import com.contentful.java.cma.model.CMAAsset
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.Assert.*
import java.util.*
import org.junit.Test as test
import org.junit.Ignore as ignore

class UploadE2ETests {
    private val TOKEN = System.getenv("CMA_TOKEN")
    private val SPACE = System.getenv("CMA_SPACE")
    private val CORE_URL = System.getenv("CMA_URL")
    private val UPLOAD_URL = System.getenv("CMA_UPLOAD_URL")
    private val MAXIMUM_ATTEMPTS = 30
    private val LOCALE = "en-US"

    @test
    @ignore
    fun testSmallImageUploadAndAssetCreation() {
        testE2EUpload("upload_post_payload.jpg")
    }

    @test
    @ignore
    fun testBigImageUploadAndAssetCreation() {
        testE2EUpload("upload_post_payload_bigger.jpg")
    }

    private fun testE2EUpload(fileName: String) {
        val client = CMAClient.Builder()
                .setAccessToken(TOKEN)
                .setCoreEndpoint(CORE_URL)
                .setUploadEndpoint(UPLOAD_URL)
                .build()

        // upload file
        val file = TestUtils.fileToInputStream(fileName)
        val uploadResponse = client.uploads().create(SPACE, file)
        assertEquals(uploadResponse.sys["type"], "Upload")

        // create asset
        val asset = CMAAsset()
        val resourceLinkSys = HashMap<String, String>()
        resourceLinkSys.put("type", "Link")
        resourceLinkSys.put("linkType", "Upload")
        resourceLinkSys.put("id", uploadResponse.resourceId)

        val resourceLink = HashMap<String, Map<String, String>>()
        resourceLink.put("sys", resourceLinkSys)

        val assetFile = HashMap<String, Any>()
        assetFile.put("fileName", fileName)
        assetFile.put("uploadFrom", resourceLink)
        assetFile.put("contentType", "image/jpg")

        asset.setField("title", fileName, LOCALE)
        asset.setField("file", assetFile, LOCALE)

        val createdAsset = client.assets().create(SPACE, asset)
        client.assets().process(createdAsset, LOCALE)

        // poll for upload
        var uploadFrom: Any?
        var url: String?
        var attempts = MAXIMUM_ATTEMPTS
        do {
            if (attempts < MAXIMUM_ATTEMPTS) {
                // give Contentful some time to update
                Thread.sleep(500)
            }

            val retrievedAsset = client.assets().fetchOne(SPACE, createdAsset.resourceId)

            val retrievedFields = retrievedAsset.fields
            val retrievedFileField = retrievedFields["file"]
            val retrievedFileFieldLocalized = retrievedFileField!![LOCALE] as Map<String, Any?>

            uploadFrom = retrievedFileFieldLocalized["uploadFrom"]
            url = retrievedFileFieldLocalized["url"] as String?

        } while (--attempts > 0 && uploadFrom != null && url == null)

        // assert existence
        assertNotEquals("Infinite loop hit", 0, attempts.toLong())
        assertNull(uploadFrom)
        assertNotNull(url)

        // download upload again
        val ok = OkHttpClient.Builder().build()
        val call = ok.newCall(Request.Builder().url("https:" + url!!).build())
        val response = call.execute()

        // verify bits
        val uploadedBytes = response.body().bytes()
        val expectedBytes = TestUtils.fileToBytes(fileName)

        assertArrayEquals("Downloaded and uploaded bytes.", expectedBytes, uploadedBytes)
    }
}
