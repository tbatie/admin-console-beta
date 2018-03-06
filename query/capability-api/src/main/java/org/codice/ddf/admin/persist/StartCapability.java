package org.codice.ddf.admin.persist;

import static org.codice.ddf.admin.commons.Messages.CAPABILITY_NOT_FOUND;
import static org.codice.ddf.admin.commons.Messages.FAILED_TO_START_CAPABILITY;
import static org.codice.ddf.admin.commons.Messages.failedToStartCapability;

import java.util.List;
import java.util.Set;

import org.codice.ddf.admin.api.Field;
import org.codice.ddf.admin.api.report.Report;
import org.codice.ddf.admin.commons.CapabilityUtils;
import org.codice.ddf.admin.services.api.Capability;
import org.codice.ddf.admin.common.fields.base.BaseFunctionField;
import org.codice.ddf.admin.common.fields.base.scalar.BooleanField;
import org.codice.ddf.admin.common.fields.base.scalar.StringField;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public class StartCapability extends BaseFunctionField<BooleanField> {

  public static final String FUNCTION_NAME = "startCapability";

  public static final String DESCRIPTION = "Starts the specified capability";

  public static final String CAPABILITY_NAME = "capabilityName";

  private static final BooleanField RETURN_TYPE = new BooleanField();

  private StringField capabilityNameArg;

  private List<Capability> capabilities;

  private CapabilityUtils capabilitiesUtils;

  public StartCapability(List<Capability> capabilities) {
    super(FUNCTION_NAME, DESCRIPTION);
    capabilityNameArg = new StringField(CAPABILITY_NAME);

    this.capabilities = capabilities;
    capabilitiesUtils = new CapabilityUtils();
  }


  @Override
  public List<Field> getArguments() {
    return ImmutableList.of(capabilityNameArg);
  }

  @Override
  public BooleanField performFunction() {
    Report<Capability> matchedCapability = capabilitiesUtils.matchCapability(capabilities, capabilityNameArg);
    addErrorMessages(matchedCapability);

    if(containsErrorMsgs()) {
      return BooleanField.of(false);
    }

    boolean startedSuccessfully = matchedCapability.getResult().start();
    if(!startedSuccessfully) {
      addErrorMessage(failedToStartCapability(getPath()));
    }

    return BooleanField.of(startedSuccessfully);
  }

  @Override
  public BooleanField getReturnType() {
    return RETURN_TYPE;
  }

  @Override
  public StartCapability newInstance() {
    return new StartCapability(capabilities);
  }

  @Override
  public Set<String> getFunctionErrorCodes() {
    return ImmutableSet.of(CAPABILITY_NOT_FOUND, FAILED_TO_START_CAPABILITY);
  }
}
