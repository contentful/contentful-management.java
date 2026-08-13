package com.contentful.java.cma;

import com.contentful.java.cma.model.CMAArray;
import com.contentful.java.cma.model.CMAUsage;
import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import java.util.Map;

public interface ServiceOrganizationUsage {

    /**
     * @deprecated The {@code GET /organizations/{organization_id}/organization_periodic_usages}
     * endpoint is deprecated in favor of the new Usage API. It will be removed on
     * 2027-02-28, after which requests will return 410 Gone.
     */
    @Deprecated
    @GET("organizations/{organization_id}/organization_periodic_usages")
    Flowable<CMAArray<CMAUsage>> fetchAll(
            @Path("organization_id") String organizationId
    );

    /**
     * @deprecated The {@code GET /organizations/{organization_id}/organization_periodic_usages}
     * endpoint is deprecated in favor of the new Usage API. It will be removed on
     * 2027-02-28, after which requests will return 410 Gone.
     */
    @Deprecated
    @GET("organizations/{organization_id}/organization_periodic_usages")
    Flowable<CMAArray<CMAUsage>> fetchAll(
            @Path("organization_id") String organizationId,
            @QueryMap Map<String, String> query
    );
}
