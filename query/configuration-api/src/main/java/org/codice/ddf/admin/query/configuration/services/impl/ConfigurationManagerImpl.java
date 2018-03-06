package org.codice.ddf.admin.query.configuration.services.impl;

import java.util.List;

import org.codice.ddf.admin.query.configuration.services.api.ConfigurationManager;
import org.codice.ddf.admin.query.configuration.services.api.security.GuestClaimsConfiguration;
import org.codice.ddf.admin.query.configuration.services.api.security.KeystoreConfiguration;
import org.codice.ddf.admin.query.configuration.services.api.security.StsConfiguration;
import org.codice.ddf.admin.query.configuration.services.api.security.TemporaryUserConfiguration;
import org.codice.ddf.admin.query.configuration.services.api.security.TruststoreConfiguration;
import org.codice.ddf.admin.query.configuration.services.api.system.SystemInformationConfiguration;
import org.codice.ddf.admin.query.configuration.services.api.theme.SystemBannerConfiguration;

public class ConfigurationManagerImpl implements ConfigurationManager {

  @Override
  public boolean setTemporaryUsers(List<TemporaryUserConfiguration> users) {
    return false;
  }

  @Override
  public boolean setGuestClaimsConfiguration(GuestClaimsConfiguration guestClaims) {
    return false;
  }

  @Override
  public boolean setSystemBannerConfiguration(SystemBannerConfiguration systemBannerConfiguration) {
    return false;
  }

  @Override
  public boolean setSystemInformationConfiguration(
          SystemInformationConfiguration systemInformation) {
    return false;
  }

  @Override
  public boolean setKeystoreConfiguration(KeystoreConfiguration keystoreConfiguration) {
    return false;
  }

  @Override
  public boolean setTruststoreConfiguration(TruststoreConfiguration truststoreConfiguration) {
    return false;
  }

  @Override
  public boolean setStsConfiguration(StsConfiguration stsConfiguration) {
    return false;
  }







  @Override
  public List<TemporaryUserConfiguration> getTemporaryUsers() {
    return null;
  }

  @Override
  public GuestClaimsConfiguration getGuestClaimsConfiguration() {
    return null;
  }

  @Override
  public SystemBannerConfiguration getSystemBannerConfiguration() {
    return null;
  }

  @Override
  public SystemInformationConfiguration getSystemInformationConfiguration() {
    return null;
  }

  @Override
  public KeystoreConfiguration getKeystoreConfiguration() {
    return null;
  }

  @Override
  public TruststoreConfiguration getTruststoreConfiguration() {
    return null;
  }

  @Override
  public StsConfiguration getStsConfiguration() {
    return null;
  }
}
