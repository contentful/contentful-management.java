package com.contentful.java.cma.model;

/**
 * This model can be used to control the appearance and usability of ui elements on Contentful.
 */
public class CMAPayload {
  CMAEntities entities;

  public CMAEntities getEntities() {
    return entities;
  }

  public CMAPayload setEntities(CMAEntities entities) {
    this.entities = entities;
    return this;
  }
}