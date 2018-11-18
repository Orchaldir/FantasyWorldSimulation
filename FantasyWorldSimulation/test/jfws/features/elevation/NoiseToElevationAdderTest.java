package jfws.features.elevation;

import javafx.scene.paint.Color;
import jfws.maps.sketch.SketchCell;
import jfws.maps.sketch.terrain.TerrainType;
import jfws.maps.sketch.terrain.TerrainTypeImpl;
import jfws.util.math.interpolation.Interpolator2d;
import jfws.util.math.noise.Noise;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;

class NoiseToElevationAdderTest {

	public static final double ELEVATION0 = 1.5;

	public static final double HILL_NOISE0 = 3.3;
	public static final double HILL_NOISE1 = 44.8;

	public static final double RESOLUTION0 = 2.0;
	public static final double RESOLUTION1 = 0.5;

	public static final double NOISE_FACTOR = 5.0;
	public static final double NOISE_VALUE = 4.0;

	private static final TerrainType TERRAIN_TYPE0 = new TerrainTypeImpl("0", Color.BLUE, 0, 0, HILL_NOISE0);
	private static final TerrainType TERRAIN_TYPE1 = new TerrainTypeImpl("1", Color.GREEN, 0, 0, HILL_NOISE1);
	public static final int TARGET_X = 3;
	public static final int TARGET_Y = 24;

	private SketchCell cellA;
	private SketchCell cellB;
	private Interpolator2d interpolator;
	private Noise noise;
	private NoiseToElevationAdder<SketchCell> noiseToElevationAdder;

	@BeforeEach
	public void setUp() {
		cellA = new SketchCell(TERRAIN_TYPE0, ELEVATION0);
		cellB = new SketchCell(TERRAIN_TYPE1, 0);

		interpolator = mock(Interpolator2d.class);
		noise = mock(Noise.class);

		noiseToElevationAdder = new NoiseToElevationAdder<>(interpolator, noise, RESOLUTION0);
	}

	private void verifyNoCall() {
		verify(interpolator, never()).interpolate(any(), anyDouble(), anyDouble());
		verify(noise, never()).calculateNoise(anyDouble(), anyDouble());
	}

	@Test
	public void testGetResolution() {
		assertThat(noiseToElevationAdder.getResolution(), is(equalTo(RESOLUTION0)));

		verifyNoCall();
	}

	@Test
	public void testSetResolution() {
		noiseToElevationAdder.setResolution(RESOLUTION1);

		assertThat(noiseToElevationAdder.getResolution(), is(equalTo(RESOLUTION1)));

		verifyNoCall();
	}

	@Test
	public void testGetSourceValue() {
		assertThat(noiseToElevationAdder.getSourceValue(cellA), is(equalTo(HILL_NOISE0)));
		assertThat(noiseToElevationAdder.getSourceValue(cellB), is(equalTo(HILL_NOISE1)));

		verifyNoCall();
	}

	// setTargetValue()

	@Test
	public void testSetTargetValue() {
		when(noise.calculateNoise(anyDouble(), anyDouble())).thenReturn(NOISE_VALUE);

		noiseToElevationAdder.setTargetValue(cellA, TARGET_X, TARGET_Y, NOISE_FACTOR);

		assertThat(cellA.getElevation(), is(equalTo(ELEVATION0 + NOISE_VALUE * NOISE_FACTOR)));

		verify(interpolator, never()).interpolate(any(), anyDouble(), anyDouble());
		verify(noise).calculateNoise(eq(TARGET_X / RESOLUTION0), eq(TARGET_Y / RESOLUTION0));
	}

}