/*
 * Copyright (C) 2014 Contentful GmbH
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

package com.contentful.java.cma.lib

import org.apache.commons.io.FileUtils
import java.io.File
import org.apache.commons.io.IOUtils
import com.squareup.okhttp.mockwebserver.RecordedRequest
import com.google.gson.JsonParser
import kotlin.test.assertTrue

/**
 * Utils.
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