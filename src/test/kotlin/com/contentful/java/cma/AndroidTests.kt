package com.contentful.java.cma

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import com.contentful.java.cma.lib.TestUtils
import com.contentful.java.cma.model.CMAArray
import com.contentful.java.cma.model.CMAAsset
import okhttp3.mockwebserver.MockResponse
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import org.junit.Test as test

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class AndroidTests : BaseTest() {
    @test
    fun testCallbackExecutesOnMainThread() {
        val responseBody = TestUtils.fileToString("asset_fetch_all_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val activity = Robolectric.buildActivity(TestActivity::class.java)
                .withIntent(Intent().putExtra("EXTRA_URL", server!!.url("/").toString()))
                .create()
                .get()

        while (activity.callbackLooper == null) {
            Thread.sleep(1000)
        }

        assertEquals(activity.mainThreadLooper, activity.callbackLooper)
    }

    class TestActivity : Activity() {
        val mainThreadLooper = Looper.getMainLooper()!!
        var callbackLooper: Looper? = null

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            val cb = object : CMACallback<CMAArray<CMAAsset>>() {
                override fun onSuccess(result: CMAArray<CMAAsset>?) {
                    callbackLooper = Looper.myLooper()
                }
            }

            val androidClient = CMAClient.Builder()
                    .setAccessToken("token")
                    .setCoreEndpoint(intent.getStringExtra("EXTRA_URL"))
                    .build()

            androidClient.assets().async().fetchAll("space-id", "environment-id", cb)
        }
    }
}