package jfws.features.elevation.noise;

import jfws.features.elevation.ElevationCell;
import jfws.util.map.Map2d;
import jfws.util.map.MapInterpolator;
import jfws.util.math.interpolation.Interpolator2d;
import jfws.util.math.noise.Noise;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class ElevationNoiseWithInterpolation<T extends NoiseAmplitudeStorage, U extends ElevationCell>
		implements ElevationNoise<U> {

	@Getter
	private final String name;

	@Getter
	private ElevationNoiseCellInterpolation cellInterpolation;

	@Getter
	private MapInterpolator mapInterpolator;

	public ElevationNoiseWithInterpolation(String name, Interpolator2d interpolator, Noise noise, int index) {
		this.name = name;
		cellInterpolation = new ElevationNoiseCellInterpolation(interpolator, noise, index);
		mapInterpolator =  new MapInterpolator(cellInterpolation);
	}

	public ElevationNoiseWithInterpolation(String name, ElevationNoiseCellInterpolation cellInterpolation,
										   MapInterpolator mapInterpolator) {
		this.name = name;
		this.cellInterpolation = cellInterpolation;
		this.mapInterpolator =  mapInterpolator;
	}

	@Override
	public void addTo(Map2d<U> map) {
		Optional<Map2d<T>> optionalParentMap = map.getParentMap();

		if(!optionalParentMap.isPresent()) {
			log.warn("addTo(): Map has no parent map!");
			throw new IllegalArgumentException("Map has no parent map!");
		}

		Map2d<T> parentMap = optionalParentMap.get();

		log.info("addTo(): index={}", cellInterpolation.getIndex());

		mapInterpolator.interpolate(parentMap.getCellMap(), map.getCellMap());
	}
}