package org.codice.ddf.admin.discover;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.codice.ddf.admin.services.api.CapabilitiesProfile;
import org.codice.ddf.admin.common.fields.base.function.GetFunctionField;
import org.codice.ddf.admin.fields.CapabilitiesProfileField;

public class GetCapabilitiesProfiles extends GetFunctionField<CapabilitiesProfileField.ListImpl> {

  public static final String FUNCTION_NAME = "capabilitiesProfiles";

  public static final String DESCRIPTION = "Represents a set of capabilitythat can be installed.";

  public static final CapabilitiesProfileField.ListImpl RETURN_TYPE = new CapabilitiesProfileField.ListImpl();

  private List<CapabilitiesProfile> profiles;

  public GetCapabilitiesProfiles(List<CapabilitiesProfile> profiles) {
    super(FUNCTION_NAME, DESCRIPTION);
    this.profiles = profiles;
  }

  @Override
  public CapabilitiesProfileField.ListImpl performFunction() {
    return new CapabilitiesProfileField.ListImpl(profiles);
  }

  @Override
  public CapabilitiesProfileField.ListImpl getReturnType() {
    return RETURN_TYPE;
  }

  @Override
  public GetCapabilitiesProfiles newInstance() {
    return new GetCapabilitiesProfiles(profiles);
  }

  @Override
  public Set<String> getFunctionErrorCodes() {
    return Collections.emptySet();
  }
}
