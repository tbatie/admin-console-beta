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
package org.codice.ddf.admin.sources.opensearch;

import static java.net.HttpURLConnection.HTTP_OK;
import static org.codice.ddf.admin.api.handler.ConfigurationMessage.createInternalErrorMsg;
import static org.codice.ddf.admin.commons.sources.SourceHandlerCommons.DISCOVERED_URL;
import static org.codice.ddf.admin.commons.sources.SourceHandlerCommons.UNKNOWN_ENDPOINT;
import static org.codice.ddf.admin.commons.sources.SourceHandlerCommons.VERIFIED_CAPABILITIES;
import static org.codice.ddf.admin.commons.sources.SourceHandlerCommons.createCommonSourceConfigMsg;
import static org.codice.ddf.admin.commons.sources.SourceHandlerCommons.createDocument;

import java.util.List;
import java.util.Optional;

import javax.net.ssl.SSLPeerUnverifiedException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.codice.ddf.admin.api.config.sources.OpenSearchSourceConfiguration;
import org.codice.ddf.admin.api.handler.commons.UrlAvailability;
import org.codice.ddf.admin.api.handler.report.ProbeReport;
import org.codice.ddf.admin.api.handler.report.Report;
import org.codice.ddf.admin.commons.requests.RequestUtils;
import org.w3c.dom.Document;

import com.google.common.collect.ImmutableList;

public class OpenSearchSourceUtils {

    private static final List<String> OPENSEARCH_MIME_TYPES = ImmutableList.of(
            "application/atom+xml");

    private static final List<String> URL_FORMATS = ImmutableList.of(
            "https://%s:%d/services/catalog/query",
            "https://%s:%d/catalog/query",
            "http://%s:%d/services/catalog/query",
            "http://%s:%d/catalog/query");

    private static final String SIMPLE_QUERY_PARAMS = "?q=test&mr=1&src=local";

    private static final String TOTAL_RESULTS_XPATH = "//os:totalResults|//opensearch:totalResults";

    /**
     * Confirms whether or not an endpoint has Opensearch capabilities.
     * SUCCESS TYPES - VERIFIED_CAPABILITIES,
     * FAILURE TYPES - CANNOT_CONNECT, CERT_ERROR, UNKNOWN_ENDPOINT
     * WARNING TYPES - UNTRUSTED_CA
     * RETURN TYPES -  CONTENT_TYPE, CONTENT, STATUS_CODE
     *
     * @param url
     * @return report
     */
    public static Report verifyOpensearchCapabilites(String url, String username, String password) {
        ProbeReport requestResults = RequestUtils.sendGetRequest(url + SIMPLE_QUERY_PARAMS, username, password);
        if(requestResults.containsFailureMessages()) {
            return requestResults;
        }

        ProbeReport capabilitiesReport = new ProbeReport();
        int statusCode = requestResults.getProbeResult(RequestUtils.STATUS_CODE);
        String contentType = requestResults.getProbeResult(RequestUtils.CONTENT_TYPE);

        if (statusCode == HTTP_OK && OPENSEARCH_MIME_TYPES.contains(contentType)) {
            return capabilitiesReport.addMessage(createCommonSourceConfigMsg(UNKNOWN_ENDPOINT));
        }

        Document capabilitiesXml;
        try {
            capabilitiesXml = createDocument(requestResults.getProbeResult(RequestUtils.CONTENT));
        } catch (Exception e) {
            return capabilitiesReport.addMessage(createInternalErrorMsg("Unable to read response from endpoint."));
        }

        boolean isOpensearchResponse;
        XPath xpath = XPathFactory.newInstance().newXPath();
        try {
            isOpensearchResponse = (Boolean) xpath.compile(TOTAL_RESULTS_XPATH).evaluate(capabilitiesXml, XPathConstants.BOOLEAN
        } catch (XPathExpressionException e1) {
            return capabilitiesReport.addMessage(createCommonSourceConfigMsg(UNKNOWN_ENDPOINT));
        }

        return capabilitiesReport.addMessage(createCommonSourceConfigMsg(isOpensearchResponse ?
                VERIFIED_CAPABILITIES :
                UNKNOWN_ENDPOINT));
    }

    public static ProbeReport discoverOpensearchUrl(String hostname, int port, String username, String password) {
        for(String urlFormat : URL_FORMATS) {
            String testUrl = String.format(urlFormat, hostname, port);
            Report testReport = verifyOpensearchCapabilites(testUrl, username, password);
            if(!testReport.containsFailureMessages()) {
                return new ProbeReport(testReport.messages()).probeResult(DISCOVERED_URL, testUrl);
            }
        }
        return new ProbeReport(createCommonSourceConfigMsg(UNKNOWN_ENDPOINT));
    }

    public ProbeReport getPreferredOpensearchonfig(String url, String username, String password) {
            return new OpenSearchSourceConfiguration().endpointUrl(url);
    }

    //Given a config, returns the correct URL format for the endpoint if one exists
    public UrlAvailability confirmEndpointUrl(OpenSearchSourceConfiguration config) {
        Optional<UrlAvailability> result = URL_FORMATS.stream()
                .map(formatUrl -> String.format(formatUrl,
                        config.sourceHostName(),
                        config.sourcePort()))
                .map(url -> getUrlAvailability(url, config.sourceUserName(), config.sourceUserPassword()))
                .filter(avail -> avail.isAvailable() || avail.isCertError())
                .findFirst();
        return result.isPresent() ? result.get() : null;
    }

    // Given a configuration with and endpointUrl, determines if that URL is available as an OS source
    public UrlAvailability getUrlAvailability(String url, String un, String pw) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document responseXml = builder.parse(response
                    .getEntity()
                    .getContent());
            queryResponse = (Boolean) xpath.compile(TOTAL_RESULTS_XPATH).evaluate(responseXml, XPathConstants.BOOLEAN);
            status = response.getStatusLine().getStatusCode();
            contentType = response.getEntity().getContentType().getValue();
            if (status == HTTP_OK && OPENSEARCH_MIME_TYPES.contains(contentType) && queryResponse) {
                return result.trustedCertAuthority(true).certError(false).available(true);
            } else {
                return result.trustedCertAuthority(true)
                        .certError(false)
                        .available(false);
            }
        } catch (SSLPeerUnverifiedException e) {
            // This is the hostname != cert name case - if this occurs, the URL's SSL cert configuration
            // is incorrect, or a serious network security issue has occurred.
            return result.trustedCertAuthority(false)
                    .certError(true)
                    .available(false);
        } catch (Exception e) {
            try {
                closeClientAndResponse(client, response);
                client = getCloseableHttpClient(true);
                response = client.execute(request);
                status = response.getStatusLine()
                        .getStatusCode();
                contentType = response.getEntity()
                        .getContentType()
                        .getValue();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setNamespaceAware(true);
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document responseXml = builder.parse(response
                        .getEntity()
                        .getContent());
                queryResponse = (Boolean) xpath.compile(TOTAL_RESULTS_XPATH).evaluate(responseXml, XPathConstants.BOOLEAN);
                if (status == HTTP_OK && OPENSEARCH_MIME_TYPES.contains(contentType) && queryResponse) {
                    return result.trustedCertAuthority(false)
                            .certError(false)
                            .available(true);
                }
            } catch (Exception e1) {
                return result.trustedCertAuthority(false)
                        .certError(false)
                        .available(false);
            }
        } finally {
            closeClientAndResponse(client, response);
        }
        return result;
    }
}
