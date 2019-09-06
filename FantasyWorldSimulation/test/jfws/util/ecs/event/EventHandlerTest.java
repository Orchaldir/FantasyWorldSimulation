package jfws.util.ecs.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class EventHandlerTest {

	private static final Integer EVENT_0 = 33;

	private EventSubscriber<Integer> subscriber0;
	private EventSubscriber<Integer> subscriber1;
	private EventSubscriber<Integer> subscriber2;

	private EventHandler<Integer> handler;

	@BeforeEach
	public void setUp() {
		subscriber0 = mock(EventSubscriber.class);
		subscriber1 = mock(EventSubscriber.class);
		subscriber2 = mock(EventSubscriber.class);

		handler = new EventHandler<>();
	}

	@Test
	public void testNoSubscribers() {
		handler.publish(EVENT_0);

		verifyZeroInteractions(subscriber0, subscriber1, subscriber2);
	}

	@Test
	public void testSubscribers() {
		handler.add(subscriber0);
		handler.add(subscriber1);
		handler.add(subscriber2);

		handler.publish(EVENT_0);

		verify(subscriber0, only()).update(EVENT_0);
		verify(subscriber1, only()).update(EVENT_0);
		verify(subscriber2, only()).update(EVENT_0);
		verifyNoMoreInteractions(subscriber0, subscriber1, subscriber2);
	}

}