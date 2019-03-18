package jfws.features.temperature;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static jfws.features.temperature.TemperatureLevel.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

class TemperatureLevelTest {

	@Nested
	class TestGetLevelIndex {

		@Test
		public void testGetLevelIndexBelowMinimum() {
			assertThat(getLevelIndex(-1.0), is(equalTo(0.0)));
		}

		@Test
		public void testGetLevelIndexAboveMaximum() {
			assertThat(getLevelIndex(1.5), is(equalTo(6.0)));
		}

		@Test
		public void testGetLevelIndex() {
			assertThat(getLevelIndex(LEVEL_WIDTH * 0), is(closeTo(0.0, 0.01)));
			assertThat(getLevelIndex(LEVEL_WIDTH * 1), is(closeTo(1.0, 0.01)));
			assertThat(getLevelIndex(LEVEL_WIDTH * 2), is(closeTo(2.0, 0.01)));
			assertThat(getLevelIndex(LEVEL_WIDTH * 3), is(closeTo(3.0, 0.01)));
			assertThat(getLevelIndex(LEVEL_WIDTH * 4), is(closeTo(4.0, 0.01)));
			assertThat(getLevelIndex(LEVEL_WIDTH * 5), is(closeTo(5.0, 0.01)));
			assertThat(getLevelIndex(LEVEL_WIDTH * 6), is(closeTo(6.0, 0.01)));
		}

		@Test
		public void testGetLevelIndexOfCenterOfTheRange() {
			assertThat(getLevelIndex(LEVEL_WIDTH * 0.5), is(closeTo(0.5, 0.01)));
			assertThat(getLevelIndex(LEVEL_WIDTH * 1.5), is(closeTo(1.5, 0.01)));
			assertThat(getLevelIndex(LEVEL_WIDTH * 2.5), is(closeTo(2.5, 0.01)));
			assertThat(getLevelIndex(LEVEL_WIDTH * 3.5), is(closeTo(3.5, 0.01)));
			assertThat(getLevelIndex(LEVEL_WIDTH * 4.5), is(closeTo(4.5, 0.01)));
			assertThat(getLevelIndex(LEVEL_WIDTH * 5.5), is(closeTo(5.5, 0.01)));
		}
	}

	@Nested
	class TestFrom {

		@Test
		public void testFromBelowMinimum() {
			assertThat(from(-1.0), is(equalTo(VERY_COLD)));
		}

		@Test
		public void testFromAboveMaximum() {
			assertThat(from(1.5), is(equalTo(VERY_HOT)));
		}

		@Test
		public void testFrom() {
			assertThat(from(LEVEL_WIDTH * 0), is(equalTo(VERY_COLD)));
			assertThat(from(LEVEL_WIDTH * 1), is(equalTo(COLD)));
			assertThat(from(LEVEL_WIDTH * 2), is(equalTo(COOL)));
			assertThat(from(LEVEL_WIDTH * 3), is(equalTo(TEMPERATE)));
			assertThat(from(LEVEL_WIDTH * 4), is(equalTo(WARM)));
			assertThat(from(LEVEL_WIDTH * 5), is(equalTo(HOT)));
			assertThat(from(LEVEL_WIDTH * 6), is(equalTo(VERY_HOT)));
		}
	}

	@Nested
	class TestGetColor {

		@Test
		public void testGetColorBelowMinimum() {
			assertThat(getColor(-1.0), is(equalTo(VERY_COLD.getColor())));
		}

		@Test
		public void testGetColorAboveMaximum() {
			assertThat(getColor(1.5), is(equalTo(VERY_HOT.getColor())));
		}

		@Test
		public void testGetColor() {
			assertThat(getColor(LEVEL_WIDTH * 0), is(equalTo(VERY_COLD.getColor())));
			assertThat(getColor(LEVEL_WIDTH * 1), is(equalTo(COLD.getColor())));
			assertThat(getColor(LEVEL_WIDTH * 2), is(equalTo(COOL.getColor())));
			assertThat(getColor(LEVEL_WIDTH * 3), is(equalTo(TEMPERATE.getColor())));
			assertThat(getColor(LEVEL_WIDTH * 4), is(equalTo(WARM.getColor())));
			assertThat(getColor(LEVEL_WIDTH * 5), is(equalTo(HOT.getColor())));
			assertThat(getColor(LEVEL_WIDTH * 6), is(equalTo(VERY_HOT.getColor())));
		}

		@Test
		public void testGetColorInterpolation() {
			Color color = getColor(LEVEL_WIDTH * 4.5);

			assertThat(color.getBlue(), is(equalTo(0.0)));
			assertThat(color.getGreen(), is(closeTo(0.823, 0.001)));
			assertThat(color.getRed(), is(equalTo(1.0)));
		}
	}
}