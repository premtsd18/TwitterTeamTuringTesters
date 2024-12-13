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
    private StreamingMessageSender streamingMessageSender;



    @Override
    public void notify(UserDto user, String message) {

            SendEmailEventDto sendEmailEventDto=new SendEmailEventDto();
            sendEmailEventDto.setTo(user.getEmail());
            sendEmailEventDto.setSubject("Twitter Notification");
            sendEmailEventDto.setBody(message);

        streamingMessageSender.sendMessage(sendEmailEventDto);


    }
}
