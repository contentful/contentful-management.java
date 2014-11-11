package com.contentful.java.cma

import com.squareup.okhttp.mockwebserver.MockWebServer
import com.google.gson.Gson
import org.junit.Before as before
import org.junit.After as after
import com.google.gson.GsonBuilder
import com.contentful.java.cma.lib.TestCallback
import kotlin.test.assertNull
import kotlin.test.assertNotNull

/**
 * BaseTest.
 */
open class BaseTest {
    var server: MockWebServer? = null
    var client: CMAClient? = null
    var gson: Gson? = null

    before fun setUp() {
        // MockWebServer
        server = MockWebServer()
        server!!.play()

        // Client
        client = CMAClient.Builder()
                .setAccessToken("yomama")
                .setEndpoint(server!!.getUrl("/").toString())
                .build()

        gson = GsonBuilder().create()
    }

    after fun tearDown() {
        server!!.shutdown()
    }

    fun <T> assertTestCallback(cb: TestCallback<T>): T {
        cb.await()
        assertNull(cb.error)
        if (cb.allowEmpty) {
            return null
        }
        assertNotNull(cb.value)
        return cb.value!!
    }
}

// Extensions
fun <T : CMAResource> T.setSpaceId(spaceId: String): T {
    if (this.sys == null) {
        this.sys = hashMapOf()
    }

    this.sys.put("space", hashMapOf(Pair("sys", hashMapOf(Pair("id", spaceId)))))

    return this@setSpaceId
}

fun <T : CMAResource> T.setVersion(version: Double): T {
    if (this.sys == null) {
        this.sys = hashMapOf()
    }

    this.sys.put("version", version);
    return this@setVersion
}