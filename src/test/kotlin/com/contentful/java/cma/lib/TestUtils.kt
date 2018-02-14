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

package com.contentful.java.cma.lib

import org.apache.commons.io.FileUtils
import java.io.File
import java.io.InputStream
import kotlin.test.assertEquals

class TestUtils {
    companion object {
        fun fileToString(fileName: String): String =
                FileUtils.readFileToString(File("src/test/resources/$fileName"), "UTF-8")

        fun fileToBytes(fileName: String): ByteArray =
                FileUtils.readFileToByteArray(File("src/test/resources/$fileName"))

        fun fileToInputStream(fileName: String): InputStream =
                FileUtils.openInputStream(File("src/test/resources/$fileName"))
    }
}

class ModuleTestUtils {
    companion object {
        fun assertUpdateWithoutVersion(method: () -> Unit) {
            try {
                method()
            } catch (e: IllegalArgumentException) {
                val msg = "Cannot perform update action on a " +
                        "resource that has no version associated."

                assertEquals(msg, e.message)
                throw e
            }
        }
    }
}
