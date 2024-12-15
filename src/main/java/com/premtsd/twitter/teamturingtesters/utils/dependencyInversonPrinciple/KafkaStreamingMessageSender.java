package com.premtsd.twitter.teamturingtesters.utils.dependencyInversonPrinciple;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.premtsd.twitter.teamturingtesters.dto.SendEmailEventDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaStreamingMessageSender implements StreamingMessageSender {

    @Autowired
    public KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void sendMessage(SendEmailEventDto sendEmailEventDto) {
        String str="";
        try {
            str = objectMapper.writeValueAsString(sendEmailEventDto);
        }catch (Exception e){
            System.out.println("Error in json serialization");
        }

        kafkaTemplate.send("topic1", str);
        try {
            SendEmailEventDto event = objectMapper.readValue(str, SendEmailEventDto.class);
        }catch (Exception e){
            System.out.println("Error in json deserialization");
        }
    }
}
