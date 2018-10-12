package jfws.generation.region;

import jfws.generation.region.terrain.TerrainType;
import jfws.util.map.ArrayMap2d;

public class RegionMap extends ArrayMap2d<TerrainType> {

	private RegionMap(int width, int height, TerrainType[] cells) {
		super(width, height, cells);
	}

	public static RegionMap create(int width, int height, TerrainType defaultType) {
		int size = width * height;
		TerrainType[] cells = new TerrainType[size];

		for(int i = 0; i < size; i++) {
			cells[i] = defaultType;
		}

		return new RegionMap(width, height, cells);
	}
}
