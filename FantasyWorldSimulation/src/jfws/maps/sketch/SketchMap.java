package jfws.maps.sketch;

import jfws.maps.sketch.elevation.ElevationGenerator;
import jfws.maps.sketch.terrain.TerrainType;
import jfws.util.map.ArrayMap2d;
import jfws.util.map.Map2d;
import lombok.Getter;

public class SketchMap {

	public static final int VERSION = 1;

	@Getter
	private final Map2d<SketchCell> cells;

	public SketchMap(int width, int height, TerrainType defaultType) {
		int size = width * height;
		SketchCell[] cellArray = new SketchCell[size];

		for(int i = 0; i < size; i++) {
			cellArray[i] = new SketchCell(defaultType, SketchCell.DEFAULT_ELEVATION);
		}

		cells = new ArrayMap2d<>(width, height, cellArray);
	}

	public void generateElevation(ElevationGenerator generator) {
		cells.getCells().forEach(c -> c.setElevation(generator.generate(c.getTerrainType())));
	}
}
