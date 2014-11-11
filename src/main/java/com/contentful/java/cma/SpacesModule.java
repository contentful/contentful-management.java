package com.contentful.java.cma;

import retrofit.client.Response;

/**
 * Spaces Module.
 */
class SpacesModule extends AbsModule<ServiceSpaces> {
  SpacesModule(ServiceSpaces retrofitService) {
    super(retrofitService);
  }

  /**
   * Create a Space.
   *
   * @param spaceName Space name
   * @return {@link CMASpace} result instance
   */
  public CMASpace create(String spaceName) {
    assertNotNull(spaceName, "spaceName");
    return service.spacesCreate(new CMASpace().setName(spaceName));
  }

  /**
   * Create a Space in an Organization.
   *
   * @param spaceName Space name
   * @param organizationId Organization ID
   * @return {@link CMASpace} result instance
   */
  public CMASpace create(String spaceName, String organizationId) {
    assertNotNull(spaceName, "spaceName");
    assertNotNull(organizationId, "organizationId");
    return service.spacesCreate(organizationId, new CMASpace().setName(spaceName));
  }

  /**
   * Delete a Space.
   *
   * @param spaceId Space ID
   * @return Retrofit {@link Response} instance
   */
  public Response delete(String spaceId) {
    assertNotNull(spaceId, "spaceId");
    return service.spacesDelete(spaceId);
  }

  /**
   * Fetch all Spaces.
   *
   * @return {@link CMAArray} result instance
   */
  public CMAArray<CMASpace> fetchAll() {
    return service.spacesFetchAll();
  }

  /**
   * Fetch a Space with a given {@code spaceId}.
   *
   * @param spaceId Space ID
   * @return {@link CMASpace} result instance
   */
  public CMASpace fetchOne(String spaceId) {
    assertNotNull(spaceId, "spaceId");
    return service.spacesFetchOne(spaceId);
  }

  /**
   * Update a Space.
   *
   * @param space Space
   * @return {@link CMASpace} result instance
   */
  public CMASpace update(CMASpace space) {
    assertNotNull(space, "space");
    assertNotNull(space.name, "space.name");
    String spaceId = getResourceIdOrThrow(space, "space");

    CMASpace update = new CMASpace();
    update.name = space.name;
    return service.spacesUpdate(space.getVersion(), spaceId, update);
  }
}
