package jfws.util.math.random;

public interface RandomNumberGenerator {

	long getSeed();

	void restart();
	void restart(long seed);

	int getInteger();
	double getGaussian();
}
