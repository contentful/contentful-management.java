package com.contentful.java.cma;

/**
 * CMASpace.
 */
public class CMASpace extends CMAResource {
  String name;

  public CMASpace setName(String name) {
    this.name = name;
    return this;
  }
}
