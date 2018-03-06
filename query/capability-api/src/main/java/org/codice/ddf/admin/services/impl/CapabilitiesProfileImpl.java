package org.codice.ddf.admin.services.impl;

import java.util.List;

import org.codice.ddf.admin.services.api.CapabilitiesProfile;
import org.codice.ddf.admin.services.api.Capability;

public class CapabilitiesProfileImpl implements CapabilitiesProfile {

  private String id;
  private String name;
  private String description;
  private List<Capability> capabilities;

  public CapabilitiesProfileImpl(String id, String name, String description, List<Capability> capabilities) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.capabilities = capabilities;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public List<Capability> getCapabilities() {
    return capabilities;
  }

  @Override
  public boolean start() {
    return capabilities.stream().map(Capability::start).anyMatch(started -> !started);
  }
}
