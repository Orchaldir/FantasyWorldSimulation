package jfws.maps.sketch;

import jfws.maps.sketch.terrain.TerrainType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;

class SketchCellTest {

	public static final double ELEVATION = 6.9;
	public static final int INDEX = 5;
	public static final double NOISE_AMPLITUDE = 55.2;
	private TerrainType type;
	private SketchCell cell;

	@BeforeEach
	public void setUp() {
		type = mock(TerrainType.class);
		cell = new SketchCell(type, ELEVATION);
	}

	@Test
	public void testGetElevation() {
		assertThat(cell.getElevation(), is(equalTo(ELEVATION)));
	}

	@Test
	public void testGetTerrainType() {
		assertThat(cell.getTerrainType(), is(equalTo(type)));
	}

	@Test
	public void testGetNoiseAmplitude() {
		when(type.getNoiseAmplitude(INDEX)).thenReturn(NOISE_AMPLITUDE);

		assertThat(cell.getNoiseAmplitude(INDEX), is(equalTo(NOISE_AMPLITUDE)));

		verify(type).getNoiseAmplitude(INDEX);
	}

}