package com.contentful.java.cma.lib

import org.apache.commons.io.FileUtils
import java.io.File
import org.apache.commons.io.IOUtils
import com.squareup.okhttp.mockwebserver.RecordedRequest
import com.google.gson.JsonParser
import kotlin.test.assertTrue

/**
 * Created by tomxor on 05/11/14.
 */

class Utils {
    class object {
        fun fileToString(fileName: String): String =
                FileUtils.readFileToString(File("src/test/resources/${fileName}"), "UTF-8")

        fun assertJsonEquals(json1: String, json2: String) {
            val parser = JsonParser()
            assertTrue(parser.parse(json1).equals(parser.parse(json2)),
                    "Expected:\n${json1}\nActual:\n${json2}\n")
        }
    }
}

fun RecordedRequest.getBodyAsString() = IOUtils.toString(getBody(), "UTF-8")