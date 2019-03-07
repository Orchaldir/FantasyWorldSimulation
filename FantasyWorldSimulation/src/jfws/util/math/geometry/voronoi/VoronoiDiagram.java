package jfws.util.math.geometry.voronoi;

import jfws.util.math.geometry.Point2d;
import jfws.util.math.geometry.Rectangle;
import jfws.util.math.geometry.mesh.Face;
import jfws.util.math.geometry.mesh.Mesh;
import jfws.util.math.geometry.mesh.MeshBuilder;
import jfws.util.math.geometry.mesh.MeshBuilder.NoData;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class VoronoiDiagram {

	private final Rectangle rectangle;
	private final MeshBuilder<NoData,NoData,VoronoiFaceData> meshBuilder = new MeshBuilder<>();

	public Mesh<NoData,NoData,VoronoiFaceData> getMesh() {
		return meshBuilder;
	}

	public Face<NoData, NoData, VoronoiFaceData> addFirstPoint(Point2d point) {
		if(!meshBuilder.getFaces().isEmpty()) {
			throw new IllegalStateException("Not the first point!");
		}
		else if(!rectangle.isInside(point)) {
			throw new IllegalArgumentException(String.format("%s is not inside!", point));
		}

		List<Point2d> corners = rectangle.getCorners();

		List<Integer> vertexIds = corners.stream().
				map(corner -> meshBuilder.createVertex(corner).getId()).
				collect(Collectors.toList());

		Face<NoData, NoData, VoronoiFaceData> face = meshBuilder.createFace(vertexIds);
		face.setData(new VoronoiFaceData(point));

		return face;
	}
}
