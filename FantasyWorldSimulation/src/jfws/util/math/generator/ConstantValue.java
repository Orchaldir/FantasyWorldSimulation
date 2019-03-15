package jfws.util.math.generator;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ConstantValue implements Generator {

	private final double value;

	@Override
	public double generate(double x, double y) {
		return value;
	}
}
