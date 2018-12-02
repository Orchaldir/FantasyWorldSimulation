package jfws.features.elevation;

import jfws.util.map.CellInterpolator;
import jfws.util.map.CellMap2d;
import jfws.util.map.MapInterpolator;
import jfws.util.map.OutsideMapException;
import jfws.util.math.interpolation.Interpolator2d;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ElevationCellInterpolator<T extends ElevationCell, U extends ElevationCell>
		extends CellInterpolator<T,U> {

	public ElevationCellInterpolator(Interpolator2d interpolator) {
		super(interpolator);
	}

	public static<T,U> MapInterpolator createMapInterpolator(Interpolator2d interpolator) {
		return new MapInterpolator(new ElevationCellInterpolator(interpolator));
	}

	@Override
	public double getSourceValue(T sourceCell) {
		return sourceCell.getElevation();
	}

	@Override
	public void setTargetValue(CellMap2d<U> targetMap, int targetX, int targetY, double targetValue) throws OutsideMapException {
		U targetCell = targetMap.getCell(targetX, targetY);
		targetCell.setElevation(targetValue);
	}
}
