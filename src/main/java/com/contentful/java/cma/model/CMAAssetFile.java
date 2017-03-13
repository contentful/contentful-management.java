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
  String url;
  String upload;
  CMAUploadLink uploadFrom;
  Details details;
  String fileName;

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

  public CMAUploadLink getUploadFrom() {
    return uploadFrom;
  }

  public CMAAssetFile setUploadFrom(CMAUploadLink uploadFrom) {
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
