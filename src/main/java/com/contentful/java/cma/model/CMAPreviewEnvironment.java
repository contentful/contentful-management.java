package com.contentful.java.cma.model;

import java.util.ArrayList;
import java.util.List;

public class CMAPreviewEnvironment extends CMAResource {

    // Name
    String name;

    // Description
    String description;

    // Configurations
    List<CMAConfiguration> configurations;

    /**
     * Creates a new preview environment.
     */
    public CMAPreviewEnvironment() {
        super(CMAType.PreviewEnvironment);
    }

    /**
     * @return the {@code name} of this preview environment.
     */
    public String getName() {
        return name;
    }

    /**
     * Update the {@code name} of the preview environment.
     *
     * @param name human readable string representing the name.
     * @return this {@code CMAPreviewEnvironment} instance.
     */
    public CMAPreviewEnvironment setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * @return a {@code description} of this preview environment
     */
    public String getDescription() {
        return description;
    }

    /**
     * Update or set the {@code description} of this preview environment.
     *
     * @param description a non null string representing the description
     * @return this {@code CMAPreviewEnvironment} instance.
     */
    public CMAPreviewEnvironment setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * @return a list of {@code configurations} this preview environment contains.
     */
    public List<CMAConfiguration> getConfigurations() {
        return configurations;
    }

    /**
     * Update the complete list of {@code configurations}.
     *
     * @param configurations the new {@code configurations} to be used.
     * @return this {@code CMAPreviewEnvironment} instance.
     */
    public CMAPreviewEnvironment setConfigurations(List<CMAConfiguration> configurations) {
        this.configurations = configurations;
        return this;
    }

    /**
     * Add a new {@code configuration} to the existing ones, creating a new list if needed.
     *
     * @param configuration the {@code configuration} to be added.
     * @return this {@code CMAPreviewEnvironment} instance.
     */
    public CMAPreviewEnvironment addConfiguration(CMAConfiguration configuration) {
        if (configurations == null) {
            configurations = new ArrayList<CMAConfiguration>();
        }

        configurations.add(configuration);
        return this;
    }

    /**
     * @return the {@code id} of the preview environment
     */
    @Override public String getId() {
        return super.getId();
    }

    /**
     * Sets the {@code id} of this preview environment.
     *
     * @param id to be set.
     * @return this {@code CMAPreviewEnvironment} instance.
     */
    @SuppressWarnings("unchecked") @Override public CMAPreviewEnvironment setId(String id) {
        return super.setId(id);
    }

    /**
     * @return a human readable string, representing the object.
     */
    @Override public String toString() {
        return "CMAPreviewEnvironment { " + super.toString() + " "
                + "name = " + getName() + ", "
                + "description = " + getDescription() + ", "
                + "configurations = " + getConfigurations() + " "
                + "}";
    }
}
