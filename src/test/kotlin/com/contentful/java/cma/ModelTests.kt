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

import com.contentful.java.cma.model.*
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import kotlin.test.assertEquals
import org.junit.Test as test

class ModelTests : BaseTest() {
    @test fun testCMAArrayToString() {
        assertEquals(
                "CMAArray { "
                        + "CMAResource { system = CMASystem { type = Array } } "
                        + "includes = null, items = null, limit = 0, skip = 0, total = 0 }",
                CMAArray<CMAEntry>().toString())
    }

    @test fun testCMAAssetToString() {
        val asset = CMAAsset()
        asset
                .fields
                .localize("en-US")
                .setDescription("description")
                .setTitle("title")

        assertEquals("CMAAsset {CMAResource { system = CMASystem { type = Asset } }"
                + "fields = CMAAsset.Fields {description = description, file = null, "
                + "title = title}}",
                asset.toString())
    }

    @test fun testCMAAssetFileToString() {
        val assetFile = CMAAssetFile()
        assetFile
                .setContentType("image/jpeg")
                .setFileName("myfile")
                .setUploadUrl("https://example.com")

        assertEquals("CMAAssetFile { "
                + "contentType = image/jpeg, details = null, fileName = myfile, "
                + "uploadUrl = https://example.com, uploadFrom = null, url = null }",
                assetFile.toString())
    }

    @test fun testCMAAssetFileDetailsToString() {
        assertEquals("Details { imageMeta = null, size = null }",
                CMAAssetFile.Details().toString())
    }

    @test fun testCMAAssetFileDetailsImageMetaToString() {
        assertEquals("ImageMeta { height = null, width = null }",
                CMAAssetFile.Details.ImageMeta().toString())
    }

    @test fun testCMAConstraintToString() {
        assertEquals("CMAConstraint { and = null, equals = null, not = null, or = null, "
                + "fieldKeyPaths = null }",
                CMAConstraint().toString())
    }

    @test fun testCMAContentTypeToString() {
        assertEquals("CMAContentType { CMAResource { system = CMASystem { "
                + "type = ContentType } } description = description, displayField = null, "
                + "fields = null, name = name }",
                CMAContentType()
                        .setDescription("description")
                        .setName("name")
                        .toString())
    }

    @test fun testCMAEntryToString() {
        val entry = CMAEntry()
        entry.localize("en-US").setField("foo", "bar")
        entry.id = "myid";

        assertEquals("CMAEntry { CMAResource { system = CMASystem { type = Entry, id = myid } } "
                + "fields = {foo={en-US=bar}} }",
                entry.toString())
    }

    @test fun testCMAFieldToString() {
        assertEquals("CMAField { arrayItems = null, disabled = false, id = null, "
                + "linkType = null, localized = false, name = null, omitted = false, "
                + "required = false, type = null, validations = null }",
                CMAField().toString())
    }

    @test fun testCMAHttpExceptionToString() {
        val request = Request.Builder().url("https://example.com").build()
        val response = Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("")
                .build()

        assertEquals("FAILED \n"
                + "\tRequest{method=GET, url=https://example.com/, tag=null}\n"
                + "\t↳ Header{}\n"
                + "\tResponse{"
                + "protocol=http/1.1, code=200, message=, url=https://example.com/}\n"
                + "\t↳ Header{}",
                CMAHttpException(request, response).toString())
    }

    @test fun testCMALinkToString() {
        assertEquals("CMALink { CMAResource { system = CMASystem { type = Link } } }",
                CMALink().toString())
    }

    @test fun testCMALocaleToString() {
        assertEquals("CMALocale { CMAResource { system = CMASystem { type = Locale } }" +
                " code = null, contentDeliveryApi = false, contentManagementApi = false," +
                " fallbackCode = null, isDefault = false, name = null, optional = false }",
                CMALocale().toString())
    }

    @test fun testCMAPermissionsToString() {
        assertEquals("CMAPermissions { contentDelivery = null, " +
                "contentModel = null, settings = null }",
                CMAPermissions().toString())
    }

    @test fun testCMAPolicyToString() {
        assertEquals("CMAPolicy { actions = null, constraint = null, effect = null }",
                CMAPolicy().toString())
    }

    @test fun testCMARoleToString() {
        assertEquals("CMARole { CMAResource { system = CMASystem "
                + "{ type = Role } } description = null, name = null, "
                + "permissions = null, policies = null }",
                CMARole().toString())
    }

    @test fun testCMASpaceToString() {
        assertEquals("CMASpace { CMAResource { system = CMASystem "
                + "{ type = Space } } defaultLocale = null, name = null }",
                CMASpace().toString())
    }

    @test fun testCMASpaceMembershipToString() {
        assertEquals("CMASpaceMembership { CMAResource { system = CMASystem"
                + " { type = SpaceMembership } } admin = false, email = null, "
                + "roles = null, user = null }",
                CMASpaceMembership().toString())
    }

    @test fun testCMASystemToString() {
        assertEquals("CMASystem {  }", CMASystem().toString())
    }

    @test fun testCMAUploadToString() {
        assertEquals("CMAUpload { CMAResource { system = CMASystem { type = Upload } } }",
                CMAUpload().toString())
    }

    @test fun testCMAUserToString() {
        assertEquals("CMAUser { CMAResource { system = CMASystem { type = User } } "
                + "activated = null, avatarUrl = null, confirmed = null, email = null, "
                + "firstName = null, lastName = null, signInCount = null }",
                CMAUser().toString())
    }

    @test fun testCMAWebhookToString() {
        assertEquals("CMAWebhook { CMAResource { system = CMASystem { "
                + "type = WebhookDefinition } } url = null, name = null, "
                + "password = null, user = null, }",
                CMAWebhook().toString())
    }

    @test fun testCMAWebhookCallToString() {
        assertEquals("CMAWebhookCall { CMAResource { system = CMASystem {"
                + " type = WebhookCallOverview } } errors = null, eventType = null, "
                + "requestAt = null, responseAt = null, statusCode = null, url = null }",
                CMAWebhookCall().toString())
    }

    @test fun testCMAWebhookCallDetailToString() {
        assertEquals("CMAWebhookCallDetail { CMAResource { system = CMASystem "
                + "{ type = WebhookCallOverview } } errors = null, eventType = null, "
                + "request = null, requestAt = null, response = null, "
                + "responseAt = null, statusCode = null, url = null }",
                CMAWebhookCallDetail().toString())
    }

    @test fun testCMAWebhookHeaderToString() {
        assertEquals("CMAWebhookHeader { key = foo, value = bar }",
                CMAWebhookHeader("foo", "bar").toString())
    }

    @test fun testCMAWebhookHealthToString() {
        assertEquals("CMAWebhookHealth { CMAResource "
                + "{ system = CMASystem { type = Webhook } } "
                + "calls = null }",
                CMAWebhookHealth().toString())
    }

    @test fun testCMAWebhookHealthCallToString() {
        assertEquals("CMAWebhookHealthCall {healthy = null, total = null}",
                CMAWebhookHealth.CMAWebhookHealthCall().toString())
    }

    @test fun testCMAWebhookRequestToString() {
        assertEquals("CMAWebhookRequest { body = null, headers = null, "
                + "method = null, url = null }",
                CMAWebhookRequest().toString())
    }

    @test fun testCMAWebhookResponseToString() {
        assertEquals("CMAWebhookResponse { body = null, "
                + "headers = null, statusCode = null, url = null }",
                CMAWebhookResponse().toString())
    }

    @test fun testOrganizationsToString() {
        assertEquals("CMAOrganizations { CMAResource { system = CMASystem { "
                + "type = Organization } } name = foo }",
                CMAOrganization().setName("foo").toString())
    }

    @test fun testApiKeyToString() {
        assertEquals("CMAApiKey { CMAResource { system = CMASystem { type = ApiKey } } "
                + "accessToken = null, description = bar, name = foo, previewApiKey = null }",
                CMAApiKey().setName("foo").setDescription("bar").toString())
    }
}