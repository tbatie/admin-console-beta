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
package org.codice.ddf.admin.sources.csw

import org.apache.http.Header
import org.apache.http.HttpEntity
import org.apache.http.StatusLine
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.impl.client.CloseableHttpClient
import org.codice.ddf.admin.api.config.sources.CswSourceConfiguration
import spock.lang.Specification

import javax.net.ssl.SSLPeerUnverifiedException

import static org.codice.ddf.admin.api.services.CswServiceProperties.*

class CswSourceUtilsTest extends Specification {

//    def utils
//    def client = Mock(CloseableHttpClient);
//    def response = Mock(CloseableHttpResponse)
//    def statusLine = Mock(StatusLine)
//    def entity = Mock(HttpEntity)
//    def cType = Mock(Header)
//    def configuration = Mock(CswSourceConfiguration)
//
//    def metacardXml = this.getClass().getClassLoader().getResourceAsStream('metacardGetCapabilities.xml')
//    def gmdXml = this.getClass().getClassLoader().getResourceAsStream('gmdGetCapabilities.xml')
//    def specXml = this.getClass().getClassLoader().getResourceAsStream('specGetCapabilities.xml')
//
//    def setup() {
//        utils = Spy(CswSourceUtils) { getCloseableHttpClient(_) >> client }
//    }
//
//    // Tests for getUrlAvailability
//    def 'test happy path trusted CA'() {
//        setup:
//        when:
//        def urlAvail = utils.getUrlAvailability("testUrl", null, null)
//
//        then:
//        1 * client.execute(_) >> response
//        1 * response.getStatusLine() >> statusLine
//        1 * statusLine.getStatusCode() >> 200
//        1 * response.getEntity() >> entity
//        1 * entity.getContentType() >> cType
//        1 * cType.getValue() >> "text/xml"
//
//        assert urlAvail.isAvailable()
//        assert !urlAvail.isCertError()
//        assert urlAvail.isTrustedCertAuthority()
//    }
//
//    def 'test bad return code noTrustClient' () {
//        when:
//        def urlAvail = utils.getUrlAvailability("testUrl", null, null)
//
//        then:
//        1 * client.execute(_) >> response
//        1 * response.getStatusLine() >> statusLine
//        1 * statusLine.getStatusCode() >> 405
//        1 * response.getEntity() >> entity
//        1 * entity.getContentType() >> cType
//        1 * cType.getValue() >> "application/xml"
//        assert !urlAvail.isAvailable()
//        assert !urlAvail.isCertError()
//        assert urlAvail.isTrustedCertAuthority()
//    }
//
//    def 'test bad mime type noTrustClient' () {
//        when:
//        def urlAvail = utils.getUrlAvailability("testUrl", null, null)
//
//        then:
//        1 * client.execute(_) >> response
//        1 * response.getStatusLine() >> statusLine
//        1 * statusLine.getStatusCode() >> 200
//        1 * response.getEntity() >> entity
//        1 * entity.getContentType() >> cType
//        1 * cType.getValue() >> "application/json"
//        assert !urlAvail.isAvailable()
//        assert !urlAvail.isCertError()
//        assert urlAvail.isTrustedCertAuthority()
//    }
//
//    def 'test cert error with noTrustClient'() {
//        when:
//        def urlAvail = utils.getUrlAvailability("testUrl", null, null)
//
//        then:
//        1 * client.execute(_) >> {throw new SSLPeerUnverifiedException("test")}
//        assert !urlAvail.isAvailable()
//        assert urlAvail.isCertError()
//        assert !urlAvail.isTrustedCertAuthority()
//
//    }
//
//    def 'test good path trustClient'() {
//        when:
//        def urlAvail = utils.getUrlAvailability("testUrl", null, null)
//
//        then:
//        2 * client.execute(_) >> {throw new IOException("exception")} >> response
//        1 * response.getStatusLine() >> statusLine
//        1 * statusLine.getStatusCode() >> 200
//        1 * response.getEntity() >> entity
//        1 * entity.getContentType() >> cType
//        1 * cType.getValue() >> "text/xml"
//
//        assert urlAvail.isAvailable()
//        assert !urlAvail.isCertError()
//        assert !urlAvail.isTrustedCertAuthority()
//    }
//
//    def 'test bad return code trustClient'() {
//        when:
//        def urlAvail = utils.getUrlAvailability("testUrl", null, null)
//
//        then:
//        2 * client.execute(_) >> {throw new IOException("exception")} >> response
//        1 * response.getStatusLine() >> statusLine
//        1 * statusLine.getStatusCode() >> 405
//        1 * response.getEntity() >> entity
//        1 * entity.getContentType() >> cType
//        1 * cType.getValue() >> "application/xml"
//        assert !urlAvail.isAvailable()
//        assert !urlAvail.isCertError()
//        assert !urlAvail.isTrustedCertAuthority()
//    }
//
//    def 'test bad mime type trustClient'() {
//        when:
//        def urlAvail = utils.getUrlAvailability("testUrl", null, null)
//
//        then:
//        2 * client.execute(_) >> {throw new IOException("exception")} >> response
//        1 * response.getStatusLine() >> statusLine
//        1 * statusLine.getStatusCode() >> 200
//        1 * response.getEntity() >> entity
//        1 * entity.getContentType() >> cType
//        1 * cType.getValue() >> "application/json"
//        assert !urlAvail.isAvailable()
//        assert !urlAvail.isCertError()
//        assert !urlAvail.isTrustedCertAuthority()
//    }
//
//    def 'test failure to connect'() {
//        when:
//        def urlAvail = utils.getUrlAvailability("testUrl", null, null)
//
//        then:
//        2 * client.execute(_) >> {throw new IOException("exception")}
//        assert !urlAvail.isAvailable()
//        assert !urlAvail.isCertError()
//        assert !urlAvail.isTrustedCertAuthority()
//    }
//
//    // Tests for getPreferredConfig
//
//    def 'test empty response'() {
//        when:
//        def config = utils.getPreferredCswConfig(configuration)
//
//        then:
//        configuration.endpointUrl() >> "test"
//        1 * client.execute(_) >> response
//        1 * response.getEntity() >> entity
//        1 * entity.getContent() >> ""
//
//        config == Optional.empty()
//    }
//
//    def 'test parse metacard:uri config'() {
//       when:
//        def config = utils.getPreferredConfig(configuration)
//
//        then:
//        configuration.endpointUrl() >> "test"
//        1 * client.execute(_) >> response
//        1 * response.getEntity() >> entity
//        1 * entity.getContent() >> metacardXml
//
//        assert config.isPresent()
//        config.get().factoryPid() == CSW_PROFILE_FACTORY_PID
//    }
//
//    def 'test parse GMD config'() {
//        when:
//        def config = utils.getPreferredCswConfig(configuration)
//
//        then:
//        configuration.endpointUrl() >> "test"
//        1 * client.execute(_) >> response
//        1 * response.getEntity() >> entity
//        1 * entity.getContent() >> gmdXml
//
//        assert config.isPresent()
//        config.get().factoryPid() == CSW_GMD_FACTORY_PID
//    }
//
//    def 'test no metacard or GMD parse config'() {
//        when:
//        def config = utils.getPreferredConfig(configuration)
//
//        then:
//        configuration.endpointUrl() >> "test"
//        1 * client.execute(_) >> response
//        1 * response.getEntity() >> entity
//        1 * entity.getContent() >> specXml
//
//        assert config.isPresent()
//        config.get().factoryPid() == CSW_SPEC_FACTORY_PID
//    }
//
//    // Tests for discover URL
//    def 'test no URL selected with bad hostname/port'() {
//        when:
//        def endpointUrl = utils.confirmEndpointUrl(configuration)
//
//        then:
//        _ * configuration.sourceHostName() >> "test"
//        _ * configuration.sourcePort() >> 443
//        _ * client.execute(_) >> {throw new IOException()}
//        endpointUrl == null
//    }
//
//    def 'test URL created with no cert error'() {
//        when:
//        def endpointUrl = utils.confirmEndpointUrl(configuration)
//
//        then:
//        _ * configuration.sourceHostName() >> "test"
//        _ * configuration.sourcePort() >> 443
//        1 * client.execute(_) >> response
//        1 * response.getStatusLine() >> statusLine
//        1 * statusLine.getStatusCode() >> 200
//        1 * response.getEntity() >> entity
//        1 * entity.getContentType() >> cType
//        1 * cType.getValue() >> "text/xml"
//
//        endpointUrl != null
//        endpointUrl.getUrl() == "https://test:443/services/csw"
//    }
}