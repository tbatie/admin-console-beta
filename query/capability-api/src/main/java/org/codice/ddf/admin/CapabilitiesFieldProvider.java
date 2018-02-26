package org.codice.ddf.admin;

import java.util.Collections;
import java.util.List;

import org.codice.ddf.admin.api.fields.FunctionField;
import org.codice.ddf.admin.capability.Capability;
import org.codice.ddf.admin.common.fields.base.function.BaseFieldProvider;
import org.codice.ddf.admin.discover.GetCapabilities;

import com.google.common.collect.ImmutableList;

public class CapabilitiesFieldProvider extends BaseFieldProvider {

  public static final String FIELD_NAME = "capabilities";
  public static final String TYPE_NAME = "CapabilitiesOperations";
  public static final String DESCRIPTION = "Don't think this needs to exist, get the graphql schema to accept function fields at the root.";

  private GetCapabilities getCapabilities;

  // TODO: tbatie - 2/21/18 - Do we really need a provider for this? Could the graphql schema get a reference to DiscoveryFunctions and MutationFunctions instead?
  public CapabilitiesFieldProvider(List<Capability> capabilities) {
    super(FIELD_NAME, TYPE_NAME, DESCRIPTION);
    getCapabilities = new GetCapabilities(capabilities);
  }

  @Override
  public List<FunctionField> getDiscoveryFunctions() {
    return ImmutableList.of(getCapabilities);
  }

  @Override
  public List<FunctionField> getMutationFunctions() {
    return Collections.emptyList();
  }
}
