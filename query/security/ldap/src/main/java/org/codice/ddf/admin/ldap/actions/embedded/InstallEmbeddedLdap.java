/**
 * Copyright (c) Codice Foundation
 * <p>
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details. A copy of the GNU Lesser General Public License
 * is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 */
package org.codice.ddf.admin.ldap.actions.embedded;

import static org.codice.ddf.admin.ldap.actions.commons.LdapMessages.internalError;
import static org.codice.ddf.admin.ldap.actions.commons.services.LdapClaimsHandlerServiceProperties.LDAP_CLAIMS_HANDLER_FEATURE;
import static org.codice.ddf.admin.ldap.actions.commons.services.LdapLoginServiceProperties.LDAP_LOGIN_FEATURE;
import static org.codice.ddf.admin.ldap.actions.embedded.EmbeddedLdapServiceProperties.ALL_DEFAULT_EMBEDDED_LDAP_CONFIG_FEATURE;
import static org.codice.ddf.admin.ldap.actions.embedded.EmbeddedLdapServiceProperties.DEFAULT_EMBEDDED_LDAP_CLAIMS_HANDLER_CONFIG_FEATURE;
import static org.codice.ddf.admin.ldap.actions.embedded.EmbeddedLdapServiceProperties.DEFAULT_EMBEDDED_LDAP_LOGIN_CONFIG_FEATURE;
import static org.codice.ddf.admin.ldap.actions.embedded.EmbeddedLdapServiceProperties.EMBEDDED_LDAP_FEATURE;
import static org.codice.ddf.admin.ldap.fields.config.LdapUseCase.ATTRIBUTE_STORE;
import static org.codice.ddf.admin.ldap.fields.config.LdapUseCase.AUTHENTICATION;
import static org.codice.ddf.admin.ldap.fields.config.LdapUseCase.AUTHENTICATION_AND_ATTRIBUTE_STORE;

import java.util.List;

import org.codice.ddf.admin.api.fields.Field;
import org.codice.ddf.admin.common.actions.BaseAction;
import org.codice.ddf.admin.common.fields.base.scalar.BooleanField;
import org.codice.ddf.admin.configurator.Configurator;
import org.codice.ddf.admin.configurator.ConfiguratorFactory;
import org.codice.ddf.admin.configurator.OperationReport;
import org.codice.ddf.admin.ldap.fields.config.LdapUseCase;

import com.google.common.collect.ImmutableList;

// TODO: tbatie - 4/4/17 - Move embedded ldap to a seperate action creator
public class InstallEmbeddedLdap extends BaseAction<BooleanField> {

    public static final String NAME = "installEmbeddedLdap";
    public static final String DESCRIPTION = "Installs the internal embedded LDAP. Used for testing purposes only. LDAP port: 1389, LDAPS port: 1636, ADMIN port: 4444";

    private LdapUseCase useCase;
    private ConfiguratorFactory configuratorFactory;

    public InstallEmbeddedLdap(ConfiguratorFactory configuratorFactory) {
        super(NAME, DESCRIPTION, new BooleanField());
        useCase = new LdapUseCase();
        this.configuratorFactory = configuratorFactory;
    }

    @Override
    public List<Field> getArguments() {
        return ImmutableList.of(useCase);
    }

    @Override
    public BooleanField performAction() {
        Configurator configurator = configuratorFactory.getConfigurator();
        switch (useCase.getValue()) {
        case AUTHENTICATION:
            configurator.startFeature(EMBEDDED_LDAP_FEATURE);
            configurator.startFeature(LDAP_LOGIN_FEATURE);
            configurator.startFeature(DEFAULT_EMBEDDED_LDAP_LOGIN_CONFIG_FEATURE);
            break;
        case ATTRIBUTE_STORE:
            configurator.startFeature(EMBEDDED_LDAP_FEATURE);
            configurator.startFeature(LDAP_CLAIMS_HANDLER_FEATURE);
            configurator.startFeature(DEFAULT_EMBEDDED_LDAP_CLAIMS_HANDLER_CONFIG_FEATURE);
            break;
        case AUTHENTICATION_AND_ATTRIBUTE_STORE:
            configurator.startFeature(EMBEDDED_LDAP_FEATURE);
            configurator.startFeature(LDAP_LOGIN_FEATURE);
            configurator.startFeature(LDAP_CLAIMS_HANDLER_FEATURE);
            configurator.startFeature(ALL_DEFAULT_EMBEDDED_LDAP_CONFIG_FEATURE);
            break;
        }

        OperationReport report = configurator.commit();

        if(report.containsFailedResults()) {
            addReturnValueMessage(internalError());
        }

        return new BooleanField(report.containsFailedResults());
    }
}
