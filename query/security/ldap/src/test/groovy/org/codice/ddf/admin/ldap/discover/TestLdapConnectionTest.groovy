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
package org.codice.ddf.admin.ldap.discover

import org.codice.ddf.admin.api.action.ActionReport
import org.codice.ddf.admin.common.actions.BaseAction
import org.codice.ddf.admin.common.fields.common.HostnameField
import org.codice.ddf.admin.common.fields.common.PortField
import org.codice.ddf.admin.common.message.DefaultMessages
import org.codice.ddf.admin.ldap.LdapTestingCommons
import org.codice.ddf.admin.ldap.TestLdapServer
import org.codice.ddf.admin.ldap.actions.commons.LdapMessages
import org.codice.ddf.admin.ldap.actions.commons.LdapTestingUtils
import org.codice.ddf.admin.ldap.actions.discover.TestLdapConnection
import org.codice.ddf.admin.ldap.fields.connection.LdapConnectionField
import org.codice.ddf.admin.ldap.fields.connection.LdapEncryptionMethodField
import org.forgerock.opendj.ldap.SSLContextBuilder
import org.forgerock.opendj.ldap.TrustManagers
import spock.lang.Specification

import javax.net.ssl.SSLContext
import java.security.GeneralSecurityException
import java.security.NoSuchAlgorithmException

import static org.junit.Assert.fail

class TestLdapConnectionTest extends Specification {
    static TestLdapServer server
    Map<String, Object> args
    TestLdapConnection action

    def setupSpec() {
        server = TestLdapServer.getInstance()
        server.startListening()
    }

    def cleanupSpec() {
        server.shutdown()
        server = null
    }

    def setup() {
        LdapTestingCommons.loadLdapTestProperties()
        action = new TestLdapConnection()
    }

    def 'Fail on missing required connection info fields'() {
        setup:
        def baseMsg = [TestLdapConnection.ID, BaseAction.ARGUMENT]
        def missingHostMsgPath = baseMsg + [LdapConnectionField.DEFAULT_FIELD_NAME, HostnameField.DEFAULT_FIELD_NAME]
        def missingPortMsgPath = baseMsg + [LdapConnectionField.DEFAULT_FIELD_NAME, PortField.DEFAULT_FIELD_NAME]
        def missingEncryptMsgPath = baseMsg + [LdapConnectionField.DEFAULT_FIELD_NAME, LdapEncryptionMethodField.DEFAULT_FIELD_NAME]

        action = new TestLdapConnection()
        args = [(LdapConnectionField.DEFAULT_FIELD_NAME): new LdapConnectionField().getValue()]
        action.setArguments(args)

        when:
        ActionReport report = action.process()

        then:
        report.messages().size() == 3
        report.messages().count {
            it.getCode() == DefaultMessages.MISSING_REQUIRED_FIELD
        } == 3
        report.result() == null

        report.messages()*.getPath() as Set == [missingHostMsgPath, missingPortMsgPath, missingEncryptMsgPath] as Set
    }

    def 'Successfully connect without encryption'() {
        setup:
        args = [(LdapConnectionField.DEFAULT_FIELD_NAME): noEncryptionLdapConnectionInfo().getValue()]
        action.setArguments(args)

        when:
        ActionReport report = action.process()

        then:
        report.messages().empty
        report.result().getValue()
    }

    def 'Successfully connect using LDAPS protocol'() {
        setup:
        args = [(LdapConnectionField.DEFAULT_FIELD_NAME): ldapsLdapConnectionInfo().getValue()]
        action.setArguments(args)
        action.setTestingUtils(new LdapTestingUtilsMock())

        when:
        ActionReport report = action.process()

        then:
        report.messages().empty
        report.result().getValue()
    }

    def 'Successfully connect using startTls on insecure port (Should not upgrade)'() {
        setup:
        args = [(LdapConnectionField.DEFAULT_FIELD_NAME): startTlsLdapConnectionInfo(server.getLdapPort()).getValue()]
        action.setArguments(args)

        when:
        ActionReport report = action.process()

        then:
        // TODO: tbatie - 5/4/17 - need to figure out a way to check if the connection was successfully upgraded to TLS or has no encryption. This information should be relayed back to the user as a warning.
//        report.messages().size() == 1
//        report.messages().get(0).getCode() == (The expected return code)
//        report.messages().get(0).getPath() == (The expected msg path)
//        report.result().getValue() == true
        report.messages().size() == 0
        report.result().getValue()
    }

    def 'Successfully connect using startTls on LDAPS port (Should upgrade)'() {
        setup:
        args = [(LdapConnectionField.DEFAULT_FIELD_NAME): startTlsLdapConnectionInfo(server.getLdapSecurePort()).getValue()]
        action.setArguments(args)
        action.setTestingUtils(new LdapTestingUtilsMock())

        when:
        ActionReport report = action.process()

        then:
        report.messages().isEmpty()
        report.result().getValue()
    }

    def 'Fail to connect to LDAP (Bad hostname)'() {
        setup:
        args = [(LdapConnectionField.DEFAULT_FIELD_NAME): ldapsLdapConnectionInfo().hostname("badHostname").getValue()]
        action.setArguments(args)

        when:
        ActionReport report = action.process()

        then:
        report.messages().size() == 1
        !report.result().getValue()
        report.messages().get(0).getCode() == LdapMessages.CANNOT_CONNECT
        report.messages().get(0).getPath() == [TestLdapConnection.ID, BaseAction.ARGUMENT, LdapConnectionField.DEFAULT_FIELD_NAME]
    }

    def 'Fail to connect to LDAP (Bad port)'() {
        setup:
        args = [(LdapConnectionField.DEFAULT_FIELD_NAME): ldapsLdapConnectionInfo().port(666).getValue()]
        action.setArguments(args)

        when:
        ActionReport report = action.process()

        then:
        report.messages().size() == 1
        !report.result().getValue()
        report.messages().get(0).getCode() == LdapMessages.CANNOT_CONNECT
        report.messages().get(0).getPath() == [TestLdapConnection.ID, BaseAction.ARGUMENT, LdapConnectionField.DEFAULT_FIELD_NAME]
    }

    def 'Fail from internal error'() {
        setup:
        args = [(LdapConnectionField.DEFAULT_FIELD_NAME): ldapsLdapConnectionInfo().getValue()]
        action.setArguments(args)
        action.setTestingUtils(new LdapTestingUtilsMock(true))

        when:
        ActionReport report = action.process()

        then:
        report.messages().size() == 1
        !report.result().getValue()
        report.messages().get(0).getCode() == DefaultMessages.INTERNAL_ERROR
        report.messages().get(0).getPath() == [TestLdapConnection.ID]
    }

    LdapConnectionField noEncryptionLdapConnectionInfo() {
        return new LdapConnectionField()
                .hostname(server.getHostname())
                .port(server.getLdapPort())
                .encryptionMethod(LdapEncryptionMethodField.NONE)
    }

    LdapConnectionField ldapsLdapConnectionInfo() {
        return new LdapConnectionField()
                .hostname(server.getHostname())
                .port(server.getLdapSecurePort())
                .encryptionMethod(LdapEncryptionMethodField.LDAPS)
    }

    LdapConnectionField startTlsLdapConnectionInfo(int port) {
        return new LdapConnectionField()
                .hostname(server.getHostname())
                .port(port)
                .encryptionMethod(LdapEncryptionMethodField.START_TLS)
    }

    /**
     * Overrides the client context to accept untrusted certs
     */
    static class LdapTestingUtilsMock extends LdapTestingUtils {

        private boolean throwException

        LdapTestingUtilsMock(boolean throwSSLContextExcp) {
            super()
            this.throwException = throwSSLContextExcp
        }

        LdapTestingUtilsMock() {
            this(false)
        }

        @Override
        SSLContext getSslContext() throws NoSuchAlgorithmException {
            if(throwException) {
                throw new RuntimeException("Throwing error for " + LdapTestingUtilsMock.class + ".")
            }
            try {
                return new SSLContextBuilder().setTrustManager(TrustManagers.trustAll())
                        .getSSLContext()
            } catch (GeneralSecurityException e) {
                fail(e.getMessage())
                return null
            }
        }
    }
}
