package jfws.maps.region;

import jfws.features.elevation.ElevationCell;
import jfws.maps.sketch.SketchMap;
import jfws.util.map.ArrayCellMap2D;
import jfws.util.map.CellMap2d;
import jfws.util.map.Map2d;
import jfws.util.map.ToCellMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class RegionMap implements Map2d<RegionCell> {

	public static final int VERSION = 1;

	@Getter
	private final CellMap2d<RegionCell> cellMap;

	@Getter
	private final ToCellMapper<RegionCell> toCellMapper;

	private Optional<SketchMap> optionalParentMap;

	public RegionMap(SketchMap parentMap, int width, int height, double resolution) {
		this.optionalParentMap = Optional.ofNullable(parentMap);
		int size = width * height;

		log.info("RegionMap(): width={} height={} size={}", width, height, size);

		RegionCell[] cellArray = new RegionCell[size];

		for(int i = 0; i < size; i++) {
			cellArray[i] = new RegionCell(ElevationCell.DEFAULT_ELEVATION);
		}

		cellMap = new ArrayCellMap2D<>(width, height, cellArray);
		toCellMapper = new ToCellMapper<>(cellMap, resolution);
	}

	public RegionMap(int width, int height, double resolution) {
		this(null, width, height, resolution);
	}

	public static RegionMap fromSketchMap(SketchMap sketchMap, int cellsPerSketchCell) {
		int width = sketchMap.getCellMap().getWidth() * cellsPerSketchCell;
		int height = sketchMap.getCellMap().getHeight() * cellsPerSketchCell;
		double resolution = sketchMap.getToCellMapper().getResolutionX() / cellsPerSketchCell;

		return new RegionMap(sketchMap, width, height, resolution);
	}

	@Override
	public Optional getParentMap() {
		return optionalParentMap;
	}
}
