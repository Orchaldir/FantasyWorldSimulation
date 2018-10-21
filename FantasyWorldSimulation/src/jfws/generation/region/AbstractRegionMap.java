package jfws.generation.region;

import jfws.generation.region.terrain.TerrainType;
import jfws.util.map.ArrayMap2d;
import lombok.Getter;

public class AbstractRegionMap {

	@Getter
	private final ArrayMap2d<AbstractRegionCell> cells;

	public AbstractRegionMap(int width, int height, TerrainType defaultType) {
		int size = width * height;
		AbstractRegionCell[] cellArray = new AbstractRegionCell[size];

		for(int i = 0; i < size; i++) {
			cellArray[i] = new AbstractRegionCell(defaultType);
		}

		cells = new ArrayMap2d<>(width, height, cellArray);
	}
}
