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

import com.contentful.java.cma.model.CMALocale
import org.junit.Test
import kotlin.test.assertEquals

open class LocalesE2E : Base() {
    @Test
    fun testLocales() {
        var locale = CMALocale().apply {
            code = "de-DE"
            name = "German"
            fallbackCode = "en-US"
        }

        locale = client.locales().create(locale)
        assertEquals("de-DE", locale.code)
        assertEquals("German", locale.name)
        assertEquals("en-US", locale.fallbackCode)

        locale.name = "German in Germany"
        locale = client.locales().update(locale)
        assertEquals("German in Germany", locale.name)

        assertEquals(204, client.locales().delete(locale))

        assertEquals(1, client.locales().fetchAll().total)
    }
}