/*
 * Copyright (C) 2014 Contentful GmbH
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
import java.util.HashMap;
import java.util.concurrent.Executor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Entries Module.
 */
public final class ModuleEntries extends AbsModule<ServiceEntries> {
  final Async async;

  public ModuleEntries(RestAdapter restAdapter, Executor callbackExecutor) {
    super(restAdapter, callbackExecutor);
    this.async = new Async();
  }

  @Override protected ServiceEntries createService(RestAdapter restAdapter) {
    return restAdapter.create(ServiceEntries.class);
  }

  /**
   * Archive an Entry.
   *
   * @param entry Entry
   * @return {@link CMAEntry} result instance
   */
  public CMAEntry archive(CMAEntry entry) {
    assertNotNull(entry, "entry");
    String entryId = getResourceIdOrThrow(entry, "entry");
    String spaceId = getSpaceIdOrThrow(entry, "entry");
    return service.archive(spaceId, entryId);
  }

  /**
   * Create a new Entry.
   * In case the given {@code entry} has an ID associated with it, that ID will be used,
   * otherwise the server will auto-generate an ID that will be contained in the response upon
   * success.
   *
   * @param spaceId Space ID
   * @param contentTypeId Content Type ID
   * @param entry Entry
   * @return {@link CMAEntry} result instance
   */
  @SuppressWarnings("unchecked")
  public CMAEntry create(String spaceId, String contentTypeId, CMAEntry entry) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(entry, "entry");

    String entryId = entry.getResourceId();
    HashMap sys = entry.getSys();
    entry.setSys(null);

    try {
      CMAEntry result;
      if (entryId == null) {
        result = service.create(spaceId, contentTypeId, entry);
      } else {
        result = service.create(spaceId, contentTypeId, entryId, entry);
      }
      entry.setSys(sys);
      return result;
    } catch (RetrofitError e) {
      entry.setSys(sys);
      throw (e);
    }
  }

  /**
   * Delete an Entry.
   *
   * @param spaceId Space ID
   * @param entryId Entry ID
   * @return Retrofit {@link Response} instance
   */
  public Response delete(String spaceId, String entryId) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(entryId, "entryId");
    return service.delete(spaceId, entryId);
  }

  /**
   * Fetch all Entries from a Space.
   *
   * @param spaceId Space ID
   * @return {@link CMAArray} result instance
   */
  public CMAArray<CMAEntry> fetchAll(String spaceId) {
    assertNotNull(spaceId, "spaceId");
    return service.fetchAll(spaceId);
  }

  /**
   * Fetch an Entry with the given {@code entryId} from a Space.
   *
   * @param spaceId Space ID
   * @param entryId Entry ID
   * @return {@link CMAEntry} result instance
   */
  public CMAEntry fetchOne(String spaceId, String entryId) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(entryId, "entryId");
    return service.fetchOne(spaceId, entryId);
  }

  /**
   * Publish an Entry.
   *
   * @param entry Entry
   * @return {@link CMAEntry} result instance
   */
  public CMAEntry publish(CMAEntry entry) {
    assertNotNull(entry, "entry");
    String entryId = getResourceIdOrThrow(entry, "entry");
    String spaceId = getSpaceIdOrThrow(entry, "entry");
    return service.publish(entry.getVersion(), spaceId, entryId);
  }

  /**
   * Un-Archive an Entry.
   *
   * @param entry Entry
   * @return {@link CMAEntry} result instance
   */
  public CMAEntry unArchive(CMAEntry entry) {
    assertNotNull(entry, "entry");
    String entryId = getResourceIdOrThrow(entry, "entry");
    String spaceId = getSpaceIdOrThrow(entry, "entry");
    return service.unArchive(spaceId, entryId);
  }

  /**
   * Un-Publish an Entry.
   *
   * @param entry Entry
   * @return {@link CMAEntry} result instance
   */
  public CMAEntry unPublish(CMAEntry entry) {
    assertNotNull(entry, "entry");
    String entryId = getResourceIdOrThrow(entry, "entry");
    String spaceId = getSpaceIdOrThrow(entry, "entry");
    return service.entriesUnPublish(spaceId, entryId);
  }

  /**
   * Update an Entry.
   *
   * @param entry Entry
   * @return {@link CMAEntry} result instance
   */
  public CMAEntry update(CMAEntry entry) {
    assertNotNull(entry, "entry");
    String entryId = getResourceIdOrThrow(entry, "entry");
    String spaceId = getSpaceIdOrThrow(entry, "entry");
    Integer version = getVersionOrThrow(entry, "update");

    CMAEntry update = new CMAEntry();
    update.setFields(entry.getFields());
    return service.update(version, spaceId, entryId, update);
  }

  /**
   * Returns a module with a set of asynchronous methods.
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
     * @param entry Entry
     * @param callback Callback
     * @return the given {@code CMACallback} instance
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
     * @param spaceId Space ID
     * @param contentTypeId Content Type ID
     * @param entry Entry
     * @param callback Callback
     * @return the given {@code CMACallback} instance
     */
    public CMACallback<CMAEntry> create(final String spaceId, final String contentTypeId,
        final CMAEntry entry, CMACallback<CMAEntry> callback) {
      return defer(new RxExtensions.DefFunc<CMAEntry>() {
        @Override CMAEntry method() {
          return ModuleEntries.this.create(spaceId, contentTypeId, entry);
        }
      }, callback);
    }

    /**
     * Delete an Entry.
     *
     * @param spaceId Space ID
     * @param entryId Entry ID
     * @param callback Callback
     * @return the given {@code CMACallback} instance
     */
    public CMACallback<Response> delete(final String spaceId, final String entryId,
        CMACallback<Response> callback) {
      return defer(new RxExtensions.DefFunc<Response>() {
        @Override Response method() {
          return ModuleEntries.this.delete(spaceId, entryId);
        }
      }, callback);
    }

    /**
     * Fetch all Entries from a Space.
     *
     * @param spaceId Space ID
     * @param callback Callback
     * @return the given {@code CMACallback} instance
     */
    public CMACallback<CMAArray<CMAEntry>> fetchAll(final String spaceId,
        CMACallback<CMAArray<CMAEntry>> callback) {
      return defer(new RxExtensions.DefFunc<CMAArray<CMAEntry>>() {
        @Override CMAArray<CMAEntry> method() {
          return ModuleEntries.this.fetchAll(spaceId);
        }
      }, callback);
    }

    /**
     * Fetch an Entry with the given {@code entryId} from a Space.
     *
     * @param spaceId Space ID
     * @param entryId Entry ID
     * @param callback Callback
     * @return the given {@code CMACallback} instance
     */
    public CMACallback<CMAEntry> fetchOne(final String spaceId, final String entryId,
        CMACallback<CMAEntry> callback) {
      return defer(new RxExtensions.DefFunc<CMAEntry>() {
        @Override CMAEntry method() {
          return ModuleEntries.this.fetchOne(spaceId, entryId);
        }
      }, callback);
    }

    /**
     * Publish an Entry.
     *
     * @param entry Entry
     * @param callback Callback
     * @return the given {@code CMACallback} instance
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
     * @param entry Entry
     * @param callback Callback
     * @return the given {@code CMACallback} instance
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
     * @param entry Entry
     * @param callback Callback
     * @return the given {@code CMACallback} instance
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
     * @param entry Entry
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
  }
}