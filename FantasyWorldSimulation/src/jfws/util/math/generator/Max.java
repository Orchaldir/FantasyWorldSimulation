package jfws.util.math.generator;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class Max implements Generator {

	private final List<Generator> generators;

	@Override
	public double generate(double x, double y) {
		double max = Double.MIN_VALUE;

		for (Generator generator : generators) {
			max = Math.max(max, generator.generate(x, y));
		}

		return max;
	}
}
