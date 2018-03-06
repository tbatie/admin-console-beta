package org.codice.ddf.admin.discover;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.codice.ddf.admin.api.fields.FunctionField;
import org.codice.ddf.admin.services.api.Capability;
import org.codice.ddf.admin.common.fields.base.function.GetFunctionField;
import org.codice.ddf.admin.fields.CapabilityField;

public class GetCapabilities extends GetFunctionField<CapabilityField.ListImpl> {

  private static final String FUNCTION_NAME = "capabilities";
  private static final String DESCRIPTION = "Returns all capabilities the system of capable of starting.";
  private List<Capability> capabilties;

  public GetCapabilities(List<Capability> capabilties) {
    super(FUNCTION_NAME, DESCRIPTION);
    this.capabilties = capabilties;
  }

  @Override
  public CapabilityField.ListImpl performFunction() {
    return new CapabilityField.ListImpl(capabilties);
  }

  @Override
  public Set<String> getFunctionErrorCodes() {
    return Collections.emptySet();
  }

  @Override
  public CapabilityField.ListImpl getReturnType() {
    return new CapabilityField.ListImpl();
  }

  @Override
  public FunctionField<CapabilityField.ListImpl> newInstance() {
    return new GetCapabilities(capabilties);
  }
}
