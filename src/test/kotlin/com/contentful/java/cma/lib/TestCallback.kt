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

import com.contentful.java.cma.CMACallback
import java.util.concurrent.CountDownLatch
import retrofit.RetrofitError

/**
 * TestCallback.
 */
class TestCallback<T>(val allowEmpty: Boolean = false) : CMACallback<T>() {
    val cdl: CountDownLatch
    var value: T = null
    var error: RetrofitError? = null

    {
        cdl = CountDownLatch(1)
    }

    override fun onSuccess(result: T) {
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