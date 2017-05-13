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
package org.codice.ddf.admin.security.wcpm.actions.persist

import org.codice.ddf.admin.api.fields.FunctionField
import org.codice.ddf.admin.api.FieldProvider
import org.codice.ddf.admin.api.report.ReportWithResult
import org.codice.ddf.admin.api.fields.ListField
import org.codice.ddf.admin.common.fields.base.BaseFunctionField
import org.codice.ddf.admin.common.fields.base.ListFieldImpl
import org.codice.ddf.admin.common.report.message.DefaultMessages
import org.codice.ddf.admin.configurator.ConfigReader
import org.codice.ddf.admin.configurator.Configurator
import org.codice.ddf.admin.configurator.ConfiguratorFactory
import org.codice.ddf.admin.configurator.OperationReport
import org.codice.ddf.admin.security.common.SecurityMessages
import org.codice.ddf.admin.security.common.fields.wcpm.ClaimsMapEntry
import org.codice.ddf.admin.security.common.fields.wcpm.ContextPolicyBin
import org.codice.ddf.admin.security.common.fields.wcpm.Realm
import org.codice.ddf.admin.security.common.services.PolicyManagerServiceProperties
import org.codice.ddf.admin.security.common.services.StsServiceProperties
import org.codice.ddf.admin.security.wcpm.WcpmFieldProvider
import org.codice.ddf.admin.security.wcpm.persist.SaveContextPolices
import org.codice.ddf.security.policy.context.impl.PolicyManager
import spock.lang.Specification

class SaveContextPoliciesTest extends Specification {
    FieldProvider queryProvider
    ConfiguratorFactory configuratorFactory
    Configurator configurator
    ConfigReader configReader
    OperationReport operationReport
    PolicyManager policyManager
    FunctionField action
    Map<String, Object> stsConfig

    String[] testClaims
    def testData

    def setup() {
        testClaims = [
                "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/nameidentifier",
                "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/emailaddress",
                "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/surname",
                "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/givenname",
                "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/role"
        ]

        testData = [
                policies: [
                        [
                                paths        : ['/'],
                                authTypes    : ['basic'],
                                realm        : 'karaf',
                                claimsMapping: [
                                        [
                                                (ClaimsMapEntry.KEY_FIELD_NAME)  : 'http://schemas.xmlsoap.org/ws/2005/05/identity/claims/role',
                                                (ClaimsMapEntry.VALUE_FIELD_NAME): 'system-admin'
                                        ]
                                ]
                        ],
                        [
                                paths        : ['/test'],
                                authTypes    : ['SAML', 'PKI'],
                                realm        : 'karaf',
                                claimsMapping: [
                                        [
                                                (ClaimsMapEntry.KEY_FIELD_NAME)  : 'http://schemas.xmlsoap.org/ws/2005/05/identity/claims/role',
                                                (ClaimsMapEntry.VALUE_FIELD_NAME): 'system-admin'
                                        ]
                                ]
                        ]
                ]
        ]

        operationReport = Mock(OperationReport)
        configuratorFactory = Mock(ConfiguratorFactory)
        configurator = Mock(Configurator)
        configReader = Mock(ConfigReader)
        configurator.updateConfigFile({
            it == PolicyManagerServiceProperties.POLICY_MANAGER_PID
        }, _, _) >> {
            args -> policyManager.setPolicies(args[1])
        }

        stsConfig = [(StsServiceProperties.STS_CLAIMS_PROPS_KEY_CLAIMS): testClaims]
        configReader.getConfig(_) >> stsConfig

        policyManager = new PolicyManager()
        ListField<ContextPolicyBin> contextPolicies = new ListFieldImpl<>(ContextPolicyBin.class)
        contextPolicies.setValue(testData.policies)
        policyManager.setPolicies(new PolicyManagerServiceProperties().contextPoliciesToPolicyManagerProps(contextPolicies.getList()))

        configurator.commit(_, _) >> operationReport
        configReader.getServiceReference(_) >> policyManager
        configuratorFactory.getConfigurator() >> configurator
        configuratorFactory.getConfigReader() >> configReader
        queryProvider = new WcpmFieldProvider(configuratorFactory)
        action = queryProvider.getMutationFunction(SaveContextPolices.FUNCTION_FIELD_NAME)
    }

    def 'Pass with valid update'() {
        setup:
        operationReport.containsFailedResults() >> false

        when:
        action.setValue(testData)
        ReportWithResult report = action.getValue()

        then:
        report.messages().isEmpty()
        report.result().getValue() == testData.policies
    }

    def 'Fail when failed to persist'() {
        setup:
        operationReport.containsFailedResults() >> true

        when:
        action.setValue(testData)
        ReportWithResult report = action.getValue()

        then:
        report.messages().size() == 1
        report.messages()[0].code == DefaultMessages.FAILED_PERSIST
        report.messages()[0].path == [WcpmFieldProvider.NAME, SaveContextPolices.FUNCTION_FIELD_NAME]
        report.result() == null
    }

    def 'Fail if no root context is present'() {
        setup:
        operationReport.containsFailedResults() >> false
        testData.policies[0].paths = ['/test']

        when:
        action.setValue(testData)
        ReportWithResult report = action.getValue()

        then:
        report.messages().size() == 1
        report.messages()[0].code == SecurityMessages.NO_ROOT_CONTEXT
        report.messages()[0].path == [WcpmFieldProvider.NAME, SaveContextPolices.FUNCTION_FIELD_NAME, BaseFunctionField.ARGUMENT, 'policies']
        report.result() == null
    }

    def 'Fail if invalid authType'() {
        setup:
        operationReport.containsFailedResults() >> false
        testData.policies[0].authTypes = ['PKI', 'COFFEE']

        when:
        action.setValue(testData)
        ReportWithResult report = action.getValue()

        then:
        report.messages().size() == 1
        report.messages()[0].code == DefaultMessages.MISSING_REQUIRED_FIELD
        report.messages()[0].path == [WcpmFieldProvider.NAME, SaveContextPolices.FUNCTION_FIELD_NAME, BaseFunctionField.ARGUMENT, 'policies', ListField.INDEX_DELIMETER + 0, 'authTypes', ListField.INDEX_DELIMETER + 1]
        report.result() == null
    }

    def 'Fail if no authType'() {
        setup:
        operationReport.containsFailedResults() >> false
        testData.policies[0].authTypes = []

        when:
        action.setValue(testData)
        ReportWithResult report = action.getValue()

        then:
        report.messages().size() == 1
        report.messages()[0].code == DefaultMessages.MISSING_REQUIRED_FIELD
        report.messages()[0].path == [WcpmFieldProvider.NAME, SaveContextPolices.FUNCTION_FIELD_NAME, BaseFunctionField.ARGUMENT, 'policies', ListField.INDEX_DELIMETER + 0, 'authTypes']
        report.result() == null

    }

    def 'Fail if invalid realm'() {
        setup:
        operationReport.containsFailedResults() >> false
        testData.policies[0].realm = 'COFFEE'

        when:
        action.setValue(testData)
        ReportWithResult report = action.getValue()

        then:
        report.messages().size() == 1
        report.messages()[0].code == DefaultMessages.MISSING_REQUIRED_FIELD
        report.messages()[0].path == [WcpmFieldProvider.NAME, SaveContextPolices.FUNCTION_FIELD_NAME, BaseFunctionField.ARGUMENT, 'policies', ListField.INDEX_DELIMETER + 0, Realm.DEFAULT_FIELD_NAME]
        report.result() == null
    }

    def 'Fail if no realm'() {
        setup:
        operationReport.containsFailedResults() >> false
        testData.policies[0].realm = null

        when:
        action.setValue(testData)
        ReportWithResult report = action.getValue()

        then:
        report.messages().size() == 1
        report.messages()[0].code == DefaultMessages.MISSING_REQUIRED_FIELD
        report.messages()[0].path == [WcpmFieldProvider.NAME, SaveContextPolices.FUNCTION_FIELD_NAME, BaseFunctionField.ARGUMENT, 'policies', ListField.INDEX_DELIMETER + 0, Realm.DEFAULT_FIELD_NAME]
        report.result() == null
    }

    def 'Pass if no claims Mapping'() {
        setup:
        operationReport.containsFailedResults() >> false
        testData.policies[0].claimsMapping = []

        when:
        action.setValue(testData)
        ReportWithResult report = action.getValue()

        then:
        report.messages().isEmpty()
        report.result().getValue() == testData.policies
    }

    def 'Fail if claim entry with no value '() {
        setup:
        operationReport.containsFailedResults() >> false
        testData.policies[0].claimsMapping[0][ClaimsMapEntry.VALUE_FIELD_NAME] = null

        when:
        action.setValue(testData)
        ReportWithResult report = action.getValue()

        then:
        report.messages().size() == 1
        report.messages()[0].code == DefaultMessages.MISSING_REQUIRED_FIELD
        report.messages()[0].path == [WcpmFieldProvider.NAME, SaveContextPolices.FUNCTION_FIELD_NAME, BaseFunctionField.ARGUMENT, 'policies', ListField.INDEX_DELIMETER + 0, 'claimsMapping', ListField.INDEX_DELIMETER + 0, 'claimValue']
        report.result() == null
    }

    def 'Fail if claim is not supported'() {
        setup:
        operationReport.containsFailedResults() >> false
        testData.policies[0].claimsMapping[0][ClaimsMapEntry.KEY_FIELD_NAME] = 'unsupportedClaim'

        when:
        action.setValue(testData)
        ReportWithResult report = action.getValue()

        then:
        report.messages().size() == 1
        report.messages()[0].code == SecurityMessages.INVALID_CLAIM_TYPE
        report.messages()[0].path == [WcpmFieldProvider.NAME, SaveContextPolices.FUNCTION_FIELD_NAME, BaseFunctionField.ARGUMENT, 'policies', ListField.INDEX_DELIMETER + 0, 'claimsMapping', ListField.INDEX_DELIMETER + 0, "claim"]
        report.result() == null
    }
}