package org.codice.ddf.admin.capability;

public interface Capability {

  String getName();

  String getDescription();

  boolean start();
}
