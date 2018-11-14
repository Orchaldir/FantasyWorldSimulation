package jfws.util.math.noise;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimplexNoiseTest {

	private static final double RANGE = 100.0;

	private SimplexNoise noise = new SimplexNoise();

	@Test
	void testCalculateNoiseInInterval() {
		for(double x = -RANGE; x < RANGE; x++) {
			for(double y = -RANGE; y < RANGE; y++) {
			}
		}
	}

}