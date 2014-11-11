package com.contentful.java.cma.lib

import com.contentful.java.cma.CMACallback
import java.util.concurrent.CountDownLatch
import retrofit.RetrofitError

/**
 * Created by tomxor on 11/11/14.
 */
class TestCallback<T>(val allowEmpty: Boolean = false) : CMACallback<T>() {
    val cdl: CountDownLatch
    var value: T? = null
    var error: RetrofitError? = null

    {
        cdl = CountDownLatch(1)
    }

    override fun onSuccess(result: T?) {
        value = result
        cdl.countDown()
    }

    override fun onFailure(retrofitError: RetrofitError?) {
        super<CMACallback>.onFailure(retrofitError)
        error = retrofitError
        cdl.countDown()
    }

    throws(javaClass<InterruptedException>())
    public fun await() {
        cdl.await()
    }
}