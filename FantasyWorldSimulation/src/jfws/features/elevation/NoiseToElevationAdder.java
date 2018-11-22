package jfws.features.elevation;

import jfws.util.map.MapInterpolator;
import jfws.util.math.interpolation.Interpolator2d;
import jfws.util.math.noise.Noise;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NoiseToElevationAdder<T extends NoiseAmplitudeStorage, U extends ElevationCell>
		extends MapInterpolator<T,U> {

	private Noise noise;

	@Getter
	@Setter
	private double resolution;

	private int index;

	public NoiseToElevationAdder(Interpolator2d interpolator, Noise noise, double resolution, int index) {
		super(interpolator);
		this.noise = noise;
		this.resolution = resolution;
		this.index = index;
	}

	@Override
	public double getSourceValue(NoiseAmplitudeStorage sourceStorage) {
		return sourceStorage.getNoiseAmplitude(index);
	}

	@Override
	public void setTargetValue(U targetCell, int targetX, int targetY, double noiseFactor) {
		double oldElevation = targetCell.getElevation();
		double scaledNoise = noise.calculateNoise(targetX / resolution, targetY/ resolution) * noiseFactor;
		double newElevation = oldElevation + scaledNoise;
		targetCell.setElevation(newElevation);
	}
}
