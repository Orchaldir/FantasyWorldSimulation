package jfws.features.elevation;

import jfws.maps.sketch.SketchCell;
import jfws.util.map.MapInterpolator;
import jfws.util.math.interpolation.Interpolator2d;
import jfws.util.math.noise.Noise;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NoiseToElevationAdder<U extends ElevationCell>
		extends MapInterpolator<SketchCell,U> {

	private Noise noise;

	@Getter
	@Setter
	private double resolution;

	public NoiseToElevationAdder(Interpolator2d interpolator, Noise noise, double resolution) {
		super(interpolator);
		this.noise = noise;
		this.resolution = resolution;
	}

	@Override
	public double getSourceValue(SketchCell sourceCell) {
		return sourceCell.getTerrainType().getHillNoise();
	}

	@Override
	public void setTargetValue(U targetCell, int targetX, int targetY, double noiseFactor) {
		double oldElevation = targetCell.getElevation();
		double newElevation = oldElevation + noise.calculateNoise(targetX / resolution, targetY/ resolution) * noiseFactor;
		targetCell.setElevation(newElevation);
	}
}
