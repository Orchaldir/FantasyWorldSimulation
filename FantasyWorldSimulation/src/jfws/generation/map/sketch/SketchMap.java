package jfws.generation.map.sketch;

import jfws.generation.map.terrain.type.TerrainType;
import jfws.util.map.ArrayMap2d;

public class SketchMap extends ArrayMap2d<TerrainType> {

	private SketchMap(int width, int height, TerrainType[] cells) {
		super(width, height, cells);
	}

	public static SketchMap create(int width, int height, TerrainType defaultType) {
		int size = width * height;
		TerrainType[] cells = new TerrainType[size];

		for(int i = 0; i < size; i++) {
			cells[i] = defaultType;
		}

		return new SketchMap(width, height, cells);
	}
}
