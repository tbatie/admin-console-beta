package org.codice.ddf.admin.query.configuration.services.impl.profile;

import java.util.List;

import org.codice.ddf.admin.query.configuration.services.api.InstallationProfile;
import org.codice.ddf.admin.query.configuration.services.api.security.GuestClaimsConfiguration;
import org.codice.ddf.admin.query.configuration.services.api.security.KeystoreConfiguration;
import org.codice.ddf.admin.query.configuration.services.api.security.StsConfiguration;
import org.codice.ddf.admin.query.configuration.services.api.security.TemporaryUserConfiguration;
import org.codice.ddf.admin.query.configuration.services.api.security.TruststoreConfiguration;
import org.codice.ddf.admin.query.configuration.services.api.system.SystemInformationConfiguration;
import org.codice.ddf.admin.query.configuration.services.api.theme.SystemBannerConfiguration;

public class StandardInstallationProfile implements InstallationProfile {

  public static final String ID = "standard_installation";

  public static final String NAME = "Standard Installation";

  public static final String DESCRIPTION = "knladsg;klnags;klnagsdnkl nklas gklnags klnags gs aklkags as ";

  @Override
  public String getId() {
    return ID;
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String getDescription() {
    return DESCRIPTION;
  }

  @Override
  public GuestClaimsConfiguration getGuestClaims() {
    return null;
  }

  @Override
  public List<TemporaryUserConfiguration> getTemporaryUsers() {
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
  public KeystoreConfiguration getKeyStoreConfiguration() {
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
