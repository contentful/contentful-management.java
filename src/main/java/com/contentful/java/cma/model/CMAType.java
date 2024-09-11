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
  BulkAction,
  ContentType,
  EditorInterface,
  Entry,
  Error,
  Environment,
  Link,
  Locale,
  Organization,
  OrganizationPeriodicUsage,
  SpacePeriodicUsage,
  PersonalAccessToken,
  PreviewApiKey,
  Role,
  ScheduledAction,
  Snapshot,
  Space,
  SpaceMembership,
  Tag,
  Upload,
  User,
  @SerializedName("Extension") UiExtension,
  Webhook,
  WebhookCallOverview,
  WebhookDefinition;
}
