package jfws.util.math.random;

import lombok.Getter;

import java.util.Random;

public class GeneratorWithRandom implements RandomNumberGenerator {

	@Getter
	private long seed;
	private Random random;

	public GeneratorWithRandom(long seed) {
		restart(seed);
	}

	@Override
	public void restart() {
		random = new Random(seed);
	}

	@Override
	public void restart(long seed) {
		this.seed = seed;
		restart();
	}

	@Override
	public int getInteger() {
		return random.nextInt();
	}

	@Override
	public double getGaussian() {
		return random.nextGaussian();
	}
}
