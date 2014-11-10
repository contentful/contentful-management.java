package com.contentful.java.cma;

import java.util.HashMap;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Entries Module.
 */
class EntriesModule extends AbsModule {
  public EntriesModule(CMAClient client) {
    super(client);
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
    return client.service.entriesArchive(spaceId, entryId);
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
  @SuppressWarnings("unchecked")
  public CMAEntry create(String spaceId, CMAEntry entry) {
    assertNotNull(spaceId, "spaceId");
    assertNotNull(entry, "entry");

    String entryId = entry.getResourceId();
    HashMap sys = entry.sys;
    entry.sys = null;

    try {
      CMAEntry result;
      if (entryId == null) {
        result = client.service.entriesCreate(spaceId, entry);
      } else {
        result = client.service.entriesCreate(spaceId, entryId, entry);
      }
      entry.sys = sys;
      return result;
    } catch (RetrofitError e) {
      entry.sys = sys;
      throw(e);
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
    return client.service.entriesDelete(spaceId, entryId);
  }

  /**
   * Fetch all Entries from a Space.
   *
   * @param spaceId Space ID
   * @return {@link CMAArray} result instance
   */
  public CMAArray<CMAEntry> fetchAll(String spaceId) {
    assertNotNull(spaceId, "spaceId");
    return client.service.entriesFetchAll(spaceId);
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
    return client.service.entriesFetchOne(spaceId, entryId);
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
    return client.service.entriesPublish(entry.getVersion(), spaceId, entryId);
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
    return client.service.entriesUnArchive(spaceId, entryId);
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
    return client.service.entriesUnPublish(spaceId, entryId);
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
    return client.service.entriesUpdate(entry.getVersion(), spaceId, entryId, update);
  }
}