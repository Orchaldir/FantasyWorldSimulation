package jfws.util.map;

import jfws.util.math.interpolation.Interpolator2d;
import lombok.extern.slf4j.Slf4j;

import static jfws.util.math.interpolation.Interpolator1d.ARRAY_SIZE;

@Slf4j
public abstract class MapInterpolator<T, U> {

	public static final String TARGET_MAP_IS_SMALLER_EXCEPTION = "Target map is smaller than the source map!";
	public static final String WIDTH_AND_HEIGHT_DO_NOT_MATCH_EXCEPTION = "Cell size does not match for width & height!";

	private Interpolator2d interpolator;
	private final double[][] sourceValues = new double[ARRAY_SIZE][ARRAY_SIZE];

	public MapInterpolator(Interpolator2d interpolator) {
		this.interpolator = interpolator;
	}

	public void interpolate(Map2d<T> sourceMap, Map2d<U> targetMap) throws OutsideMapException {
		int cellSize = calculateCellSize(sourceMap, targetMap);

		for(int y = 0; y < sourceMap.getHeight(); y++) {
			for(int x = 0; x < sourceMap.getWidth(); x++) {
				prepareSourceValues(sourceMap, x, y);
				interpolateCell(targetMap, x, y, cellSize);
			}
		}
		log.debug("interpolate(): finished");
	}

	public static <T, U>
	int calculateCellSize(Map2d<T> sourceMap, Map2d<U> targetMap) {
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

	private void prepareSourceValues(Map2d<T> sourceMap, int sourceX, int sourceY) throws OutsideMapException {
		for(int y = 0; y < ARRAY_SIZE; y++) {
			for(int x = 0; x < ARRAY_SIZE; x++) {
				sourceValues[x][y] = getSourceValue(sourceMap, sourceX+x-1, sourceY+y-1);
			}
		}
	}

	private double getSourceValue(Map2d<T> sourceMap, int sourceX, int sourceY) throws OutsideMapException {
		int limitedX = Math.min(Math.max(0, sourceX), sourceMap.getWidth()-1);
		int limitedY = Math.min(Math.max(0, sourceY), sourceMap.getHeight()-1);

		return getSourceValue(sourceMap.getCell(limitedX, limitedY));
	}

	public abstract double getSourceValue(T sourceCell);

	private void interpolateCell(Map2d<U> targetMap, int sourceX, int sourceY, int cellSize) throws OutsideMapException {
		for(int cellY = 0; cellY < cellSize; cellY++) {
			int targetY = getTargetIndex(sourceY, cellSize, cellY);

			for(int cellX = 0; cellX < cellSize; cellX++) {
				int targetX = getTargetIndex(sourceX, cellSize, cellX);
				U targetCell = targetMap.getCell(targetX, targetY);
				double targetValue = interpolator.interpolate(sourceValues, cellX / (double)cellSize, cellY / (double)cellSize);
				setTargetValue(targetCell, targetX, targetY, targetValue);
			}
		}
	}

	public abstract void setTargetValue(U targetCell, int targetX, int targetY, double targetValue);

	private int getTargetIndex(int sourceIndex, int cellSize, int cellX) {
		return sourceIndex * cellSize + cellX;
	}
}
