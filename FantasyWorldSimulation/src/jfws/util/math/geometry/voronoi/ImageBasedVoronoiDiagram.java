package jfws.util.math.geometry.voronoi;

import jfws.util.math.geometry.Point2d;
import jfws.util.math.geometry.Rectangle;
import jfws.util.math.geometry.mesh.Mesh;
import jfws.util.math.geometry.mesh.MeshBuilder;
import jfws.util.math.geometry.mesh.MeshBuilder.NoData;
import jfws.util.math.geometry.mesh.Vertex;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class ImageBasedVoronoiDiagram {

	private class MapData {
		public int closestPointId = -1;
		public double distance = Double.MAX_VALUE;
	}
	private class PointData {
		public final Point2d point;
		public final List<Vertex<NoData>> vertices = new ArrayList<>(3);

		public PointData(Point2d point) {
			this.point = point;
		}
	}

	private final Rectangle rectangle;
	private final MeshBuilder<NoData,NoData,VoronoiFaceData> meshBuilder = new MeshBuilder<>();

	private final double closestPointResolution;
	private int closestPointSizeX;
	private int closestPointSizeY;
	private MapData[][] closestPointMap;

	private List<PointData> pointDataList;

	public Mesh<NoData,NoData,VoronoiFaceData> getMesh() {
		return meshBuilder;
	}

	public void update(List<Point2d> points) {
		meshBuilder.clear();

		initClosestPointMap();
		fillClosestPointMap(points);
		findVertices();
		findVerticesAtBorder();
		createVerticesAtCorners();
		createFaces();
	}

	private void createFaces() {
		log.info("createFaces()");
		for (PointData pointData : pointDataList) {
			List<Integer> vertices = sortVertices(pointData);

			meshBuilder.createFace(vertices);
		}
	}

	private List<Integer> sortVertices(PointData pointData) {
		return pointData.vertices.stream().
				sorted((v0, v1) ->
					((Double)pointData.point.getAngleTo(v0.getPoint())).
					compareTo(pointData.point.getAngleTo(v1.getPoint()))).
				map(Vertex::getId).collect(Collectors.toList());
	}

	private void createVerticesAtCorners() {
		log.info("createVerticesAtCorners()");
		createVerticesAtCorner(0,0);
		createVerticesAtCorner(closestPointSizeX - 1,0);
		createVerticesAtCorner(0,closestPointSizeY - 1);
		createVerticesAtCorner(closestPointSizeX - 1,closestPointSizeY - 1);
	}

	private void createVerticesAtCorner(int x, int y) {
		int closestPointId = closestPointMap[x][y].closestPointId;
		Point2d vertexPoint = createPoint(x,  y);
		Vertex<NoData> vertex = meshBuilder.createVertex(vertexPoint);
		pointDataList.get(closestPointId).vertices.add(vertex);
	}

	private void findVerticesAtBorder() {
		log.info("findVerticesAtBorder()");
		findVerticesAtBorderX(0);
		findVerticesAtBorderX(closestPointSizeY - 1);
		findVerticesAtBorderY(0);
		findVerticesAtBorderY(closestPointSizeX - 1);
	}

	private void findVerticesAtBorderX(int y) {
		for (int x = 0; x < closestPointSizeX - 1; x++) {
			int closestPointId0 = closestPointMap[x][y].closestPointId;
			int closestPointId1 = closestPointMap[x+1][y].closestPointId;

			if(closestPointId0 != closestPointId1) {
				Point2d vertexPoint = createPoint(x + 1,  y);
				Vertex<NoData> vertex = meshBuilder.createVertex(vertexPoint);

				log.info("findVerticesAtBorderX(): x={} y={} closestPointIds: {}!={} vertex={}",
						x, y, closestPointId0, closestPointId1, vertex.getId());

				pointDataList.get(closestPointId0).vertices.add(vertex);
				pointDataList.get(closestPointId1).vertices.add(vertex);
			}
		}
	}

	private void findVerticesAtBorderY(int x) {
		for (int y = 0; y < closestPointSizeY - 1; y++) {
			int closestPointId0 = closestPointMap[x][y].closestPointId;
			int closestPointId1 = closestPointMap[x][y+1].closestPointId;

			if(closestPointId0 != closestPointId1) {
				Point2d vertexPoint = createPoint(x,  y + 1);
				Vertex<NoData> vertex = meshBuilder.createVertex(vertexPoint);

				log.info("findVerticesAtBorderY(): y={} x={} closestPointIds: {}!={} vertex={}",
						y, x, closestPointId0, closestPointId1, vertex.getId());

				pointDataList.get(closestPointId0).vertices.add(vertex);
				pointDataList.get(closestPointId1).vertices.add(vertex);
			}
		}
	}

	private void findVertices() {
		log.info("findVertices()");
		for (int x = 0; x < closestPointSizeX - 1; x++) {
			for (int y = 0; y < closestPointSizeY - 1; y++) {
				Set<Integer> closestPointIds = new HashSet<>(4);

				closestPointIds.add(closestPointMap[x][y].closestPointId);
				closestPointIds.add(closestPointMap[x+1][y].closestPointId);
				closestPointIds.add(closestPointMap[x][y+1].closestPointId);
				closestPointIds.add(closestPointMap[x+1][y+1].closestPointId);

				if(closestPointIds.size() > 2) {
					Point2d vertexPoint = createPoint(x + 1,  y + 1);
					Vertex<NoData> vertex = meshBuilder.createVertex(vertexPoint);

					log.info("findVertices(): x={} y={} closestPointIds={} vertex={}",
							x, y, closestPointIds, vertex.getId());

					for (Integer closestPointId : closestPointIds) {
						PointData pointData = pointDataList.get(closestPointId);
						pointData.vertices.add(vertex);
					}
				}
			}
		}
	}

	private void fillClosestPointMap(List<Point2d> points) {
		log.info("fillClosestPointMap(): points={}", points.size());

		pointDataList = new ArrayList<>(points.size());

		for (Point2d point : points) {
			int id = pointDataList.size();
			PointData pointData = new PointData(point);

			int pointX = getCellX(point);
			int pointY = getCellY(point);

			log.info("fillClosestPointMap(): id={} {} x={} y={}", id, point, pointX, pointY);

			for (int x = 0; x < closestPointSizeX; x++) {
				for (int y = 0; y < closestPointSizeY; y++) {
					MapData mapData = closestPointMap[x][y];
					double distance = Math.hypot(pointX - x, pointY - y);

					if(distance < mapData.distance) {
						mapData.distance = distance;
						mapData.closestPointId = id;
					}
				}
			}

			pointDataList.add(pointData);
		}
	}

	private void initClosestPointMap() {
		closestPointSizeX = getCellDistance(rectangle.getSize().getX());
		closestPointSizeY = getCellDistance(rectangle.getSize().getY());

		log.info("initClosestPointMap():  size={} resolution={} -> {}*{}",
				rectangle.getSize(), closestPointResolution, closestPointSizeX, closestPointSizeY);

		closestPointMap = new  MapData[closestPointSizeX][closestPointSizeY];

		for (int x = 0; x < closestPointSizeX; x++) {
			for (int y = 0; y < closestPointSizeY; y++) {
				closestPointMap[x][y] = new MapData();
			}
		}
	}

	private int getCellDistance(double value) {
		return (int) Math.ceil(value * closestPointResolution);
	}

	private int getCellX(Point2d point) {
		return getCellDistance(point.getX() - rectangle.getStart().getX());
	}

	private int getCellY(Point2d point) {
		return getCellDistance(point.getY() - rectangle.getStart().getY());
	}

	private Point2d createPoint(double cellX, double cellY) {
		double x = rectangle.getStart().getX() + cellX / closestPointResolution;
		double y = rectangle.getStart().getY() + cellY / closestPointResolution;
		return new Point2d(x, y);
	}
}
