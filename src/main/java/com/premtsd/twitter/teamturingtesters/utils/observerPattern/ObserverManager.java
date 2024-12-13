package com.premtsd.twitter.teamturingtesters.utils.observerPattern;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
public class ObserverManager {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
            private EmailNotify emailNotify;
    @Autowired
            private TerminalNotify terminalNotify;
    List<Observer> observers = new ArrayList<Observer>();
    @Autowired
    private WebPageNotify webPageNotify;

    @PostConstruct
    public void init() {
        // Add observers to the list after the bean is initialized
        addObserver(terminalNotify);
        addObserver(emailNotify);
        addObserver(webPageNotify);
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

}
