package jfws.maps.sketch;

import jfws.maps.sketch.elevation.ElevationGenerator;
import jfws.maps.sketch.terrain.TerrainType;
import jfws.util.map.ArrayCellMap2D;
import jfws.util.map.CellMap2d;
import jfws.util.map.Map2d;
import jfws.util.map.ToCellMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static jfws.features.elevation.ElevationCell.DEFAULT_ELEVATION;

@Slf4j
public class SketchMap implements Map2d<SketchCell> {

	public static final int VERSION = 1;
	public static final double SKETCH_TO_WORLD = 100.0;

	@Getter
	private final CellMap2d<SketchCell> cellMap;

	@Getter
	private final ToCellMapper<SketchCell> toCellMapper;

	public SketchMap(int width, int height, TerrainType defaultType) {
		int size = width * height;

		log.info("SketchMap(): width={} height={} size={}", width, height, size);

		SketchCell[] cellArray = new SketchCell[size];

		for(int i = 0; i < size; i++) {
			cellArray[i] = new SketchCell(defaultType, DEFAULT_ELEVATION);
		}

		cellMap = new ArrayCellMap2D<>(width, height, cellArray);
		toCellMapper = new ToCellMapper<>(cellMap, SKETCH_TO_WORLD);
	}

	public SketchMap(CellMap2d<SketchCell> cellMap) {
		log.info("SketchMap(): cells={}", cellMap.getSize());
		this.cellMap = cellMap;
		toCellMapper = new ToCellMapper<>(cellMap, SKETCH_TO_WORLD);
	}

	public void generateElevation(ElevationGenerator generator) {
		log.debug("generateElevation()");
		generator.prepare();
		cellMap.getCells().forEach(c -> c.setElevation(generator.generate(c.getTerrainType())));
	}

	@Override
	public <U> Optional<Map2d<U>> getParentMap() {
		return Optional.empty();
	}
}
