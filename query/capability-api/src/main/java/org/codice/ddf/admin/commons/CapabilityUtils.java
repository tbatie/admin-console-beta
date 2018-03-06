package org.codice.ddf.admin.commons;

import static org.codice.ddf.admin.commons.Messages.capabilityNotFound;
import static org.codice.ddf.admin.commons.Messages.profileNotFound;

import java.util.List;
import java.util.Optional;

import org.codice.ddf.admin.api.report.Report;
import org.codice.ddf.admin.services.api.CapabilitiesProfile;
import org.codice.ddf.admin.services.api.Capability;
import org.codice.ddf.admin.common.fields.base.scalar.StringField;
import org.codice.ddf.admin.common.report.ReportImpl;

public class CapabilityUtils {

  public Report<CapabilitiesProfile> matchProfile(List<CapabilitiesProfile> profiles, StringField profileName) {
    Optional<CapabilitiesProfile> matchedProfile =
            profiles
                    .stream()
                    .filter(pro -> pro.getName().equals(profileName.getValue()))
                    .findFirst();

    if (!matchedProfile.isPresent()) {
      return new ReportImpl<>(profileNotFound(profileName.getPath()));
    }

    return new ReportImpl<>(matchedProfile.get());
  }

  public Report<Capability> matchCapability(List<Capability> capabilities, StringField capabilityName) {
    Optional<Capability> capability =
        capabilities
            .stream()
            .filter(cap -> cap.getName().equals(capabilityName.getValue()))
            .findFirst();

    if (!capability.isPresent()) {
      return new ReportImpl<>(capabilityNotFound(capabilityName.getPath()));
    }

    return new ReportImpl<>(capability.get());
  }
}
