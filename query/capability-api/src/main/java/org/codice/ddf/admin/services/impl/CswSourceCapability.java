package org.codice.ddf.admin.services.impl;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import org.apache.karaf.features.FeaturesService;

public class CswSourceCapability extends FeatureCapability {

  public static final String ID = "csw_source_capability";

  public static final String NAME = "Csw Sources";

  public static final String DESCRIPTION = "Allows for creation of Catalog Service for the Web (CSW) sources . CSW sources can be used to federate to other system to retrieve metadata.";

  public static final Set<String> FEATURES_TO_START = ImmutableSet.of("spatial-csw");

  public CswSourceCapability(FeaturesService featuresService) {
    super(ID, NAME, DESCRIPTION, FEATURES_TO_START, featuresService);
  }

}
