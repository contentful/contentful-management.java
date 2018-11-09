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

package com.contentful.java.cma

import com.contentful.java.cma.lib.TestCallback
import com.contentful.java.cma.lib.TestUtils
import com.contentful.java.cma.model.CMAEntry
import com.contentful.java.cma.model.CMALink
import com.contentful.java.cma.model.CMAType
import com.contentful.java.cma.model.rich.*
import com.google.gson.Gson
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import java.util.logging.LogManager
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import org.junit.Test as test

class RichFieldTests {
    var server: MockWebServer? = null
    var client: CMAClient? = null
    var gson: Gson? = null

    @Before
    fun setUp() {
        LogManager.getLogManager().reset()
        // MockWebServer
        server = MockWebServer()
        server!!.start()

        // Client
        client = CMAClient.Builder()
                .setAccessToken("token")
                .setCoreEndpoint(server!!.url("/").toString())
                .setUploadEndpoint(server!!.url("/").toString())
                .setSpaceId("configuredSpaceId")
                .setEnvironmentId("configuredEnvironmentId")
                .build()

        gson = CMAClient.createGson()
    }

    @After
    fun tearDown() {
        server!!.shutdown()
    }

    @test
    fun testCreateOneWithId() {
        val requestBody = TestUtils.fileToString("rich_text_create_one_payload.json")
        val responseBody = TestUtils.fileToString("rich_text_create_one.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val document = CMARichDocument().addContent(
                CMARichParagraph().addContent(
                        CMARichText(
                                "delete me soon™",
                                listOf(CMARichMark.CMARichMarkBold())
                        )
                )
        )

        val entry = CMAEntry()
                .setId("richtexttestentry")
                .setField("name", "en-US", "testing_created_this_entry_and_will_delete_it_soon")
                .setField("rich", "en-US", document)

        assertTestCallback(client!!.entries().async().create(
                "rich",
                entry,
                TestCallback()) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals(requestBody, recordedRequest.body.readUtf8())
    }

    @test
    fun testFetchOneWithId() {
        val responseBody = TestUtils.fileToString("rich_text_get_one.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val all = assertTestCallback(client!!.entries().async().fetchAll(
                TestCallback()) as TestCallback)!!

        val result = all.items.first()

        val sys = result.system
        val fields = result.fields

        assertEquals(CMAType.Entry, sys.type)
        assertEquals("6VkkamQ5zO4qWIugKkWowK", sys.id)
        assertEquals(2, fields.size)

        val name = result.getField<Any?>("name", "en-US")
        assertEquals("simple_text_code", name)

        val field = result.getField<Any?>("rich", "en-US")
        assertTrue(field is CMARichDocument)

        val document = result.getField<CMARichDocument>("rich", "en-US")

        assertTrue(document.content.first() is CMARichParagraph)
        val paragraph = document.content.first() as CMARichParagraph

        assertTrue(paragraph.content.first() is CMARichText)
        val text = paragraph.content.first() as CMARichText

        assertEquals(1, text.marks.size)
        assertTrue(text.marks.first() is CMARichMark.CMARichMarkCode)

        assertEquals("This is some simple text", text.value)
    }

    @test
    fun testUpdateOne() {
        val requestBody = TestUtils.fileToString("rich_text_update_one_payload.json")
        val responseBody = TestUtils.fileToString("rich_text_update_one.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        /// This should be fetched from Contentful, here for simplicity, mocking an entry.
        val entry = CMAEntry()
                .setId("6VkkamQ5zO4qWIugKkWowK")
                .setSpaceId("spaceid")
                .setEnvironmentId("master")
                .setVersion(1337)
                .setField("name", "en-US", "simple_text_code")

        entry.setField(
                "rich",
                "en-US",
                CMARichDocument().addContent(
                        CMARichParagraph().addContent(
                                CMARichText("delete me soon™")
                        )
                )
        )

        val result = assertTestCallback(client!!.entries().async()
                .update(entry, TestCallback()) as TestCallback)!!

        assertEquals("6VkkamQ5zO4qWIugKkWowK", result.id)
        assertEquals(CMAType.Entry, result.system.type)

        val fields = result.fields.entries.toList()
        assertEquals(2, fields.size)
        assertEquals("name", fields[0].key)
        assertEquals("simple_text_code", fields[0].value["en-US"])
        assertEquals("rich", fields[1].key)
        assertTrue(fields[1].value["en-US"] is CMARichDocument)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.method)
        assertEquals("/spaces/spaceid/environments/master/entries/6VkkamQ5zO4qWIugKkWowK",
                recordedRequest.path)
        assertNotNull(recordedRequest.getHeader("X-Contentful-Version"))
        assertEquals(requestBody, recordedRequest.body.readUtf8())
    }

    @test
    fun testText() {
        val responseBody = TestUtils.fileToString("rich_text_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))
        val array = assertTestCallback(client!!.entries().async().fetchAll(
                TestCallback()) as TestCallback)!!

        val result = array.items.first {
            it.getField<String>("name", "en-US") == "simple_text"
        }


        val document = result.getField<CMARichDocument>("rich", "en-US")
        assertNotNull(document)

        val paragraph = document.content.first() as CMARichParagraph
        assertNotNull(paragraph)

        val text = paragraph.content.first() as CMARichText

        assertNotNull(text)
        assertEquals("This is some simple text", text.value)

        assertNotNull(text.marks)
        assertEquals(0, text.marks.size)
    }

    @test
    fun testEntryHyperlink() {
        val responseBody = TestUtils.fileToString("rich_text_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))
        val array = assertTestCallback(client!!.entries().async().fetchAll(
                TestCallback()) as TestCallback)!!

        val result = array.items.first {
            it.getField<String>("name", "en-US") == "simple_entry_hyperlink"
        }


        val document = result.getField<CMARichDocument>("rich", "en-US")
        assertNotNull(document)

        val paragraph = document.content.first() as CMARichParagraph
        assertNotNull(paragraph)
        assertEquals(3, paragraph.content.size)

        val link = paragraph.content.first { it is CMARichHyperLink } as CMARichHyperLink

        assertNotNull(link)
        assertEquals(1, link.content.size)
        assertTrue(link.content.first() is CMARichText)

        val text = link.content[0] as CMARichText
        assertNotNull(text)
        assertEquals("Entry hyperlink to \"Hello World\"", text.value)

        assertNotNull(text.marks)
        assertEquals(0, text.marks.size)

        assertTrue(link.data is CMALink)
        val target = link.data as CMALink
        assertEquals("BvBm2SNTEs40wUwyoI0Qo", target.id)
        assertEquals(CMAType.Entry, target.system.linkType)
    }

    @test
    fun testAssetHyperlink() {
        val responseBody = TestUtils.fileToString("rich_text_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))
        val array = assertTestCallback(client!!.entries().async().fetchAll(
                TestCallback()) as TestCallback)!!

        val result = array.items.first {
            it.getField<String>("name", "en-US") == "simple_asset_hyperlink"
        }


        val document = result.getField<CMARichDocument>("rich", "en-US")
        assertNotNull(document)

        val paragraph = document.content.first() as CMARichParagraph
        assertNotNull(paragraph)
        assertEquals(3, paragraph.content.size)

        val link = paragraph.content.first { it is CMARichHyperLink } as CMARichHyperLink

        assertNotNull(link)
        assertEquals(1, link.content.size)
        assertTrue(link.content.first() is CMARichText)

        val text = link.content[0] as CMARichText
        assertNotNull(text)
        assertEquals("Asset hyperlink to cat image", text.value)

        assertNotNull(text.marks)
        assertEquals(0, text.marks.size)

        assertTrue(link.data is CMALink)
        val target = link.data as CMALink
        assertEquals("47Q1142uaI6SuK8UoGeeQy", target.id)
        assertEquals(CMAType.Asset, target.system.linkType)
    }

    @test
    fun testHyperlink() {
        val responseBody = TestUtils.fileToString("rich_text_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))
        val array = assertTestCallback(client!!.entries().async().fetchAll(
                TestCallback()) as TestCallback)!!

        val result = array.items.first {
            it.getField<String>("name", "en-US") == "simple_hyperlink"
        }

        val document = result.getField<CMARichDocument>("rich", "en-US")
        assertNotNull(document)

        val paragraph = document.content.first() as CMARichParagraph
        assertNotNull(paragraph)
        assertEquals(3, paragraph.content.size)

        val link = paragraph.content.first { it is CMARichHyperLink } as CMARichHyperLink

        assertNotNull(link)
        assertEquals(1, link.content.size)
        assertTrue(link.content.first() is CMARichText)

        val text = link.content[0] as CMARichText
        assertNotNull(text)
        assertEquals("Regular hyperlink to example.com", text.value)

        assertNotNull(text.marks)
        assertEquals(0, text.marks.size)

        assertTrue(link.data is String)
        val target = link.data as String
        assertEquals("https://www.example.com/", target)
    }

    @test
    fun testEntryInline() {
        val responseBody = TestUtils.fileToString("rich_text_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))
        val array = assertTestCallback(client!!.entries().async().fetchAll(
                TestCallback()) as TestCallback)!!

        val result = array.items.first {
            it.getField<String>("name", "en-US") == "simple_entry_inline"
        }


        val document = result.getField<CMARichDocument>("rich", "en-US")
        assertNotNull(document)

        val paragraph = document.content.first() as CMARichParagraph
        assertNotNull(paragraph)
        assertEquals(3, paragraph.content.size)

        val link = paragraph.content.first { it is CMARichHyperLink } as CMARichHyperLink

        assertNotNull(link)
        assertEquals(0, link.content.size)

        assertTrue(link.data is CMALink)
        val target = link.data as CMALink
        assertEquals("BvBm2SNTEs40wUwyoI0Qo", target.id)
        assertEquals(CMAType.Entry, target.system.linkType)
    }

    @test
    fun testAssetBlock() {
        val responseBody = TestUtils.fileToString("rich_text_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))
        val array = assertTestCallback(client!!.entries().async().fetchAll(
                TestCallback()) as TestCallback)!!

        val result = array.items.first {
            it.getField<String>("name", "en-US") == "simple_asset_block"
        }


        val document = result.getField<CMARichDocument>("rich", "en-US")
        assertNotNull(document)

        val link = document.content.first { it is CMARichEmbeddedLink } as CMARichEmbeddedLink

        assertNotNull(link)
        assertEquals(0, link.content.size)

        assertTrue(link.data is CMALink)
        val target = link.data as CMALink
        assertEquals("47Q1142uaI6SuK8UoGeeQy", target.id)
        assertEquals(CMAType.Asset, target.system.linkType)
    }

    @test
    fun testHorizontalRule() {
        val responseBody = TestUtils.fileToString("rich_text_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))
        val array = assertTestCallback(client!!.entries().async().fetchAll(
                TestCallback()) as TestCallback)!!

        val result = array.items.first {
            it.getField<String>("name", "en-US") == "simple_horizontal_rule"
        }


        val document = result.getField<CMARichDocument>("rich", "en-US")
        assertNotNull(document)
        assertEquals(2, document.content.size)

        assertTrue(document.content.first() is CMARichHorizontalRule)
    }

    @test
    fun testTextCode() {
        val responseBody = TestUtils.fileToString("rich_text_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))
        val array = assertTestCallback(client!!.entries().async().fetchAll(
                TestCallback()) as TestCallback)!!

        val result = array.items.first {
            it.getField<String>("name", "en-US") == "simple_text_code"
        }


        val document = result.getField<CMARichDocument>("rich", "en-US")
        assertNotNull(document)

        val paragraph = document.content.first() as CMARichParagraph
        assertNotNull(paragraph)
        assertEquals(1, paragraph.content.size)

        val text = paragraph.content.first() as CMARichText
        assertNotNull(text)
        assertEquals("This is some updated simple rich text", text.value)

        assertNotNull(text.marks)
        assertEquals(1, text.marks.size)
        assertTrue(text.marks.first() is CMARichMark.CMARichMarkCode)
    }

    @test
    fun testTextUnderline() {
        val responseBody = TestUtils.fileToString("rich_text_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))
        val array = assertTestCallback(client!!.entries().async().fetchAll(
                TestCallback()) as TestCallback)!!

        val result = array.items.first {
            it.getField<String>("name", "en-US") == "simple_text_underline"
        }


        val document = result.getField<CMARichDocument>("rich", "en-US")
        assertNotNull(document)

        val paragraph = document.content.first() as CMARichParagraph
        assertNotNull(paragraph)
        assertEquals(1, paragraph.content.size)

        val text = paragraph.content.first() as CMARichText
        assertNotNull(text)
        assertEquals("This is some simple text", text.value)

        assertNotNull(text.marks)
        assertEquals(1, text.marks.size)
        assertTrue(text.marks.first() is CMARichMark.CMARichMarkUnderline)
    }

    @test
    fun testHeadline1() {
        val responseBody = TestUtils.fileToString("rich_text_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))
        val array = assertTestCallback(client!!.entries().async().fetchAll(
                TestCallback()) as TestCallback)!!

        val result = array.items.first {
            it.getField<String>("name", "en-US") == "simple_headline_1"
        }


        val document = result.getField<CMARichDocument>("rich", "en-US")
        assertNotNull(document)

        val heading = document.content.first() as CMARichHeading
        assertNotNull(heading)
        assertEquals(1, heading.level)
        assertEquals(1, heading.content.size)

        val text = heading.content.first() as CMARichText
        assertNotNull(text)
        assertEquals("This is some simple text", text.value)

        assertNotNull(text.marks)
        assertEquals(0, text.marks.size)
    }

    @test
    fun testHeadline2() {
        val responseBody = TestUtils.fileToString("rich_text_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))
        val array = assertTestCallback(client!!.entries().async().fetchAll(
                TestCallback()) as TestCallback)!!

        val result = array.items.first {
            it.getField<String>("name", "en-US") == "simple_headline_2"
        }


        val document = result.getField<CMARichDocument>("rich", "en-US")
        assertNotNull(document)

        val heading = document.content.first() as CMARichHeading
        assertNotNull(heading)
        assertEquals(2, heading.level)
        assertEquals(1, heading.content.size)

        val text = heading.content.first() as CMARichText
        assertNotNull(text)
        assertEquals("This is some simple text", text.value)

        assertNotNull(text.marks)
        assertEquals(0, text.marks.size)
    }

    @test
    fun testHeadline3() {
        val responseBody = TestUtils.fileToString("rich_text_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))
        val array = assertTestCallback(client!!.entries().async().fetchAll(
                TestCallback()) as TestCallback)!!

        val result = array.items.first {
            it.getField<String>("name", "en-US") == "simple_headline_3"
        }


        val document = result.getField<CMARichDocument>("rich", "en-US")
        assertNotNull(document)

        val heading = document.content.first() as CMARichHeading
        assertNotNull(heading)
        assertEquals(3, heading.level)
        assertEquals(1, heading.content.size)

        val text = heading.content.first() as CMARichText
        assertNotNull(text)
        assertEquals("This is some simple text", text.value)

        assertNotNull(text.marks)
        assertEquals(0, text.marks.size)
    }

    @test
    fun testHeadline4() {
        val responseBody = TestUtils.fileToString("rich_text_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))
        val array = assertTestCallback(client!!.entries().async().fetchAll(
                TestCallback()) as TestCallback)!!

        val result = array.items.first {
            it.getField<String>("name", "en-US") == "simple_headline_4"
        }


        val document = result.getField<CMARichDocument>("rich", "en-US")
        assertNotNull(document)

        val heading = document.content.first() as CMARichHeading
        assertNotNull(heading)
        assertEquals(4, heading.level)
        assertEquals(1, heading.content.size)

        val text = heading.content.first() as CMARichText
        assertNotNull(text)
        assertEquals("This is some simple text", text.value)

        assertNotNull(text.marks)
        assertEquals(0, text.marks.size)
    }

    @test
    fun testHeadline5() {
        val responseBody = TestUtils.fileToString("rich_text_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))
        val array = assertTestCallback(client!!.entries().async().fetchAll(
                TestCallback()) as TestCallback)!!

        val result = array.items.first {
            it.getField<String>("name", "en-US") == "simple_headline_5"
        }


        val document = result.getField<CMARichDocument>("rich", "en-US")
        assertNotNull(document)

        val heading = document.content.first() as CMARichHeading
        assertNotNull(heading)
        assertEquals(5, heading.level)
        assertEquals(1, heading.content.size)

        val text = heading.content.first() as CMARichText
        assertNotNull(text)
        assertEquals("This is some simple text", text.value)

        assertNotNull(text.marks)
        assertEquals(0, text.marks.size)
    }

    @test
    fun testHeadline6() {
        val responseBody = TestUtils.fileToString("rich_text_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))
        val array = assertTestCallback(client!!.entries().async().fetchAll(
                TestCallback()) as TestCallback)!!

        val result = array.items.first {
            it.getField<String>("name", "en-US") == "simple_headline_6"
        }


        val document = result.getField<CMARichDocument>("rich", "en-US")
        assertNotNull(document)

        val heading = document.content.first() as CMARichHeading
        assertNotNull(heading)
        assertEquals(6, heading.level)
        assertEquals(1, heading.content.size)

        val text = heading.content.first() as CMARichText
        assertNotNull(text)
        assertEquals("This is some simple text", text.value)

        assertNotNull(text.marks)
        assertEquals(0, text.marks.size)
    }

    @test
    fun testTextEmbedded() {
        val responseBody = TestUtils.fileToString("rich_text_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))
        val array = assertTestCallback(client!!.entries().async().fetchAll(
                TestCallback()) as TestCallback)!!

        val result = array.items.first {
            it.getField<String>("name", "en-US") == "simple_text_embedded"
        }


        val document = result.getField<CMARichDocument>("rich", "en-US")
        assertNotNull(document)

        val embeddedBlock = document.content.first() as CMARichHyperLink
        assertNotNull(embeddedBlock)
        assertEquals(1, embeddedBlock.content.size)

        assertTrue(embeddedBlock.data is CMALink)
        val target = embeddedBlock.data as CMALink
        assertEquals("ybxKk5Avzqam6KymAOgIG", target.id)
        assertEquals(CMAType.Entry, target.system.linkType)
    }

    @test
    fun testBlockquote() {
        val responseBody = TestUtils.fileToString("rich_text_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))
        val array = assertTestCallback(client!!.entries().async().fetchAll(
                TestCallback()) as TestCallback)!!

        val result = array.items.first {
            it.getField<String>("name", "en-US") == "simple_blockquote"
        }


        val document = result.getField<CMARichDocument>("rich", "en-US")
        assertNotNull(document)

        val quote = document.content.first() as CMARichQuote
        assertNotNull(quote)
        assertEquals(1, quote.content.size)

        val paragraph = quote.content.first() as CMARichParagraph
        assertNotNull(paragraph)
        assertEquals(1, paragraph.content.size)

        val text = paragraph.content.first() as CMARichText
        assertNotNull(text)
        assertEquals("This is some simple blockquote", text.value)

        assertNotNull(text.marks)
        assertEquals(0, text.marks.size)
    }

    @test
    fun testTextItalic() {
        val responseBody = TestUtils.fileToString("rich_text_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))
        val array = assertTestCallback(client!!.entries().async().fetchAll(
                TestCallback()) as TestCallback)!!

        val result = array.items.first {
            it.getField<String>("name", "en-US") == "simple_text_italic"
        }


        val document = result.getField<CMARichDocument>("rich", "en-US")
        assertNotNull(document)

        val paragraph = document.content.first() as CMARichParagraph
        assertNotNull(paragraph)
        assertEquals(1, paragraph.content.size)

        val text = paragraph.content.first() as CMARichText
        assertNotNull(text)
        assertEquals("This is some simple text", text.value)

        assertNotNull(text.marks)
        assertEquals(1, text.marks.size)
        assertTrue(text.marks.first() is CMARichMark.CMARichMarkItalic)
    }

    @test
    fun testTextBold() {
        val responseBody = TestUtils.fileToString("rich_text_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))
        val array = assertTestCallback(client!!.entries().async().fetchAll(
                TestCallback()) as TestCallback)!!

        val result = array.items.first {
            it.getField<String>("name", "en-US") == "simple_text_bold"
        }


        val document = result.getField<CMARichDocument>("rich", "en-US")
        assertNotNull(document)

        val paragraph = document.content.first() as CMARichParagraph
        assertNotNull(paragraph)
        assertEquals(1, paragraph.content.size)

        val text = paragraph.content.first() as CMARichText
        assertNotNull(text)
        assertEquals("This is some simple text", text.value)

        assertNotNull(text.marks)
        assertEquals(1, text.marks.size)
        assertTrue(text.marks.first() is CMARichMark.CMARichMarkBold)
    }

    @test
    fun testTextMixedBoldItalicUnderlineCodeAll() {
        val responseBody = TestUtils.fileToString("rich_text_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))
        val array = assertTestCallback(client!!.entries().async().fetchAll(
                TestCallback()) as TestCallback)!!

        val result = array.items.first {
            it.getField<String>("name", "en-US") ==
                    "simple_text_mixed_bold_italic_underline_code_all"
        }

        val document = result.getField<CMARichDocument>("rich", "en-US")
        assertNotNull(document)

        val paragraph = document.content.first() as CMARichParagraph
        assertNotNull(paragraph)
        assertEquals(9, paragraph.content.size)

        val text = paragraph.content.last() as CMARichText
        assertNotNull(text)
        assertEquals("text", text.value)

        assertNotNull(text.marks)
        assertEquals(4, text.marks.size)
        assertTrue(text.marks.first() is CMARichMark.CMARichMarkBold)
        assertEquals(
                listOf(
                        CMARichMark.CMARichMarkBold::class.java,
                        CMARichMark.CMARichMarkItalic::class.java,
                        CMARichMark.CMARichMarkUnderline::class.java,
                        CMARichMark.CMARichMarkCode::class.java
                ),
                text.marks.map { it.javaClass }
        )
    }

    @test
    fun testUnorderedList() {
        val responseBody = TestUtils.fileToString("rich_text_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))
        val array = assertTestCallback(client!!.entries().async().fetchAll(
                TestCallback()) as TestCallback)!!

        val result = array.items.first {
            it.getField<String>("name", "en-US") == "simple_unordered_list"
        }

        val document = result.getField<CMARichDocument>("rich", "en-US")
        assertNotNull(document)

        val list = document.content.first() as CMARichUnorderedList
        assertNotNull(list)
        assertEquals(6, list.content.size)

        fun textInList(index: Int): CharSequence {
            val element = list.content[index]
            assertTrue(element is CMARichListItem, "$index is a list item")
            val item = element as CMARichListItem
            assertTrue(item.content.first() is CMARichParagraph, "$index has one child")
            val paragraph = item.content.first() as CMARichParagraph
            assertTrue(paragraph.content.first() is CMARichText, "$index[0] is text")
            val text = paragraph.content.first() as CMARichText
            return text.value
        }

        var i = 0
        assertEquals("This", textInList(i++))
        assertEquals("is", textInList(i++))
        assertEquals("some", textInList(i++))
        assertEquals("simple", textInList(i++))
        assertEquals("unordered", textInList(i++))
        assertEquals("list", textInList(i++))
    }

    @test
    fun testOrderedList() {
        val responseBody = TestUtils.fileToString("rich_text_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))
        val array = assertTestCallback(client!!.entries().async().fetchAll(
                TestCallback()) as TestCallback)!!

        val result = array.items.first {
            it.getField<String>("name", "en-US") == "simple_ordered_list"
        }

        val document = result.getField<CMARichDocument>("rich", "en-US")
        assertNotNull(document)

        val list = document.content.first() as CMARichOrderedList
        assertNotNull(list)
        assertEquals(5, list.content.size)

        fun textInList(index: Int): CharSequence {
            val element = list.content[index]
            assertTrue(element is CMARichListItem, "$index is a list item")
            val item = element as CMARichListItem
            assertTrue(item.content.first() is CMARichParagraph, "$index has one child")
            val paragraph = item.content.first() as CMARichParagraph
            assertTrue(paragraph.content.first() is CMARichText, "$index[0] is text")
            val text = paragraph.content.first() as CMARichText
            return text.value
        }

        var i = 0
        assertEquals("This ", textInList(i++))
        assertEquals("is", textInList(i++))
        assertEquals("some", textInList(i++))
        assertEquals("simple", textInList(i++))
        assertEquals("text", textInList(i++))
    }

    @test
    fun testOrderedNestedList() {
        val responseBody = TestUtils.fileToString("rich_text_get_all.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))
        val array = assertTestCallback(client!!.entries().async().fetchAll(
                TestCallback()) as TestCallback)!!

        val result = array.items.first {
            it.getField<String>("name", "en-US") == "simple_ordered_nested_list"
        }

        val document = result.getField<CMARichDocument>("rich", "en-US")
        assertNotNull(document)

        val list = document.content.first() as CMARichOrderedList
        assertNotNull(list)
        assertEquals(1, list.content.size)

        fun traverse(block: CMARichBlock): List<CharSequence> =
                if (block.content.size > 0) {
                    val found = mutableListOf<CharSequence>()
                    for (content in block.content) {
                        if (content is CMARichText) {
                            found.add(content.value)
                        } else if (content is CMARichBlock) {
                            found.addAll(traverse(content))
                        }
                    }
                    found
                } else {
                    mutableListOf()
                }

        assertEquals(listOf("This ", "is", "some", "simple", "nested", "list"), traverse(list))
    }
}