package com.premtsd.twitter.teamturingtesters.utils.observerPattern;

import com.premtsd.twitter.teamturingtesters.dto.SendEmailEventDto;
import com.premtsd.twitter.teamturingtesters.dto.UserDto;
import com.premtsd.twitter.teamturingtesters.utils.dependencyInversonPrinciple.StreamingMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailNotify implements Notifier {


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
