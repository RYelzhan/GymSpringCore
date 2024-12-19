package com.epam.wca.gym.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.ConnectionFactory;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@EnableJms
@RequiredArgsConstructor
public class MessagingConfig {
    private final ConnectionFactory connectionFactory;
    private final ObjectMapper objectMapper;

    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        var converter = new MappingJackson2MessageConverter();

        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        converter.setObjectMapper(objectMapper);

        return converter;
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        var jmsTemplate = new JmsTemplate();

        jmsTemplate.setConnectionFactory(connectionFactory);
        jmsTemplate.setMessageConverter(jacksonJmsMessageConverter());
        jmsTemplate.setSessionTransacted(true); // Ensures that message acknowledgment is transactional

        return jmsTemplate;
    }

    @Bean
    public DefaultJmsListenerContainerFactory defaultJmsListenerContainerFactory() {
        var factory = new DefaultJmsListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrency("1-1");
        factory.setSessionTransacted(true);
        factory.setTransactionManager(jmsTransactionManager());
        factory.setErrorHandler(t ->
                log.error("Handling error in listener for messages. Error: {}", t.getMessage())
        );

        return factory;
    }

    @Bean(name = "jmsTransactionManager")
    public PlatformTransactionManager jmsTransactionManager() {
        return new JmsTransactionManager(connectionFactory);
    }

    @Primary
    @Bean(name = "transactionManager")
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
