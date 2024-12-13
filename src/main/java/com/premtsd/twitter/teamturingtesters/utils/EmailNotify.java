package com.premtsd.twitter.teamturingtesters.utils;

import com.premtsd.twitter.teamturingtesters.dto.SendEmailEventDto;
import com.premtsd.twitter.teamturingtesters.dto.UserDto;
import com.premtsd.twitter.teamturingtesters.entity.User;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class EmailNotify implements Observer{

    @Autowired
    public KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private static ObjectMapper objectMapper;



    @Override
    public void notify(UserDto user, String message) {

            objectMapper = new ObjectMapper();
            SendEmailEventDto sendEmailEventDto=new SendEmailEventDto();
            sendEmailEventDto.setTo(user.getEmail());
            sendEmailEventDto.setSubject("Twitter Notification");
            sendEmailEventDto.setBody(message);
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
