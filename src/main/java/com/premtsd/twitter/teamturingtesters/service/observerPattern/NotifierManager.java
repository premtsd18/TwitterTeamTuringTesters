package com.premtsd.twitter.teamturingtesters.service.observerPattern;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
public class NotifierManager {
    @Autowired
            private EmailNotify emailNotify;
    @Autowired
            private TerminalNotify terminalNotify;
    List<Notifier> observers = new ArrayList<Notifier>();
    @Autowired
    private WebPageNotify webPageNotify;

    @PostConstruct
    public void init() {
        // Add observers to the list after the bean is initialized
        addObserver(terminalNotify);
        addObserver(emailNotify);
        addObserver(webPageNotify);
    }

    public void addObserver(Notifier observer) {
        observers.add(observer);
    }
    public void removeObserver(Notifier observer) {
        observers.remove(observer);
    }

}
