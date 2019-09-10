package jfws.feature.rpg.system;

import jfws.feature.rpg.component.Health;
import jfws.feature.rpg.component.Statistics;
import jfws.feature.rpg.event.Hit;
import jfws.feature.rpg.event.Killed;
import jfws.feature.rpg.rpg.Damage;
import jfws.feature.rpg.rules.unit.Trait;
import jfws.feature.rpg.rules.unit.TraitChecker;
import jfws.util.ecs.component.ComponentStorage;
import jfws.util.ecs.event.EventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class HealthSystemTest {

	private static final Trait TOUGHNESS_TRAIT = new Trait("Toughness");

	private static final int ATTACKER_ID = 0;
	private static final int TARGET_ID = 1;

	private static final int DAMAGE = 5;
	private static final int TOUGHNESS = 6;

	private static final Hit EVENT = new Hit(new Damage(DAMAGE), ATTACKER_ID, TARGET_ID);

	private ComponentStorage<Health> healthStorage;
	private ComponentStorage<Statistics> statisticsStorage;
	private Health health;
	private Statistics statistics;
	private TraitChecker checker;
	private EventPublisher<Killed> killedPublisher;

	private HealthSystem system;

	@BeforeEach
	public void setUp() {
		healthStorage = mock(ComponentStorage.class);
		statisticsStorage = mock(ComponentStorage.class);
		health = mock(Health.class);
		statistics = mock(Statistics.class);
		checker = mock(TraitChecker.class);
		killedPublisher = mock(EventPublisher.class);

		system = new HealthSystem(healthStorage, statisticsStorage, checker, TOUGHNESS_TRAIT, killedPublisher);
	}

	@Test
	public void testNotHealthComponent() {
		Hit event = new Hit(new Damage(DAMAGE), ATTACKER_ID, 4);

		NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> system.update(event));

		assertThat(exception.getMessage(), is("Entity '4' has no health component!"));
	}

	@Test
	public void testNotHurt() {
		int penalty = 1;
		setup(penalty, 2);

		system.update(EVENT);

		verify(checker, times(1)).check(TOUGHNESS - penalty, DAMAGE);
		verify(health, times(1)).getToughnessPenalty();
		verifyNoMoreInteractions(health);
		verifyZeroInteractions(killedPublisher);
	}

	@Test
	public void testHurtBySuccess() {
		testHurt(1);
	}

	@Test
	public void testHurtByDraw() {
		testHurt(0);
	}

	@Test
	public void testKilled() {
		setup(0, -1);

		system.update(EVENT);

		verify(health, times(1)).getToughnessPenalty();
		verifyNoMoreInteractions(health);

		ArgumentCaptor<Killed> captor = ArgumentCaptor.forClass(Killed.class);
		verify(killedPublisher, times(1)).publish(captor.capture());
		verifyNoMoreInteractions(killedPublisher);

		Killed killedEvent = captor.getValue();
		assertThat(killedEvent.killerId, is(ATTACKER_ID));
		assertThat(killedEvent.targetId, is(TARGET_ID));
	}

	private void testHurt(int i) {
		int penalty = 0;
		setup(penalty, i);

		system.update(EVENT);

		verify(checker, times(1)).check(TOUGHNESS - penalty, DAMAGE);
		verify(health, times(1)).getToughnessPenalty();
		verify(health, times(1)).increaseToughnessPenalty();
		verifyNoMoreInteractions(health);
		verifyZeroInteractions(killedPublisher);
	}

	private void setup(int penalty, int checkResult) {
		when(healthStorage.get(TARGET_ID)).thenReturn(Optional.of(health));
		when(statisticsStorage.get(TARGET_ID)).thenReturn(Optional.of(statistics));

		when(health.getToughnessPenalty()).thenReturn(penalty);
		when(statistics.getRank(TOUGHNESS_TRAIT)).thenReturn(TOUGHNESS);

		when(checker.check(TOUGHNESS - penalty, DAMAGE)).thenReturn(checkResult);
	}

}