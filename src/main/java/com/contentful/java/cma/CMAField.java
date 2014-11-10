package com.contentful.java.cma;

/**
 * CMAField.
 */
public class CMAField {
  String id;
  String name;
  String type;
  String linkType;

  public CMAField setId(String id) {
    this.id = id;
    return this;
  }

  public CMAField setName(String name) {
    this.name = name;
    return this;
  }

  public CMAField setType(String type) {
    this.type = type;
    return this;
  }

  public CMAField setLinkType(String linkType) {
    this.linkType = linkType;
    return this;
  }
}
