package com.contentful.java.cma.model;

/**
 * A contentful resource will be of one of those types. If a new type gets added, this enum will be
 * set to null.
 */
public enum CMAType {
  ApiKey,
  Array,
  Asset,
  ContentType,
  Entry,
  Link,
  Locale,
  Organization,
  PersonalAccessToken,
  Role,
  Space,
  SpaceMembership,
  Upload,
  User,
  Webhook,
  WebhookCallOverview,
  WebhookDefinition
}
