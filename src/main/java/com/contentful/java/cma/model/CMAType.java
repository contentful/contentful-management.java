package com.contentful.java.cma.model;

import com.google.gson.annotations.SerializedName;

/**
 * A Contentful resource will be of one of those types. If a new type gets added, this enum will be
 * set to null.
 */
public enum CMAType {
  ApiKey,
  Array,
  Asset,
  ContentType,
  EditorInterface,
  Entry,
  Environment,
  Link,
  Locale,
  Organization,
  PersonalAccessToken,
  PreviewApiKey,
  Role,
  Snapshot,
  Space,
  SpaceMembership,
  Upload,
  User,
  @SerializedName("Extension") UiExtension,
  Webhook,
  WebhookCallOverview,
  WebhookDefinition
}
