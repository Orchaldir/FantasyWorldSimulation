package jfws.util.math.random;

import lombok.Getter;
import lombok.ToString;

import java.util.Random;

@ToString(of = {"seed"})
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
	public int getInteger(int maxValue) {
		return random.nextInt(maxValue + 1);
	}

	@Override
	public double getDoubleBetweenZeroAndOne() {
		return random.nextDouble();
	}

	@Override
	public double getGaussian() {
		return random.nextGaussian();
	}
}
