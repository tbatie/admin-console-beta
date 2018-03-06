package org.codice.ddf.admin.query.configuration.services.api.security;

public interface KeystoreConfiguration {

  String getKeystorePassword();

  String getPrivateKeyPassword();

  byte[] getKeystoreFile();
}
