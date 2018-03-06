package org.codice.ddf.admin.services.impl;

import java.util.Set;

import org.apache.karaf.features.FeaturesService;

import com.google.common.collect.ImmutableSet;

public class EmbeddedLdapCapability extends FeatureCapability {

  public static final String ID = "embedded_ldap_capability";

  public static final String NAME = "Embedded LDAP";

  public static final String DESCRIPTION = "Starts a LDAP testing server full of fake users. Not to be used in production.";

  public static final Set<String> FEATURES_TO_START = ImmutableSet.of("opendj-embedded","ldap-embedded-default-configs");

  public EmbeddedLdapCapability(FeaturesService featuresService) {
    super(ID, NAME, DESCRIPTION, FEATURES_TO_START, featuresService);
  }

}