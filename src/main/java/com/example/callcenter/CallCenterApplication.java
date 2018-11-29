package com.example.callcenter;

import com.example.callcenter.service.Dispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;

@SpringBootApplication
@EnableJpaRepositories
@EnableJms
public class CallCenterApplication implements JmsListenerConfigurer {

    @Value("${queue.name}")
    private String queueName;

    @Value("${worker.name}")
    private String workerName;

    @Value("${worker.enabled}")
    private boolean workerEnabled;

    @Autowired
    private Dispatcher dispatcher;

    public static void main(String[] args) {
        SpringApplication.run(CallCenterApplication.class, args);
    }

    @Override
    public void configureJmsListeners(JmsListenerEndpointRegistrar jmsListenerEndpointRegistrar) {
        if (workerEnabled) {
            SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
            endpoint.setId(workerName);
            endpoint.setDestination(queueName);
            endpoint.setMessageListener(dispatcher);
            jmsListenerEndpointRegistrar.registerEndpoint(endpoint);
        }
    }
}
