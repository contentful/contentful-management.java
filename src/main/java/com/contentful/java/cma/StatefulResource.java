package com.contentful.java.cma;

/**
 * Created by tomxor on 10/11/14.
 */
abstract class StatefulResource extends CMAResource {
  public Boolean isArchived() {
    return sys != null && sys.get("archivedVersion") != null;
  }

  public Boolean isPublished() {
    return sys != null && sys.get("publishedVersion") != null;
  }
}
