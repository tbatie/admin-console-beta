package org.codice.ddf.admin.services.api;

import java.util.List;

public interface CapabilitiesProfile {

  String getId();

  String getName();

  String getDescription();

  List<Capability> getCapabilities();

  boolean start();
}