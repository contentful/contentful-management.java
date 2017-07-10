/*
 * Copyright (C) 2017 Contentful GmbH
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

package com.contentful.java.cma;

import com.contentful.java.cma.model.CMAArray;
import com.contentful.java.cma.model.CMAOrganization;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import io.reactivex.Flowable;

/**
 * Organizations Service.
 */
interface ServiceOrganizations {
  @GET("/organizations")
  Flowable<CMAArray<CMAOrganization>> fetchAll();

  @GET("/organizations")
  Flowable<CMAArray<CMAOrganization>> fetchAll(@QueryMap Map<String, String> query);
}

