package org.codice.ddf.admin.capability;

import java.util.List;

public abstract class FeatureCapability implements Capability {
  private String name;
  private String description;
  private List<String> featuresToStart;

  public FeatureCapability(String name, String description, List<String> featuresToStart) {
    this.name = name;
    this.description = description;
    this.featuresToStart = featuresToStart;
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
  public boolean start() {
    // TODO: tbatie - 2/21/18 - Start features needed
    return false;
  }

}
