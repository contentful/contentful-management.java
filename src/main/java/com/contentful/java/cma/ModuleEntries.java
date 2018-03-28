/*
 * Copyright (C) 2018 Contentful GmbH
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
import com.contentful.java.cma.model.CMASnapshot;
import com.contentful.java.cma.model.CMASystem;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import retrofit2.Retrofit;

import static com.contentful.java.cma.Constants.DEFAULT_ENVIRONMENT;

/**
 * Entries Module.
 */
public final class ModuleEntries extends AbsModule<ServiceEntries> {
  final Async async;

  public ModuleEntries(Retrofit retrofit, Executor callbackExecutor) {
    super(retrofit, callbackExecutor);
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
   * Create a new Entry.
   * <p>
   * In case the given {@code entry} has an ID associated with it, that ID will be used,
   * otherwise the server will auto-generate an ID that will be contained in the response upon
   * success.
   *
   * @param spaceId       Space ID
   * @param environmentId Environment ID
   * @param contentTypeId Content Type ID
   * @param entry         Entry
   * @return {@link CMAEntry} result instance
   * @throws IllegalArgumentException if spaceId is null.
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
   * Create a new Entry in master environment.
   * <p>
   * In case the given {@code entry} has an ID associated with it, that ID will be used,
   * otherwise the server will auto-generate an ID that will be contained in the response upon
   * success.
   *
   * @param spaceId       Space ID
   * @param contentTypeId Content Type ID
   * @param entry         Entry
   * @return {@link CMAEntry} result instance
   * @throws IllegalArgumentException if spaceId is null.
   * @throws IllegalArgumentException if entry is null.
   */
  @SuppressWarnings("unchecked")
  public CMAEntry create(String spaceId, String contentTypeId, CMAEntry entry) {
    return create(spaceId, DEFAULT_ENVIRONMENT, contentTypeId, entry);
  }

  /**
   * Delete an Entry.
   *
   * @param spaceId       Space ID
   * @param environmentId Environment ID
   * @param entryId       Entry ID
   * @return Integer representing the success (204) of the action
   * @throws IllegalArgumentException if spaceId is null.
   * @throws IllegalArgumentException if entry is null.
   */
  public Integer delete(String spaceId, String environmentId, String entryId) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(environmentId, "environmentId");
    assertNotNull(entryId, "entryId");

    return service.delete(spaceId, environmentId, entryId).blockingFirst().code();
  }

  /**
   * Delete an Entry in master environment.
   *
   * @param spaceId Space ID
   * @param entryId Entry ID
   * @return Integer representing the success (204) of the action
   * @throws IllegalArgumentException if spaceId is null.
   * @throws IllegalArgumentException if entry is null.
   */
  public Integer delete(String spaceId, String entryId) {
    return delete(spaceId, DEFAULT_ENVIRONMENT, entryId);
  }

  /**
   * Delete an Entry.
   *
   * @param entry Entry ID
   * @return Integer representing the success (204) of the action
   * @throws IllegalArgumentException if spaceId is null.
   * @throws IllegalArgumentException if entry is null.
   */
  public Integer delete(CMAEntry entry) {
    assertNotNull(entry.getSpaceId(), "spaceId");
    assertNotNull(entry.getEnvironmentId(), "environmentId");
    assertNotNull(entry.getId(), "entryId");

    return delete(entry.getSpaceId(), entry.getEnvironmentId(), entry.getId());
  }

  /**
   * Fetch all Entries from a Space.
   * <p>
   * This fetch uses the default parameter defined in {@link DefaultQueryParameter#FETCH} and uses
   * the default Environment {@link Constants#DEFAULT_ENVIRONMENT}.
   *
   * @param spaceId Space ID
   * @return {@link CMAArray} result instance
   * @throws IllegalArgumentException if spaceId is null.
   */
  public CMAArray<CMAEntry> fetchAll(String spaceId) {
    return fetchAll(spaceId, DEFAULT_ENVIRONMENT, new HashMap<String, String>());
  }

  /**
   * Fetch all Entries from a Space.
   * <p>
   * This fetch uses the default parameter defined in {@link DefaultQueryParameter#FETCH} and uses
   * the default Environment {@link Constants#DEFAULT_ENVIRONMENT}.
   *
   * @param spaceId Space ID
   * @param query         Query
   * @return {@link CMAArray} result instance
   * @throws IllegalArgumentException if spaceId is null.
   */
  public CMAArray<CMAEntry> fetchAll(String spaceId, Map<String, String> query) {
    return fetchAll(spaceId, DEFAULT_ENVIRONMENT, query);
  }

  /**
   * Fetch all Entries from an Environment.
   * <p>
   * This fetch uses the default parameter defined in {@link DefaultQueryParameter#FETCH}
   *
   * @param spaceId       Space ID
   * @param environmentId Environment ID
   * @return {@link CMAArray} result instance
   * @throws IllegalArgumentException if spaceId is null.
   * @throws IllegalArgumentException if environmentId is null.
   */
  public CMAArray<CMAEntry> fetchAll(String spaceId, String environmentId) {
    return fetchAll(spaceId, environmentId, new HashMap<String, String>());
  }

  /**
   * Fetch all Entries from a Space with a query url parameter map.
   *
   * @param spaceId       Space ID
   * @param environmentId Environment ID
   * @param query         Query
   * @return {@link CMAArray} result instance
   * @throws IllegalArgumentException if spaceId is null.
   * @throws IllegalArgumentException if environmentId is null.
   */
  public CMAArray<CMAEntry> fetchAll(
      String spaceId,
      String environmentId,
      Map<String, String> query) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(environmentId, "environmentId");

    DefaultQueryParameter.putIfNotSet(query, DefaultQueryParameter.FETCH);
    return service.fetchAll(spaceId, environmentId, query).blockingFirst();
  }

  /**
   * Fetch an Entry with the given {@code entryId} from an Environment.
   *
   * @param spaceId       Space ID
   * @param environmentId Environment ID
   * @param entryId       Entry ID
   * @return {@link CMAEntry} result instance
   */
  public CMAEntry fetchOne(String spaceId, String environmentId, String entryId) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(environmentId, "environmentId");
    assertNotNull(entryId, "entryId");
    return service.fetchOne(spaceId, environmentId, entryId).blockingFirst();
  }

  /**
   * Fetch an Entry with the given {@code entryId} from a Space, using the default environment.
   *
   * @param spaceId Space ID
   * @param entryId Entry ID
   * @return {@link CMAEntry} result instance
   */
  public CMAEntry fetchOne(String spaceId, String entryId) {
    return fetchOne(spaceId, DEFAULT_ENVIRONMENT, entryId);
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
  public final class Async {
    /**
     * Archive an Entry.
     *
     * @param entry    Entry
     * @param callback Callback
     * @return the given {@code CMACallback} instance
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
     * Create a new Entry.
     * In case the given {@code entry} has an ID associated with it, that ID will be used,
     * otherwise the server will auto-generate an ID that will be contained in the response upon
     * success.
     *
     * @param spaceId       Space ID
     * @param contentTypeId Content Type ID
     * @param entry         Entry
     * @param callback      Callback
     * @return the given {@code CMACallback} instance
     * @throws IllegalArgumentException if spaceId is null.
     * @throws IllegalArgumentException if entry is null.
     */
    public CMACallback<CMAEntry> create(
        final String spaceId,
        final String contentTypeId,
        final CMAEntry entry, CMACallback<CMAEntry> callback) {
      return defer(new RxExtensions.DefFunc<CMAEntry>() {
        @Override CMAEntry method() {
          return ModuleEntries.this.create(spaceId, contentTypeId, entry);
        }
      }, callback);
    }

    /**
     * Create a new Entry in an Environment.
     * In case the given {@code entry} has an ID associated with it, that ID will be used,
     * otherwise the server will auto-generate an ID that will be contained in the response upon
     * success.
     *
     * @param spaceId       Space ID
     * @param environmentId Environment ID
     * @param contentTypeId Content Type ID
     * @param entry         Entry
     * @param callback      Callback
     * @return the given {@code CMACallback} instance
     * @throws IllegalArgumentException if spaceId is null.
     * @throws IllegalArgumentException if environmentId is null.
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
     * @param spaceId  Space ID
     * @param entryId  Entry ID
     * @param callback Callback
     * @return the given {@code CMACallback} instance
     * @throws IllegalArgumentException if spaceId is null.
     * @throws IllegalArgumentException if entry is null.
     */
    public CMACallback<Integer> delete(
        final String spaceId,
        final String entryId,
        CMACallback<Integer> callback) {
      return defer(new RxExtensions.DefFunc<Integer>() {
        @Override Integer method() {
          return ModuleEntries.this.delete(spaceId, entryId);
        }
      }, callback);
    }

    /**
     * Delete an Entry from an environment.
     *
     * @param spaceId       Space ID
     * @param environmentId Environment ID
     * @param entryId       Entry ID
     * @param callback      Callback
     * @return the given {@code CMACallback} instance
     * @throws IllegalArgumentException if spaceId is null.
     * @throws IllegalArgumentException if environmentId is null.
     * @throws IllegalArgumentException if entry is null.
     */
    public CMACallback<Integer> delete(
        final String spaceId,
        final String environmentId,
        final String entryId,
        CMACallback<Integer> callback) {
      return defer(new RxExtensions.DefFunc<Integer>() {
        @Override Integer method() {
          return ModuleEntries.this.delete(spaceId, environmentId, entryId);
        }
      }, callback);
    }

    /**
     * Delete an Entry.
     *
     * @param entry    Entry
     * @param callback Callback
     * @return the given {@code CMACallback} instance
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
     * Fetch all Entries from a Space.
     *
     * @param spaceId  Space ID
     * @param callback Callback
     * @return the given {@code CMACallback} instance
     * @throws IllegalArgumentException if spaceId is null.
     */
    public CMACallback<CMAArray<CMAEntry>> fetchAll(
        final String spaceId,
        CMACallback<CMAArray<CMAEntry>> callback) {
      return defer(new RxExtensions.DefFunc<CMAArray<CMAEntry>>() {
        @Override CMAArray<CMAEntry> method() {
          return ModuleEntries.this.fetchAll(spaceId);
        }
      }, callback);
    }

    /**
     * Fetch all Entries from a Space.
     *
     * @param spaceId  Space ID
     * @param query    query to be performed
     * @param callback Callback
     * @return the given {@code CMACallback} instance
     * @throws IllegalArgumentException if spaceId is null.
     */
    public CMACallback<CMAArray<CMAEntry>> fetchAll(
        final String spaceId,
        final Map<String, String> query,
        CMACallback<CMAArray<CMAEntry>> callback) {
      return defer(new RxExtensions.DefFunc<CMAArray<CMAEntry>>() {
        @Override CMAArray<CMAEntry> method() {
          DefaultQueryParameter.putIfNotSet(query, DefaultQueryParameter.FETCH);
          return ModuleEntries.this.fetchAll(spaceId, query);
        }
      }, callback);
    }

    /**
     * Fetch all Entries from an Environment.
     *
     * @param spaceId       Space ID
     * @param environmentId Environment ID
     * @param callback      Callback
     * @return the given {@code CMACallback} instance
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
     * @return the given {@code CMACallback} instance
     * @throws IllegalArgumentException if spaceId is null.
     */
    public CMACallback<CMAArray<CMAEntry>> fetchAll(
        final String spaceId,
        final String environmentId,
        final Map<String, String> query,
        CMACallback<CMAArray<CMAEntry>> callback) {
      return defer(new RxExtensions.DefFunc<CMAArray<CMAEntry>>() {
        @Override CMAArray<CMAEntry> method() {
          DefaultQueryParameter.putIfNotSet(query, DefaultQueryParameter.FETCH);
          return ModuleEntries.this.fetchAll(spaceId, environmentId, query);
        }
      }, callback);
    }

    /**
     * Fetch an Entry with the given {@code entryId} from a Space.
     *
     * @param spaceId  Space ID
     * @param entryId  Entry ID
     * @param callback Callback
     * @return the given {@code CMACallback} instance
     */
    public CMACallback<CMAEntry> fetchOne(
        final String spaceId,
        final String entryId,
        CMACallback<CMAEntry> callback) {
      return defer(new RxExtensions.DefFunc<CMAEntry>() {
        @Override CMAEntry method() {
          return ModuleEntries.this.fetchOne(spaceId, entryId);
        }
      }, callback);
    }

    /**
     * Fetch an Entry with the given {@code entryId} from a Space.
     *
     * @param spaceId       Space ID
     * @param environmentId Environment ID
     * @param entryId       Entry ID
     * @param callback      Callback
     * @return the given {@code CMACallback} instance
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
     * Publish an Entry.
     *
     * @param entry    Entry
     * @param callback Callback
     * @return the given {@code CMACallback} instance
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
     * @return the given {@code CMACallback} instance
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
     * @return the given {@code CMACallback} instance
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
     * @return the given {@code CMACallback} instance
     */
    public CMACallback<CMAEntry> update(final CMAEntry entry, CMACallback<CMAEntry> callback) {
      return defer(new RxExtensions.DefFunc<CMAEntry>() {
        @Override CMAEntry method() {
          return ModuleEntries.this.update(entry);
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