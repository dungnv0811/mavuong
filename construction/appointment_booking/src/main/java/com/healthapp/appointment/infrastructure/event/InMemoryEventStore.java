package com.healthapp.appointment.infrastructure.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class InMemoryEventStore {
    private final List<Object> events = new ArrayList<>();
    private final ApplicationEventPublisher springPublisher;

    public InMemoryEventStore(ApplicationEventPublisher springPublisher) {
        this.springPublisher = springPublisher;
    }

    public void publish(Object event) {
        events.add(event);
        springPublisher.publishEvent(event);
    }

    public List<Object> allEvents() {
        return Collections.unmodifiableList(events);
    }
}


