package com.contentful.java.cma.model;

import java.util.List;

public class CMAMetadata {
    List<CMATag> tags;

    List<CMALink> taxonomy;

    public List<CMALink> getConcepts() {
        return concepts;
    }

    public void setConcepts(List<CMALink> concepts) {
        this.concepts = concepts;
    }

    List<CMALink> concepts;

    public List<CMALink> getTaxonomy() {
        return taxonomy;
    }

    public void setTaxonomy(List<CMALink> taxonomy) {
        this.taxonomy = taxonomy;
    }

    /**
     * Gets the list of tags associated with this resource.
     *
     * @return The list of {@link CMATag} instances representing the tags.
     */
    public List<CMATag> getTags() {
        return tags;
    }

    /**
     * Sets the list of tags for this resource.
     *
     * @param tags The list of {@link CMATag} instances to be associated with the resource.
     */
    public void setTags(List<CMATag> tags) {
        this.tags = tags;
    }

    /**
     * @return a human-readable string, representing the object.
     */
    @Override public String toString() {
        return "CMAMetadata { "
                + "tags = " + getTags() + "}";
    }
}
