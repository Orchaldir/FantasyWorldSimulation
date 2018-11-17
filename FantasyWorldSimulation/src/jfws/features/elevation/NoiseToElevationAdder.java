package jfws.features.elevation;

import jfws.maps.region.RegionCell;
import jfws.maps.sketch.SketchCell;
import jfws.util.map.MapInterpolator;
import jfws.util.math.interpolation.Interpolator2d;
import jfws.util.math.noise.Noise;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NoiseToElevationAdder<U extends ElevationCell>
		extends MapInterpolator<SketchCell,U> {

	private Noise noise;

	public NoiseToElevationAdder(Interpolator2d interpolator, Noise noise) {
		super(interpolator);
		this.noise = noise;
	}

	@Override
	public double getSourceValue(SketchCell sourceCell) {
		return 50.0;
	}

	@Override
	public void setTargetValue(U targetCell, int targetX, int targetY, double noiseFactor) {
		double oldElevation = targetCell.getElevation();
		double newElevation = oldElevation + noise.calculateNoise(targetX/100.0, targetY/100.0) * noiseFactor;
		targetCell.setElevation(newElevation);
	}
}
