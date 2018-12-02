package jfws.features.elevation.noise;

import jfws.features.elevation.ElevationCell;
import jfws.maps.region.RegionCell;
import jfws.maps.sketch.SketchCell;
import jfws.util.map.CellMap2d;
import jfws.util.map.OutsideMapException;
import jfws.util.math.interpolation.Interpolator2d;
import jfws.util.math.noise.Noise;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;

class ElevationNoiseCellInterpolationTest {

	public static final double ELEVATION = 1.5;

	public static final double HILL_NOISE = 3.3;

	public static final double NOISE_FACTOR = 5.0;
	public static final double NOISE_VALUE = 4.0;

	public static final double TARGET_X = 3;
	public static final double TARGET_Y = 24;
	public static final int INDEX = 3;

	private SketchCell sourceCell;
	private RegionCell regionCell;
	private CellMap2d<RegionCell> regionMap;

	private Interpolator2d interpolator;
	private Noise noise;
	private ElevationNoiseCellInterpolation<SketchCell,RegionCell> cellInterpolation;

	@BeforeEach
	public void setUp() {
		sourceCell = mock(SketchCell.class);
		regionCell = mock(RegionCell.class);
		regionMap = mock(CellMap2d.class);

		interpolator = mock(Interpolator2d.class);
		noise = mock(Noise.class);

		cellInterpolation = new ElevationNoiseCellInterpolation<>(interpolator, noise, INDEX);
	}

	private void verifyNoCall() {
		verifyNoCallToInterpolate();
		verify(noise, never()).calculateNoise(anyDouble(), anyDouble());
	}

	private void verifyNoCallToInterpolate() {
		verify(interpolator, never()).interpolate(any(), anyDouble(), anyDouble());
	}

	@Test
	public void testGetIndex() {
		assertThat(cellInterpolation.getIndex(), is(INDEX));
	}

	@Test
	public void testGetSourceValue() {
		when(sourceCell.getNoiseAmplitude(INDEX)).thenReturn(HILL_NOISE);

		assertThat(cellInterpolation.getSourceValue(sourceCell), is(equalTo(HILL_NOISE)));

		verify(sourceCell, never()).getElevation();
		verify(sourceCell, never()).setElevation(anyDouble());
		verify(sourceCell).getNoiseAmplitude(INDEX);
		verifyNoCall();
	}

	// setTargetValue()

	@Test
	public void testSetTargetValue() throws OutsideMapException {
		when(regionMap.getCell((int)TARGET_X, (int)TARGET_Y)).thenReturn(regionCell);
		when(regionCell.getElevation()).thenReturn(ELEVATION);
		when(noise.calculateNoise(anyDouble(), anyDouble())).thenReturn(NOISE_VALUE);

		cellInterpolation.setTargetValue(regionMap, (int)TARGET_X, (int)TARGET_Y, NOISE_FACTOR);

		verifyNoCallToInterpolate();
		verify(regionCell).getElevation();
		verify(regionCell).setElevation(ELEVATION + NOISE_VALUE * NOISE_FACTOR);
		verify(noise).calculateNoise(eq(TARGET_X), eq(TARGET_Y));
	}
}