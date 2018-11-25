package jfws.features.elevation.noise;

import jfws.maps.region.RegionCell;
import jfws.maps.sketch.SketchCell;
import jfws.maps.sketch.SketchMap;
import jfws.util.map.Map2d;
import jfws.util.math.interpolation.Interpolator2d;
import jfws.util.math.noise.Noise;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ElevationNoiseWithInterpolationTest {

	public static final String NAME = "Noise3";

	public static final double ELEVATION = 1.5;

	public static final double HILL_NOISE = 3.3;

	public static final double RESOLUTION0 = 2.0;
	public static final double RESOLUTION1 = 0.5;

	public static final double NOISE_FACTOR = 5.0;
	public static final double NOISE_VALUE = 4.0;

	public static final int TARGET_X = 3;
	public static final int TARGET_Y = 24;
	public static final int INDEX = 3;

	private SketchCell sourceCell;
	private RegionCell regionCell;
	private Interpolator2d interpolator;
	private Noise noise;
	private ElevationNoiseWithInterpolation<SketchCell,RegionCell> elevationNoise;

	@BeforeEach
	public void setUp() {
		sourceCell = mock(SketchCell.class);
		regionCell = mock(RegionCell.class);

		interpolator = mock(Interpolator2d.class);
		noise = mock(Noise.class);

		elevationNoise = new ElevationNoiseWithInterpolation<>(NAME, interpolator, noise, RESOLUTION0, INDEX);
	}

	private void verifyNoCall() {
		verifyNoCallToInterpolate();
		verify(noise, never()).calculateNoise(anyDouble(), anyDouble());
	}

	private void verifyNoCallToInterpolate() {
		verify(interpolator, never()).interpolate(any(), anyDouble(), anyDouble());
	}

	@Test
	public void testGetName() {
		assertThat(elevationNoise.getName(), is(equalTo(NAME)));

		verifyNoCall();
	}

	@Test
	public void testGetResolution() {
		assertThat(elevationNoise.getResolution(), is(equalTo(RESOLUTION0)));

		verifyNoCall();
	}

	@Test
	public void testSetResolution() {
		elevationNoise.setResolution(RESOLUTION1);

		assertThat(elevationNoise.getResolution(), is(equalTo(RESOLUTION1)));

		verifyNoCall();
	}

	@Test
	public void testGetSourceValue() {
		when(sourceCell.getNoiseAmplitude(INDEX)).thenReturn(HILL_NOISE);

		assertThat(elevationNoise.getSourceValue(sourceCell), is(equalTo(HILL_NOISE)));

		verify(sourceCell, never()).getElevation();
		verify(sourceCell, never()).setElevation(anyDouble());
		verify(sourceCell).getNoiseAmplitude(INDEX);
		verifyNoCall();
	}

	// setTargetValue()

	@Test
	public void testSetTargetValue() {
		when(regionCell.getElevation()).thenReturn(ELEVATION);
		when(noise.calculateNoise(anyDouble(), anyDouble())).thenReturn(NOISE_VALUE);

		elevationNoise.setTargetValue(regionCell, TARGET_X, TARGET_Y, NOISE_FACTOR);

		verifyNoCallToInterpolate();
		verify(regionCell).getElevation();
		verify(regionCell).setElevation(ELEVATION + NOISE_VALUE * NOISE_FACTOR);
		verify(noise).calculateNoise(eq(TARGET_X / RESOLUTION0), eq(TARGET_Y / RESOLUTION0));
	}

	// addTo()

	@Test
	public void testAddToWithoutParent() {
		Map2d<RegionCell> map = mock(Map2d.class);
		ElevationNoiseWithInterpolation<SketchCell,RegionCell> elevationNoiseSpy = spy(elevationNoise);

		doReturn(Optional.empty()).when(map).getParentMap();

		assertThrows(IllegalArgumentException.class, () -> elevationNoise.addTo(map));

		verify(map, times(1)).getParentMap();
		verify(elevationNoiseSpy, never()).interpolate(any(), any());
	}

	@Test
	public void testAddTo() {
		Map2d<RegionCell> map = mock(Map2d.class);
		Map2d<SketchMap> parentMap = mock(Map2d.class);
		ElevationNoiseWithInterpolation<SketchCell,RegionCell> elevationNoiseSpy = spy(elevationNoise);

		doReturn(Optional.of(parentMap)).when(map).getParentMap();
		doNothing().when(elevationNoiseSpy).interpolate(any(), any());

		elevationNoiseSpy.addTo(map);

		verify(map, times(1)).getParentMap();
		verify(parentMap, never()).getParentMap();
		verify(elevationNoiseSpy, times(1)).interpolate(any(), any());
	}

}