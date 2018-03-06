package org.codice.ddf.admin.persist;

import static org.codice.ddf.admin.commons.Messages.FAILED_TO_START_PROFILE;
import static org.codice.ddf.admin.commons.Messages.PROFILE_NOT_FOUND;
import static org.codice.ddf.admin.commons.Messages.failedToStartProfile;

import java.util.List;
import java.util.Set;

import org.codice.ddf.admin.api.Field;
import org.codice.ddf.admin.api.fields.FunctionField;
import org.codice.ddf.admin.api.report.Report;
import org.codice.ddf.admin.common.fields.base.BaseFunctionField;
import org.codice.ddf.admin.common.fields.base.scalar.BooleanField;
import org.codice.ddf.admin.common.fields.base.scalar.StringField;
import org.codice.ddf.admin.commons.CapabilityUtils;
import org.codice.ddf.admin.services.api.CapabilitiesProfile;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public class StartCapabilitiesProfile extends BaseFunctionField<BooleanField> {

  public static final String FUNCTION_NAME = "startProfile";

  public static final String DESCRIPTION = "Starts the specified profile";

  public static final BooleanField RETURN_TYPE = new BooleanField();

  public static final String PROFILE_NAME = "profileName";

  private StringField profileNameArg;

  private CapabilityUtils capabilityUtils;

  private List<CapabilitiesProfile> profiles;

  public StartCapabilitiesProfile(List<CapabilitiesProfile> profiles) {
    super(FUNCTION_NAME, DESCRIPTION);
    profileNameArg = new StringField(PROFILE_NAME);
    profileNameArg.isRequired(true);

    this.profiles = profiles;
    capabilityUtils = new CapabilityUtils();
  }

  @Override
  public BooleanField performFunction() {
    Report<CapabilitiesProfile> matchedProfile = capabilityUtils.matchProfile(profiles, profileNameArg);
    addErrorMessages(matchedProfile);

    if(containsErrorMsgs()) {
      return BooleanField.of(false);
    }

    boolean startedSuccessfully = matchedProfile.getResult().start();
    if(!startedSuccessfully) {
      addErrorMessage(failedToStartProfile(getPath()));
    }

    return BooleanField.of(startedSuccessfully);
  }

  @Override
  public Set<String> getFunctionErrorCodes() {
    return ImmutableSet.of(PROFILE_NOT_FOUND, FAILED_TO_START_PROFILE);
  }

  @Override
  public List<Field> getArguments() {
    return ImmutableList.of(profileNameArg);
  }

  @Override
  public BooleanField getReturnType() {
    return RETURN_TYPE;
  }

  @Override
  public FunctionField<BooleanField> newInstance() {
    return new StartCapabilitiesProfile(profiles);
  }
}
