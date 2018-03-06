package org.codice.ddf.admin.query.configuration.services.impl;

import org.codice.ddf.admin.query.configuration.services.api.security.TruststoreConfiguration;

public class TruststoreConfigurationImpl implements TruststoreConfiguration {

  private String password;
  private byte[] file;

  public TruststoreConfigurationImpl(String password, byte[] file) {
    this.password = password;
    this.file = file;
  }
  @Override
  public String getTruststorePassword() {
    return password;
  }

  @Override
  public byte[] getTruststoreFile() {
    return file;
  }
}
