package jfws.util.map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import static jfws.util.math.interpolation.Interpolator1d.ARRAY_SIZE;

@AllArgsConstructor
@Slf4j
public class MapInterpolator<T, U> {

	public static final String TARGET_MAP_IS_SMALLER_EXCEPTION = "Target map is smaller than the source map!";
	public static final String WIDTH_AND_HEIGHT_DO_NOT_MATCH_EXCEPTION = "Cell size does not match for width & height!";

	@Getter
	private CellInterpolator<T,U> cellInterpolator;
	private final double[][] sourceValues = new double[ARRAY_SIZE][ARRAY_SIZE];

	public void interpolate(CellMap2d<T> sourceMap, CellMap2d<U> targetMap) {
		int cellSize = calculateCellSize(sourceMap, targetMap);

		for(int y = 0; y < sourceMap.getHeight(); y++) {
			for(int x = 0; x < sourceMap.getWidth(); x++) {
				cellInterpolator.prepareSourceValues(sourceMap, x, y);
				cellInterpolator.interpolateCell(targetMap, x, y, cellSize);
			}
		}

		log.debug("interpolate(): finished");
	}

	public int calculateCellSize(CellMap2d<T> sourceMap, CellMap2d<U> targetMap) {
		int cellSizeX = targetMap.getWidth() / sourceMap.getWidth();

		if(cellSizeX == 0) {
			log.error("calculateCellSize(): {} width: source={} target={}",
					TARGET_MAP_IS_SMALLER_EXCEPTION, sourceMap.getWidth(), targetMap.getWidth());
			throw new IllegalArgumentException(TARGET_MAP_IS_SMALLER_EXCEPTION);
		}

		int cellSizeY = targetMap.getHeight() / sourceMap.getHeight();

		if(cellSizeX != cellSizeY) {
			log.error("calculateCellSize(): {} cellSize: width={} height={}",
					WIDTH_AND_HEIGHT_DO_NOT_MATCH_EXCEPTION, cellSizeX, cellSizeY);
			throw new IllegalArgumentException(WIDTH_AND_HEIGHT_DO_NOT_MATCH_EXCEPTION);
		}

		log.info("calculateCellSize(): cellSize={}", cellSizeX);

		return cellSizeX;
	}


}
