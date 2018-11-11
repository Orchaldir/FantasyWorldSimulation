package jfws.maps.sketch.elevation;

import jfws.maps.sketch.terrain.TerrainType;
import jfws.util.math.random.RandomNumberGenerator;

public class ElevationGeneratorWithNoise implements ElevationGenerator {

	private RandomNumberGenerator generator;

	public ElevationGeneratorWithNoise(RandomNumberGenerator generator) {
		this.generator = generator;
	}

	@Override
	public void prepare() {
		generator.restart();
	}

	@Override
	public double generate(TerrainType type) {
		return type.getBaseElevation() + generator.getGaussian() * type.getElevationVariation();
	}
}
