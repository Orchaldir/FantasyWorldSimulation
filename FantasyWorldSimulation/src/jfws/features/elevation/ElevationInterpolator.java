package jfws.features.elevation;

import jfws.util.map.MapInterpolator;
import jfws.util.math.interpolation.Interpolator2d;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ElevationInterpolator<T extends ElevationCell, U extends ElevationCell>
		extends MapInterpolator<T,U> {

	public ElevationInterpolator(Interpolator2d interpolator) {
		super(interpolator);
	}

	@Override
	public double getSourceValue(T sourceCell) {
		return sourceCell.getElevation();
	}

	@Override
	public void setTargetValue(U targetCell, int targetX, int targetY, double targetValue) {
		targetCell.setElevation(targetValue);
	}
}
