package com.contentful.java.cma;

import com.contentful.java.cma.model.CMAArray;
import com.contentful.java.cma.model.CMAUsage;
import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import java.util.Map;

public interface ServiceSpaceUsage {
    @GET("/organizations/{organization_id}/space_periodic_usages")
    Flowable<CMAArray<CMAUsage>> fetchAll(
            @Path("organization_id") String organizationId
    );

    @GET("/organizations/{organization_id}/space_periodic_usages")
    Flowable<CMAArray<CMAUsage>> fetchAll(
            @Path("organization_id") String organizationId,
            @QueryMap Map<String, String> query
    );
}
