package org.codice.ddf.admin.query.configuration;

import java.util.List;

import org.codice.ddf.admin.api.fields.FunctionField;
import org.codice.ddf.admin.common.fields.base.function.BaseFieldProvider;
import org.codice.ddf.admin.query.configuration.persist.SetGuestClaims;
import org.codice.ddf.admin.query.configuration.persist.SetKeystore;
import org.codice.ddf.admin.query.configuration.persist.SetStsClaims;
import org.codice.ddf.admin.query.configuration.persist.SetSystemBanner;
import org.codice.ddf.admin.query.configuration.persist.SetSystemInformation;
import org.codice.ddf.admin.query.configuration.persist.SetTemporaryUsers;
import org.codice.ddf.admin.query.configuration.persist.SetTruststore;
import org.codice.ddf.admin.query.configuration.services.api.ConfigurationManager;

import com.google.common.collect.ImmutableList;

public class ConfigurationFieldProvider extends BaseFieldProvider {

  public static final String FIELD_NAME = "configs";

  public static final String FIELD_TYPE_NAME = "Configurations";

  public static final String DESCRIPTION = "Contains the configurations of the system.";

  private SetGuestClaims setGuestClaims;

  private SetKeystore setKeystore;

  private SetStsClaims setStsClaims;

  private SetSystemBanner setSystemBanner;

  private SetSystemInformation setSystemInformation;

  private SetTemporaryUsers setTemporaryUsers;

  private SetTruststore setTruststore;

  public ConfigurationFieldProvider(ConfigurationManager configurationManager) {
    super(FIELD_NAME, FIELD_TYPE_NAME, DESCRIPTION);
    this.setGuestClaims = new SetGuestClaims(configurationManager);
    this.setKeystore = new SetKeystore(configurationManager);
    this.setStsClaims = new SetStsClaims(configurationManager);
    this.setSystemBanner = new SetSystemBanner(configurationManager);
    this.setTemporaryUsers = new SetTemporaryUsers(configurationManager);
    this.setTruststore = new SetTruststore(configurationManager);
  }

  @Override
  public List<FunctionField> getDiscoveryFunctions() {
    return ImmutableList.of();
  }

  @Override
  public List<FunctionField> getMutationFunctions() {
    return ImmutableList.of(
        setGuestClaims,
        setKeystore,
        setStsClaims,
        setSystemBanner,
        setTemporaryUsers,
        setTruststore);
  }
}
