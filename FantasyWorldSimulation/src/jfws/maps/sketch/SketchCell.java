package jfws.maps.sketch;

import jfws.features.elevation.ElevationCell;
import jfws.features.elevation.NoiseAmplitudeStorage;
import jfws.maps.sketch.terrain.TerrainType;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SketchCell implements ElevationCell, NoiseAmplitudeStorage {

	private TerrainType terrainType;

	private double elevation;

	@Override
	public double getNoiseAmplitude(int index) {
		return terrainType.getHillNoise();
	}
}
