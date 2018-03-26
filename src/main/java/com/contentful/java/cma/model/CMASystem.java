package com.contentful.java.cma.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * Record of all system properties a resource can have.
 * <p>
 * This type adds up all system properties to have one unified way of accessing them.
 */
public class CMASystem {

  CMALink contentType;
  String createdAt;
  CMALink createdBy;
  String firstPublishedAt;
  String id;
  CMAType linkType;
  String publishedAt;
  CMALink publishedBy;
  Integer publishedCounter;
  Integer publishedVersion;
  CMALink space;
  CMAType type;
  String updatedAt;
  CMALink updatedBy;
  Integer version;
  Integer archivedVersion;

  @SerializedName("status")
  CMALink environmentStatus;

  /**
   * @return the content type if this resource can have one.
   */
  public CMALink getContentType() {
    return contentType;
  }

  /**
   * Change the content type of this resource.
   *
   * @param contentType the new content type to be used.
   * @return the calling instance, for ease of chaining.
   */
  public CMASystem setContentType(CMALink contentType) {
    this.contentType = contentType;
    return this;
  }

  /**
   * @return a string representing the time this resource was created.
   */
  public String getCreatedAt() {
    return createdAt;
  }

  /**
   * @return a link to the user who created this resource initially.
   */
  public CMALink getCreatedBy() {
    return createdBy;
  }

  /**
   * @return a string representing the time this resource was first published.
   */
  public String getFirstPublishedAt() {
    return firstPublishedAt;
  }

  /**
   * @return a string representing the id of this resource.
   */
  public String getId() {
    return id;
  }

  /**
   * Update or set the id of this resource.
   *
   * @param id the new id to be used.
   * @return the instance calling this method for easy chaining.
   */
  public CMASystem setId(String id) {
    this.id = id;
    return this;
  }

  /**
   * @return what type this link links to, if any.
   */
  public CMAType getLinkType() {
    return linkType;
  }

  /**
   * Set the type of this link. Aka what object does this link link to?
   *
   * @param linkType one type to link to.
   * @return this system, to ease the chaining.
   */
  public CMASystem setLinkType(CMAType linkType) {
    this.linkType = linkType;
    return this;
  }

  /**
   * @return when was this resource last published?
   */
  public String getPublishedAt() {
    return publishedAt;
  }

  /**
   * @return a link containing the user who published this entry.
   */
  public CMALink getPublishedBy() {
    return publishedBy;
  }

  /**
   * @return the published counter state.
   */
  public Integer getPublishedCounter() {
    return publishedCounter;
  }

  /**
   * @return the published version of this resource.
   */
  public Integer getPublishedVersion() {
    return publishedVersion;
  }

  /**
   * @return the space this resource is in, if any.
   */
  public CMALink getSpace() {
    return space;
  }

  /**
   * Update the space used.
   *
   * @param space update the space of this element.
   * @return this instance to chain different setters together.
   */
  public CMASystem setSpace(CMALink space) {
    this.space = space;
    return this;
  }

  /**
   * @return the type of this ressource.
   * @see CMAType
   */
  public CMAType getType() {
    return type;
  }

  /**
   * Update this type.
   * <p>
   * This method is especially usefull for creating new resources before uploading them.
   *
   * @param type what type is this resource?
   * @return this system to make chaining setters easier.
   */
  public CMASystem setType(CMAType type) {
    this.type = type;
    return this;
  }

  /**
   * @return a time when this resource was updated last?
   */
  public String getUpdatedAt() {
    return updatedAt;
  }

  /**
   * @return a link to the user updating it last.
   */
  public CMALink getUpdatedBy() {
    return updatedBy;
  }

  /**
   * @return what version is this resource at?
   */
  public Integer getVersion() {
    return version;
  }

  /**
   * Update/set the version of this resource.
   *
   * @param version which version are we referring to?
   * @return this system to ease chaining setters.
   */
  public CMASystem setVersion(Integer version) {
    this.version = version;
    return this;
  }

  /**
   * @return the version number of the last archived version.
   */
  public Integer getArchivedVersion() {
    return archivedVersion;
  }

  /**
   * Return a link to the state of this environment
   *
   * This field is only active on an environment.
   * @return a link to the status.
   * @see CMAEnvironment#getStatus()
   */
  public CMALink getEnvironmentalStatus() {
    return environmentStatus;
  }

  /**
   * @return a human readable string, representing the object.
   */
  @Override public String toString() {
    final HashMap<String, Object> map = new HashMap<String, Object>();

    map.put("archivedVersion", getArchivedVersion());
    map.put("contentType", getContentType());
    map.put("createdAt", getCreatedAt());
    map.put("createdBy", getCreatedBy());
    map.put("firstPublishedAt", getFirstPublishedAt());
    map.put("id", getId());
    map.put("linkType", getLinkType());
    map.put("publishedAt", getPublishedAt());
    map.put("publishedBy", getPublishedBy());
    map.put("publishedCounter", getPublishedCounter());
    map.put("publishedVersion", getPublishedVersion());
    map.put("space", getSpace());
    map.put("type", getType());
    map.put("updatedAt", getUpdatedAt());
    map.put("updatedBy", getUpdatedBy());
    map.put("version", getVersion());
    map.put("status", getEnvironmentalStatus());

    final StringBuilder builder = new StringBuilder("CMASystem { ");
    String separator = "";
    for (final String key : map.keySet()) {
      final Object value = map.get(key);
      if (value != null) {
        builder
            .append(separator)
            .append(key)
            .append(" = ")
            .append(value);
        separator = ", ";
      }
    }

    builder.append(" }");
    return builder.toString();
  }
}
