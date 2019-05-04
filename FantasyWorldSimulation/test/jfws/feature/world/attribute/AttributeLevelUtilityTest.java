package jfws.feature.world.attribute;

import jfws.feature.world.attribute.temperature.TemperatureLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static jfws.feature.world.attribute.temperature.TemperatureLevel.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

class AttributeLevelUtilityTest {

	private static final double ERROR = 0.001;

	private AttributeLevelUtility<TemperatureLevel> utility;

	@BeforeEach
	public void setUp() {
		utility = new AttributeLevelUtility<>(TemperatureLevel.values());
	}

	@Test
	public void testGetters() {
		assertThat(utility.getLevelWidth(), is(closeTo(0.166666, ERROR)));
	}

	@Nested
	class TestGetValue {

		@Test
		public void testGetValue() {
			assertThat(utility.getValue(TEMPERATE), closeTo(utility.getLevelWidth() * 3.5, ERROR));
		}

		@Test
		public void testGetValueWithNull() {
			assertThrows(IllegalArgumentException.class, () -> utility.getValue(null));
		}
	}
}