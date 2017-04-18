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
 **/
package org.codice.ddf.admin.ldap.actions.persist;

import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.codice.ddf.admin.ldap.actions.commons.LdapMessages.internalError;
import static org.codice.ddf.admin.ldap.actions.commons.LdapMessages.serviceDoesNotExistError;
import static org.codice.ddf.admin.ldap.actions.commons.services.LdapClaimsHandlerServiceProperties.LDAP_CLAIMS_HANDLER_FEATURE;
import static org.codice.ddf.admin.ldap.actions.commons.services.LdapClaimsHandlerServiceProperties.LDAP_CLAIMS_HANDLER_MANAGED_SERVICE_FACTORY_PID;
import static org.codice.ddf.admin.ldap.actions.commons.services.LdapClaimsHandlerServiceProperties.ldapConfigToLdapClaimsHandlerService;
import static org.codice.ddf.admin.ldap.actions.commons.services.LdapLoginServiceProperties.LDAP_LOGIN_FEATURE;
import static org.codice.ddf.admin.ldap.actions.commons.services.LdapLoginServiceProperties.LDAP_LOGIN_MANAGED_SERVICE_FACTORY_PID;
import static org.codice.ddf.admin.ldap.actions.commons.services.LdapLoginServiceProperties.ldapConfigurationToLdapLoginService;
import static org.codice.ddf.admin.ldap.fields.config.LdapUseCase.ATTRIBUTE_STORE;
import static org.codice.ddf.admin.ldap.fields.config.LdapUseCase.AUTHENTICATION;
import static org.codice.ddf.admin.ldap.fields.config.LdapUseCase.AUTHENTICATION_AND_ATTRIBUTE_STORE;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.codice.ddf.admin.api.fields.Field;
import org.codice.ddf.admin.api.fields.ListField;
import org.codice.ddf.admin.common.actions.BaseAction;
import org.codice.ddf.admin.common.fields.base.ListFieldImpl;
import org.codice.ddf.admin.configurator.Configurator;
import org.codice.ddf.admin.configurator.ConfiguratorFactory;
import org.codice.ddf.admin.configurator.OperationReport;
import org.codice.ddf.admin.ldap.actions.commons.LdapTestingUtils;
import org.codice.ddf.admin.ldap.actions.commons.services.LdapServiceCommons;
import org.codice.ddf.admin.ldap.fields.config.LdapConfigurationField;

import com.google.common.collect.ImmutableList;

public class SaveLdapConfiguration extends BaseAction<ListField<LdapConfigurationField>> {

    public static final String NAME = "saveLdapConfig";

    public static final String DESCRIPTION = "Saves the LDAP configuration.";

    private LdapConfigurationField config;
    private ConfiguratorFactory configuratorFactory;
    private LdapServiceCommons serviceCommons;
    private LdapTestingUtils testingUtils;

    public SaveLdapConfiguration(ConfiguratorFactory configuratorFactory) {
        super(NAME, DESCRIPTION, new ListFieldImpl<>(LdapConfigurationField.class));
        config = new LdapConfigurationField();
        this.configuratorFactory = configuratorFactory;
        this.serviceCommons = new LdapServiceCommons();
        this.testingUtils = new LdapTestingUtils();
    }

    @Override
    public List<Field> getArguments() {
        return ImmutableList.of(config);
    }

    @Override
    public ListField<LdapConfigurationField> performAction() {
        Configurator configurator = configuratorFactory.getConfigurator();

        if (config.settingsField().useCase()
                .equals(AUTHENTICATION) || config.settingsField().useCase()
                .equals(AUTHENTICATION_AND_ATTRIBUTE_STORE)) {

            Map<String, Object> ldapLoginServiceProps = ldapConfigurationToLdapLoginService(config);
            configurator.startFeature(LDAP_LOGIN_FEATURE);
            if(isNotEmpty(config.pid())) {
                configurator.updateConfigFile(config.pid(), ldapLoginServiceProps, false);
            } else {
                configurator.createManagedService(LDAP_LOGIN_MANAGED_SERVICE_FACTORY_PID,
                        ldapLoginServiceProps);
            }
        }

        if (config.settingsField().useCase()
                .equals(ATTRIBUTE_STORE) || config.settingsField().useCase()
                .equals(AUTHENTICATION_AND_ATTRIBUTE_STORE)) {

            Path newAttributeMappingPath = Paths.get(System.getProperty("ddf.home"),
                    "etc",
                    "ws-security",
                    "ldapAttributeMap-" + UUID.randomUUID()
                            .toString() + ".props");
            Map<String, Object> ldapClaimsServiceProps = ldapConfigToLdapClaimsHandlerService(config);
            configurator.createPropertyFile(newAttributeMappingPath, config.settingsField().attributeMap());
            configurator.startFeature(LDAP_CLAIMS_HANDLER_FEATURE);
            configurator.createManagedService(LDAP_CLAIMS_HANDLER_MANAGED_SERVICE_FACTORY_PID,
                    ldapClaimsServiceProps);
        }

        OperationReport report = configurator.commit("LDAP Configuration saved with details: {}",
                config.toString());

        if(report.containsFailedResults()) {
            addReturnValueMessage(internalError());
        }

        return serviceCommons.getLdapConfigurations(configuratorFactory);
    }

    @Override
    public void validate() {
        super.validate();
        if(containsErrorMsgs()) {
            return;
        }

        if (config.pid() != null && !testingUtils.serviceExists(config.pid(), configuratorFactory.getConfigReader())) {
            addArgumentMessage(serviceDoesNotExistError(null));
        } else {
            addReturnValueMessages(testingUtils.ldapConnectionExists(config, configuratorFactory));
        }
    }
}
