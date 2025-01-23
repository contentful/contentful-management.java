package com.contentful.java.cma;

import com.contentful.java.cma.RxExtensions.DefFunc;
import com.contentful.java.cma.model.CMAArray;
import com.contentful.java.cma.model.CMAConcept;
import com.contentful.java.cma.model.CMAConceptScheme;
import com.contentful.java.cma.model.CMATotalConcepts;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import retrofit2.Retrofit;

/**
 * This module contains all the taxonomy options available to this SDK.
 */
public class ModuleTaxonomy extends AbsModule<ServiceTaxonomy> {
    private final Async async;

    public ModuleTaxonomy(Retrofit retrofit, Executor callbackExecutor,
                          String spaceId, String environmentId, boolean environmentIdConfigured) {
        super(retrofit, callbackExecutor, spaceId, environmentId, environmentIdConfigured);
        async = new Async();
    }

    @Override
    protected ServiceTaxonomy createService(Retrofit retrofit) {
        return retrofit.create(ServiceTaxonomy.class);
    }

    public Async async() {
        return async;
    }

    /** Synchronous Methods **/

    // Concept Endpoints
    public CMAConcept fetchConcept(String organizationId, String conceptId) {
        return service.fetchConcept(organizationId, conceptId).blockingFirst();
    }

    public CMAConcept updateConcept(String organizationId,
                                    String conceptId,
                                    int version,
                                    List<Map<String,
                                    Object>> operations) {
        return service.updateConcept(
                organizationId, conceptId, version, operations).blockingFirst();
    }

    public void deleteConcept(String organizationId, String conceptId) {
        service.deleteConcept(organizationId, conceptId).blockingFirst();
    }

    public CMAConcept createConcept(String organizationId, CMAConcept concept) {
        return service.createConcept(organizationId, concept).blockingFirst();
    }

    public CMAConcept createConceptWithId(
            String organizationId, String conceptId, CMAConcept concept) {
        return service.createConceptWithId(organizationId, conceptId, concept).blockingFirst();
    }

    public CMAArray<CMAConcept> fetchConcepts(String organizationId, Map<String, String> query) {
        return service.fetchConcepts(organizationId, query).blockingFirst();
    }

    public CMAArray<CMAConcept> fetchConceptDescendants(
            String organizationId, String conceptId, Map<String, String> query) {
        return service.fetchConceptDescendants(organizationId, conceptId, query).blockingFirst();
    }

    public CMAArray<CMAConcept> fetchConceptAncestors(
            String organizationId, String conceptId, Map<String, String> query) {
        return service.fetchConceptAncestors(organizationId, conceptId, query).blockingFirst();
    }

    public CMATotalConcepts fetchTotalConcepts(String organizationId) {
        return service.fetchTotalConcepts(organizationId).blockingFirst();
    }

    // Concept Scheme Endpoints
    public CMAConceptScheme fetchConceptScheme(String organizationId, String conceptSchemeId) {
        return service.fetchConceptScheme(organizationId, conceptSchemeId).blockingFirst();
    }

    public CMAConceptScheme updateConceptScheme(
            String organizationId,
            String conceptSchemeId,
            int version,
            Map<String, Object> operations) {
        return service.updateConceptScheme(
                organizationId, conceptSchemeId, version, operations).blockingFirst();
    }

    public void deleteConceptScheme(String organizationId, String conceptSchemeId) {
        service.deleteConceptScheme(organizationId, conceptSchemeId).blockingFirst();
    }

    public CMAConceptScheme createConceptScheme(
            String organizationId, CMAConceptScheme conceptScheme) {
        return service.createConceptScheme(organizationId, conceptScheme).blockingFirst();
    }

    public CMAConceptScheme createConceptSchemeWithId(
            String organizationId, String conceptSchemeId, CMAConceptScheme conceptScheme) {
        return service.createConceptSchemeWithId(
                organizationId, conceptSchemeId, conceptScheme).blockingFirst();
    }

    public CMAArray<CMAConceptScheme> fetchConceptSchemes(
            String organizationId, Map<String, String> query) {
        return service.fetchConceptSchemes(organizationId, query).blockingFirst();
    }

    public CMATotalConcepts fetchTotalConceptSchemes(String organizationId) {
        return service.fetchTotalConceptSchemes(organizationId).blockingFirst();
    }

    /** Asynchronous Methods **/

    public class Async {

        // Concept Endpoints
        public CMACallback<CMAConcept> fetchConcept(
                final String organizationId,
                final String conceptId,
                CMACallback<CMAConcept> callback) {
            return defer(new DefFunc<CMAConcept>() {
                @Override
                CMAConcept method() {
                    return ModuleTaxonomy.this.fetchConcept(organizationId, conceptId);
                }
            }, callback);
        }

        public CMACallback<CMAConcept> updateConcept(
                final String organizationId,
                final String conceptId,
                final int version,
                final List<Map<String, Object>> operations,
                CMACallback<CMAConcept> callback) {
            return defer(new DefFunc<CMAConcept>() {
                @Override
                CMAConcept method() {
                    return ModuleTaxonomy.this.updateConcept(
                            organizationId, conceptId, version, operations);
                }
            }, callback);
        }

        public CMACallback<Void> deleteConcept(
                final String organizationId, final String conceptId, CMACallback<Void> callback) {
            return defer(new DefFunc<Void>() {
                @Override
                Void method() {
                    ModuleTaxonomy.this.deleteConcept(organizationId, conceptId);
                    return null;
                }
            }, callback);
        }

        public CMACallback<CMAConcept> createConcept(
                final String organizationId,
                final CMAConcept concept,
                CMACallback<CMAConcept> callback) {
            return defer(new DefFunc<CMAConcept>() {
                @Override
                CMAConcept method() {
                    return ModuleTaxonomy.this.createConcept(organizationId, concept);
                }
            }, callback);
        }

        public CMACallback<CMAConcept> createConceptWithId(
                final String organizationId,
                final String conceptId,
                final CMAConcept concept,
                CMACallback<CMAConcept> callback) {
            return defer(new DefFunc<CMAConcept>() {
                @Override
                CMAConcept method() {
                    return ModuleTaxonomy.this.createConceptWithId(
                            organizationId, conceptId, concept);
                }
            }, callback);
        }

        public CMACallback<CMAArray<CMAConcept>> fetchConcepts(
                final String organizationId,
                final Map<String, String> query,
                CMACallback<CMAArray<CMAConcept>> callback) {
            return defer(new DefFunc<CMAArray<CMAConcept>>() {
                @Override
                CMAArray<CMAConcept> method() {
                    return ModuleTaxonomy.this.fetchConcepts(organizationId, query);
                }
            }, callback);
        }

        public CMACallback<CMAArray<CMAConcept>> fetchConceptDescendants(
                final String organizationId,
                final String conceptId,
                final Map<String, String> query,
                CMACallback<CMAArray<CMAConcept>> callback) {
            return defer(new DefFunc<CMAArray<CMAConcept>>() {
                @Override
                CMAArray<CMAConcept> method() {
                    return ModuleTaxonomy.this.fetchConceptDescendants(
                            organizationId, conceptId, query);
                }
            }, callback);
        }

        public CMACallback<CMAArray<CMAConcept>> fetchConceptAncestors(
                final String organizationId,
                final String conceptId,
                final Map<String, String> query,
                CMACallback<CMAArray<CMAConcept>> callback) {
            return defer(new DefFunc<CMAArray<CMAConcept>>() {
                @Override
                CMAArray<CMAConcept> method() {
                    return ModuleTaxonomy.this.fetchConceptAncestors(
                            organizationId, conceptId, query);
                }
            }, callback);
        }

        public CMACallback<CMATotalConcepts> fetchTotalConcepts(
                final String organizationId, CMACallback<CMATotalConcepts> callback) {
            return defer(new DefFunc<CMATotalConcepts>() {
                @Override
                CMATotalConcepts method() {
                    return ModuleTaxonomy.this.fetchTotalConcepts(organizationId);
                }
            }, callback);
        }

        // Concept Scheme Endpoints
        public CMACallback<CMAConceptScheme> fetchConceptScheme(
                final String organizationId,
                final String conceptSchemeId,
                CMACallback<CMAConceptScheme> callback) {
            return defer(new DefFunc<CMAConceptScheme>() {
                @Override
                CMAConceptScheme method() {
                    return ModuleTaxonomy.this.fetchConceptScheme(organizationId, conceptSchemeId);
                }
            }, callback);
        }

        public CMACallback<CMAConceptScheme> updateConceptScheme(
                final String organizationId,
                final String conceptSchemeId,
                final int version,
                final Map<String, Object> operations,
                CMACallback<CMAConceptScheme> callback) {
            return defer(new DefFunc<CMAConceptScheme>() {
                @Override
                CMAConceptScheme method() {
                    return ModuleTaxonomy.this.updateConceptScheme(
                            organizationId, conceptSchemeId, version, operations);
                }
            }, callback);
        }

        public CMACallback<Void> deleteConceptScheme(
                final String organizationId,
                final String conceptSchemeId,
                CMACallback<Void> callback) {
            return defer(new DefFunc<Void>() {
                @Override
                Void method() {
                    ModuleTaxonomy.this.deleteConceptScheme(organizationId, conceptSchemeId);
                    return null;
                }
            }, callback);
        }

        public CMACallback<CMAConceptScheme> createConceptScheme(
                final String organizationId,
                final CMAConceptScheme conceptScheme,
                CMACallback<CMAConceptScheme> callback) {
            return defer(new DefFunc<CMAConceptScheme>() {
                @Override
                CMAConceptScheme method() {
                    return ModuleTaxonomy.this.createConceptScheme(organizationId, conceptScheme);
                }
            }, callback);
        }

        public CMACallback<CMAConceptScheme> createConceptSchemeWithId(
                final String organizationId,
                final String conceptSchemeId,
                final CMAConceptScheme conceptScheme,
                CMACallback<CMAConceptScheme> callback) {
            return defer(new DefFunc<CMAConceptScheme>() {
                @Override
                CMAConceptScheme method() {
                    return ModuleTaxonomy.this.createConceptSchemeWithId(
                            organizationId, conceptSchemeId, conceptScheme);
                }
            }, callback);
        }

        public CMACallback<CMAArray<CMAConceptScheme>> fetchConceptSchemes(
                final String organizationId,
                final Map<String, String> query,
                CMACallback<CMAArray<CMAConceptScheme>> callback) {
            return defer(new DefFunc<CMAArray<CMAConceptScheme>>() {
                @Override
                CMAArray<CMAConceptScheme> method() {
                    return ModuleTaxonomy.this.fetchConceptSchemes(organizationId, query);
                }
            }, callback);
        }

        public CMACallback<CMATotalConcepts> fetchTotalConceptSchemes(
                final String organizationId,
                CMACallback<CMATotalConcepts> callback) {
            return defer(new DefFunc<CMATotalConcepts>() {
                @Override
                CMATotalConcepts method() {
                    return ModuleTaxonomy.this.fetchTotalConceptSchemes(organizationId);
                }
            }, callback);
        }
    }
}