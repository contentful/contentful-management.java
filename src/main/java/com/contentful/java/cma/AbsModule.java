package com.contentful.java.cma;

/**
 * Base Module.
 */
abstract class AbsModule<T> {
  final T service;

  AbsModule(T retrofitService) {
    this.service = retrofitService;
  }

  void assertNotNull(Object object, String param) {
    if (object == null) {
      throw new IllegalArgumentException(String.format(
          "%s may not be null.", param));
    }
  }

  String getResourceIdOrThrow(CMAResource resource, String param) {
    String resourceId = resource.getResourceId();
    if (resourceId == null) {
      throw new IllegalArgumentException(String.format(
          "%s.setId() was not called.", param));
    }
    return resourceId;
  }

  String getSpaceIdOrThrow(CMAResource resource, String param) {
    String spaceId = resource.getSpaceId();
    if (spaceId == null) {
      throw new IllegalArgumentException(String.format(
          "%s must have a space associated.", param));
    }
    return spaceId;
  }
}
