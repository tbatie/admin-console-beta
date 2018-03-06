package org.codice.ddf.admin.query.configuration.services.api.system;

public interface SystemInformationConfiguration {

  String getHostName();

  Integer getHttpPort();

  Integer getHttpsPort();

  String getSiteContact();

  String getSiteName();

  String getVersion();

}
