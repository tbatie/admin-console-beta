package org.codice.ddf.admin.services.impl;

import java.util.Set;

import org.apache.karaf.features.FeaturesService;

import com.google.common.collect.ImmutableSet;

public class IdpCapability extends FeatureCapability {

  public static final String ID = "idp_auth_capability";

  public static final String NAME = "External Identity Provider";

  public static final String DESCRIPTION = "Allows an external system to provide authentication using Single Sign On (SSO).";

  public static final Set<String> FEATURES_TO_START = ImmutableSet.of("security-idp");

  public IdpCapability(FeaturesService featuresService) {
    super(ID, NAME, DESCRIPTION, FEATURES_TO_START, featuresService);
  }

}
