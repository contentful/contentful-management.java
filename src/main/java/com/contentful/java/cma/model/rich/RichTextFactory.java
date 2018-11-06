package com.contentful.java.cma.model.rich;

import com.contentful.java.cma.model.CMAEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * This factory will be used in order to create the {@see cmaRichTextNode}-graph representation of
 * the Contentful data returned by a rich text - field.
 */
@SuppressWarnings("unchecked")
public class RichTextFactory {
  private static final int HEADING_LEVEL_1 = 1;
  private static final int HEADING_LEVEL_2 = 2;
  private static final int HEADING_LEVEL_3 = 3;
  private static final int HEADING_LEVEL_4 = 4;
  private static final int HEADING_LEVEL_5 = 5;
  private static final int HEADING_LEVEL_6 = 6;

  /**
   * Interface for resolving the type of a node by its raw representation.
   */
  private interface Resolver {
    CMARichNode resolve(Map<String, Object> raw);
  }

  /**
   * Resolves a block of rich text
   *
   * @param <T> a block to be resolved.
   */
  private static class BlockResolver<T extends CMARichBlock> implements Resolver {
    final Supplier<T> supplier;

    /**
     * Create a block resolver based on its given supplier.
     *
     * @param supplier an object to create more objects of type T.
     */
    BlockResolver(Supplier<T> supplier) {
      this.supplier = supplier;
    }

    /**
     * This method is called in order to try to create  rich text block node from a raw map
     * representation.
     *
     * @param raw representation of the block node coming from Contentful.
     * @return the rich node if resolving was successful.
     */
    @Override public CMARichNode resolve(Map<String, Object> raw) {
      final T resolved = getcmaType(raw);

      final List<Map<String, Object>> contents = (List<Map<String, Object>>) raw.get("content");
      for (final Map<String, Object> rawNode : contents) {
        final CMARichNode resolvedNode = resolveRichNode(rawNode);
        if (resolvedNode != null) {
          resolved.content.add(resolvedNode);
        }
      }
      return resolved;
    }

    /**
     * Convenience method to try and find out the type of the given raw map representation.
     *
     * @param raw a map coming from Contentful, parsed from the json response.
     * @return a new node based on the type of T.
     */
    T getcmaType(Map<String, Object> raw) {
      return supplier.get();
    }
  }

  /**
   * Resolve only headings from Contentful.
   */
  private static class HeadingResolver extends BlockResolver<CMARichHeading> {
    final int level;

    /**
     * Create resolver using the given heading.
     *
     * @param level the level of the headings nesting. Should be positive and less then 7.
     */
    HeadingResolver(int level) {
      super(() -> new CMARichHeading(level));
      this.level = level;
    }
  }

  /**
   * Simple interface for providing an instance based on a type.
   *
   * @param <T> the type an instance should be created for.
   */
  private interface SupplierWithData<T> {
    /**
     * Create an object of type T.
     *
     * @param data the initialization data needed.
     * @return An instance of type T.
     */
    T get(Object data);
  }

  /**
   * Resolves a block containing more data.
   *
   * @param <T> Which type should the block be?
   */
  private static class BlockAndDataResolver<T extends CMARichBlock>
      extends BlockResolver<T> {
    final SupplierWithData<T> supplier;
    final String dataFieldKey;

    /**
     * Create the resolver.
     *
     * @param supplier     how to generate an object of type T?
     * @param dataFieldKey what other keys to be filtered?
     */
    BlockAndDataResolver(SupplierWithData<T> supplier, String dataFieldKey) {
      super(null);
      this.supplier = supplier;
      this.dataFieldKey = dataFieldKey;
    }

    /**
     * Create an object of T
     *
     * @param raw a map coming from Contentful, parsed from the json response.
     * @return an object of Type T.
     */
    @Override
    T getcmaType(Map<String, Object> raw) {
      return supplier.get(raw.get(dataFieldKey));
    }
  }

  private static final Map<String, Resolver> RESOLVER_MAP = new HashMap<>();

  static {
    // add leafs
    RESOLVER_MAP.put("text", raw -> new CMARichText(
        (CharSequence) raw.get("value"),
        resolveMarks((List<Map<String, Object>>) raw.get("marks"))
    ));
    RESOLVER_MAP.put("hr", raw -> new CMARichHorizontalRule());

    // add blocks
    RESOLVER_MAP.put("blockquote", new BlockResolver<>(CMARichQuote::new));
    RESOLVER_MAP.put("paragraph", new BlockResolver<>(CMARichParagraph::new));
    RESOLVER_MAP.put("document", new BlockResolver<>(CMARichDocument::new));
    RESOLVER_MAP.put("list-item", new BlockResolver<>(CMARichListItem::new));
    RESOLVER_MAP.put("ordered-list", new BlockResolver<>(CMARichOrderedList::new));
    RESOLVER_MAP.put("unordered-list", new BlockResolver<>(CMARichUnorderedList::new));
    RESOLVER_MAP.put("hyperlink", new BlockAndDataResolver<>(CMARichHyperLink::new, "data"));
    RESOLVER_MAP.put("embedded-entry-block",
        new BlockAndDataResolver<>(CMARichEmbeddedLink::new, "data"));
    RESOLVER_MAP.put("heading-1", new HeadingResolver(HEADING_LEVEL_1));
    RESOLVER_MAP.put("heading-2", new HeadingResolver(HEADING_LEVEL_2));
    RESOLVER_MAP.put("heading-3", new HeadingResolver(HEADING_LEVEL_3));
    RESOLVER_MAP.put("heading-4", new HeadingResolver(HEADING_LEVEL_4));
    RESOLVER_MAP.put("heading-5", new HeadingResolver(HEADING_LEVEL_5));
    RESOLVER_MAP.put("heading-6", new HeadingResolver(HEADING_LEVEL_6));
  }

  public static void resolveRichTextField(CMAEntry entry) {
    for (final Map.Entry<String, LinkedHashMap<String, Object>> field
        : entry.getFields().entrySet()) {
      final String fieldId = field.getKey();
      for (final String locale : field.getValue().keySet()) {
        final Object value = field.getValue().get(locale);
        if (value instanceof Map && ((Map) value).containsKey("nodeType")) {
          entry.setField(
              fieldId,
              locale,
              RESOLVER_MAP
                  .get("document")
                  .resolve(
                      (Map<String, Object>) value
                  )
          );
        }
      }
    }
  }

  /**
   * Specific method for resolving rich text marks.
   *
   * @param rawMarks the json responded map from Contentful
   * @return objectified and parsed objects.
   */
  static List<CMARichMark> resolveMarks(List<Map<String, Object>> rawMarks) {
    final List<CMARichMark> marks = new ArrayList<>(rawMarks.size());
    for (final Map<String, Object> rawMark : rawMarks) {
      final String type = (String) rawMark.get("type");
      if ("bold".equals(type)) {
        marks.add(new CMARichMark.CMARichMarkBold());
      } else if ("italic".equals(type)) {
        marks.add(new CMARichMark.CMARichMarkItalic());
      } else if ("underline".equals(type)) {
        marks.add(new CMARichMark.CMARichMarkUnderline());
      } else if ("code".equals(type)) {
        marks.add(new CMARichMark.CMARichMarkCode());
      } else {
        marks.add(new CMARichMark.CMARichMarkCustom(type));
      }
    }
    return marks;
  }

  /**
   * Resolve one node.
   *
   * @param rawNode the map response from Contentful
   * @return a CMARichNode from this SDK.
   */
  static CMARichNode resolveRichNode(Map<String, Object> rawNode) {
    final String type = (String) rawNode.get("nodeType");
    if (RESOLVER_MAP.containsKey(type)) {
      return RESOLVER_MAP.get(type).resolve(rawNode);
    } else {
      return null;
    }
  }
}