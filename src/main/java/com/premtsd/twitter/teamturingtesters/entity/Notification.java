package com.premtsd.twitter.teamturingtesters.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public boolean isSent;
    public String text;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public User user;
}
