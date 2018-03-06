package org.codice.ddf.admin;

import java.util.List;

import org.codice.ddf.admin.api.fields.FunctionField;
import org.codice.ddf.admin.services.api.Capability;
import org.codice.ddf.admin.services.api.CapabilitiesProfile;
import org.codice.ddf.admin.common.fields.base.function.BaseFieldProvider;
import org.codice.ddf.admin.discover.GetCapabilities;
import org.codice.ddf.admin.discover.GetCapabilitiesProfiles;
import org.codice.ddf.admin.persist.StartCapability;
import org.codice.ddf.admin.persist.StartCapabilitiesProfile;

import com.google.common.collect.ImmutableList;

public class CapabilitiesFieldProvider extends BaseFieldProvider {

  public static final String FIELD_NAME = "capabilities";
  public static final String TYPE_NAME = "CapabilitiesOperations";
  public static final String DESCRIPTION = "Various operations for testing and retrieving existing Capabilities";

  private GetCapabilities getCapabilities;
  private GetCapabilitiesProfiles getProfiles;

  private StartCapability startCapability;
  private StartCapabilitiesProfile startProfile;

  // TODO: tbatie - 2/21/18 - Do we really need a provider for this? Could the graphql schema get a reference to DiscoveryFunctions and MutationFunctions instead?
  public CapabilitiesFieldProvider(List<CapabilitiesProfile> profiles, List<Capability> capabilities) {
    super(FIELD_NAME, TYPE_NAME, DESCRIPTION);
    getCapabilities = new GetCapabilities(capabilities);
    getProfiles = new GetCapabilitiesProfiles(profiles);

    startCapability = new StartCapability(capabilities);
    startProfile = new StartCapabilitiesProfile(profiles);
  }

  @Override
  public List<FunctionField> getDiscoveryFunctions() {
    return ImmutableList.of(getProfiles, getCapabilities);
  }

  @Override
  public List<FunctionField> getMutationFunctions() {
    return ImmutableList.of(startProfile, startCapability);
  }
}
