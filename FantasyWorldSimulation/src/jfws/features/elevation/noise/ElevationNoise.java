package jfws.features.elevation.noise;

import jfws.features.elevation.ElevationCell;
import jfws.util.map.Map2d;

public interface ElevationNoise<T extends ElevationCell> {

	void add(Map2d<T> map);

}
