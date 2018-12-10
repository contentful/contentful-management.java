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

import com.contentful.java.cma.CMAClient
import com.contentful.java.cma.model.CMAEntry
import org.junit.BeforeClass
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

open class EntryE2E : Base() {
    companion object {
        @BeforeClass
        @JvmStatic
        fun setUpEntriesSuite() {
            client = CMAClient.Builder().apply {
                if (!(PROXY.isNullOrEmpty())) {
                    setCoreEndpoint(PROXY)
                    setUploadEndpoint(PROXY)
                }
                setAccessToken(ACCESS_TOKEN)
                setSpaceId(SPACE_ID)
                setCoreCallFactory(createCallFactory())
                setUploadCallFactory(createCallFactory())
            }.build()
        }
    }

    @Test
    fun testEntries() {
        val initialEntryCount = client.entries().fetchAll().total

        var entry = CMAEntry()
        entry.setField("title", "en-US", "My Title")

        entry = client.entries().create("theOnlyContentModel", entry)
        assertEquals("My Title", entry.fields["title"]!!["en-US"])

        assertFalse(entry.isPublished)
        entry = client.entries().publish(entry)
        assertTrue(entry.isPublished)

        val snapshots = client.entries().fetchAllSnapshots(entry)
        assertEquals(1, snapshots.items.size)

        val snapshot = client.entries().fetchOneSnapshot(entry, snapshots.items.first().id)
        assertTrue(snapshot.snapshot is CMAEntry)
        assertEquals(snapshots.items.first().toString().trim(), snapshot.toString().trim())

        entry = client.entries().unPublish(entry)
        assertFalse(entry.isPublished)

        entry.setField("title", "en-US", "Another Title")
        entry = client.entries().update(entry)
        assertEquals("Another Title", entry.fields["title"]!!["en-US"])

        entry = client.entries().publish(entry)
        entry = client.entries().unPublish(entry)

        assertFalse(entry.isArchived)
        entry = client.entries().archive(entry)
        assertTrue(entry.isArchived)

        entry = client.entries().unArchive(entry)
        assertFalse(entry.isArchived)

        assertEquals(204, client.entries().delete(entry))

        assertEquals(initialEntryCount, client.entries().fetchAll().total)
    }
}