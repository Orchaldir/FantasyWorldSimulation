package jfws.feature.rpg.rules.action.selector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

class SelfSelectorTest {

	private static final TargetSituation TARGET_SELF = new TargetSituation(0, 0, 0);
	private static final TargetSituation TARGET_OTHER = new TargetSituation(0, 1, 0);

	private SelfSelector selector;

	@BeforeEach
	public void setUp() {
		selector = new SelfSelector();
	}

	@Test
	public void testTargetSelf() {
		assertTrue(selector.canSelectEntity(TARGET_SELF));
		assertFalse(selector.requiresTraitCheck());
		assertThat(selector.getModifier(TARGET_SELF), is(0));
		assertThat(selector.getTrait(), is(Optional.empty()));
	}

	@Test
	public void testTargetOther() {
		assertFalse(selector.canSelectEntity(TARGET_OTHER));
		assertFalse(selector.requiresTraitCheck());
		assertThat(selector.getModifier(TARGET_OTHER), is(0));
		assertThat(selector.getTrait(), is(Optional.empty()));
	}

}