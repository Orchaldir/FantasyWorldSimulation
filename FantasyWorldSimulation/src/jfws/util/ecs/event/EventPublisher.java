package jfws.util.ecs.event;

public interface EventPublisher<T> {

	void publish(T event);

}
