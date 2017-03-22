package com.contentful.java.cma.model;

public class CMAAssetFile {
  public static class Details {
    public static class ImageMeta {
      Long width;
      Long height;

      public Long getWidth() {
        return width;
      }

      public Long getHeight() {
        return height;
      }
    }

    Long size;
    ImageMeta imageMeta;

    public Long getSize() {
      return size;
    }

    public ImageMeta getImageMeta() {
      return imageMeta;
    }
  }

  String contentType;
  Details details;
  String url;
  String upload;
  String fileName;
  CMALink uploadFrom;

  public String getUrl() {
    return url;
  }

  public String getUploadUrl() {
    return upload;
  }

  public CMAAssetFile setUploadUrl(String upload) {
    this.upload = upload;
    return this;
  }

  /**
   * @return a link to the upload from {@link com.contentful.java.cma.ModuleUploads}
   */
  public CMALink getUploadFrom() {
    return uploadFrom;
  }

  /**
   * Set a link to a Contentful Upload entry, to be used for processing the binary data from.
   *
   * @return the calling instance for chaining.
   * @see com.contentful.java.cma.ModuleUploads
   */
  public CMAAssetFile setUploadFrom(CMALink uploadFrom) {
    this.uploadFrom = uploadFrom;
    return this;
  }

  public Details getDetails() {
    return details;
  }

  public CMAAssetFile setDetails(Details details) {
    this.details = details;
    return this;
  }

  public String getFileName() {
    return fileName;
  }

  public CMAAssetFile setFileName(String fileName) {
    this.fileName = fileName;
    return this;
  }

  public String getContentType() {
    return contentType;
  }

  public CMAAssetFile setContentType(String contentType) {
    this.contentType = contentType;
    return this;
  }
}
