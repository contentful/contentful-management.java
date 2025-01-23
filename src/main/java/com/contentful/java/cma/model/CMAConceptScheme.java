package com.contentful.java.cma.model;

import java.util.LinkedHashMap;
import java.util.List;
public class CMAConceptScheme extends CMAResource {
    private String uri;
    private LinkedHashMap<String, String> prefLabel;
    private LinkedHashMap<String, String> definition;
    private List<CMALink> topConcepts;
    private List<CMALink> concepts;
    private int totalConcepts;

    // Constructor
    public CMAConceptScheme() {
        super(CMAType.TaxonomyConceptScheme);
    }

    // Getters and Setters
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public LinkedHashMap<String, String> getPrefLabel() {
        return prefLabel;
    }

    public void setPrefLabel(LinkedHashMap<String, String> prefLabel) {
        this.prefLabel = prefLabel;
    }

    public LinkedHashMap<String, String> getDefinition() {
        return definition;
    }

    public void setDefinition(LinkedHashMap<String, String> definition) {
        this.definition = definition;
    }

    public List<CMALink> getTopConcepts() {
        return topConcepts;
    }

    public void setTopConcepts(List<CMALink> topConcepts) {
        this.topConcepts = topConcepts;
    }

    public List<CMALink> getConcepts() {
        return concepts;
    }

    public void setConcepts(List<CMALink> concepts) {
        this.concepts = concepts;
    }

    public int getTotalConcepts() {
        return totalConcepts;
    }

    public void setTotalConcepts(int totalConcepts) {
        this.totalConcepts = totalConcepts;
    }
}