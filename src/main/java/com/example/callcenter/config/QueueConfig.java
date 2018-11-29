package com.example.callcenter.config;

import com.example.callcenter.service.Dispatcher;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.RedeliveryPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class QueueConfig {
    @Value("${activemq.brokerUrl}")
    private String brokerUrl;

    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL(brokerUrl);

        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        redeliveryPolicy.setMaximumRedeliveries(4);

        ActiveMQPrefetchPolicy policy = new ActiveMQPrefetchPolicy();
        policy.setQueuePrefetch(1);
        activeMQConnectionFactory.setPrefetchPolicy(policy);

        activeMQConnectionFactory.setRedeliveryPolicy(redeliveryPolicy);
        return activeMQConnectionFactory;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(activeMQConnectionFactory());
        factory.setConcurrency("10");
        return factory;
    }

    @Bean
    public CachingConnectionFactory cachingConnectionFactory() {
        return new CachingConnectionFactory(activeMQConnectionFactory());
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        return new JmsTemplate(cachingConnectionFactory());
    }

    @Bean
    public Dispatcher queue() {
        return new Dispatcher();
    }
}
