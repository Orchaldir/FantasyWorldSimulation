package jfws.util.math.geometry.voronoi;

import jfws.util.math.geometry.Point2d;
import jfws.util.math.geometry.Rectangle;
import jfws.util.math.geometry.mesh.Mesh;
import jfws.util.math.geometry.mesh.MeshBuilder;
import jfws.util.math.geometry.mesh.NoData;
import jfws.util.math.geometry.mesh.Vertex;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class ImageBasedVoronoiDiagram implements VoronoiDiagram {

	private class MapData {
		public int closestPointId = -1;
		public double distance = Double.MAX_VALUE;
	}

	private class PointData {
		public final int id;
		public final Point2d point;
		public final int x;
		public final int y;
		public final List<Vertex<NoData>> vertices = new ArrayList<>(3);

		public PointData(int id, Point2d point, int x, int y) {
			this.id = id;
			this.point = point;
			this.x = x;
			this.y = y;
		}
	}

	@RequiredArgsConstructor
	private class QueueData {
		public final PointData pointData;
		public final int x;
		public final int y;
	}

	private final Rectangle rectangle;
	private final MeshBuilder<NoData,NoData,VoronoiFaceData> meshBuilder = new MeshBuilder<>();

	private final double closestPointResolution;
	private int closestPointSizeX;
	private int closestPointSizeY;
	private MapData[][] closestPointMap;

	private List<PointData> pointDataList;

	@Override
	public Mesh<NoData,NoData,VoronoiFaceData> getMesh() {
		return meshBuilder;
	}

	@Override
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
		int id = 0;

		for (PointData pointData : pointDataList) {
			if(pointData.vertices.size() < 3) {
				log.warn("createFaces(): Face {} as too few points!", id, pointData.point);
				continue;
			}

			List<Vertex<NoData>> vertices = sortVertices(pointData);
			List<Integer> vertexIds = vertices.stream().map(Vertex::getId).collect(Collectors.toList());

			meshBuilder.createFace(vertexIds);
			id++;
		}
	}

	private List<Vertex<NoData>> sortVertices(PointData pointData) {
		return pointData.vertices.stream().
				sorted((v0, v1) ->
					((Double)pointData.point.getAngleTo(v0.getPoint())).
					compareTo(pointData.point.getAngleTo(v1.getPoint()))).
				collect(Collectors.toList());
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

				log.debug("findVerticesAtBorderX(): x={} y={} closestPointIds: {}!={} vertex={}",
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

				log.debug("findVerticesAtBorderY(): y={} x={} closestPointIds: {}!={} vertex={}",
						y, x, closestPointId0, closestPointId1, vertex.getId());

				pointDataList.get(closestPointId0).vertices.add(vertex);
				pointDataList.get(closestPointId1).vertices.add(vertex);
			}
		}
	}

	private void findVertices() {
		log.info("findVertices()");
		final int windowSize = 1;
		int columnSize = closestPointSizeY - windowSize;
		Vertex<NoData>[] lastVertices = new Vertex[columnSize];

		for (int x = 0; x < closestPointSizeX - windowSize; x++) {
			for (int y = 0; y < columnSize; y++) {
				Set<Integer> closestPointIds = new HashSet<>(4);

				closestPointIds.add(closestPointMap[x][y].closestPointId);
				closestPointIds.add(closestPointMap[x+1][y].closestPointId);
				closestPointIds.add(closestPointMap[x][y+1].closestPointId);
				closestPointIds.add(closestPointMap[x+1][y+1].closestPointId);

				if(closestPointIds.size() > 2) {
					if(y > 0 && lastVertices[y-1] != null) {
						log.info("findVertices(): Merged x={} y={} with previous vertex.", x, y);
						lastVertices[y] = lastVertices[y-1];
						continue;
					}
					else if(lastVertices[y] != null) {
						log.info("findVertices(): Merged x={} y={} with above vertex.", x, y);
						continue;
					}

					Point2d vertexPoint = createPoint(x + 1,  y + 1);
					Vertex<NoData> vertex = meshBuilder.createVertex(vertexPoint);

					log.info("findVertices(): x={} y={} point={} closestPointIds={} vertex={}",
							x, y, vertexPoint, closestPointIds, vertex.getId());

					for (Integer closestPointId : closestPointIds) {
						PointData pointData = pointDataList.get(closestPointId);
						pointData.vertices.add(vertex);
					}

					lastVertices[y] = vertex;
				}
				else {
					lastVertices[y] = null;
				}
			}
		}
	}

	private void fillClosestPointMap(List<Point2d> points) {
		log.info("fillClosestPointMap(): points={}", points.size());

		Queue<QueueData> queue = new LinkedList<>();
		pointDataList = new ArrayList<>(points.size());

		for (Point2d point : points) {
			int id = pointDataList.size();
			PointData pointData = new PointData(id, point, getCellX(point), getCellY(point));

			log.info("fillClosestPointMap(): id={} {} x={} y={}", id, point, pointData.x, pointData.y);

			queue.add(new QueueData(pointData, pointData.x, pointData.y));
			pointDataList.add(pointData);
		}

		while(!queue.isEmpty()) {
			QueueData queueData = queue.remove();
			PointData pointData = queueData.pointData;

			if(checkClosestPointMap(pointData, queueData.x,queueData.y)) {
				if(queueData.x > 0) {
					queue.add(new QueueData(pointData, queueData.x-1, queueData.y));
				}
				if(queueData.x < closestPointSizeX - 1) {
					queue.add(new QueueData(pointData, queueData.x+1, queueData.y));
				}
				if(queueData.y > 0) {
					queue.add(new QueueData(pointData, queueData.x, queueData.y-1));
				}
				if(queueData.y < closestPointSizeY - 1) {
					queue.add(new QueueData(pointData, queueData.x, queueData.y+1));
				}
			}
		}
	}

	private boolean checkClosestPointMap(PointData pointData, int x, int y) {
		MapData mapData = closestPointMap[x][y];
		double distance = Math.hypot(pointData.x - x, pointData.y - y);

		if(distance < mapData.distance) {
			mapData.distance = distance;
			mapData.closestPointId = pointData.id;
			return true;
		}

		return false;
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
