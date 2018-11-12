package com.contentful.java.cma.model.rich;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * A leaf element of the rich text node graph: Render a given text with the given markings.
 */
public class CMARichText extends CMARichNode {
  @NonNull
  private List<CMARichMark> marks = new ArrayList<>();
  @NonNull
  private CharSequence value;

  /**
   * Create a value with the given marks.
   *
   * @param value the value to be displayed.
   * @param marks the marks to be used if any.
   * @throws NullPointerException if one of the arguments is null.
   */
  public CMARichText(@NonNull CharSequence value, @NonNull List<CMARichMark> marks) {
    super("text");

    if (value == null) {
      throw new NullPointerException("value was null");
    }
    if (marks == null) {
      throw new NullPointerException("marks is null");
    }

    this.marks.addAll(marks);
    this.value = value;
  }

  /**
   * Create a text with the given marks.
   *
   * @param value the text to be displayed.
   * @throws NullPointerException if value is null.
   */
  public CMARichText(@NonNull String value) {
    this(value, Collections.emptyList());
  }

  /**
   * @return the value text of this node.
   */
  @NonNull public CharSequence getValue() {
    return value;
  }

  /**
   * Update the value of this text.
   *
   * @return this instance for chaining.
   */
  @NonNull public CMARichText setValue(@NonNull CharSequence value) {
    if (value == null) {
      throw new NullPointerException("value is null.");
    }
    this.value = value;
    return this;
  }

  /**
   * @return the currently setup marks of this text.
   */
  @NonNull public List<CMARichMark> getMarks() {
    return marks;
  }

  /**
   * Adds the given marks to this texts marks.
   *
   * @return this instance for chaining.
   * @throws NullPointerException if marks is null.
   */
  @NonNull public CMARichText addMarks(@NonNull CMARichMark... marks) {
    this.marks.addAll(Arrays.asList(marks));
    return this;
  }

  /**
   * Updates the marks of this text.
   *
   * @return this instance for chaining.
   * @throws NullPointerException if marks is null.
   */
  @NonNull public CMARichText setMarks(@NonNull List<CMARichMark> marks) {
    this.marks.clear();
    this.marks.addAll(marks);
    return this;
  }
}
