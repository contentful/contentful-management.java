/*
 * Copyright (C) 2019 Contentful GmbH
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

import com.contentful.java.cma.CMAClient
import com.contentful.java.cma.Constants
import com.contentful.java.cma.model.CMAContentType
import com.contentful.java.cma.model.CMAField
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

open class ContentTypesE2E : Base() {
    @Test
    fun testContentType() {
        var contentType = CMAContentType().apply {
            id = "testid"
            name = "testName"
            description = "testTypeDescription"

            addField(CMAField().apply {
                id = "id"
                name = "name"
                type = Constants.CMAFieldType.Symbol
            })
        }

        contentType = client.contentTypes().create(contentType)
        assertEquals("testid", contentType.id)
        assertEquals("testName", contentType.name)

        assertFalse(contentType.isPublished)

        contentType = client.contentTypes().publish(contentType)
        assertTrue(contentType.isPublished)

        val editorInterface = client.editorInterfaces().fetchOne(contentType.id)
        assertNotNull(editorInterface)
        assertEquals("singleLine", editorInterface.controls[0].widgetId)

        contentType = client.contentTypes().unPublish(contentType)
        assertFalse(contentType.isPublished)

        contentType = client.contentTypes().fetchOne(contentType.id)
        assertFalse(contentType.isPublished)

        contentType.addField(CMAField().apply {
            id = "id2"
            name = "another field name"
            type = Constants.CMAFieldType.Integer
        })
        contentType = client.contentTypes().update(contentType)
        assertEquals("another field name", contentType.fields.last().name)

        contentType = client.contentTypes().publish(contentType)
        contentType = client.contentTypes().unPublish(contentType)

        assertEquals(204, client.contentTypes().delete(contentType))

        assertEquals(1, client.contentTypes().fetchAll().total)
    }

    @Test
    fun testSnapshots() {
        val snapshotClient = CMAClient.Builder().apply {
            if (!(PROXY.isNullOrEmpty())) {
                setCoreEndpoint(PROXY)
                setUploadEndpoint(PROXY)
            }
            setAccessToken(ACCESS_TOKEN)
            setSpaceId(SPACE_ID)
            setCoreCallFactory(createCallFactory())
            setUploadCallFactory(createCallFactory())
        }.build()

        val contentType = snapshotClient.contentTypes().fetchAll(mapOf("limit" to "1")).items.first()

        val snapshots = snapshotClient.contentTypes().fetchAllSnapshots(contentType)
        assertEquals(1, snapshots.items.size)

        val snapshot = snapshotClient.contentTypes().fetchOneSnapshot(contentType, snapshots.items.first().id)
        assertEquals(snapshots.items.first().toString().trim(), snapshot.toString().trim())
    }
}