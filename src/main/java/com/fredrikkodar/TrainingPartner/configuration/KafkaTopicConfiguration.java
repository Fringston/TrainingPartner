package com.fredrikkodar.TrainingPartner.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

//Klass som skapar ämnen i Kafka
@Configuration
public class KafkaTopicConfiguration {

    //Skapar ett ämne för användaruppgifter
    @Bean
    public NewTopic userTopic() {
        return TopicBuilder.name("UserTopic").build();
    }

    //Skapar ett ämne för övningar
    @Bean
    public NewTopic exerciseTopic() {
        return TopicBuilder.name("ExerciseTopic").build();
    }

    //Skapar ett ämne för muskelgrupper
    @Bean
    public NewTopic muscleGroupTopic() {return TopicBuilder.name("MuscleGroupTopic").build();
    }
}