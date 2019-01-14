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

import com.contentful.java.cma.model.CMAEntry
import com.contentful.java.cma.model.CMALink
import com.contentful.java.cma.model.CMAType
import com.contentful.java.cma.model.rich.CMARichDocument
import com.contentful.java.cma.model.rich.CMARichHyperLink
import com.contentful.java.cma.model.rich.CMARichParagraph
import com.contentful.java.cma.model.rich.CMARichText
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

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
                                CMALink(
                                        client.entries().fetchAll().items.first()
                                )
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
        assertNotNull(data as CMALink)
        assertEquals(data.system.type, CMAType.Link)
        assertEquals(data.system.linkType, CMAType.Entry)

        assertEquals(204, client.entries().delete(entry))
        assertEquals(previousItemCount, client.entries().fetchAll().total)
    }

    @Test
    fun testRichTextCombined() {
        val previousItemCount = client.entries().fetchAll().total

        val richText = CMARichDocument().addContent(
                CMARichParagraph().addContent(
                        CMARichText("Hello World"),
                        CMARichHyperLink(
                                CMALink(
                                        client.entries().fetchAll().items.first()
                                )
                        )
                )
        )

        var entry = CMAEntry()
        entry.setField("title", "en-US", "rich text title")
        entry.setField("rich_text", "en-US", richText)

        entry = client.entries().create("theOnlyContentModel", entry)

        val document = entry.getField<CMARichDocument>("rich_text", "en-US")
        assertEquals(1, document.content.size)

        val paragraph = document.content.first() as CMARichParagraph
        assertEquals(2, paragraph.content.size)

        val text = paragraph.content.first()
        assertNotNull(text as CMARichText)
        assertEquals("Hello World", text.value)

        val link = paragraph.content.second()
        assertNotNull(link as CMARichHyperLink)
        val data = link.data

        assertNotNull(data as CMALink)
        assertNotNull(data.id)

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
