/*
 * Copyright (C) 2019 Contentful GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.contentful.java.cma;

import com.contentful.java.cma.model.CMAArray;
import com.contentful.java.cma.model.CMAEntry;
import com.contentful.java.cma.model.CMAEntryPatch;
import com.contentful.java.cma.model.CMAEntryReferences;
import com.contentful.java.cma.model.CMASnapshot;
import com.contentful.java.cma.model.CMASystem;
import com.contentful.java.cma.model.patch.CMAEntryJsonPatchItem;
import retrofit2.Retrofit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import static com.contentful.java.cma.model.patch.JsonPatchOperator.ADD;

/**
 * Entries Module.
 */
public class ModuleEntries extends AbsModule<ServiceEntries> {
  final Async async;

  /**
   * Create entries module.
   *
   * @param retrofit                the retrofit instance to be used to create the service.
   * @param callbackExecutor        to tell on which thread it should run.
   * @param spaceId                 the space to be used when not given.
   * @param environmentId           the environment to be used when not given.
   * @param environmentIdConfigured internal helper to see if environment was set.
   */
  public ModuleEntries(
      Retrofit retrofit,
      Executor callbackExecutor,
      String spaceId,
      String environmentId,
      boolean environmentIdConfigured) {
    super(retrofit, callbackExecutor, spaceId, environmentId, environmentIdConfigured);
    this.async = new Async();
  }

  @Override protected ServiceEntries createService(Retrofit retrofit) {
    return retrofit.create(ServiceEntries.class);
  }

  /**
   * Archive an Entry.
   *
   * @param entry Entry
   * @return {@link CMAEntry} result instance
   * @throws IllegalArgumentException if entry is null.
   * @throws IllegalArgumentException if entry's id is null.
   */
  public CMAEntry archive(CMAEntry entry) {
    assertNotNull(entry, "entry");

    final String entryId = getResourceIdOrThrow(entry, "entry");
    final String spaceId = getSpaceIdOrThrow(entry, "entry");
    final String environmentId = entry.getEnvironmentId();

    return service.archive(spaceId, environmentId, entryId).blockingFirst();
  }

  /**
   * Create a new Entry in the configured space and environment.
   * <p>
   * In case the given {@code entry} has an ID associated with it, that ID will be used,
   * otherwise the server will auto-generate an ID that will be contained in the response upon
   * success.
   *
   * @param contentTypeId Content Type ID
   * @param entry         Entry
   * @return {@link CMAEntry} result instance
   * @throws IllegalArgumentException if configured space id is null.
   * @throws IllegalArgumentException if configured environment id is null.
   * @throws IllegalArgumentException if contentTypeId is null.
   * @throws IllegalArgumentException if entry is null.
   * @see CMAClient.Builder#setSpaceId(String)
   * @see CMAClient.Builder#setEnvironmentId(String)
   */
  @SuppressWarnings("unchecked")
  public CMAEntry create(String contentTypeId, CMAEntry entry) {
    return create(spaceId, environmentId, contentTypeId, entry);
  }

  /**
   * Create a new Entry.
   * <p>
   * In case the given {@code entry} has an ID associated with it, that ID will be used,
   * otherwise the server will auto-generate an ID that will be contained in the response upon
   * success.
   * <p>
   * This method will override the configuration specified through
   * {@link CMAClient.Builder#setSpaceId(String)} and
   * {@link CMAClient.Builder#setEnvironmentId(String)}.
   *
   * @param spaceId       Space ID
   * @param environmentId Environment ID
   * @param contentTypeId Content Type ID
   * @param entry         Entry
   * @return {@link CMAEntry} result instance
   * @throws IllegalArgumentException if spaceId is null.
   * @throws IllegalArgumentException if environmentId is null.
   * @throws IllegalArgumentException if contentTypeId is null.
   * @throws IllegalArgumentException if entry is null.
   */
  @SuppressWarnings("unchecked")
  public CMAEntry create(
      String spaceId,
      String environmentId,
      String contentTypeId,
      CMAEntry entry) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(environmentId, "environmentId");
    assertNotNull(entry, "entry");

    final String entryId = entry.getSystem().getId();

    final CMASystem sys = entry.getSystem();
    entry.setSystem(null);

    try {
      if (entryId == null) {
        return service.create(spaceId, environmentId, contentTypeId, entry).blockingFirst();
      } else {
        return service.create(spaceId, environmentId, contentTypeId, entryId, entry)
            .blockingFirst();
      }
    } finally {
      entry.setSystem(sys);
    }
  }

  /**
   * Delete an Entry.
   *
   * @param entry Entry ID
   * @return Integer representing the success (204) of the action
   * @throws IllegalArgumentException if spaceId is null.
   * @throws IllegalArgumentException if environmentId is null.
   * @throws IllegalArgumentException if entryId is null.
   * @throws IllegalArgumentException if entry is null.
   */
  public Integer delete(CMAEntry entry) {
    assertNotNull(entry.getSpaceId(), "spaceId");
    assertNotNull(entry.getEnvironmentId(), "environmentId");
    assertNotNull(entry.getId(), "entryId");

    return service.delete(
        entry.getSpaceId(),
        entry.getEnvironmentId(),
        entry.getId()
    ).blockingFirst().code();
  }

  /**
   * Fetch all Entries from the configured space and environment.
   * <p>
   * This fetch uses the default parameter defined in {@link DefaultQueryParameter#FETCH}.
   *
   * @return {@link CMAArray} result instance
   * @throws IllegalArgumentException if configured space id is null.
   * @throws IllegalArgumentException if configured environment id is null.
   * @see CMAClient.Builder#setSpaceId(String)
   * @see CMAClient.Builder#setEnvironmentId(String)
   */
  public CMAArray<CMAEntry> fetchAll() {
    return fetchAll(spaceId, environmentId);
  }

  /**
   * Fetch all entries matching the query from the configured space and environment.
   * <p>
   * This fetch uses the default parameter defined in {@link DefaultQueryParameter#FETCH}
   *
   * @param query the criteria to filter on.
   * @return {@link CMAArray} result instance
   * @throws IllegalArgumentException if configured space id is null.
   * @throws IllegalArgumentException if configured environment id is null.
   * @see CMAClient.Builder#setSpaceId(String)
   * @see CMAClient.Builder#setEnvironmentId(String)
   */
  public CMAArray<CMAEntry> fetchAll(Map<String, String> query) {
    return fetchAll(spaceId, environmentId, query);
  }

  /**
   * Fetch all entries from the given space and environment.
   * <p>
   * This fetch uses the default parameter defined in {@link DefaultQueryParameter#FETCH}
   * <p>
   * This method will override the configuration specified through
   * {@link CMAClient.Builder#setSpaceId(String)} and
   * {@link CMAClient.Builder#setEnvironmentId(String)}.
   *
   * @param spaceId       Space ID
   * @param environmentId Environment ID
   * @return {@link CMAArray} result instance
   * @throws IllegalArgumentException if spaceId is null.
   * @throws IllegalArgumentException if environmentId is null.
   */
  public CMAArray<CMAEntry> fetchAll(String spaceId, String environmentId) {
    return fetchAll(spaceId, environmentId, new HashMap<>());
  }

  /**
   * Fetch all entries from the given space and environment matching the query.
   *
   * @param spaceId       Space ID
   * @param environmentId Environment ID
   * @param query         Query
   * @return {@link CMAArray} of entries matching the query.
   * @throws IllegalArgumentException if spaceId is null.
   * @throws IllegalArgumentException if environmentId is null.
   */
  public CMAArray<CMAEntry> fetchAll(
      String spaceId,
      String environmentId,
      Map<String, String> query) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(environmentId, "environmentId");

    Map<String, String> enhancedQuery =
      DefaultQueryParameter.putIfNotSet(query, DefaultQueryParameter.FETCH);
    return service.fetchAll(spaceId, environmentId, enhancedQuery).blockingFirst();
  }

  /**
   * Fetch an entry with the given {@code entryId} from the configured space and environment.
   *
   * @param entryId Entry ID
   * @return {@link CMAEntry} result instance
   * @throws IllegalArgumentException if configured space id is null.
   * @throws IllegalArgumentException if configured environment id is null.
   * @throws IllegalArgumentException if entry id is null.
   * @see CMAClient.Builder#setSpaceId(String)
   * @see CMAClient.Builder#setEnvironmentId(String)
   */
  public CMAEntry fetchOne(String entryId) {
    return fetchOne(spaceId, environmentId, entryId);
  }

  /**
   * Fetch an entry with the given entryId from the given environment and space.
   *
   * @param spaceId       Space ID
   * @param environmentId Environment ID
   * @param entryId       Entry ID
   * @return {@link CMAEntry} result instance
   * @throws IllegalArgumentException if space id is null.
   * @throws IllegalArgumentException if environment id is null.
   * @throws IllegalArgumentException if entry id is null.
   */
  public CMAEntry fetchOne(String spaceId, String environmentId, String entryId) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(environmentId, "environmentId");
    assertNotNull(entryId, "entryId");
    return service.fetchOne(spaceId, environmentId, entryId).blockingFirst();
  }

  /**
   * Recursively collects references of an entry and their descendants
   * from the configured space and environment.
   *
   * @param entryId       Entry ID
   * @param maxDepth      Level of the entry descendants from 1 up to 10 maximum
   * @throws IllegalArgumentException if configured space id is null.
   * @throws IllegalArgumentException if configured environment id is null.
   * @throws IllegalArgumentException if entry id is null.
   * @throws IllegalArgumentException if max depth is null or is not from 1 up to 10 maximum.
   */
  public CMAEntryReferences fetchReferences(String entryId, Integer maxDepth) {
   return fetchReferences(spaceId, environmentId, entryId, maxDepth);
  }

  /**
   * Recursively collects references of an entry and their descendants
   * from the given environment and space.
   *
   * @param spaceId       Space ID
   * @param environmentId Environment ID
   * @param entryId       Entry ID
   * @param maxDepth      Level of the entry descendants from 1 up to 10 maximum
   * @throws IllegalArgumentException if space id is null.
   * @throws IllegalArgumentException if environment id is null.
   * @throws IllegalArgumentException if entry id is null.
   * @throws IllegalArgumentException if max depth is null or is not from 1 up to 10 maximum.
   */
  public CMAEntryReferences fetchReferences(
          String spaceId,
          String environmentId,
          String entryId,
          Integer maxDepth) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(environmentId, "environmentId");
    assertNotNull(entryId, "entryId");
    assertNotNull(maxDepth, "maxDepth");
    if (maxDepth < 1 || maxDepth > 10) {
      throw new IllegalArgumentException(String.format(
              "%s may not be less than 1 or bigger than 10.", "maxDepth"));
    }

    return service.fetchReferences(spaceId, environmentId, entryId, maxDepth).blockingFirst();
  }

  /**
   * Publish an Entry.
   *
   * @param entry Entry
   * @return {@link CMAEntry} result instance
   * @throws IllegalArgumentException if entry is null.
   * @throws IllegalArgumentException if entry's id is null.
   * @throws IllegalArgumentException if entry's space id is null.
   */
  public CMAEntry publish(CMAEntry entry) {
    assertNotNull(entry, "entry");
    final String entryId = getResourceIdOrThrow(entry, "entry");
    final String environmentId = entry.getEnvironmentId();
    final String spaceId = getSpaceIdOrThrow(entry, "entry");

    return service.publish(
        entry.getSystem().getVersion(),
        spaceId,
        environmentId,
        entryId).blockingFirst();
  }

  /**
   * Un-Archive an Entry.
   *
   * @param entry Entry
   * @return {@link CMAEntry} result instance
   * @throws IllegalArgumentException if entry is null.
   * @throws IllegalArgumentException if entry's id is null.
   * @throws IllegalArgumentException if entry's space id is null.
   */
  public CMAEntry unArchive(CMAEntry entry) {
    assertNotNull(entry, "entry");
    final String entryId = getResourceIdOrThrow(entry, "entry");
    final String spaceId = getSpaceIdOrThrow(entry, "entry");
    final String environmentId = entry.getEnvironmentId();

    return service.unArchive(spaceId, environmentId, entryId).blockingFirst();
  }

  /**
   * Un-Publish an Entry.
   *
   * @param entry Entry
   * @return {@link CMAEntry} result instance
   * @throws IllegalArgumentException if entry is null.
   * @throws IllegalArgumentException if entry's id is null.
   * @throws IllegalArgumentException if entry's space id is null.
   */
  public CMAEntry unPublish(CMAEntry entry) {
    assertNotNull(entry, "entry");
    final String entryId = getResourceIdOrThrow(entry, "entry");
    final String spaceId = getSpaceIdOrThrow(entry, "entry");
    final String environmentId = entry.getEnvironmentId();

    return service.unPublish(spaceId, environmentId, entryId).blockingFirst();
  }

  /**
   * Update an Entry.
   *
   * @param entry Entry
   * @return {@link CMAEntry} result instance
   * @throws IllegalArgumentException if entry is null.
   * @throws IllegalArgumentException if entry's id is null.
   * @throws IllegalArgumentException if entry's space id is null.
   * @throws IllegalArgumentException if entry's version is null.
   */
  public CMAEntry update(CMAEntry entry) {
    assertNotNull(entry, "entry");
    final String entryId = getResourceIdOrThrow(entry, "entry");
    final String spaceId = getSpaceIdOrThrow(entry, "entry");
    final String environmentId = entry.getEnvironmentId();
    final Integer version = getVersionOrThrow(entry, "update");

    final CMASystem system = entry.getSystem();
    entry.setSystem(null);
    try {
      return service.update(version, spaceId, environmentId, entryId, entry).blockingFirst();
    } finally {
      entry.setSystem(system);
    }
  }

  /**
   * Patch an Entry.
   *
   * @param entry Entry
   * @param patch contains list of field updates
   * @return {@link CMAEntry} result instance
   * @throws IllegalArgumentException if entry is null.
   * @throws IllegalArgumentException if patch is null.
   * @throws IllegalArgumentException if entry's id is null.
   * @throws IllegalArgumentException if entry's space id is null.
   * @throws IllegalArgumentException if entry's version is null.
   */
  public CMAEntry patch(CMAEntry entry, CMAEntryPatch patch) {
    assertNotNull(entry, "entry");
    assertNotNull(patch, "fieldsPatch");
    final String entryId = getResourceIdOrThrow(entry, "entry");
    final String spaceId = getSpaceIdOrThrow(entry, "entry");
    final String environmentId = entry.getEnvironmentId();
    final Integer version = getVersionOrThrow(entry, "patch");

    List<CMAEntryJsonPatchItem> patchItems = patch.getFieldUpdates().stream()
            .map(fu -> new CMAEntryJsonPatchItem(ADD, fu.getFieldPath(), fu.getValue()))
            .collect(Collectors.toList());
    return service.patch(version, spaceId, environmentId, entryId, patchItems).blockingFirst();
  }

  /**
   * Fetch all snapshots of an entry.
   *
   * @param entry the entry whose snapshots to be returned.
   * @return an array of snapshots.
   * @throws IllegalArgumentException if entry is null.
   * @throws IllegalArgumentException if entry's id is null.
   * @throws IllegalArgumentException if entry's space id is null.
   */
  public CMAArray<CMASnapshot> fetchAllSnapshots(CMAEntry entry) {
    assertNotNull(entry, "entry");

    final String entryId = getResourceIdOrThrow(entry, "entry");
    final String spaceId = getSpaceIdOrThrow(entry, "entry");
    final String environmentId = entry.getEnvironmentId();

    return service.fetchAllSnapshots(spaceId, environmentId, entryId).blockingFirst();
  }

  /**
   * Fetch a specific snapshot of an entry.
   *
   * @param entry      the entry whose snapshot to be returned.
   * @param snapshotId the snapshot to be returned.
   * @return an array of snapshots.
   * @throws IllegalArgumentException if entry is null.
   * @throws IllegalArgumentException if entry's id is null.
   * @throws IllegalArgumentException if entry's space id is null.
   * @throws IllegalArgumentException if snapshotId is null.
   */
  public CMASnapshot fetchOneSnapshot(CMAEntry entry, String snapshotId) {
    assertNotNull(entry, "entry");
    assertNotNull(snapshotId, "snapshotId");

    final String entryId = getResourceIdOrThrow(entry, "entry");
    final String spaceId = getSpaceIdOrThrow(entry, "entry");
    final String environmentId = entry.getEnvironmentId();

    return service.fetchOneSnapshot(spaceId, environmentId, entryId, snapshotId).blockingFirst();
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
     * Archive an Entry.
     *
     * @param entry    Entry
     * @param callback Callback
     * @return the given CMACallback instance
     * @throws IllegalArgumentException if entry is null.
     * @throws IllegalArgumentException if entry's id is null.
     */
    public CMACallback<CMAEntry> archive(final CMAEntry entry, CMACallback<CMAEntry> callback) {
      return defer(new RxExtensions.DefFunc<CMAEntry>() {
        @Override CMAEntry method() {
          return ModuleEntries.this.archive(entry);
        }
      }, callback);
    }

    /**
     * Create a new Entry on the configured space and environment.
     * <p>
     * In case the given {@code entry} has an ID associated with it, that ID will be used,
     * otherwise the server will auto-generate an ID that will be contained in the response upon
     * success.
     *
     * @param contentTypeId Content Type ID
     * @param entry         Entry
     * @param callback      Callback
     * @return the given CMACallback instance
     * @throws IllegalArgumentException if configured spaceId is null.
     * @throws IllegalArgumentException if configured entry is null.
     * @throws IllegalArgumentException if contentTypeId is null.
     * @throws IllegalArgumentException if entry is null.
     * @see CMAClient.Builder#setSpaceId(String)
     * @see CMAClient.Builder#setEnvironmentId(String)
     */
    public CMACallback<CMAEntry> create(
        final String contentTypeId,
        final CMAEntry entry,
        CMACallback<CMAEntry> callback) {
      return defer(new RxExtensions.DefFunc<CMAEntry>() {
        @Override CMAEntry method() {
          return ModuleEntries.this.create(contentTypeId, entry);
        }
      }, callback);
    }

    /**
     * Create a new entry in the given space and environment.
     * <p>
     * In case the given {@code entry} has an ID associated with it, that ID will be used,
     * otherwise the server will auto-generate an ID that will be contained in the response upon
     * success.
     * <p>
     * This method will override the configuration specified through
     * {@link CMAClient.Builder#setSpaceId(String)} and
     * {@link CMAClient.Builder#setEnvironmentId(String)}.
     *
     * @param spaceId       Space ID
     * @param environmentId Environment ID
     * @param contentTypeId Content Type ID
     * @param entry         Entry
     * @param callback      Callback
     * @return the given CMACallback instance
     * @throws IllegalArgumentException if spaceId is null.
     * @throws IllegalArgumentException if environmentId is null.
     * @throws IllegalArgumentException if contentTypeId is null.
     * @throws IllegalArgumentException if entry is null.
     */
    public CMACallback<CMAEntry> create(
        final String spaceId,
        final String environmentId,
        final String contentTypeId,
        final CMAEntry entry,
        CMACallback<CMAEntry> callback) {
      return defer(new RxExtensions.DefFunc<CMAEntry>() {
        @Override CMAEntry method() {
          return ModuleEntries.this.create(spaceId, environmentId, contentTypeId, entry);
        }
      }, callback);
    }

    /**
     * Delete an Entry.
     *
     * @param entry    Entry
     * @param callback Callback
     * @return the given CMACallback instance
     * @throws IllegalArgumentException if spaceId is null.
     * @throws IllegalArgumentException if entry is null.
     */
    public CMACallback<Integer> delete(final CMAEntry entry, CMACallback<Integer> callback) {
      return defer(new RxExtensions.DefFunc<Integer>() {
        @Override Integer method() {
          return ModuleEntries.this.delete(entry);
        }
      }, callback);
    }

    /**
     * Fetch all Entries from the configured space and environment.
     *
     * @param callback Callback
     * @return the given CMACallback instance
     * @throws IllegalArgumentException if configured spaceId is null.
     * @throws IllegalArgumentException if configured environmentId is null.
     * @see CMAClient.Builder#setSpaceId(String)
     * @see CMAClient.Builder#setEnvironmentId(String)
     */
    public CMACallback<CMAArray<CMAEntry>> fetchAll(
        CMACallback<CMAArray<CMAEntry>> callback) {
      return defer(new RxExtensions.DefFunc<CMAArray<CMAEntry>>() {
        @Override CMAArray<CMAEntry> method() {
          return ModuleEntries.this.fetchAll();
        }
      }, callback);
    }

    /**
     * Fetch all entries in the space and environment matching the given query.
     *
     * @param query    query to be performed
     * @param callback Callback
     * @return the given CMACallback instance
     * @throws IllegalArgumentException if configured spaceId is null.
     * @throws IllegalArgumentException if configured environmentId is null.
     * @see CMAClient.Builder#setSpaceId(String)
     * @see CMAClient.Builder#setEnvironmentId(String)
     */
    public CMACallback<CMAArray<CMAEntry>> fetchAll(
        final Map<String, String> query,
        CMACallback<CMAArray<CMAEntry>> callback) {
      return defer(new RxExtensions.DefFunc<CMAArray<CMAEntry>>() {
        @Override CMAArray<CMAEntry> method() {
          return ModuleEntries.this.fetchAll(query);
        }
      }, callback);
    }

    /**
     * Fetch all entries from the given space and environment.
     * <p>
     * This method will override the configuration specified through
     * {@link CMAClient.Builder#setSpaceId(String)} and
     * {@link CMAClient.Builder#setEnvironmentId(String)}.
     *
     * @param spaceId       Space ID
     * @param environmentId Environment ID
     * @param callback      Callback
     * @return the given CMACallback instance
     * @throws IllegalArgumentException if spaceId is null.
     * @throws IllegalArgumentException if environmentId is null.
     */
    public CMACallback<CMAArray<CMAEntry>> fetchAll(
        final String spaceId,
        final String environmentId,
        CMACallback<CMAArray<CMAEntry>> callback) {
      return defer(new RxExtensions.DefFunc<CMAArray<CMAEntry>>() {
        @Override CMAArray<CMAEntry> method() {
          return ModuleEntries.this.fetchAll(spaceId, environmentId);
        }
      }, callback);
    }

    /**
     * Fetch all Entries from a Space with a query.
     *
     * @param spaceId       Space ID
     * @param environmentId Environment ID
     * @param query         Query
     * @param callback      Callback
     * @return the given CMACallback instance
     * @throws IllegalArgumentException if spaceId is null.
     * @throws IllegalArgumentException if environmentId is null.
     */
    public CMACallback<CMAArray<CMAEntry>> fetchAll(
        final String spaceId,
        final String environmentId,
        final Map<String, String> query,
        CMACallback<CMAArray<CMAEntry>> callback) {
      return defer(new RxExtensions.DefFunc<CMAArray<CMAEntry>>() {
        @Override CMAArray<CMAEntry> method() {
          return ModuleEntries.this.fetchAll(spaceId, environmentId, query);
        }
      }, callback);
    }

    /**
     * Fetch an Entry with the given {@code entryId} from the configured space and environment.
     *
     * @param entryId  Entry ID
     * @param callback Callback
     * @return the given CMACallback instance
     * @throws IllegalArgumentException if configured spaceId is null.
     * @throws IllegalArgumentException if configured environmentId is null.
     */
    public CMACallback<CMAEntry> fetchOne(
        final String entryId,
        CMACallback<CMAEntry> callback) {
      return defer(new RxExtensions.DefFunc<CMAEntry>() {
        @Override CMAEntry method() {
          return ModuleEntries.this.fetchOne(entryId);
        }
      }, callback);
    }

    /**
     * Fetch an Entry with the given {@code entryId} from a Space.entry
     *
     * @param spaceId       Space ID
     * @param environmentId Environment ID
     * @param entryId       Entry ID
     * @param callback      Callback
     * @return the given CMACallback instance
     */
    public CMACallback<CMAEntry> fetchOne(
        final String spaceId,
        final String environmentId,
        final String entryId,
        CMACallback<CMAEntry> callback) {
      return defer(new RxExtensions.DefFunc<CMAEntry>() {
        @Override CMAEntry method() {
          return ModuleEntries.this.fetchOne(spaceId, environmentId, entryId);
        }
      }, callback);
    }

    /**
     * Recursively collects references of an entry and their descendants
     * from the configured space and environment.
     *
     * @param entryId       Entry ID
     * @param maxDepth      Level of the entry descendants from 1 up to 10 maximum
     * @param callback      Callback
     * @return the given CMACallback instance
     */
    public CMACallback<CMAEntryReferences> fetchReferences(
            final String entryId,
            final Integer maxDepth,
            CMACallback<CMAEntryReferences> callback) {
      return defer(new RxExtensions.DefFunc<CMAEntryReferences>() {
        @Override CMAEntryReferences method() {
          return ModuleEntries.this.fetchReferences(entryId, maxDepth);
        }
      }, callback);
    }

    /**
     * Recursively collects references of an entry and their descendants
     * from the given environment and space.
     *
     * @param spaceId       Space ID
     * @param environmentId Environment ID
     * @param entryId       Entry ID
     * @param maxDepth      Level of the entry descendants from 1 up to 10 maximum
     * @param callback      Callback
     * @return the given CMACallback instance
     */
    public CMACallback<CMAEntryReferences> fetchReferences(
            final String spaceId,
            final String environmentId,
            final String entryId,
            final Integer maxDepth,
            CMACallback<CMAEntryReferences> callback) {
      return defer(new RxExtensions.DefFunc<CMAEntryReferences>() {
        @Override CMAEntryReferences method() {
          return ModuleEntries.this.fetchReferences(spaceId, environmentId, entryId, maxDepth);
        }
      }, callback);
    }

    /**
     * Publish an Entry.
     *
     * @param entry    Entry
     * @param callback Callback
     * @return the given CMACallback instance
     * @throws IllegalArgumentException if entry is null.
     * @throws IllegalArgumentException if entry's id is null.
     * @throws IllegalArgumentException if entry's space id is null.
     */
    public CMACallback<CMAEntry> publish(final CMAEntry entry, CMACallback<CMAEntry> callback) {
      return defer(new RxExtensions.DefFunc<CMAEntry>() {
        @Override CMAEntry method() {
          return ModuleEntries.this.publish(entry);
        }
      }, callback);
    }

    /**
     * Un-Archive an Entry.
     *
     * @param entry    Entry
     * @param callback Callback
     * @return the given CMACallback instance
     * @throws IllegalArgumentException if entry is null.
     * @throws IllegalArgumentException if entry's id is null.
     * @throws IllegalArgumentException if entry's space id is null.
     */
    public CMACallback<CMAEntry> unArchive(final CMAEntry entry, CMACallback<CMAEntry> callback) {
      return defer(new RxExtensions.DefFunc<CMAEntry>() {
        @Override CMAEntry method() {
          return ModuleEntries.this.unArchive(entry);
        }
      }, callback);
    }

    /**
     * Un-Publish an Entry.
     *
     * @param entry    Entry
     * @param callback Callback
     * @return the given CMACallback instance
     * @throws IllegalArgumentException if entry is null.
     * @throws IllegalArgumentException if entry's id is null.
     * @throws IllegalArgumentException if entry's space id is null.
     */
    public CMACallback<CMAEntry> unPublish(final CMAEntry entry, CMACallback<CMAEntry> callback) {
      return defer(new RxExtensions.DefFunc<CMAEntry>() {
        @Override CMAEntry method() {
          return ModuleEntries.this.unPublish(entry);
        }
      }, callback);
    }

    /**
     * Update an Entry.
     *
     * @param entry    Entry
     * @param callback Callback
     * @return the given CMACallback instance
     */
    public CMACallback<CMAEntry> update(final CMAEntry entry, CMACallback<CMAEntry> callback) {
      return defer(new RxExtensions.DefFunc<CMAEntry>() {
        @Override CMAEntry method() {
          return ModuleEntries.this.update(entry);
        }
      }, callback);
    }

    /**
     * Patch an Entry.
     *
     * @param entry    Entry
     * @param patch    contains list of field updates
     * @param callback Callback
     * @return the given CMACallback instance
     */
    public CMACallback<CMAEntry> patch(
            final CMAEntry entry,
            final CMAEntryPatch patch,
            CMACallback<CMAEntry> callback
    ) {
      return defer(new RxExtensions.DefFunc<CMAEntry>() {
        @Override CMAEntry method() {
          return ModuleEntries.this.patch(entry, patch);
        }
      }, callback);
    }


    /**
     * Fetch all snapshots of an entry.
     *
     * @param entry    the entry whose snapshots to be returned.
     * @param callback the callback to be informed about success or failure.
     * @return a callback for an array of snapshots.
     * @throws IllegalArgumentException if entry is null.
     * @throws IllegalArgumentException if entry's id is null.
     * @throws IllegalArgumentException if entry's space id is null.
     */
    public CMACallback<CMAArray<CMASnapshot>> fetchAllSnapshots(
        final CMAEntry entry,
        CMACallback<CMAArray<CMASnapshot>> callback) {
      return defer(new RxExtensions.DefFunc<CMAArray<CMASnapshot>>() {
        @Override CMAArray<CMASnapshot> method() {
          return ModuleEntries.this.fetchAllSnapshots(entry);
        }
      }, callback);
    }

    /**
     * Fetch a specific snapshot of an entry.
     *
     * @param entry      the entry whose snapshot to be returned.
     * @param snapshotId the snapshot to be returned.
     * @param callback   the callback to be informed about success or failure.
     * @return a callback for an array of snapshots.
     * @throws IllegalArgumentException if entry is null.
     * @throws IllegalArgumentException if entry's id is null.
     * @throws IllegalArgumentException if entry's space id is null.
     * @throws IllegalArgumentException if snapshotId is null.
     */
    public CMACallback<CMASnapshot> fetchOneSnapshot(
        final CMAEntry entry,
        final String snapshotId,
        CMACallback<CMASnapshot> callback) {
      return defer(new RxExtensions.DefFunc<CMASnapshot>() {
        @Override CMASnapshot method() {
          return ModuleEntries.this.fetchOneSnapshot(entry, snapshotId);
        }
      }, callback);
    }
  }
}
