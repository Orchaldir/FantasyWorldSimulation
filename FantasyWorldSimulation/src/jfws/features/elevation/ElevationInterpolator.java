package jfws.features.elevation;

import jfws.util.map.Map2d;
import jfws.util.map.OutsideMapException;
import jfws.util.math.interpolation.Interpolator2d;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static jfws.util.math.interpolation.Interpolator1d.ARRAY_SIZE;

@AllArgsConstructor
@Slf4j
public class ElevationInterpolator {

	private Interpolator2d interpolator;
	private final double[][] sourceValues = new double[ARRAY_SIZE][ARRAY_SIZE];

	public <T extends ElevationCell, U extends ElevationCell>
	void interpolate(Map2d<T> sourceMap, Map2d<U> targetMap, int cellSize) throws OutsideMapException {
		log.debug("interpolate(): cellSize={}", cellSize);
		for(int y = 0; y < sourceMap.getHeight(); y++) {
			for(int x = 0; x < sourceMap.getWidth(); x++) {
				prepareSourceValues(sourceMap, x, y);
				interpolateCell(targetMap, x, y, cellSize);
			}
		}
		log.debug("interpolate(): finished");
	}

	private <T extends ElevationCell>
	void prepareSourceValues(Map2d<T> sourceMap, int sourceX, int sourceY) throws OutsideMapException {
		for(int y = 0; y < ARRAY_SIZE; y++) {
			for(int x = 0; x < ARRAY_SIZE; x++) {
				sourceValues[x][y] = getSourceValue(sourceMap, sourceX+x-1, sourceY+y-1);
			}
		}
	}

	private <T extends ElevationCell>
	double getSourceValue(Map2d<T> sourceMap, int sourceX, int sourceY) throws OutsideMapException {
		int limitedX = Math.min(Math.max(0, sourceX), sourceMap.getWidth()-1);
		int limitedY = Math.min(Math.max(0, sourceY), sourceMap.getHeight()-1);

		return sourceMap.getCell(limitedX, limitedY).getElevation();
	}

	private <U extends ElevationCell>
	void interpolateCell(Map2d<U> targetMap, int sourceX, int sourceY, int cellSize) throws OutsideMapException {
		for(int cellY = 0; cellY < cellSize; cellY++) {
			int targetY = getTargetIndex(sourceY, cellSize, cellY);

			for(int cellX = 0; cellX < cellSize; cellX++) {
				int targetX = getTargetIndex(sourceX, cellSize, cellX);
				U targetCell = targetMap.getCell(targetX, targetY);
				double elevation = interpolator.interpolate(sourceValues, cellX / (double)cellSize, cellY / (double)cellSize);
				targetCell.setElevation(elevation);
			}
		}
	}

	private int getTargetIndex(int sourceIndex, int cellSize, int cellX) {
		return sourceIndex * cellSize + cellX;
	}
}
