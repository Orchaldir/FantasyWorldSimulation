package jfws.util.math.geometry.voronoi;

import jfws.util.math.geometry.Point2d;
import jfws.util.math.geometry.mesh.Face;
import jfws.util.math.geometry.mesh.Mesh;

import java.util.List;

public interface VoronoiDiagram<V,E,F> {
	Mesh<V,E,F> getMesh();

	Face<V,E,F> getFace(Point2d point);

	void update(List<Point2d> points);
}
