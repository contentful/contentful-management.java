package com.contentful.java.cma;

import com.contentful.java.cma.model.CMAArray;
import com.contentful.java.cma.model.CMARole;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Service class to define the REST interface to Contentful.
 */
public interface ServiceRoles {
  @GET("spaces/{spaceId}/roles")
  Flowable<CMAArray<CMARole>> fetchAll(@Path("spaceId") String spaceId);

  @GET("spaces/{spaceId}/roles")
  Flowable<CMAArray<CMARole>> fetchAll(
      @Path("spaceId") String spaceId,
      @QueryMap Map<String, String> query
  );

  @GET("spaces/{spaceId}/roles/{roleId}")
  Flowable<CMARole> fetchOne(
      @Path("spaceId") String spaceId,
      @Path("roleId") String roleId
  );

  @POST("spaces/{spaceId}/roles/")
  Flowable<CMARole> create(
      @Path("spaceId") String spaceId,
      @Body CMARole role
  );

  @PUT("spaces/{spaceId}/roles/{roleId}")
  Flowable<CMARole> update(
      @Path("spaceId") String spaceId,
      @Path("roleId") String roleId,
      @Body CMARole role,
      @Header("X-Contentful-Version") Integer version
  );

  @DELETE("spaces/{spaceId}/roles/{roleId}")
  Flowable<Response<Void>> delete(
      @Path("spaceId") String spaceId,
      @Path("roleId") String roleId
  );
}
