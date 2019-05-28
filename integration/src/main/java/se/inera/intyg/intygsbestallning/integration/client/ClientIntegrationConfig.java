/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.inera.intyg.intygsbestallning.integration.client;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transport.http.HttpConduitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import se.inera.intyg.intygsbestallning.common.property.IntegrationProperties;
import se.inera.intyg.intygsbestallning.common.property.NtjpWsProperties;
import se.riv.intygsbestallning.certificate.order.respondtoorder.v1.rivtabp21.RespondToOrderResponderInterface;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

@Configuration
@ComponentScan(basePackages = "se.inera.intyg.intygsbestallning.integration.client")
@Profile("!myndighet-stub")
@EnableConfigurationProperties({ NtjpWsProperties.class, IntegrationProperties.class })
public class ClientIntegrationConfig {

    private static final Logger LOG = LoggerFactory.getLogger(ClientIntegrationConfig.class);

    private final IntegrationProperties integrationProperties;
    private final NtjpWsProperties ntjpWsProperties;

    public ClientIntegrationConfig(IntegrationProperties integrationProperties, NtjpWsProperties ntjpWsProperties) {
        this.integrationProperties = integrationProperties;
        this.ntjpWsProperties = ntjpWsProperties;
    }

    @Bean
    public RespondToOrderResponderInterface respondToOrderClient(
            @Qualifier("ntjpConduitConfig") final HttpConduitConfig ntjpConduitConfig) {

        LOG.info("Instantiating respondToOrderClient");

        JaxWsProxyFactoryBean proxyFactoryBean = new JaxWsProxyFactoryBean();
        proxyFactoryBean.setAddress(integrationProperties.getMyndighetIntegrationUrl() + integrationProperties.getRespondToOrderUrl());
        proxyFactoryBean.setServiceClass(RespondToOrderResponderInterface.class);

        RespondToOrderResponderInterface respondToOrderResponderInterface = (RespondToOrderResponderInterface) proxyFactoryBean.create();

        final Client client = ClientProxy.getClient(respondToOrderResponderInterface);
        final HTTPConduit httpConduit = (HTTPConduit) client.getConduit();
        ntjpConduitConfig.apply(httpConduit);

        return respondToOrderResponderInterface;
    }

    @Bean("ntjpConduitConfig")
    public HttpConduitConfig ntjpConduitConfig(@Qualifier("ntjpKeyManager") final KeyManager[] ntjpKeyManager,
            @Qualifier("ntjpTrustManager") final TrustManager[] ntjpTrustManager) {

        LOG.info("Instantiating ntjpConduitConfig");

        final TLSClientParameters tlsClientParameters = new TLSClientParameters();
        tlsClientParameters.setTrustManagers(ntjpTrustManager);
        tlsClientParameters.setKeyManagers(ntjpKeyManager);

        final HttpConduitConfig config = new HttpConduitConfig();
        config.setTlsClientParameters(tlsClientParameters);

        return config;
    }

    @Bean("ntjpKeyManager")
    // Needed to due bug in Spotbugs. https://github.com/spotbugs/spotbugs/issues/756
    @SuppressFBWarnings("RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE")
    public KeyManager[] ntjpKeyManager()
            throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException, IOException, CertificateException {

        NtjpWsProperties.NtjpCertificate certificate = ntjpWsProperties.getCertificate();
        char[] password = certificate.getPassword().toCharArray();

        try (InputStream is = certificate.getFile().getInputStream()) {
            KeyStore keyStore = KeyStore.getInstance(certificate.getType());
            keyStore.load(is, password);

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, password);

            return keyManagerFactory.getKeyManagers();
        }
    }

    @Bean("ntjpTrustManager")
    // Needed to due bug in Spotbugs. https://github.com/spotbugs/spotbugs/issues/756
    @SuppressFBWarnings("RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE")
    public TrustManager[] ntjpTrustManager() throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {

        NtjpWsProperties.NtjpTruststore truststore = ntjpWsProperties.getTruststore();
        char[] password = truststore.getPassword().toCharArray();

        try (InputStream is = truststore.getFile().getInputStream()) {
            KeyStore keyStore = KeyStore.getInstance(truststore.getType());
            keyStore.load(is, password);

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            return trustManagerFactory.getTrustManagers();
        }
    }
}
