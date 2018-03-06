package org.codice.ddf.admin.commons;

import java.util.List;

import org.codice.ddf.admin.api.report.ErrorMessage;
import org.codice.ddf.admin.common.report.message.ErrorMessageImpl;

public class Messages {

  public static final String PROFILE_NOT_FOUND = "PROFILE_NOT_FOUND";

  public static final String CAPABILITY_NOT_FOUND = "CAPABILITY_NOT_FOUND";

  public static final String FAILED_TO_START_PROFILE = "FAILED_TO_START_PROFILE";

  public static final String FAILED_TO_START_CAPABILITY = "FAILED_TO_START_CAPABILITY";

  private Messages() {}

  public static ErrorMessage profileNotFound(List<Object> path) {
    return new ErrorMessageImpl(PROFILE_NOT_FOUND, path);
  }

  public static ErrorMessage failedToStartProfile(List<Object> path) {
    return new ErrorMessageImpl(FAILED_TO_START_PROFILE, path);
  }

  public static ErrorMessage capabilityNotFound(List<Object> path) {
    return new ErrorMessageImpl(CAPABILITY_NOT_FOUND, path);
  }

  public static ErrorMessage failedToStartCapability(List<Object> path) {
    return new ErrorMessageImpl(FAILED_TO_START_CAPABILITY, path);
  }
}
