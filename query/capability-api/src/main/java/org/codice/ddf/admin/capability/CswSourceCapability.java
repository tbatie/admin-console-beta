package org.codice.ddf.admin.capability;

import java.util.Collections;

public class CswSourceCapability extends FeatureCapability {

  public static final String NAME = "Csw Sources";

  public static final String DESCRIPTION = "Allows for creation of CSW sources. CSW sources can be used to federate to other system to retrieve metadata.";

  // TODO: tbatie - 2/21/18 - Start up necessary features
  public CswSourceCapability() {
    super(NAME, DESCRIPTION, Collections.emptyList());
  }
}
