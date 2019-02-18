package jfws.util.math.geometry.distribution;

import jfws.util.math.geometry.Point2d;

import java.util.List;

public interface PointDistribution {

	List<Point2d> distributePoints(Point2d size, int N);

}
