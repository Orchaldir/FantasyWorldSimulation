package jfws.util.map;

import jfws.util.math.interpolation.Interpolator2d;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static jfws.util.math.interpolation.Interpolator1d.ARRAY_SIZE;

@AllArgsConstructor
@Slf4j
public abstract class CellInterpolator<T, U> {

	private Interpolator2d interpolator;
	protected final double[][] sourceValues = new double[ARRAY_SIZE][ARRAY_SIZE];

	public void prepareSourceValues(CellMap2d<T> sourceMap, int sourceX, int sourceY) throws OutsideMapException {
		for(int y = 0; y < ARRAY_SIZE; y++) {
			for(int x = 0; x < ARRAY_SIZE; x++) {
				sourceValues[x][y] = getSourceValue(sourceMap, sourceX+x-1, sourceY+y-1);
			}
		}
	}

	private double getSourceValue(CellMap2d<T> sourceMap, int sourceX, int sourceY) throws OutsideMapException {
		int limitedX = Math.min(Math.max(0, sourceX), sourceMap.getWidth()-1);
		int limitedY = Math.min(Math.max(0, sourceY), sourceMap.getHeight()-1);

		return getSourceValue(sourceMap.getCell(limitedX, limitedY));
	}

	public abstract double getSourceValue(T sourceCell);

	public void interpolateCell(CellMap2d<U> targetMap, int sourceX, int sourceY, int cellSize) throws OutsideMapException {
		for(int cellY = 0; cellY < cellSize; cellY++) {
			int targetY = getTargetIndex(sourceY, cellSize, cellY);

			for(int cellX = 0; cellX < cellSize; cellX++) {
				int targetX = getTargetIndex(sourceX, cellSize, cellX);
				double targetValue = interpolator.interpolate(sourceValues, cellX / (double)cellSize, cellY / (double)cellSize);
				setTargetValue(targetMap, targetX, targetY, targetValue);
			}
		}
	}

	public abstract void setTargetValue(CellMap2d<U> targetMap, int targetX, int targetY, double targetValue) throws OutsideMapException;

	private int getTargetIndex(int sourceIndex, int cellSize, int cellX) {
		return sourceIndex * cellSize + cellX;
	}
}
