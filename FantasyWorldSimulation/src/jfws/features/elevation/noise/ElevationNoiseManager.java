package jfws.features.elevation.noise;

import jfws.features.elevation.ElevationCell;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ElevationNoiseManager<T extends ElevationCell> {

	private final Map<String,ElevationNoise<T>> elevationNoiseMap = new HashMap<>();

	public void add(ElevationNoise<T> elevationNoise) {
		if(elevationNoiseMap.containsKey(elevationNoise.getName())) {
			log.warn("add(): Can not add \"{}\" twice!", elevationNoise.getName());
			throw new IllegalArgumentException();
		}

		elevationNoiseMap.put(elevationNoise.getName(), elevationNoise);
	}

	public Collection<ElevationNoise<T>> getAll() {
		return elevationNoiseMap.values();
	}

	public int getSize() {
		return elevationNoiseMap.size();
	}
}