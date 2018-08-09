/*
 * Copyright (C) 2018 Contentful GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.contentful.java.cma.e2e

import com.contentful.java.cma.model.CMAAsset
import com.contentful.java.cma.model.CMAAssetFile
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

open class AssetsE2E : Base() {
    @Test
    fun testAssets() {
        var asset = CMAAsset()
        asset.fields = CMAAsset.Fields().apply {
            setDescription("en-US", "Description")
            setFile("en-US", CMAAssetFile().apply {
                fileName = "file name"
                contentType = "image/png"
                uploadUrl = "https://www.contentful.com/assets/images/favicons/favicon.png"
            })
        }

        asset = client.assets().create(asset)
        assertEquals("Description", asset.fields.getDescription("en-US"))

        assertEquals(204, client.assets().process(asset, "en-US"))

        for (i in 10 downTo 0) {
            asset = client.assets().fetchOne(asset.id)
            if (asset.fields.getFile("en-US").url != null
                    && asset.fields.getFile("en-US").url.isNotEmpty()) {
                break
            }

            Thread.sleep(100L)
        }

        assertNotNull(asset.fields.getFile("en-US").url.isNotEmpty(), "Could not process asset in time")

        assertFalse(asset.isPublished)
        asset = client.assets().publish(asset)
        assertTrue(asset.isPublished)

        asset = client.assets().unPublish(asset)
        assertFalse(asset.isPublished)

        asset.fields.setDescription("en-US", "Another Description")
        asset = client.assets().update(asset)
        assertEquals("Another Description", asset.fields.getDescription("en-US"))

        asset = client.assets().publish(asset)
        asset = client.assets().unPublish(asset)

        assertFalse(asset.isArchived)
        asset = client.assets().archive(asset)
        assertTrue(asset.isArchived)

        asset = client.assets().unArchive(asset)
        assertFalse(asset.isArchived)

        assertEquals(204, client.assets().delete(asset))
    }
}