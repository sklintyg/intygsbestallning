package se.inera.intyg.intygsbestallning.integration.client;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import se.riv.intygsbestallning.certificate.order.respondtoorder.v1.rivtabp21.RespondToOrderResponderInterface;
import se.riv.intygsbestallning.certificate.order.respondtoorder.v1.rivtabp21.RespondToOrderResponderService;
import se.riv.intygsbestallning.certificate.order.respondtoperformerrequest.v1.rivtabp21.RespondToPerformerRequestResponderInterface;
import se.inera.intyg.intygsbestallning.integration.IntegrationProperties;

@Configuration
@ComponentScan(basePackages = "se.inera.intyg.intygsbestallning.integration.client")
@Profile("!myndighet-stub")
public class ClientIntegrationConfig {

    @Autowired
    private IntegrationProperties properties;

    @Bean
    public RespondToOrderResponderService respondToOrderClient() {
        JaxWsProxyFactoryBean proxyFactoryBean = new JaxWsProxyFactoryBean();
        proxyFactoryBean.setAddress(properties.getMyndighetIntegrationUrl() + properties.getRespondToOrderUrl());
        proxyFactoryBean.setServiceClass(RespondToPerformerRequestResponderInterface.class);
        return (RespondToOrderResponderService) proxyFactoryBean.create();
    }
}
