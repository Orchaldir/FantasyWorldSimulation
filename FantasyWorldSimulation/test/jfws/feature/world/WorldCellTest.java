package jfws.feature.world;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static jfws.feature.world.WorldCell.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

class WorldCellTest {

	private static final double ELEVATION_VALUE = 1.1;
	private static final double TEMPERATURE_VALUE = 2.2;
	private static final double RAINFALL_VALUE = 3.3;
	private static final double MANA_VALUE = 4.4;

	private WorldCell cell;

	@BeforeEach
	public void setUp() {
		cell = new WorldCell();
		cell.setAttribute(ELEVATION, ELEVATION_VALUE);
		cell.setAttribute(TEMPERATURE, TEMPERATURE_VALUE);
		cell.setAttribute(RAINFALL, RAINFALL_VALUE);
		cell.setAttribute(MANA_LEVEL, MANA_VALUE);
	}

	@Test
	public void testGetAttribute() {
		assertThat(cell.getAttribute(ELEVATION), is(equalTo(ELEVATION_VALUE)));
		assertThat(cell.getAttribute(TEMPERATURE), is(equalTo(TEMPERATURE_VALUE)));
		assertThat(cell.getAttribute(RAINFALL), is(equalTo(RAINFALL_VALUE)));
		assertThat(cell.getAttribute(MANA_LEVEL), is(equalTo(MANA_VALUE)));
	}

	@Test
	public void testAddToAttribute() {
		cell.addToAttribute(TEMPERATURE, 3.5);

		assertThat(cell.getAttribute(ELEVATION), is(equalTo(ELEVATION_VALUE)));
		assertThat(cell.getAttribute(TEMPERATURE), is(equalTo(5.7)));
		assertThat(cell.getAttribute(RAINFALL), is(equalTo(RAINFALL_VALUE)));
		assertThat(cell.getAttribute(MANA_LEVEL), is(equalTo(MANA_VALUE)));
	}

	@Test
	public void testGetElevation() {
		assertThat(cell.getElevation(), is(equalTo(ELEVATION_VALUE)));
	}

	@Test
	public void testGetTemperature() {
		assertThat(cell.getTemperature(), is(equalTo(TEMPERATURE_VALUE)));
	}

	@Test
	public void testGetRainfall() {
		assertThat(cell.getRainfall(), is(equalTo(RAINFALL_VALUE)));
	}

	@Test
	public void testGetManaLevel() {
		assertThat(cell.getManaLevel(), is(equalTo(MANA_VALUE)));
	}
}