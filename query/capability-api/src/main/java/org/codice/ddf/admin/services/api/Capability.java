package org.codice.ddf.admin.services.api;

public interface Capability {

  String getId();

  String getName();

  String getDescription();

  boolean start();
}
