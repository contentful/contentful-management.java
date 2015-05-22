package com.contentful.java.cma

import org.robolectric.RobolectricTestRunner
import org.junit.runner.RunWith
import org.junit.Test as test
import android.app.Activity
import org.robolectric.Robolectric
import android.os.Bundle
import com.contentful.java.cma.lib.TestUtils
import com.squareup.okhttp.mockwebserver.MockResponse
import android.content.Intent
import android.os.Looper
import kotlin.test.assertEquals
import com.contentful.java.cma.model.CMAAsset
import com.contentful.java.cma.model.CMAArray
import retrofit.RestAdapter
import org.robolectric.annotation.Config
import java.util.concurrent.CountDownLatch
import kotlin.test.assertNotNull

[RunWith(javaClass<RobolectricTestRunner>())]
[Config(manifest=Config.NONE)]
class AndroidTests : BaseTest() {
    test fun testCallbackExecutesOnMainThread() {
        val responseBody = TestUtils.fileToString("asset_fetch_all_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val activity = Robolectric.buildActivity(javaClass<TestActivity>())
                .withIntent(Intent().putExtra("EXTRA_URL", server!!.getUrl("/").toString()))
                .create()
                .get()

        while (activity.callbackLooper == null) {
            Thread.sleep(1000)
        }

        assertEquals(activity.mainThreadLooper, activity.callbackLooper)
    }

    class TestActivity : Activity() {
        val mainThreadLooper = Looper.getMainLooper()
        var callbackLooper: Looper? = null

        override fun onCreate(savedInstanceState: Bundle?) {
            super<Activity>.onCreate(savedInstanceState)
            val cb = object : CMACallback<CMAArray<CMAAsset>>() {
                override fun onSuccess(result: CMAArray<CMAAsset>?) {
                    callbackLooper = Looper.myLooper()
                }
            }

            val androidClient = CMAClient.Builder()
                    .setAccessToken("token")
                    .setEndpoint(getIntent().getStringExtra("EXTRA_URL"))
                    .build()

            androidClient.assets().async().fetchAll("space-id", cb)
        }
    }
}