package com.contentful.java.cma;

import com.contentful.java.cma.model.CMAArray;
import com.contentful.java.cma.model.CMATag;
import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Tags service.
 */
public interface ServiceTags {
  @GET("/spaces/{spaceId}/environments/{environmentId}/tags")
  Flowable<CMAArray<CMATag>> fetchAll(
      @Path("spaceId") String spaceId,
      @Path("environmentId") String environmentId
  );
}
