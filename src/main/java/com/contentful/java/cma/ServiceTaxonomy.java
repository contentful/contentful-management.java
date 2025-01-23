package com.contentful.java.cma;

import com.contentful.java.cma.model.CMAArray;
import com.contentful.java.cma.model.CMAConcept;
import com.contentful.java.cma.model.CMAConceptScheme;
import com.contentful.java.cma.model.CMATotalConcepts;
import io.reactivex.Flowable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;


import java.util.List;
import java.util.Map;

/**
 * Service class to define the REST interface for taxonomy-related operations.
 */
public interface ServiceTaxonomy {

    /** Concept Endpoints **/

    @GET("organizations/{organizationId}/taxonomy/concepts/{id}")
    Flowable<CMAConcept> fetchConcept(@Path("organizationId") String organizationId,
                                      @Path("id") String conceptId);

    @PATCH("organizations/{organizationId}/taxonomy/concepts/{id}")
    @Headers("Content-Type: application/json-patch+json")
    Flowable<CMAConcept> updateConcept(
            @Path("organizationId") String organizationId,
            @Path("id") String conceptId,
            @Header("X-Contentful-Version") int version,
            @Body List<Map<String, Object>> operations);

    @DELETE("organizations/{organizationId}/taxonomy/concepts/{id}")
    Flowable<Response<Void>> deleteConcept(
            @Path("organizationId") String organizationId, @Path("id") String conceptId);

    @POST("organizations/{organizationId}/taxonomy/concepts")
    Flowable<CMAConcept> createConcept(@Path("organizationId") String organizationId,
                                       @Body CMAConcept concept);

    @PUT("organizations/{organizationId}/taxonomy/concepts/{conceptId}")
    Flowable<CMAConcept> createConceptWithId(
            @Path("organizationId") String organizationId,
            @Path("conceptId") String conceptId,
            @Body CMAConcept concept);

    @GET("organizations/{organizationId}/taxonomy/concepts")
    Flowable<CMAArray<CMAConcept>> fetchConcepts(
            @Path("organizationId") String organizationId, @QueryMap Map<String, String> query);

    @GET("organizations/{organizationId}/taxonomy/concepts/{id}/descendants")
    Flowable<CMAArray<CMAConcept>> fetchConceptDescendants(
            @Path("organizationId") String organizationId,
            @Path("id") String conceptId,
            @QueryMap Map<String, String> query);

    @GET("organizations/{organizationId}/taxonomy/concepts/{id}/ancestors")
    Flowable<CMAArray<CMAConcept>> fetchConceptAncestors(
            @Path("organizationId") String organizationId,
            @Path("id") String conceptId,
            @QueryMap Map<String, String> query);

    @GET("organizations/{organizationId}/taxonomy/concepts/total")
    Flowable<CMATotalConcepts> fetchTotalConcepts(@Path("organizationId") String organizationId);

    /** Concept Scheme Endpoints **/

    @GET("organizations/{organizationId}/taxonomy/concept-schemes/{conceptSchemeId}")
    Flowable<CMAConceptScheme> fetchConceptScheme(
            @Path("organizationId") String organizationId,
            @Path("conceptSchemeId") String conceptSchemeId);

    @PATCH("organizations/{organizationId}/taxonomy/concept-schemes/{conceptSchemeId}")
    Flowable<CMAConceptScheme> updateConceptScheme(
            @Path("organizationId") String organizationId,
            @Path("conceptSchemeId") String conceptSchemeId,
            @Header("X-Contentful-Version") int version,
            @Body Map<String, Object> operations);

    @DELETE("organizations/{organizationId}/taxonomy/concept-schemes/{conceptSchemeId}")
    Flowable<Response<Void>> deleteConceptScheme(
            @Path("organizationId") String organizationId,
            @Path("conceptSchemeId") String conceptSchemeId);

    @POST("organizations/{organizationId}/taxonomy/concept-schemes")
    Flowable<CMAConceptScheme> createConceptScheme(
            @Path("organizationId") String organizationId, @Body CMAConceptScheme conceptScheme);

    @PUT("organizations/{organizationId}/taxonomy/concept-schemes/{conceptSchemeId}")
    Flowable<CMAConceptScheme> createConceptSchemeWithId(
            @Path("organizationId") String organizationId,
            @Path("conceptSchemeId") String conceptSchemeId,
            @Body CMAConceptScheme conceptScheme);

    @GET("organizations/{organizationId}/taxonomy/concept-schemes")
    Flowable<CMAArray<CMAConceptScheme>> fetchConceptSchemes(
            @Path("organizationId") String organizationId, @QueryMap Map<String, String> query);

    @GET("organizations/{organizationId}/taxonomy/concepts-schemes/total")
    Flowable<CMATotalConcepts> fetchTotalConceptSchemes(
            @Path("organizationId") String organizationId);
}
