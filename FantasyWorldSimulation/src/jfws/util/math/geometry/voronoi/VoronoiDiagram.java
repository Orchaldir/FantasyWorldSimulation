package jfws.util.math.geometry.voronoi;

import jfws.util.math.geometry.Point2d;
import jfws.util.math.geometry.mesh.Mesh;
import jfws.util.math.geometry.mesh.NoData;

import java.util.List;

public interface VoronoiDiagram<V,E,F> {
	Mesh<V,E,F> getMesh();

	void update(List<Point2d> points);
}
