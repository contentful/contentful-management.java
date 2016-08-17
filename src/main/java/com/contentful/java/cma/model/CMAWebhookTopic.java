/*
 * Copyright (C) 2016 Contentful GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.contentful.java.cma.model;

import com.google.gson.annotations.SerializedName;

/**
 * A class exporting all possible combinations of topics.
 */
@SuppressWarnings("unused")
public enum CMAWebhookTopic {
  @SerializedName("*.*")All,

  @SerializedName("Entry.*")EntryAll,
  @SerializedName("Entry.create")EntryCreate,
  @SerializedName("Entry.save")EntrySave,
  @SerializedName("Entry.autoSave")EntryAutoSave,
  @SerializedName("Entry.archive")EntryArchive,
  @SerializedName("Entry.unarchive")EntryUnarchive,
  @SerializedName("Entry.publish")EntryPublish,
  @SerializedName("Entry.unpublish")EntryUnpublish,
  @SerializedName("Entry.delete")EntryDelete,

  @SerializedName("ContentType.*")ContentTypeAll,
  @SerializedName("ContentType.create")ContentTypeCreate,
  @SerializedName("ContentType.save")ContentTypeSave,
  @SerializedName("ContentType.publish")ContentTypePublish,
  @SerializedName("ContentType.unpublish")ContentTypeUnpublish,
  @SerializedName("ContentType.delete")ContentTypeDelete,

  @SerializedName("Asset.*")AssetAll,
  @SerializedName("Asset.create")AssetCreate,
  @SerializedName("Asset.save")AssetSave,
  @SerializedName("Asset.autoSave")AssetAutoSave,
  @SerializedName("Asset.archive")AssetArchive,
  @SerializedName("Asset.unarchive")AssetUnarchive,
  @SerializedName("Asset.publish")AssetPublish,
  @SerializedName("Asset.unpublish")AssetUnpublish,
  @SerializedName("Asset.delete")AssetDelete,
}
