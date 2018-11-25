package jfws.features.elevation.noise;

import jfws.features.elevation.ElevationCell;
import jfws.util.map.Map2d;
import jfws.util.map.MapInterpolator;
import jfws.util.math.interpolation.Interpolator2d;
import jfws.util.math.noise.Noise;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class ElevationNoiseWithInterpolation<T extends NoiseAmplitudeStorage, U extends ElevationCell>
		extends MapInterpolator<T,U> implements ElevationNoise<U> {

	@Getter
	private final String name;

	private Noise noise;

	@Getter
	@Setter
	private double resolution;

	private int index;

	public ElevationNoiseWithInterpolation(String name, Interpolator2d interpolator, Noise noise, double resolution, int index) {
		super(interpolator);
		this.name = name;
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

	@Override
	public void addTo(Map2d<U> map) {
		Optional<Map2d<T>> optionalParentMap = map.getParentMap();

		if(!optionalParentMap.isPresent()) {
			log.warn("addTo(): Map has no parent map!");
			throw new IllegalArgumentException("Map has no parent map!");
		}

		Map2d<T> parentMap = optionalParentMap.get();

		log.info("addTo(): index={}", index);

		interpolate(parentMap.getCellMap(), map.getCellMap());
	}
}
