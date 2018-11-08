package jfws.features.elevation;

public interface ElevationCell {

	double MIN_ELEVATION =  -200.0;
	double DEFAULT_ELEVATION = 0.0;
	double HILL_ELEVATION =  100.0;
	double MAX_ELEVATION =  1000.0;

	double getElevation();

	void setElevation(double value);
}
