package org.codice.ddf.admin.services.impl;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.apache.karaf.features.FeaturesService;
import org.codice.ddf.admin.services.api.Capability;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class FeatureCapability implements Capability {

  private static final Logger LOGGER = LoggerFactory.getLogger(FeatureCapability.class);

  private String id;
  private String name;
  private String description;
  private Set<String> featuresToStart;
  private FeaturesService featuresService;

  public FeatureCapability(String id, String name, String description, Set<String> featuresToStart, FeaturesService featuresService) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.featuresToStart = featuresToStart;
    this.featuresService = featuresService;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public boolean start() {
    try {
      featuresService.installFeatures(featuresToStart, EnumSet.of(FeaturesService.Option.NoAutoRefreshBundles));
    } catch (Exception e) {
      LOGGER.error("Failed to install capability " + id, e);
      return false;
    }

    return true;
  }
}
