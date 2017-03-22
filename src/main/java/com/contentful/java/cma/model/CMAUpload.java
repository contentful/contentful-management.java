package com.contentful.java.cma.model;

/**
 * Class representing an uploaded file, to be used as an asset.
 */
public class CMAUpload extends CMAResource {
  /**
   * create an upload
   */
  public CMAUpload() {
    super(CMAType.Upload);
  }
}
