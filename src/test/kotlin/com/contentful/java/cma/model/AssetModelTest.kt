package com.contentful.java.cma.model

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class AssetModelTest {
    @Test
    fun testRemoveDetailsAndUrlOnUploadSettingUpload() {
        val asset = CMAAsset()
        asset.setId("assetid")
                .setSpaceId("spaceid")
                .setVersion(1)
        asset.fields.localize("en-US")
                .file = CMAAssetFile()
                .setContentType("image/jpeg")
                .setUrl("foo")
                .setFileName("example.jpg")

        assertEquals("foo", asset.fields.getFile("en-US").url)

        asset.fields.getFile("en-US").setUploadUrl("http://foo/bar")

        assertNotNull(asset.fields.getFile("en-US").uploadUrl)

        assertNull(asset.fields.getFile("en-US").uploadFrom)
        assertNull(asset.fields.getFile("en-US").details)
        assertNull(asset.fields.getFile("en-US").url)
    }

    @Test
    fun testRemoveDetailsAndUrlOnUploadSettingUploadFrom() {
        val asset = CMAAsset()
        asset.setId("assetid")
                .setSpaceId("spaceid")
                .setVersion(1)
        asset.fields.localize("en-US")
                .file = CMAAssetFile()
                .setContentType("image/jpeg")
                .setUrl("foo")
                .setFileName("example.jpg")

        assertEquals("foo", asset.fields.getFile("en-US").url)

        asset.fields.getFile("en-US").setUploadFrom(CMALink())

        assertNotNull(asset.fields.getFile("en-US").uploadFrom)

        assertNull(asset.fields.getFile("en-US").details)
        assertNull(asset.fields.getFile("en-US").upload)
        assertNull(asset.fields.getFile("en-US").url)
    }

}