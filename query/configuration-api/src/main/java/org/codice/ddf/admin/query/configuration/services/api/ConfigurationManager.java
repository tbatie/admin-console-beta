package org.codice.ddf.admin.query.configuration.services.api;

import java.util.List;

import org.codice.ddf.admin.query.configuration.services.api.security.GuestClaimsConfiguration;
import org.codice.ddf.admin.query.configuration.services.api.security.KeystoreConfiguration;
import org.codice.ddf.admin.query.configuration.services.api.security.StsConfiguration;
import org.codice.ddf.admin.query.configuration.services.api.security.TemporaryUserConfiguration;
import org.codice.ddf.admin.query.configuration.services.api.security.TruststoreConfiguration;
import org.codice.ddf.admin.query.configuration.services.api.system.SystemInformationConfiguration;
import org.codice.ddf.admin.query.configuration.services.api.theme.SystemBannerConfiguration;

public interface ConfigurationManager {

  boolean setTemporaryUsers(List<TemporaryUserConfiguration> users);

  boolean setGuestClaimsConfiguration(GuestClaimsConfiguration guestClaims);

  boolean setSystemBannerConfiguration(SystemBannerConfiguration systemBannerConfiguration);

  boolean setSystemInformationConfiguration(SystemInformationConfiguration systemInformation);

  boolean setKeystoreConfiguration(KeystoreConfiguration keystoreConfiguration);

  boolean setTruststoreConfiguration(TruststoreConfiguration truststoreConfiguration);

  boolean setStsConfiguration(StsConfiguration stsConfiguration);

  List<TemporaryUserConfiguration> getTemporaryUsers();

  GuestClaimsConfiguration getGuestClaimsConfiguration();

  SystemBannerConfiguration getSystemBannerConfiguration();

  SystemInformationConfiguration getSystemInformationConfiguration();

  KeystoreConfiguration getKeystoreConfiguration();

  TruststoreConfiguration getTruststoreConfiguration();

  StsConfiguration getStsConfiguration();

}
