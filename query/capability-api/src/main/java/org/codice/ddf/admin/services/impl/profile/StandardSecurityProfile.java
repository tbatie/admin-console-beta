package org.codice.ddf.admin.services.impl.profile;

import java.util.List;

import org.codice.ddf.admin.services.api.Capability;
import org.codice.ddf.admin.services.impl.CapabilitiesProfileImpl;

public class StandardSecurityProfile extends CapabilitiesProfileImpl {

  public static final String ID = "standard_security_profile";

  public static final String NAME = "Standard Security Profile";

  public static final String DESCRIPTION = "Starts up a standard set of security capabilities such as STS, PDP and Embedded LDAP. lmde;flasd;fladsfasdf";


  public StandardSecurityProfile(List<Capability> capabilities) {
    super(ID, NAME, DESCRIPTION, capabilities);
  }
}
