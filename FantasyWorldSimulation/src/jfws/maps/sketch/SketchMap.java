package jfws.maps.sketch;

import jfws.maps.sketch.elevation.ElevationGenerator;
import jfws.maps.sketch.terrain.TerrainType;
import jfws.util.map.ArrayMap2d;
import jfws.util.map.Map2d;
import jfws.util.map.ToCellMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import static jfws.features.elevation.ElevationCell.DEFAULT_ELEVATION;

@Slf4j
public class SketchMap {

	public static final int VERSION = 1;
	public static final double SKETCH_TO_WORLD = 100.0;

	@Getter
	private final Map2d<SketchCell> cells;

	@Getter
	private final ToCellMapper<SketchCell> toCellMapper;

	public SketchMap(int width, int height, TerrainType defaultType) {
		int size = width * height;

		log.info("SketchMap(): width={} height={} size={}", width, height, size);

		SketchCell[] cellArray = new SketchCell[size];

		for(int i = 0; i < size; i++) {
			cellArray[i] = new SketchCell(defaultType, DEFAULT_ELEVATION);
		}

		cells = new ArrayMap2d<>(width, height, cellArray);
		toCellMapper = new ToCellMapper<>(cells, SKETCH_TO_WORLD);
	}

	public SketchMap(Map2d<SketchCell> cells) {
		log.info("SketchMap(): cells={}", cells.getSize());
		this.cells = cells;
		toCellMapper = new ToCellMapper<>(cells, SKETCH_TO_WORLD);
	}

	public void generateElevation(ElevationGenerator generator) {
		log.debug("generateElevation()");
		cells.getCells().forEach(c -> c.setElevation(generator.generate(c.getTerrainType())));
	}
}
