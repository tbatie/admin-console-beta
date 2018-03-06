package org.codice.ddf.admin.query.configuration.services.api.security;

public interface TruststoreConfiguration {

  String getTruststorePassword();

  byte[] getTruststoreFile();
}
