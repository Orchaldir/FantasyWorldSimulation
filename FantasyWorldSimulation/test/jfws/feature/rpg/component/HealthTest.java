package jfws.feature.rpg.component;

import org.junit.jupiter.api.Test;

import static jfws.feature.rpg.component.Health.TOUGHNESS_PENALTY_CAN_NOT_BE_NEGATIVE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HealthTest {

	@Test
	public void testInitToughnessPenaltyBelowZero() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Health(-1));

		assertThat(exception.getMessage(), is(TOUGHNESS_PENALTY_CAN_NOT_BE_NEGATIVE));
	}

	@Test
	public void testIncreaseToughnessPenalty() {
		Health health = new Health(2);
		health.increaseToughnessPenalty();

		assertThat(health.getToughnessPenalty(), is(3));
	}

	@Test
	public void testDecreaseToughnessPenalty() {
		Health health = new Health(7);
		health.decreaseToughnessPenalty();

		assertThat(health.getToughnessPenalty(), is(6));
	}

	@Test
	public void testDecreaseToughnessPenaltyBelowZero() {
		Health health = new Health(0);

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> health.decreaseToughnessPenalty());

		assertThat(exception.getMessage(), is(TOUGHNESS_PENALTY_CAN_NOT_BE_NEGATIVE));
	}

}