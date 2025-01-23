package com.contentful.java.cma.model;

import java.util.LinkedHashMap;
import java.util.List;
public class CMAConcept extends CMAResource {
    private String uri;
    private LinkedHashMap<String, String> prefLabel;
    private LinkedHashMap<String, List<String>> altLabels;
    private LinkedHashMap<String, List<String>> hiddenLabels;
    private List<String> notations;
    private LinkedHashMap<String, String> note;
    private LinkedHashMap<String, String> changeNote;
    private LinkedHashMap<String, String> definition;
    private LinkedHashMap<String, String> editorialNote;
    private LinkedHashMap<String, String> example;
    private LinkedHashMap<String, String> historyNote;
    private LinkedHashMap<String, String> scopeNote;
    private List<CMALink> broader;
    private List<CMALink> related;

    public CMAConcept() {
        super(CMAType.TaxonomyConcept);
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

    public LinkedHashMap<String, List<String>> getAltLabels() {
        return altLabels;
    }

    public void setAltLabels(LinkedHashMap<String, List<String>> altLabels) {
        this.altLabels = altLabels;
    }

    public LinkedHashMap<String, List<String>> getHiddenLabels() {
        return hiddenLabels;
    }

    public void setHiddenLabels(LinkedHashMap<String, List<String>> hiddenLabels) {
        this.hiddenLabels = hiddenLabels;
    }

    public List<String> getNotations() {
        return notations;
    }

    public void setNotations(List<String> notations) {
        this.notations = notations;
    }

    public LinkedHashMap<String, String> getNote() {
        return note;
    }

    public void setNote(LinkedHashMap<String, String> note) {
        this.note = note;
    }

    public LinkedHashMap<String, String> getChangeNote() {
        return changeNote;
    }

    public void setChangeNote(LinkedHashMap<String, String> changeNote) {
        this.changeNote = changeNote;
    }

    public LinkedHashMap<String, String> getDefinition() {
        return definition;
    }

    public void setDefinition(LinkedHashMap<String, String> definition) {
        this.definition = definition;
    }

    public LinkedHashMap<String, String> getEditorialNote() {
        return editorialNote;
    }

    public void setEditorialNote(LinkedHashMap<String, String> editorialNote) {
        this.editorialNote = editorialNote;
    }

    public LinkedHashMap<String, String> getExample() {
        return example;
    }

    public void setExample(LinkedHashMap<String, String> example) {
        this.example = example;
    }

    public LinkedHashMap<String, String> getHistoryNote() {
        return historyNote;
    }

    public void setHistoryNote(LinkedHashMap<String, String> historyNote) {
        this.historyNote = historyNote;
    }

    public LinkedHashMap<String, String> getScopeNote() {
        return scopeNote;
    }

    public void setScopeNote(LinkedHashMap<String, String> scopeNote) {
        this.scopeNote = scopeNote;
    }

    public List<CMALink> getBroader() {
        return broader;
    }

    public void setBroader(List<CMALink> broader) {
        this.broader = broader;
    }

    public List<CMALink> getRelated() {
        return related;
    }

    public void setRelated(List<CMALink> related) {
        this.related = related;
    }
}
