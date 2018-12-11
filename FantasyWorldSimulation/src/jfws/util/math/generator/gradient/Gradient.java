package jfws.util.math.generator.gradient;

import jfws.util.math.generator.Generator;
import jfws.util.math.interpolation.TwoValueInterpolator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class Gradient implements Generator {

	protected final TwoValueInterpolator interpolator;
	protected final double maxDistance;
	protected final double valueAtCenter;
	protected final double valueAtMax;

	protected double generate(double distance) {
		if(distance >= maxDistance) {
			return valueAtMax;
		}

		final double factor = distance / maxDistance;

		return interpolator.interpolate(valueAtCenter, valueAtMax, factor);
	}
}
