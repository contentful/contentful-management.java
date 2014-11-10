package com.contentful.java.cma;

import java.util.List;

/**
 * CMAArray.
 */
public class CMAArray<T extends CMAResource> extends CMAResource {
  List<T> items;
  int total;
  int skip;
  int limit;
}
