package com.healthapp.appointment.infrastructure.event;

import org.springframework.stereotype.Component;

@Component
public class EventPublisher {
    private final InMemoryEventStore eventStore;

    public EventPublisher(InMemoryEventStore eventStore) {
        this.eventStore = eventStore;
    }

    public void publish(Object event) {
        eventStore.publish(event);
    }
}


