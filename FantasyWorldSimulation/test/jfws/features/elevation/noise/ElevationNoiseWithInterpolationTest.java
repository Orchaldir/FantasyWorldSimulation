package jfws.features.elevation.noise;

import jfws.maps.region.RegionCell;
import jfws.maps.sketch.SketchCell;
import jfws.maps.sketch.SketchMap;
import jfws.util.map.Map2d;
import jfws.util.map.MapInterpolator;
import jfws.util.math.interpolation.Interpolator2d;
import jfws.util.math.noise.Noise;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ElevationNoiseWithInterpolationTest {

	public static final String NAME = "Noise3";

	private ElevationNoiseCellInterpolation cellInterpolation;
	private MapInterpolator mapInterpolator;
	private ElevationNoiseWithInterpolation<SketchCell,RegionCell> elevationNoise;

	@BeforeEach
	public void setUp() {
		cellInterpolation = mock(ElevationNoiseCellInterpolation.class);
		mapInterpolator = mock(MapInterpolator.class);

		elevationNoise = new ElevationNoiseWithInterpolation<>(NAME, cellInterpolation, mapInterpolator);
	}

	private void verifyNoCall() {
		verifyNoCallToInterpolate();
		verify(cellInterpolation, never()).getSourceValue(any());
		verify(cellInterpolation, never()).setTargetValue(any(), anyInt(), anyInt(), anyDouble());
	}

	private void verifyNoCallToInterpolate() {
		verify(mapInterpolator, never()).interpolate(any(), any());
		verify(mapInterpolator, never()).calculateCellSize(any(), any());
	}

	@Test
	public void testConstructor() {
		Interpolator2d interpolator = mock(Interpolator2d.class);
		Noise noise = mock(Noise.class);
		int index = 55;

		ElevationNoiseWithInterpolation interpolation = new ElevationNoiseWithInterpolation(NAME, interpolator, noise, index);

		assertThat(interpolation.getName(), is(equalTo(NAME)));
		assertThat(interpolation.getCellInterpolation().getIndex(), is(equalTo(index)));
		assertThat(interpolation.getCellInterpolation().getNoise(), is(equalTo(noise)));
		assertNotNull(interpolation.getMapInterpolator());
	}

	@Test
	public void testGetName() {
		assertThat(elevationNoise.getName(), is(equalTo(NAME)));

		verifyNoCall();
	}

	// addTo()

	@Test
	public void testAddToWithoutParent() {
		Map2d<RegionCell> map = mock(Map2d.class);

		doReturn(Optional.empty()).when(map).getParentMap();

		assertThrows(IllegalArgumentException.class, () -> elevationNoise.addTo(map));

		verify(map, times(1)).getParentMap();
		verifyNoCall();
	}

	@Test
	public void testAddTo() {
		Map2d<RegionCell> map = mock(Map2d.class);
		Map2d<SketchMap> parentMap = mock(Map2d.class);

		doReturn(Optional.of(parentMap)).when(map).getParentMap();

		elevationNoise.addTo(map);

		verify(map, times(1)).getParentMap();
		verify(parentMap, never()).getParentMap();
		verify(mapInterpolator, times(1)).interpolate(any(), any());
	}
}