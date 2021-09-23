package com.contentful.java.cma;

import com.contentful.java.cma.model.CMAArray;
import com.contentful.java.cma.model.CMAPreviewEnvironment;
import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import java.util.Map;

/**
 * Preview Environments service.
 */
public interface ServicePreviewEnvironments {
  @GET("/spaces/{spaceId}/preview_environments")
  Flowable<CMAArray<CMAPreviewEnvironment>> fetchAll(
      @Path("spaceId") String spaceId,
      @QueryMap Map<String, String> query
  );
}
