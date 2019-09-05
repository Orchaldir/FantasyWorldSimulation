package jfws.util.ecs.event;

public interface EventSubscriber<T> {

	void update(T event);

}
