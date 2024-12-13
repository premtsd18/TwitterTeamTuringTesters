package com.premtsd.twitter.teamturingtesters.utils;

import com.premtsd.twitter.teamturingtesters.dto.SendEmailEventDto;

public interface StreamingMessageSender {
    public void sendMessage(SendEmailEventDto sendEmailEventDto);
}
