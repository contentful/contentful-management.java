package com.contentful.java.cma;

import com.contentful.java.cma.model.CMAArray;
import com.contentful.java.cma.model.CMALocale;

import io.reactivex.Flowable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Service class to define the REST interface to Contentful.
 */
public interface ServiceLocales {
  @GET("spaces/{spaceId}/environments/{environmentId}/locales")
  Flowable<CMAArray<CMALocale>> fetchAll(
      @Path("spaceId") String spaceId,
      @Path("environmentId") String environmentId
  );

  @GET("spaces/{spaceId}/environments/{environmentId}/locales/{localeId}")
  Flowable<CMALocale> fetchOne(
      @Path("spaceId") String spaceId,
      @Path("environmentId") String environmentId,
      @Path("localeId") String localeId
  );

  @POST("spaces/{spaceId}/environments/{environmentId}/locales/")
  Flowable<CMALocale> create(
      @Path("spaceId") String spaceId,
      @Path("environmentId") String environmentId,
      @Body CMALocale locale
  );

  @PUT("spaces/{spaceId}/environments/{environmentId}/locales/{localeId}")
  Flowable<CMALocale> update(
      @Path("spaceId") String spaceId,
      @Path("environmentId") String environmentId,
      @Path("localeId") String localeId,
      @Body CMALocale locale,
      @Header("X-Contentful-Version") Integer version
  );

  @DELETE("spaces/{spaceId}/environments/{environmentId}/locales/{localeId}")
  Flowable<Response<Void>> delete(
      @Path("spaceId") String spaceId,
      @Path("environmentId") String environmentId,
      @Path("localeId") String localeId
  );
}
