package jfws.features.elevation;

import jfws.util.map.Map2d;
import jfws.util.map.OutsideMapException;
import jfws.util.math.interpolation.Interpolator2d;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class ElevationInterpolator {

	private Interpolator2d interpolator;
	private final double[][] values = new double[4][4];

	public <T extends ElevationCell, U extends ElevationCell>
	void interpolate(Map2d<T> sourceMap, Map2d<U> targetMap, int cellSize) throws OutsideMapException {
		for(int y = 0; y < sourceMap.getHeight(); y++) {
			for(int x = 0; x < sourceMap.getWidth(); x++) {
				prepareSourceValues(sourceMap, x, y);
				interpolate(targetMap, cellSize, x, y);
			}
		}
	}

	private <T extends ElevationCell>
	void prepareSourceValues(Map2d<T> sourceMap, int sourceX, int sourceY) throws OutsideMapException {
		for(int y = 0; y < 4; y++) {
			for(int x = 0; x < 4; x++) {
				values[x][y] = getSourceValue(sourceMap, sourceX+x-1, sourceY+y-1);
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
	void interpolate(Map2d<U> targetMap, int cellSize, int sourceX, int sourceY) throws OutsideMapException {
		for(int y = 0; y < cellSize; y++) {
			int targetY = sourceY * cellSize + y;
			for(int x = 0; x < cellSize; x++) {
				int targetX = sourceX * cellSize + x;
				U targetCell = targetMap.getCell(targetX, targetY);
				double elevation = interpolator.interpolate(values, x / (double)cellSize, y / (double)cellSize);
				targetCell.setElevation(elevation);
			}
		}
	}
}
