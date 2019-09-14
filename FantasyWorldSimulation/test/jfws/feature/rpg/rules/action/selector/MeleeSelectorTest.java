package jfws.feature.rpg.rules.action.selector;

import jfws.feature.rpg.rules.unit.Trait;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MeleeSelectorTest {

	private static final TargetSituation TARGET_SELF = new TargetSituation(0, 0, 0);
	private static final TargetSituation TARGET_OTHER = new TargetSituation(0, 1, 0);

	private static final int MODIFIER =  55;
	private static final Optional<Trait> TRAIT = Optional.of(new Trait("test"));

	private MeleeSelector selector;

	@BeforeEach
	public void setUp() {
		selector = new MeleeSelector(2, MODIFIER, TRAIT);
	}

	@Test
	public void testCanNotTargetSelf() {
		assertFalse(selector.canSelectEntity(TARGET_SELF));
	}

	@Test
	public void testDifferentDistances() {
		testCanSelectEntity(0, true);
		testCanSelectEntity(1, true);
		testCanSelectEntity(2, true);
		testCanSelectEntity(3, false);
	}

	private void testCanSelectEntity(int distance, boolean canSelect) {
		TargetSituation situation = new TargetSituation(3, 9, distance);

		assertThat(selector.canSelectEntity(situation), is(canSelect));
		assertTrue(selector.requiresToHitCheck());
		assertThat(selector.getToHitModifier(situation), is(MODIFIER));
		assertThat(selector.getToHitTrait(), is(TRAIT));
	}

}