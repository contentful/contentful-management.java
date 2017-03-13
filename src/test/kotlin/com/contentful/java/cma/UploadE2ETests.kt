package com.contentful.java.cma

import com.contentful.java.cma.lib.TestUtils
import com.contentful.java.cma.model.CMAAsset
import com.contentful.java.cma.model.CMAAssetFile
import com.contentful.java.cma.model.CMAUploadLink
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.Assert.*
import org.junit.Ignore as ignore
import org.junit.Test as test

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

        assertEquals(uploadResponse.getSysAttribute("type"), "Upload")

        // create asset
        val asset = CMAAsset()

        asset
                .localize(LOCALE)
                .setTitle(fileName)
                .setDescription("Simple sample, please ignore and/or delete!")
                .file = CMAAssetFile()
                .setFileName(fileName)
                .setUploadFrom(CMAUploadLink().setId(uploadResponse.resourceId))
                .setContentType("image/jpg")

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

            val retrievedFile = retrievedAsset.localize(LOCALE).file

            uploadFrom = retrievedFile.uploadFrom
            url = retrievedFile.url

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
