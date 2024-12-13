package com.premtsd.twitter.teamturingtesters.dto;

import lombok.Data;

@Data
public class NotificationDto {
    private long id;
    private String text;
    private boolean isSent;
    private String userName; // First-level user data
}
