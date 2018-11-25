package jfws.maps.region;

import jfws.features.elevation.ElevationCell;
import jfws.maps.sketch.SketchMap;
import jfws.util.map.CellMap2d;
import jfws.util.map.OutsideMapException;
import jfws.util.map.ToCellMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static jfws.maps.sketch.SketchMap.SKETCH_TO_WORLD;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

class RegionMapTest {

	public static final int WIDTH = 2;
	public static final int HEIGHT = 3;
	public static final double RESOLUTION = 0.5;
	public static final int CELLS_PER_SKETCH_CELL = 2;

	private SketchMap sketchMap;
	private RegionMap regionMap;
	private CellMap2d<RegionCell> regionCellMap;
	private ToCellMapper<RegionCell> toCellMapper;

	@BeforeEach
	public void setUp() {
		sketchMap = new SketchMap(WIDTH, HEIGHT, null);
		regionMap = new RegionMap(WIDTH, HEIGHT, RESOLUTION);
		prepare();
	}

	private void prepare() {
		regionCellMap = regionMap.getCellMap();
		toCellMapper = regionMap.getToCellMapper();
	}

	@Test
	public void testWidth() {
		assertThat(regionCellMap.getWidth(), is(equalTo(WIDTH)));
	}

	@Test
	public void testHeight() {
		assertThat(regionCellMap.getHeight(), is(equalTo(HEIGHT)));
	}

	@Test
	public void testResolution() {
		assertThat(toCellMapper.getResolutionX(), is(equalTo(RESOLUTION)));
		assertThat(toCellMapper.getResolutionY(), is(equalTo(RESOLUTION)));
	}

	@Test
	public void testGetParentMap() {
		assertThat(regionMap.getParentMap().isPresent(), is(false));
	}

	@Test
	public void testElevation() throws OutsideMapException {
		for(int index = 0; index < regionCellMap.getSize(); index++) {
			assertThat(regionCellMap.getCell(index).getElevation(), is(equalTo(ElevationCell.DEFAULT_ELEVATION)));
		}
	}

	@Test
	public void testFromSketchMap() {
		regionMap = RegionMap.fromSketchMap(sketchMap, CELLS_PER_SKETCH_CELL);
		prepare();

		assertThat(regionCellMap.getWidth(), is(equalTo(WIDTH * CELLS_PER_SKETCH_CELL)));
		assertThat(regionCellMap.getHeight(), is(equalTo(HEIGHT * CELLS_PER_SKETCH_CELL)));

		double resolution = SKETCH_TO_WORLD / (double) CELLS_PER_SKETCH_CELL;

		assertThat(toCellMapper.getResolutionX(), is(equalTo(resolution)));
		assertThat(toCellMapper.getResolutionY(), is(equalTo(resolution)));

		assertThat(regionMap.getParentMap().isPresent(), is(true));
		assertThat(regionMap.getParentMap().get(), is(equalTo(sketchMap)));
	}

}