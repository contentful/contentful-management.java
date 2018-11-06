package com.contentful.java.cma.model.rich;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * A collection of several nodes.
 */
public class CMARichBlock extends CMARichNode {
  @NonNull final List<CMARichNode> content = new LinkedList<>();

  /**
   * Updates the old content by overwriting it with the new content.
   *
   * @param content the new content to be used.
   * @return this instance for chaining.
   */
  @SuppressWarnings("unchecked")
  @NonNull public CMARichBlock setContent(@NonNull List<CMARichNode> content) {
    if (content == null) {
      throw new NullPointerException("content is null");
    }

    this.content.clear();
    this.content.addAll(content);

    return this;
  }

  /**
   * adds content to the already existing content.
   *
   * @param content elements to be added.
   * @return this instance for chaining.
   */
  @SuppressWarnings("unchecked")
  @NonNull public CMARichBlock addContent(@NonNull CMARichNode... content) {
    if (content == null) {
      throw new NullPointerException("content is null");
    }

    this.content.addAll(Arrays.asList(content));

    return this;
  }

  /**
   * @return a changeable list of contents of this block.
   */
  @NonNull public List<CMARichNode> getContent() {
    return content;
  }
}
