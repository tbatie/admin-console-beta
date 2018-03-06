package org.codice.ddf.admin.query.configuration.discover;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.codice.ddf.admin.api.fields.FunctionField;
import org.codice.ddf.admin.common.fields.base.function.GetFunctionField;
import org.codice.ddf.admin.query.configuration.fields.InstallationProfileField;
import org.codice.ddf.admin.query.configuration.services.api.InstallationProfile;

public class GetInstallationProfiles extends GetFunctionField<InstallationProfileField.ListImpl> {

  public static final String FUNCTION_NAME = "installationProfiles";

  public static final String DESCRIPTION = "Retrieves default profiles recommended for the user to use during a standard installation.";

  public static final InstallationProfileField.ListImpl RETURN_TYPE = new InstallationProfileField.ListImpl();

  private List<InstallationProfile> profiles;

  public GetInstallationProfiles(List<InstallationProfile> profiles) {
    super(FUNCTION_NAME, DESCRIPTION);
    this.profiles = profiles;
  }

  @Override
  public InstallationProfileField.ListImpl performFunction() {
    return new InstallationProfileField.ListImpl(profiles);
  }

  @Override
  public InstallationProfileField.ListImpl getReturnType() {
    return RETURN_TYPE;
  }

  @Override
  public FunctionField<InstallationProfileField.ListImpl> newInstance() {
    return new GetInstallationProfiles(profiles);
  }


  @Override
  public Set<String> getFunctionErrorCodes() {
    return Collections.emptySet();
  }
}
