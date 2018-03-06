package org.codice.ddf.admin.query.configuration.services.api.security;

import java.util.List;
import java.util.Map;

public interface TemporaryUserConfiguration {

  String getUsername();

  String getPassword();

  List<String> getRoles();

  Map<String, String> getClaimsMapping();
}
