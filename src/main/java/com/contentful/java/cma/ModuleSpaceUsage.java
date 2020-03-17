package com.contentful.java.cma;

import com.contentful.java.cma.model.CMAArray;
import com.contentful.java.cma.model.CMAUsage;
import retrofit2.Retrofit;

import java.util.Map;
import java.util.concurrent.Executor;

public class ModuleSpaceUsage extends AbsModule<ServiceSpaceUsage> {
    final Async async;

    /**
     * Create this module.
     *
     * @param retrofit                the retrofit instance to be used to create the service.
     * @param callbackExecutor        to tell on which thread it should run.
     * @param environmentIdConfigured internal helper to see if environment was set.
     */
    ModuleSpaceUsage(
            Retrofit retrofit,
            Executor callbackExecutor,
            String spaceId,
            String environmentId,
            boolean environmentIdConfigured) {
        super(retrofit, callbackExecutor, spaceId, environmentId, environmentIdConfigured);
        this.async = new Async();
    }

    @Override
    protected ServiceSpaceUsage createService(Retrofit retrofit) {
        return retrofit.create(ServiceSpaceUsage.class);
    }

    /**
     * Fetch all space usage the token has access to.
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
         * Fetch all space usage accessible.
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
                    return ModuleSpaceUsage.this.fetchAll(organizationId, query);
                }
            }, callback);
        }
    }
}
