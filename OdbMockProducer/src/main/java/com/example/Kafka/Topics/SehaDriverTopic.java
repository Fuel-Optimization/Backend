package com.example.Kafka.Topics;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
@Configuration
public class SehaDriverTopic {
    @Bean
    public NewTopic topic(){
        return TopicBuilder.name("FuelOpt")
                .build();
    }
}