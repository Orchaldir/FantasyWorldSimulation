package jfws.util.math.random;

public interface RandomNumberGenerator {

	long getSeed();

	void restart();
	void restart(long seed);

	int getInteger();
	int getInteger(int maxValue);

	double getDoubleBetweenZeroAndOne();
	double getGaussian();
}
