package org.codice.ddf.admin.query.configuration.services.impl;

import java.util.List;

import org.codice.ddf.admin.query.configuration.services.api.security.StsConfiguration;

public class StsConfigurationImpl implements StsConfiguration {

  private List<String> claims;

  public StsConfigurationImpl(List<String> claims) {
    this.claims = claims;
  }

  @Override
  public List<String> getClaims() {
    return claims;
  }
}
