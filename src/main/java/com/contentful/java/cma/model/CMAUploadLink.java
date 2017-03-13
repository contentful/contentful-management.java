package com.contentful.java.cma.model;

public class CMAUploadLink extends CMAResource {
  public CMAUploadLink() {
    setSysAttribute("type", "Link");
    setSysAttribute("linkType", "Upload");
  }
}
