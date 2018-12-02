package jfws.features.elevation.noise;

import jfws.features.elevation.ElevationCell;
import jfws.util.map.CellInterpolator;
import jfws.util.map.CellMap2d;
import jfws.util.map.Map2d;
import jfws.util.map.OutsideMapException;
import jfws.util.math.interpolation.Interpolator2d;
import jfws.util.math.noise.Noise;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

public class ElevationNoiseCellInterpolation<T extends NoiseAmplitudeStorage, U extends ElevationCell>
		extends CellInterpolator<T,U> {

	@Getter
	private Noise noise;

	@Getter
	private int index;

	public ElevationNoiseCellInterpolation(Interpolator2d interpolator, Noise noise, int index) {
		super(interpolator);
		this.noise = noise;
		this.index = index;
	}

	@Override
	public double getSourceValue(NoiseAmplitudeStorage sourceStorage) {
		return sourceStorage.getNoiseAmplitude(index);
	}

	@Override
	public void setTargetValue(CellMap2d<U> targetMap, int targetX, int targetY, double noiseFactor) throws OutsideMapException {
		U targetCell = targetMap.getCell(targetX, targetY);

		double oldElevation = targetCell.getElevation();
		double scaledNoise = noise.calculateNoise(targetX, targetY) * noiseFactor;
		double newElevation = oldElevation + scaledNoise;

		targetCell.setElevation(newElevation);
	}
}
