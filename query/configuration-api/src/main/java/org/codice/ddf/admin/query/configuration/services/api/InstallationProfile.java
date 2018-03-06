package org.codice.ddf.admin.query.configuration.services.api;

import java.util.List;

import org.codice.ddf.admin.query.configuration.services.api.security.GuestClaimsConfiguration;
import org.codice.ddf.admin.query.configuration.services.api.security.KeystoreConfiguration;
import org.codice.ddf.admin.query.configuration.services.api.security.StsConfiguration;
import org.codice.ddf.admin.query.configuration.services.api.security.TemporaryUserConfiguration;
import org.codice.ddf.admin.query.configuration.services.api.security.TruststoreConfiguration;
import org.codice.ddf.admin.query.configuration.services.api.system.SystemInformationConfiguration;
import org.codice.ddf.admin.query.configuration.services.api.theme.SystemBannerConfiguration;

public interface InstallationProfile {

  String getId();

  String getName();

  String getDescription();

  GuestClaimsConfiguration getGuestClaims();

  List<TemporaryUserConfiguration> getTemporaryUsers();

  SystemBannerConfiguration getSystemBannerConfiguration();

  SystemInformationConfiguration getSystemInformationConfiguration();

  KeystoreConfiguration getKeyStoreConfiguration();

  TruststoreConfiguration getTruststoreConfiguration();

  StsConfiguration getStsConfiguration();

}
