package com.contentful.java.cma;

import com.contentful.java.cma.model.CMAArray;
import com.contentful.java.cma.model.CMAUsage;
import retrofit2.Retrofit;

import java.util.Map;
import java.util.concurrent.Executor;

public class ModuleOrganizationUsage extends AbsModule<ServiceOrganizationUsage> {
    final Async async;

    /**
     * Create this module.
     *
     * @param retrofit                the retrofit instance to be used to create the service.
     * @param callbackExecutor        to tell on which thread it should run.
     * @param environmentIdConfigured internal helper to see if environment was set.
     */
    ModuleOrganizationUsage(
            Retrofit retrofit,
            Executor callbackExecutor,
            String spaceId,
            String environmentId,
            boolean environmentIdConfigured) {
        super(retrofit, callbackExecutor, spaceId, environmentId, environmentIdConfigured);
        this.async = new Async();
    }

    @Override
    protected ServiceOrganizationUsage createService(Retrofit retrofit) {
        return retrofit.create(ServiceOrganizationUsage.class);
    }

    /**
     * Fetch all organization usage the token has access to.
     *
     * @param query the criteria to narrow down the search result.
     * @return {@link CMAUsage} result instance
     */
    public CMAArray<CMAUsage> fetchAll(
            String organizationId,
            Map<String, String> query) {
        if (query == null) {
            return service.fetchAll(organizationId).blockingFirst();
        } else {
            return service.fetchAll(organizationId, query).blockingFirst();
        }
    }

    /**
     * @return a module with a set of asynchronous methods.
     */
    public Async async() {
        return async;
    }

    /**
     * Async module.
     */
    public class Async {
        /**
         * Fetch all organizations accessible.
         *
         * @param callback the callback to be informed about success or failure.


         * @return {@link CMAUsage} result callback.
         */
        public CMACallback<CMAArray<CMAUsage>> fetchAll(
                String organizationId,
                Map<String, String> query,
                CMACallback<CMAArray<CMAUsage>> callback) {
            return defer(new RxExtensions.DefFunc<CMAArray<CMAUsage>>() {
                @Override
                CMAArray<CMAUsage> method() {
                    return ModuleOrganizationUsage.this.fetchAll(organizationId, query);
                }
            }, callback);
        }
    }
}
