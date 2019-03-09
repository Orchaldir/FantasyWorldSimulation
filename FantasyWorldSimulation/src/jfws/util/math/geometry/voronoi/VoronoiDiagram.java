package jfws.util.math.geometry.voronoi;

import jfws.util.math.geometry.Point2d;
import jfws.util.math.geometry.mesh.Mesh;
import jfws.util.math.geometry.mesh.NoData;

import java.util.List;

public interface VoronoiDiagram {
	Mesh<NoData, NoData,VoronoiFaceData> getMesh();

	void update(List<Point2d> points);
}
