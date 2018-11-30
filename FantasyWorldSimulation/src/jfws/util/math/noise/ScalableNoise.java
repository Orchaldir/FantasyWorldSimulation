package jfws.util.math.noise;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class ScalableNoise implements Noise {

	private Noise noise;

	@Getter
	@Setter
	private double resolution;

	@Override
	public double calculateNoise(double x, double y) {
		return noise.calculateNoise(x / resolution, y / resolution);
	}
}
