package com.contentful.java.cma;

import com.contentful.java.cma.model.CMAArray;
import com.contentful.java.cma.model.CMAOrganizationUsage;
import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import java.util.Map;

public interface ServiceOrganizationUsage {

    @GET("/organizations/{organization_id}/organization_periodic_usages")
    Flowable<CMAArray<CMAOrganizationUsage>> fetchAll(
            @Path("organization_id") String organizationId
    );

    @GET("/organizations/{organization_id}/organization_periodic_usages")
    Flowable<CMAArray<CMAOrganizationUsage>> fetchAll(
            @Path("organization_id") String organizationId,
            @QueryMap Map<String, String> query
    );
}
