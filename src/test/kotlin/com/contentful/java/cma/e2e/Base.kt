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
import com.contentful.java.cma.Constants
import com.contentful.java.cma.interceptor.AuthorizationHeaderInterceptor
import com.contentful.java.cma.interceptor.ContentTypeInterceptor
import com.contentful.java.cma.interceptor.ErrorInterceptor
import com.contentful.java.cma.interceptor.UserAgentHeaderInterceptor
import com.contentful.java.cma.model.CMAEnvironment
import com.contentful.java.cma.model.CMAEnvironmentStatus
import okhttp3.Call
import okhttp3.OkHttpClient
import org.junit.AfterClass
import org.junit.BeforeClass
import java.util.concurrent.TimeUnit
import kotlin.test.assertTrue
import kotlin.test.fail

open class Base {
    companion object {
        private const val ENVIRONMENT_ID = "java_e2e"
        private const val INTERMEDIATE_WAIT_TIME = 500L

        val SPACE_ID: String = System.getenv("JAVA_CMA_E2E_SPACE_ID")!!
        private val ACCESS_TOKEN: String = System.getenv("JAVA_CMA_E2E_TOKEN")!!
        private val PROXY: String? = System.getenv("JAVA_CMA_E2E_PROXY")

        lateinit var client: CMAClient
        lateinit var environment: CMAEnvironment;

        @BeforeClass
        @JvmStatic
        fun setUpSuite() {
            client = CMAClient.Builder().apply {
                if (!(PROXY.isNullOrEmpty())) {
                    setCoreEndpoint(PROXY)
                    setUploadEndpoint(PROXY)
                }
                setAccessToken(ACCESS_TOKEN)
                setEnvironmentId(ENVIRONMENT_ID)
                setSpaceId(SPACE_ID)
                setCoreCallFactory(createCallFactory())
                setUploadCallFactory(createCallFactory())
            }.build()

            environment = CMAEnvironment().setName("testing $ENVIRONMENT_ID").setId(ENVIRONMENT_ID)
            environment = client.environments().create(environment)

            var maxAttempts = 20
            var current: CMAEnvironment
            do {
                current = client.environments().fetchOne(SPACE_ID, environment.id)

                if (current.status != CMAEnvironmentStatus.Ready) {
                    try {
                        Thread.sleep(INTERMEDIATE_WAIT_TIME)
                    } catch (e: InterruptedException) {
                        fail("Could not wait for environment creation.")
                    }
                }
            } while (current.status != CMAEnvironmentStatus.Ready && --maxAttempts > 0)

            assertTrue("Environment not ready. $maxAttempts attempts left") { maxAttempts > 0 }

            environment = current
        }

        @AfterClass
        @JvmStatic
        fun tearDownSuite() {
            client.environments().delete(environment)
        }

        private fun createCallFactory(): Call.Factory? {
            val okBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
                    .addInterceptor(AuthorizationHeaderInterceptor(ACCESS_TOKEN))
                    .addInterceptor(UserAgentHeaderInterceptor("E2EUserAgent"))
                    .addInterceptor(ContentTypeInterceptor(Constants.DEFAULT_CONTENT_TYPE))
                    .addInterceptor(ErrorInterceptor())
                    .connectTimeout(2, TimeUnit.MINUTES)
                    .readTimeout(2, TimeUnit.MINUTES)
                    .writeTimeout(2, TimeUnit.MINUTES)

            return okBuilder.build()
        }
    }
}