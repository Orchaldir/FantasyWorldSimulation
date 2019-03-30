package jfws.feature.world.attribute.elevation;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ElevationColorSelectorTest {

	private ElevationColorSelector colorSelector = new ElevationColorSelector();

	private void assertColor(double elevation, Color color) {
		ElevationCell cell = Mockito.mock(ElevationCell.class);

		when(cell.getElevation()).thenReturn(elevation);

		assertThat(colorSelector.select(cell), is(equalTo(color)));

		verify(cell).getElevation();
	}

	@Test
	public void testGetName() {
		assertThat(colorSelector.getName(), is(equalTo("Elevation")));
	}

	@Test
	public void testMin() {
		assertColor(ElevationCell.MIN_ELEVATION, Color.DARKBLUE);
	}

	@Test
	public void testTooLow() {
		assertColor(ElevationCell.MIN_ELEVATION - 100, Color.DARKBLUE.darker());
	}

	@Test
	public void testMax() {
		assertColor(ElevationCell.MAX_ELEVATION, Color.LIGHTGREY);
	}

	@Test
	public void testTooHigh() {
		assertColor(ElevationCell.MAX_ELEVATION + 100, Color.WHITE);
	}

	@Test
	public void test() {
		assertColor(ElevationCell.DEFAULT_ELEVATION - 1, Color.CYAN);
		assertColor(ElevationCell.DEFAULT_ELEVATION, Color.LIGHTGREEN);
		assertColor(ElevationCell.HILL_ELEVATION - 1, Color.DARKGREEN);
		assertColor(ElevationCell.HILL_ELEVATION, Color.GREY);
	}

	@Test
	public void testInterpolation() {
		assertThat(colorSelector.interpolate(11.0, 10.0, 12.0, Color.BLACK, Color.WHITE),
				is(equalTo(new Color(0.5, 0.5, 0.5,  1.0))));
	}

}