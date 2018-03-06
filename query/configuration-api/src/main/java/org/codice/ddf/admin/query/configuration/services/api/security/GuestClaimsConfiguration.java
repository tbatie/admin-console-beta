package org.codice.ddf.admin.query.configuration.services.api.security;

import java.util.Map;

public interface GuestClaimsConfiguration {

  Map<String, String> getClaimsMapping();

}
