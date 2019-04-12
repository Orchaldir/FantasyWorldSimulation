package jfws.feature.world.attribute.magic;

import jfws.feature.world.attribute.AttributeCell;
import jfws.feature.world.attribute.AttributeColorSelector;
import org.junit.jupiter.api.Test;


import static jfws.feature.world.attribute.magic.ManaLevel.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

class ManaLevelTest {
	@Test
	public void testGetters() {
		double levelWidth = ManaLevel.UTILITY.getLevelWidth();
		assertThat(levelWidth, is(closeTo(0.25, 0.0001)));
	}

	@Test
	public void testCreateColorSelector() {
		AttributeColorSelector<AttributeCell> colorSelector = createColorSelector(67);

		assertThat(colorSelector.getName(), is(equalTo(ATTRIBUTE_NAME)));
		assertThat(colorSelector.getIndex(), is(equalTo(67)));
		assertThat(colorSelector.getUtility(), is(equalTo(UTILITY)));
	}
}