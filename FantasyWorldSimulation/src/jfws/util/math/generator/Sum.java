package jfws.util.math.generator;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class Sum implements Generator {

	private final List<Generator> generators;

	@Override
	public double generate(double x, double y) {
		double sum = 0;

		for (Generator generator : generators) {
			sum += generator.generate(x, y);
		}

		return sum;
	}
}
