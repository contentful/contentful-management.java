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

import com.contentful.java.cma.model.CMAEntry
import com.contentful.java.cma.model.rich.CMARichDocument
import com.contentful.java.cma.model.rich.CMARichHyperLink
import com.contentful.java.cma.model.rich.CMARichParagraph
import com.contentful.java.cma.model.rich.CMARichText
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

open class RichTextE2E : Base() {
    @Test
    fun testRichTextTexts() {
        val previousItemCount = client.entries().fetchAll().total

        val richText = CMARichDocument().addContent(
                CMARichParagraph().addContent(
                        CMARichText("Sample Text")
                )
        )

        var entry = CMAEntry()
        entry.setField("title", "en-US", "rich text text title")
        entry.setField("rich_text", "en-US", richText)

        entry = client.entries().create("theOnlyContentModel", entry)
        assertEquals(1, entry.getField<CMARichDocument>("rich_text", "en-US").content.size)

        val paragraph = entry.getField<CMARichDocument>("rich_text", "en-US").content.first()
        assertNotNull(paragraph as CMARichParagraph)

        val text = paragraph.content.first()
        assertNotNull(text as CMARichText)
        assertEquals("Sample Text", text.value)

        assertEquals(204, client.entries().delete(entry))
        assertEquals(previousItemCount, client.entries().fetchAll().total)
    }

    @Test
    fun testRichTextLinks() {
        val previousItemCount = client.entries().fetchAll().total

        val richText = CMARichDocument().addContent(
                CMARichParagraph().addContent(
                        CMARichHyperLink(
                                client.entries().fetchAll().items.first()
                        )
                )
        )

        var entry = CMAEntry()
        entry.setField("title", "en-US", "rich text links title")
        entry.setField("rich_text", "en-US", richText)

        entry = client.entries().create("theOnlyContentModel", entry)
        assertEquals(1, entry.getField<CMARichDocument>("rich_text", "en-US").content.size)

        val paragraph = entry.getField<CMARichDocument>("rich_text", "en-US").content.first()
        assertNotNull(paragraph as CMARichParagraph)

        val link = paragraph.content.first()
        assertNotNull(link as CMARichHyperLink)

        val data = link.data
        assertNotNull(data as CMAEntry)
        assertNotEquals("rich text links title", data.getField("title", "en-US"))

        assertEquals(204, client.entries().delete(entry))
        assertEquals(previousItemCount, client.entries().fetchAll().total)
    }

    @Test
    fun testRichTextCombined() {
        val previousItemCount = client.entries().fetchAll().total

        val richText = CMARichDocument().addContent(
                CMARichText("Hello World"),
                CMARichHyperLink(
                        client.entries().fetchAll().items.first()
                )
        )

        var entry = CMAEntry()
        entry.setField("title", "en-US", "rich text title")
        entry.setField("rich_text", "en-US", richText)

        entry = client.entries().create("theOnlyContentModel", entry)
        assertEquals(2, entry.getField<CMARichDocument>("rich_text", "en-US").content.size)

        val text = entry.getField<CMARichDocument>("rich_text", "en-US").content.first()
        assertTrue(text is CMARichText)
        assertEquals("Hello World", (text as CMARichText).value)

        val link = entry.getField<CMARichDocument>("rich_text", "en-US").content.second()
        assertTrue(link is CMARichHyperLink)
        val data = (link as CMARichHyperLink).data
        assertTrue(data is CMAEntry)
        assertNotEquals("rich text title", (data as CMAEntry).getField("title", "en-US"))

        assertEquals(204, client.entries().delete(entry))
        assertEquals(previousItemCount, client.entries().fetchAll().total)
    }
}

private fun <T> List<T>.second(): T {
    if (isEmpty())
        throw NoSuchElementException("List is empty.")
    if (size < 2)
        throw NoSuchElementException("List does not have two elements.")
    return this[1]
}
