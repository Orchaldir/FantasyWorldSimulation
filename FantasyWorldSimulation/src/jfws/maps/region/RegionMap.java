package jfws.maps.region;

import jfws.features.elevation.ElevationCell;
import jfws.maps.sketch.SketchCell;
import jfws.maps.sketch.SketchMap;
import jfws.util.map.ArrayMap2d;
import jfws.util.map.Map2d;
import jfws.util.map.ToCellMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RegionMap {

	public static final int VERSION = 1;

	@Getter
	private final Map2d<RegionCell> regionCellMap;

	@Getter
	private final ToCellMapper<RegionCell> toCellMapper;

	public RegionMap(int width, int height, double resolution) {
		log.info("RegionMap(): width={} height={}", width, height);
		int size = width * height;
		RegionCell[] cellArray = new RegionCell[size];

		for(int i = 0; i < size; i++) {
			cellArray[i] = new RegionCell(ElevationCell.DEFAULT_ELEVATION);
		}

		regionCellMap = new ArrayMap2d<>(width, height, cellArray);
		toCellMapper = new ToCellMapper<>(regionCellMap, resolution);
	}

	public static RegionMap fromSketchMap(SketchMap sketchMap, int cellsPerSketchCell) {
		int width = sketchMap.getCells().getWidth() * cellsPerSketchCell;
		int height = sketchMap.getCells().getHeight() * cellsPerSketchCell;
		double resolution = sketchMap.getToCellMapper().getResolutionX() / cellsPerSketchCell;

		return new RegionMap(width, height, resolution);
	}
}
