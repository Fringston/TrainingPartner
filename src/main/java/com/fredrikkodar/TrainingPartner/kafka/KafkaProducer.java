package com.fredrikkodar.TrainingPartner.kafka;

import com.fredrikkodar.TrainingPartner.entities.Exercise;
import com.fredrikkodar.TrainingPartner.entities.MuscleGroup;
import com.fredrikkodar.TrainingPartner.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

//Klass som producerar meddelanden till Kafka
@Service
public class KafkaProducer {

    //Logger för att logga meddelanden
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);

    //Använder KafkaTemplate för att skicka meddelanden till Kafka
    private KafkaTemplate<String, User> kafkaTemplate;

    //Konstruktor som tar emot KafkaTemplate
    public KafkaProducer(KafkaTemplate<String, User> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    //Metod som skickar meddelanden till UserTopic i form av UserEntity-objekt
    public void sendUserMessage(User data) {
        //Loggar meddelandet
        LOGGER.info(String.format("Message sent %s", data.toString()));
        //Skapar ett meddelande med User som payload
        Message<User> message = MessageBuilder
                .withPayload(data)
                .setHeader(KafkaHeaders.TOPIC, "UserTopic")
                .build();
        //Skickar meddelandet till UserTopic
        kafkaTemplate.send(message);
    }

    //Metod som skickar meddelanden till ExerciseTopic i form av ExerciseEntity-objekt
    public void sendExerciseMessage(Exercise data) {
        //Loggar meddelandet
        LOGGER.info(String.format("Message sent %s", data.toString()));
        //Skapar ett meddelande med Exercise som payload
        Message<Exercise> message = MessageBuilder
                .withPayload(data)
                .setHeader(KafkaHeaders.TOPIC, "ExerciseTopic")
                .build();
        //Skickar meddelandet till ExerciseTopic
        kafkaTemplate.send(message);
    }

    //Metod som skickar meddelanden till MuscleGroupTopic i form av MuscleGroupEntity-objekt
    public void sendMuscleGroupMessage(MuscleGroup data) {
        //Loggar meddelandet
        LOGGER.info(String.format("Message sent %s", data.toString()));
        //Skapar ett meddelande med MuscleGroup som payload
        Message<MuscleGroup> message = MessageBuilder
                .withPayload(data)
                .setHeader(KafkaHeaders.TOPIC, "MuscleGroupTopic")
                .build();
        //Skickar meddelandet till MuscleGroupTopic
        kafkaTemplate.send(message);
    }
}