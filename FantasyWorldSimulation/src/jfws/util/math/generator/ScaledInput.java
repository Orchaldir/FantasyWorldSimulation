package jfws.util.math.generator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class ScaledInput implements Generator {

	private Generator noise;

	@Getter
	private final double resolution;

	@Override
	public double generate(double x, double y) {
		return noise.generate(x / resolution, y / resolution);
	}
}
