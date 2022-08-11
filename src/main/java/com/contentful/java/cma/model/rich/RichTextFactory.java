package com.contentful.java.cma.model.rich;

import com.contentful.java.cma.model.CMAEntry;
import com.contentful.java.cma.model.CMALink;
import com.contentful.java.cma.model.CMAType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This factory will be used in order to create the {@link CMARichNode}-graph representation of
 * the Contentful data returned by a rich text - field.
 */
@SuppressWarnings("unchecked")
public class RichTextFactory {
  private static final int LEVEL_1 = 1;
  private static final int LEVEL_2 = 2;
  private static final int LEVEL_3 = 3;
  private static final int LEVEL_4 = 4;
  private static final int LEVEL_5 = 5;
  private static final int LEVEL_6 = 6;

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
    final SupplierWithData<T> supplier;
    final String dataFieldKey;

    /**
     * Create a block resolver based on its given supplier and data.
     *
     * @param supplier an object to create more objects of type T.
     * @param dataFieldKey field name with additional data
     */
    BlockResolver(SupplierWithData<T> supplier, String dataFieldKey) {
      this.supplier = supplier;
      this.dataFieldKey = dataFieldKey;
    }

    /**
     * This method is called in order to try to create  rich text block node from a raw map
     * representation.
     *
     * @param raw representation of the block node coming from Contentful.
     * @return the rich node if resolving was successful.
     */
    @Override public CMARichNode resolve(Map<String, Object> raw) {
      final T resolved = getType(raw);

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
    T getType(Map<String, Object> raw) {
      return supplier.get(raw.get(dataFieldKey));
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
    HeadingResolver(int level, String dataFieldKey) {
      super((data) -> new CMARichHeading(level, data), dataFieldKey);
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

    /**
     * Create the resolver.
     *
     * @param supplier     how to generate an object of type T?
     * @param dataFieldKey what other keys to be filtered?
     */
    BlockAndDataResolver(SupplierWithData<T> supplier, String dataFieldKey) {
      super(supplier, dataFieldKey);
    }

    /**
     * Create an object of T
     *
     * @param raw a map coming from Contentful, parsed from the json response.
     * @return an object of Type T.
     */
    @Override
    T getType(Map<String, Object> raw) {
      Object data = raw.get(dataFieldKey);
      if (data instanceof Map && ((Map<String, Object>) data).containsKey("target")) {
        final Object target = ((Map<String, Object>) data).get("target");
        if (target instanceof Map) {
          final Map<String, Object> targetMap = (Map<String, Object>) target;
          final Map<String, Object> sys = (Map<String, Object>) targetMap.get("sys");

          final CMALink link = new CMALink();
          link.getSystem().setId((String) sys.get("id"));
          link.getSystem().setType(CMAType.Link);
          link.getSystem().setLinkType(CMAType.valueOf((String) sys.get("linkType")));

          data = link;
        }
      } else if (data instanceof Map && ((Map<String, Object>) data).containsKey("uri")) {
        data = ((Map<String, String>) data).get("uri");
      }

      return supplier.get(data);
    }
  }

  private static final Map<String, Resolver> RESOLVER_MAP = new HashMap<>();

  static {
    // add leafs
    RESOLVER_MAP.put("text", raw -> new CMARichText(
        (CharSequence) raw.get("value"),
        resolveMarks((List<Map<String, Object>>) raw.get("marks")),
        (Map<String, Object>) raw.get("data")
    ));
    RESOLVER_MAP.put("hr", raw -> new CMARichHorizontalRule(raw.get("data")));

    // add blocks
    RESOLVER_MAP.put(new CMARichQuote().getNodeType(),
        new BlockResolver<>(CMARichQuote::new, "data"));
    RESOLVER_MAP.put(new CMARichParagraph().getNodeType(),
        new BlockResolver<>(CMARichParagraph::new, "data"));
    RESOLVER_MAP.put(new CMARichDocument().getNodeType(),
        new BlockResolver<>(CMARichDocument::new, "data"));
    RESOLVER_MAP.put(new CMARichListItem().getNodeType(),
        new BlockResolver<>(CMARichListItem::new, "data"));
    RESOLVER_MAP.put(new CMARichOrderedList().getNodeType(),
        new BlockResolver<>(CMARichOrderedList::new, "data"));
    RESOLVER_MAP.put(new CMARichUnorderedList().getNodeType(),
        new BlockResolver<>(CMARichUnorderedList::new, "data"));
    RESOLVER_MAP.put(new CMARichTable().getNodeType(),
        new BlockResolver<>(CMARichTable::new, "data"));
    RESOLVER_MAP.put(new CMARichTableRow().getNodeType(),
        new BlockResolver<>(CMARichTableRow::new, "data"));
    RESOLVER_MAP.put(new CMARichTableHeaderCell().getNodeType(),
        new BlockResolver<>(CMARichTableHeaderCell::new, "data"));
    RESOLVER_MAP.put(new CMARichTableCell().getNodeType(),
        new BlockResolver<>(CMARichTableCell::new, "data"));
    RESOLVER_MAP.put(new CMARichHyperLink().getNodeType(),
        new BlockAndDataResolver<>(CMARichHyperLink::new, "data"));
    RESOLVER_MAP.put(new CMARichHyperLink(new CMALink(CMAType.Entry)).getNodeType(),
        new BlockAndDataResolver<>(CMARichHyperLink::new, "data"));
    RESOLVER_MAP.put(new CMARichHyperLink(new CMALink(CMAType.Asset)).getNodeType(),
        new BlockAndDataResolver<>(CMARichHyperLink::new, "data"));
    RESOLVER_MAP.put(new CMARichEmbeddedLink(new CMALink(CMAType.Entry)).getNodeType(),
        new BlockAndDataResolver<>(CMARichEmbeddedLink::new, "data"));
    RESOLVER_MAP.put(new CMARichEmbeddedLink(new CMALink(CMAType.Asset)).getNodeType(),
        new BlockAndDataResolver<>(CMARichEmbeddedLink::new, "data"));
    RESOLVER_MAP.put(new CMARichEmbeddedLink(new CMALink(CMAType.Entry), true).getNodeType(),
        new BlockAndDataResolver<>(target -> new CMARichEmbeddedLink(target, true), "data"));
    RESOLVER_MAP.put(new CMARichEmbeddedLink(new CMALink(CMAType.Asset), true).getNodeType(),
        new BlockAndDataResolver<>(target -> new CMARichEmbeddedLink(target, true), "data"));
    RESOLVER_MAP.put(new CMARichHeading(LEVEL_1).getNodeType(),
        new HeadingResolver(LEVEL_1, "data"));
    RESOLVER_MAP.put(new CMARichHeading(LEVEL_2).getNodeType(),
        new HeadingResolver(LEVEL_2, "data"));
    RESOLVER_MAP.put(new CMARichHeading(LEVEL_3).getNodeType(),
        new HeadingResolver(LEVEL_3, "data"));
    RESOLVER_MAP.put(new CMARichHeading(LEVEL_4).getNodeType(),
        new HeadingResolver(LEVEL_4, "data"));
    RESOLVER_MAP.put(new CMARichHeading(LEVEL_5).getNodeType(),
        new HeadingResolver(LEVEL_5, "data"));
    RESOLVER_MAP.put(new CMARichHeading(LEVEL_6).getNodeType(),
        new HeadingResolver(LEVEL_6, "data"));
  }

  public static void resolveRichTextField(CMAEntry entry) {
    if (entry.getFields() != null) {
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