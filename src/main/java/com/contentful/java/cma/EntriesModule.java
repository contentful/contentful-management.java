package com.contentful.java.cma;

import com.contentful.java.cma.RxExtensions.DefFunc;
import java.util.HashMap;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Entries Module.
 */
class EntriesModule extends AbsModule<ServiceEntries> {
  final Async async;

  EntriesModule(ServiceEntries retrofitService) {
    super(retrofitService);
    this.async = new Async();
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
    return service.entriesArchive(spaceId, entryId);
  }

  /**
   * Create a new Entry.
   * In case the given {@code entry} has an ID associated with it, that ID will be used,
   * otherwise the server will auto-generate an ID that will be contained in the response upon
   * success.
   *
   * @param spaceId Space ID
   * @param entry Entry
   * @return {@link CMAEntry} result instance
   */
  @SuppressWarnings("unchecked") public CMAEntry create(String spaceId, CMAEntry entry) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(entry, "entry");

    String entryId = entry.getResourceId();
    HashMap sys = entry.sys;
    entry.sys = null;

    try {
      CMAEntry result;
      if (entryId == null) {
        result = service.entriesCreate(spaceId, entry);
      } else {
        result = service.entriesCreate(spaceId, entryId, entry);
      }
      entry.sys = sys;
      return result;
    } catch (RetrofitError e) {
      entry.sys = sys;
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
    return service.entriesDelete(spaceId, entryId);
  }

  /**
   * Fetch all Entries from a Space.
   *
   * @param spaceId Space ID
   * @return {@link CMAArray} result instance
   */
  public CMAArray<CMAEntry> fetchAll(String spaceId) {
    assertNotNull(spaceId, "spaceId");
    return service.entriesFetchAll(spaceId);
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
    return service.entriesFetchOne(spaceId, entryId);
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
    return service.entriesPublish(entry.getVersion(), spaceId, entryId);
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
    return service.entriesUnArchive(spaceId, entryId);
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

    CMAEntry update = new CMAEntry();
    update.fields = entry.fields;
    return service.entriesUpdate(entry.getVersion(), spaceId, entryId, update);
  }

  public Async async() {
    return async;
  }

  final class Async {
    public CMACallback<CMAEntry> archive(final CMAEntry entry, CMACallback<CMAEntry> callback) {
      return defer(new DefFunc<CMAEntry>() {
        @Override CMAEntry method() {
          return EntriesModule.this.archive(entry);
        }
      }, callback);
    }

    public CMACallback<CMAEntry> create(final String spaceId, final CMAEntry entry,
        CMACallback<CMAEntry> callback) {
      return defer(new DefFunc<CMAEntry>() {
        @Override CMAEntry method() {
          return EntriesModule.this.create(spaceId, entry);
        }
      }, callback);
    }

    public CMACallback<Response> delete(final String spaceId, final String entryId,
        CMACallback<Response> callback) {
      return defer(new DefFunc<Response>() {
        @Override Response method() {
          return EntriesModule.this.delete(spaceId, entryId);
        }
      }, callback);
    }

    public CMACallback<CMAArray<CMAEntry>> fetchAll(final String spaceId,
        CMACallback<CMAArray<CMAEntry>> callback) {
      return defer(new DefFunc<CMAArray<CMAEntry>>() {
        @Override CMAArray<CMAEntry> method() {
          return EntriesModule.this.fetchAll(spaceId);
        }
      }, callback);
    }

    public CMACallback<CMAEntry> fetchOne(final String spaceId, final String entryId,
        CMACallback<CMAEntry> callback) {
      return defer(new DefFunc<CMAEntry>() {
        @Override CMAEntry method() {
          return EntriesModule.this.fetchOne(spaceId, entryId);
        }
      }, callback);
    }

    public CMACallback<CMAEntry> publish(final CMAEntry entry, CMACallback<CMAEntry> callback) {
      return defer(new DefFunc<CMAEntry>() {
        @Override CMAEntry method() {
          return EntriesModule.this.publish(entry);
        }
      }, callback);
    }

    public CMACallback<CMAEntry> unArchive(final CMAEntry entry, CMACallback<CMAEntry> callback) {
      return defer(new DefFunc<CMAEntry>() {
        @Override CMAEntry method() {
          return EntriesModule.this.unArchive(entry);
        }
      }, callback);
    }

    public CMACallback<CMAEntry> unPublish(final CMAEntry entry, CMACallback<CMAEntry> callback) {
      return defer(new DefFunc<CMAEntry>() {
        @Override CMAEntry method() {
          return EntriesModule.this.unPublish(entry);
        }
      }, callback);
    }

    public CMACallback<CMAEntry> update(final CMAEntry entry, CMACallback<CMAEntry> callback) {
      return defer(new DefFunc<CMAEntry>() {
        @Override CMAEntry method() {
          return EntriesModule.this.update(entry);
        }
      }, callback);
    }
  }
}