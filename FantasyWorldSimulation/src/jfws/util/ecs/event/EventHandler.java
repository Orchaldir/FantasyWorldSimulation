package jfws.util.ecs.event;

import java.util.HashSet;
import java.util.Set;

public class EventHandler<T> implements EventPublisher<T> {

	private Set<EventSubscriber<T>> subscribers = new HashSet<>();

	public void add(EventSubscriber<T> subscriber) {
		subscribers.add(subscriber);
	}

	@Override
	public void publish(T event) {
		for (EventSubscriber<T> subscriber : subscribers) {
			subscriber.update(event);
		}
	}

}
