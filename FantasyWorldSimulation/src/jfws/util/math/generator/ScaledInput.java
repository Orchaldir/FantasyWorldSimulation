package jfws.util.math.generator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class ScaledInput implements Generator {

	private Generator noise;

	@Getter
	@Setter
	private double resolution;

	@Override
	public double generate(double x, double y) {
		return noise.generate(x / resolution, y / resolution);
	}
}
