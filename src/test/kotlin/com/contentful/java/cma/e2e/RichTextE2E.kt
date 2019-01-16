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
import com.contentful.java.cma.model.rich.*
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

open class RichTextE2E : Base() {
    @Test
    fun testRichTextTexts() {
        val previousItemCount = client.entries().fetchAll(
                mapOf("limit" to "0")
        ).total

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

    @Test
    fun testRichDocument() {
        val richText = CMARichDocument()
        val entry = CMAEntry()
        entry.setField("title", "en-US", "rich text title")
        entry.setField("rich_text", "en-US", richText)

        var result = client.entries().create("theOnlyContentModel", entry)

        assertNotNull(result)
        assertEquals(entry::class, result::class)

        val richResult = result.getField<CMARichDocument>("rich_text", "en-US")
        assertNotNull(richResult as CMARichDocument)
        assertEquals(0, richResult.content.size)

        result = client.entries().publish(result)

        assertNotNull(result)
        assertEquals(entry::class, result::class)
    }

    @Test
    fun testRichEmbeddedLink() {
        val richText = document(
                CMARichParagraph()
                        .addContent(
                                CMARichEmbeddedLink(
                                        CMALink(
                                                client.entries().fetchAll(
                                                        mapOf("limit" to "1")
                                                ).items.first()
                                        ), true
                                )
                        )
        )

        val entry = CMAEntry()
        entry.setField("title", "en-US", "rich text title")
        entry.setField("rich_text", "en-US", richText)

        var result = client.entries().create("theOnlyContentModel", entry)

        assertNotNull(result)
        assertEquals(entry::class, result::class)

        val richResult = result.getField<CMARichDocument>("rich_text", "en-US")
        assertNotNull(richResult as CMARichDocument)
        assertEquals(1, richResult.content.size)

        val richParagraph = richResult.content.first()
        assertNotNull(richParagraph as CMARichParagraph)
        assertEquals(1, richParagraph.content.size)

        val resultLink = richParagraph.content.first()
        assertNotNull(resultLink as CMARichEmbeddedLink)
        val resultData = resultLink.data
        assertNotNull(resultData as CMALink)
        assertNotNull(resultData.id)

        result = client.entries().publish(result)

        assertNotNull(result)
        assertEquals(entry::class, result::class)
    }

    @Test
    fun testRichHeading() {
        val richText = document(CMARichHeading(5).addContent(CMARichText("Heading of level 5.")))
        val entry = CMAEntry()
        entry.setField("title", "en-US", "rich text title")
        entry.setField("rich_text", "en-US", richText)

        var result = client.entries().create("theOnlyContentModel", entry)

        val richResult = result.getField<CMARichDocument>("rich_text", "en-US")
        assertNotNull(richResult as CMARichDocument)
        assertEquals(1, richResult.content.size)

        val resultHeading = richResult.content.first()
        assertNotNull(resultHeading as CMARichHeading)
        assertEquals(5, resultHeading.level)

        assertNotNull(result)
        assertEquals(entry::class, result::class)

        result = client.entries().publish(result)

        assertNotNull(result)
        assertEquals(entry::class, result::class)
    }

    @Test
    fun testRichHorizontalRule() {
        val richText = CMARichDocument().addContent(CMARichHorizontalRule())
        val entry = CMAEntry()
        entry.setField("title", "en-US", "rich text title")
        entry.setField("rich_text", "en-US", richText)

        var result = client.entries().create("theOnlyContentModel", entry)

        assertNotNull(result)
        assertEquals(entry::class, result::class)

        val richResult = result.getField<CMARichDocument>("rich_text", "en-US")
        assertNotNull(richResult as CMARichDocument)
        assertEquals(1, richResult.content.size)

        val resultLink = richResult.content.first()
        assertTrue(resultLink is CMARichHorizontalRule)

        result = client.entries().publish(result)

        assertNotNull(result)
        assertEquals(entry::class, result::class)
    }

    @Test
    fun testRichHyperLink() {
        val richText = CMARichDocument()
                .addContent(
                        CMARichParagraph()
                                .addContent(
                                        CMARichHyperLink("https://foobarbaz")
                                )
                )

        val entry = CMAEntry()
        entry.setField("title", "en-US", "rich text title")
        entry.setField("rich_text", "en-US", richText)

        var result = client.entries().create("theOnlyContentModel", entry)

        assertNotNull(result)
        assertEquals(entry::class, result::class)

        val richResult = result.getField<CMARichDocument>("rich_text", "en-US")
        assertNotNull(richResult as CMARichDocument)
        assertEquals(1, richResult.content.size)

        val richParagraph = richResult.content.first()
        assertNotNull(richParagraph as CMARichParagraph)
        assertEquals(1, richParagraph.content.size)

        val resultLink = richParagraph.content.first()
        assertNotNull(resultLink as CMARichHyperLink)
        assertEquals("https://foobarbaz", resultLink.data)

        result = client.entries().publish(result)

        assertNotNull(result)
        assertEquals(entry::class, result::class)
    }

    @Test
    fun testRichListItem() {
        val richText = document(
                CMARichOrderedList()
                        .addContent(
                                CMARichListItem()
                                        .addContent(
                                                CMARichParagraph().addContent(
                                                        CMARichText("listitem")
                                                )
                                        )
                        )
        )

        val entry = CMAEntry()
        entry.setField("title", "en-US", "rich text title")
        entry.setField("rich_text", "en-US", richText)

        var result = client.entries().create("theOnlyContentModel", entry)

        assertNotNull(result)
        assertEquals(entry::class, result::class)

        val richResult = result.getField<CMARichDocument>("rich_text", "en-US")
        assertNotNull(richResult as CMARichDocument)
        assertEquals(1, richResult.content.size)

        val resultList = richResult.content.first()
        assertNotNull(resultList as CMARichOrderedList)
        assertEquals(1, resultList.content.size)

        val resultItem = resultList.content.first()
        assertNotNull(resultItem as CMARichListItem)
        assertEquals(1, resultItem.content.size)

        val resultParagraph = resultItem.content.first()
        assertNotNull(resultParagraph as CMARichParagraph)
        assertEquals("listitem", (resultParagraph.content.first() as CMARichText).value)

        result = client.entries().publish(result)

        assertNotNull(result)
        assertEquals(entry::class, result::class)
    }

    @Test
    fun testRichOrderedList() {
        val richText = document(
                CMARichOrderedList()
                        .addContent(
                                CMARichListItem()
                                        .addContent(
                                                CMARichParagraph()
                                                        .addContent(
                                                                CMARichText("first")
                                                        )
                                        ),
                                CMARichListItem()
                                        .addContent(
                                                CMARichParagraph()
                                                        .addContent(
                                                                CMARichText("second")
                                                        )
                                        )
                        )
        )

        val entry = CMAEntry()
        entry.setField("title", "en-US", "rich text title")
        entry.setField("rich_text", "en-US", richText)

        var result = client.entries().create("theOnlyContentModel", entry)

        assertNotNull(result)
        assertEquals(entry::class, result::class)

        val richResult = result.getField<CMARichDocument>("rich_text", "en-US")
        assertNotNull(richResult as CMARichDocument)
        assertEquals(1, richResult.content.size)

        val resultList = richResult.content.first()
        assertNotNull(resultList as CMARichOrderedList)
        assertEquals(2, resultList.content.size)

        val first = resultList.content.first()
        assertNotNull(first as CMARichListItem)
        assertEquals("first", (
                (first.content.first() as CMARichParagraph).content.first() as CMARichText).value
        )

        val second = resultList.content.second()
        assertNotNull(second as CMARichListItem)
        assertEquals("second", (
                (second.content.first() as CMARichParagraph).content.first() as CMARichText).value
        )

        result = client.entries().publish(result)

        assertNotNull(result)
        assertEquals(entry::class, result::class)
    }

    @Test
    fun testRichParagraph() {
        val richText = document(CMARichParagraph().addContent(CMARichText("some text")))
        val entry = CMAEntry()
        entry.setField("title", "en-US", "rich text title")
        entry.setField("rich_text", "en-US", richText)

        var result = client.entries().create("theOnlyContentModel", entry)

        assertNotNull(result)
        assertEquals(entry::class, result::class)

        val richResult = result.getField<CMARichDocument>("rich_text", "en-US")
        assertNotNull(richResult as CMARichDocument)
        assertEquals(1, richResult.content.size)

        val resultParagraph = richResult.content.first()
        assertNotNull(resultParagraph as CMARichParagraph)
        assertEquals(1, resultParagraph.content.size)

        val resultText = resultParagraph.content.first()
        assertNotNull(resultText as CMARichText)
        assertEquals("some text", resultText.value)

        result = client.entries().publish(result)

        assertNotNull(result)
        assertEquals(entry::class, result::class)
    }

    @Test
    fun testRichQuote() {
        val richText = document(
                CMARichQuote()
                        .addContent(
                                CMARichParagraph()
                                        .addContent(
                                                CMARichText(
                                                        "rich quote from someone"
                                                )
                                        )
                        )
        )

        val entry = CMAEntry()
        entry.setField("title", "en-US", "rich text title")
        entry.setField("rich_text", "en-US", richText)

        var result = client.entries().create("theOnlyContentModel", entry)

        val richResult = result.getField<CMARichDocument>("rich_text", "en-US")
        assertNotNull(richResult as CMARichDocument)
        assertEquals(1, richResult.content.size)

        val resultQuote = richResult.content.first()
        assertNotNull(resultQuote as CMARichQuote)
        assertEquals(1, resultQuote.content.size)

        val resultParagraph = resultQuote.content.first()
        assertNotNull(resultParagraph as CMARichParagraph)
        assertEquals(1, resultParagraph.content.size)

        val resultText = resultParagraph.content.first()
        assertNotNull(resultText as CMARichText)
        assertEquals("rich quote from someone", resultText.value)

        assertNotNull(result)
        assertEquals(entry::class, result::class)

        result = client.entries().publish(result)

        assertNotNull(result)
        assertEquals(entry::class, result::class)
    }

    @Test
    fun testRichText() {
        val richText = document(CMARichParagraph().addContent(CMARichText("test text")))
        val entry = CMAEntry()
        entry.setField("title", "en-US", "rich text title")
        entry.setField("rich_text", "en-US", richText)

        var result = client.entries().create("theOnlyContentModel", entry)

        val richResult = result.getField<CMARichDocument>("rich_text", "en-US")
        assertNotNull(richResult as CMARichDocument)
        assertEquals(1, richResult.content.size)

        val resultParagraph = richResult.content.first()
        assertNotNull(resultParagraph as CMARichParagraph)
        assertEquals(1, resultParagraph.content.size)

        val resultText = resultParagraph.content.first()
        assertNotNull(resultText as CMARichText)
        assertEquals("test text", resultText.value)

        assertNotNull(result)
        assertEquals(entry::class, result::class)

        result = client.entries().publish(result)

        assertNotNull(result)
        assertEquals(entry::class, result::class)
    }

    @Test
    fun testRichUnorderedList() {
        val richText = document(CMARichUnorderedList())
        val entry = CMAEntry()
        entry.setField("title", "en-US", "rich text title")
        entry.setField("rich_text", "en-US", richText)

        var result = client.entries().create("theOnlyContentModel", entry)

        assertNotNull(result)
        assertEquals(entry::class, result::class)

        result = client.entries().publish(result)

        assertNotNull(result)
        assertEquals(entry::class, result::class)
    }
}

internal fun document(child: CMARichNode): CMARichNode =
        CMARichDocument()
                .addContent(
                        child
                )

internal fun <T> List<T>.second(): T {
    if (isEmpty())
        throw NoSuchElementException("List is empty.")
    if (size < 2)
        throw NoSuchElementException("List does not have two elements.")
    return this[1]
}
