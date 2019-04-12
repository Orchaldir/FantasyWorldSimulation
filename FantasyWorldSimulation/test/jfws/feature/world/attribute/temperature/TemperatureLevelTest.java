package jfws.feature.world.attribute.temperature;

import javafx.scene.paint.Color;
import jfws.feature.world.attribute.AttributeCell;
import jfws.feature.world.attribute.AttributeColorSelector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static jfws.feature.world.attribute.temperature.TemperatureLevel.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

class TemperatureLevelTest {
	
	private double levelWidth;

	@BeforeEach
	public void setUp() {
		levelWidth = TemperatureLevel.UTILITY.getLevelWidth();
	}

	@Test
	public void testGetters() {
		assertThat(levelWidth, is(closeTo(0.166666, 0.0001)));
	}

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
			assertThat(getLevelIndex(levelWidth * 0), is(closeTo(0.0, 0.01)));
			assertThat(getLevelIndex(levelWidth * 1), is(closeTo(1.0, 0.01)));
			assertThat(getLevelIndex(levelWidth * 2), is(closeTo(2.0, 0.01)));
			assertThat(getLevelIndex(levelWidth * 3), is(closeTo(3.0, 0.01)));
			assertThat(getLevelIndex(levelWidth * 4), is(closeTo(4.0, 0.01)));
			assertThat(getLevelIndex(levelWidth * 5), is(closeTo(5.0, 0.01)));
			assertThat(getLevelIndex(levelWidth * 6), is(closeTo(6.0, 0.01)));
		}

		@Test
		public void testGetLevelIndexOfCenterOfTheRange() {
			assertThat(getLevelIndex(levelWidth * 0.5), is(closeTo(0.5, 0.01)));
			assertThat(getLevelIndex(levelWidth * 1.5), is(closeTo(1.5, 0.01)));
			assertThat(getLevelIndex(levelWidth * 2.5), is(closeTo(2.5, 0.01)));
			assertThat(getLevelIndex(levelWidth * 3.5), is(closeTo(3.5, 0.01)));
			assertThat(getLevelIndex(levelWidth * 4.5), is(closeTo(4.5, 0.01)));
			assertThat(getLevelIndex(levelWidth * 5.5), is(closeTo(5.5, 0.01)));
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
			assertThat(from(levelWidth * 0), is(equalTo(VERY_COLD)));
			assertThat(from(levelWidth * 1), is(equalTo(COLD)));
			assertThat(from(levelWidth * 2), is(equalTo(COOL)));
			assertThat(from(levelWidth * 3), is(equalTo(TEMPERATE)));
			assertThat(from(levelWidth * 4), is(equalTo(WARM)));
			assertThat(from(levelWidth * 5), is(equalTo(HOT)));
			assertThat(from(levelWidth * 6), is(equalTo(VERY_HOT)));
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
			assertThat(getColor(levelWidth * 0), is(equalTo(VERY_COLD.getColor())));
			assertThat(getColor(levelWidth * 1), is(equalTo(COLD.getColor())));
			assertThat(getColor(levelWidth * 2), is(equalTo(COOL.getColor())));
			assertThat(getColor(levelWidth * 3), is(equalTo(TEMPERATE.getColor())));
			assertThat(getColor(levelWidth * 4), is(equalTo(WARM.getColor())));
			assertThat(getColor(levelWidth * 5), is(equalTo(HOT.getColor())));
			assertThat(getColor(levelWidth * 6), is(equalTo(VERY_HOT.getColor())));
		}

		@Test
		public void testGetColorInterpolation() {
			Color color = getColor(levelWidth * 4.5);

			assertThat(color.getBlue(), is(equalTo(0.0)));
			assertThat(color.getGreen(), is(closeTo(0.823, 0.001)));
			assertThat(color.getRed(), is(equalTo(1.0)));
		}
	}

	@Nested
	class TestGetClosestColor {

		@Test
		public void testGetClosestColorBelowMinimum() {
			assertThat(getClosestColor(-1.0), is(equalTo(VERY_COLD.getColor())));
		}

		@Test
		public void testGetClosestColorAboveMaximum() {
			assertThat(getClosestColor(1.5), is(equalTo(VERY_HOT.getColor())));
		}

		@Test
		public void testGetClosestColor() {
			assertThat(getClosestColor(levelWidth * 0), is(equalTo(VERY_COLD.getColor())));
			assertThat(getClosestColor(levelWidth * 1), is(equalTo(COLD.getColor())));
			assertThat(getClosestColor(levelWidth * 2), is(equalTo(COOL.getColor())));
			assertThat(getClosestColor(levelWidth * 3), is(equalTo(TEMPERATE.getColor())));
			assertThat(getClosestColor(levelWidth * 4), is(equalTo(WARM.getColor())));
			assertThat(getClosestColor(levelWidth * 5), is(equalTo(HOT.getColor())));
			assertThat(getClosestColor(levelWidth * 6), is(equalTo(VERY_HOT.getColor())));
		}

		@Test
		public void testGetClosestColorInterpolation() {
			for (double i = 4.0; i < 4.5; i += 0.1) {
				assertThat(getClosestColor(levelWidth * i), is(equalTo(WARM.getColor())));
			}

			for (double i = 4.5; i < 5.5; i += 0.1) {
				assertThat(getClosestColor(levelWidth * i), is(equalTo(HOT.getColor())));
			}
		}
	}

	@Test
	public void testCreateColorSelector() {
		AttributeColorSelector<AttributeCell> colorSelector = createColorSelector(34);

		assertThat(colorSelector.getName(), is(equalTo(ATTRIBUTE_NAME)));
		assertThat(colorSelector.getIndex(), is(equalTo(34)));
		assertThat(colorSelector.getUtility(), is(equalTo(UTILITY)));
	}

	private double getLevelIndex(double value) {
		return TemperatureLevel.UTILITY.getLevelIndex(value);
	}

	private TemperatureLevel from(double value) {
		return TemperatureLevel.UTILITY.from(value);
	}

	private Color getColor(double value) {
		return TemperatureLevel.UTILITY.getColor(value);
	}

	private Color getClosestColor(double value) {
		return TemperatureLevel.UTILITY.getClosestColor(value);
	}
}