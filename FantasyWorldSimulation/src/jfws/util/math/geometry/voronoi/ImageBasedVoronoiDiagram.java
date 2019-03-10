package jfws.util.math.geometry.voronoi;

import jfws.util.math.geometry.Point2d;
import jfws.util.math.geometry.Rectangle;
import jfws.util.math.geometry.mesh.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class ImageBasedVoronoiDiagram<V,E,F> implements VoronoiDiagram<V,E,F> {

	private class ClosestPointData<V,E,F> {
		public int closestPointId = -1;
		public double distance = Double.MAX_VALUE;
	}

	private class PointData {
		public final int id;
		public final Point2d point;
		public final int x;
		public final int y;
		public final List<Vertex<V>> vertices = new ArrayList<>(3);

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
	private final MeshBuilder<V,E,F> meshBuilder = new MeshBuilder<>();

	private final double closestPointResolution;
	private int closestPointSizeX;
	private int closestPointSizeY;
	private ClosestPointData[][] closestPointMap;

	private List<PointData> pointDataList;

	@Override
	public Mesh<V,E,F> getMesh() {
		return meshBuilder;
	}

	@Override
	public void update(List<Point2d> points) {
		meshBuilder.clear();

		createPointDataList(points);
		initClosestPointMap();
		fillClosestPointMap(points);
		findVertices();
		findVerticesAtBorder();
		createVerticesAtCorners();
		createFaces();
	}

	private void createPointDataList(List<Point2d> points) {
		log.info("createPointDataList(): points={}", points.size());

		pointDataList = new ArrayList<>(points.size());

		for (Point2d point : points) {
			int id = pointDataList.size();

			if(!rectangle.isInside(point)) {
				throw new IllegalArgumentException(String.format("Point %d is outside!", id));
			}

			PointData pointData = new PointData(id, point, getCellX(point), getCellY(point));

			log.debug("createPointDataList(): id={} {} x={} y={}", id, point, pointData.x, pointData.y);

			pointDataList.add(pointData);
		}
	}

	private void createFaces() {
		log.info("createFaces()");
		int id = 0;

		for (PointData pointData : pointDataList) {
			if(pointData.vertices.size() < 3) {
				log.warn("createFaces(): Face {} as too few points!", id, pointData.point);
				continue;
			}

			List<Vertex<V>> vertices = sortVertices(pointData);
			List<Integer> vertexIds = vertices.stream().map(Vertex::getId).collect(Collectors.toList());

			meshBuilder.createFace(vertexIds);
			id++;
		}
	}

	private List<Vertex<V>> sortVertices(PointData pointData) {
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
		Vertex<V> vertex = meshBuilder.createVertex(vertexPoint);
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
				Point2d vertexPoint = createPoint(x + 1.0,  y);
				Vertex<V> vertex = meshBuilder.createVertex(vertexPoint);

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
				Point2d vertexPoint = createPoint(x,  y + 1.0);
				Vertex<V> vertex = meshBuilder.createVertex(vertexPoint);

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
		Vertex<V>[] lastVertices = new Vertex[columnSize];

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

					Point2d vertexPoint = createPoint(x + 1.0,  y + 1.0);
					Vertex<V> vertex = meshBuilder.createVertex(vertexPoint);

					log.debug("findVertices(): x={} y={} point={} closestPointIds={} vertex={}",
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

		for (PointData pointData : pointDataList) {
			queue.add(new QueueData(pointData, pointData.x, pointData.y));
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
		ClosestPointData closestPointData = closestPointMap[x][y];
		double distance = Math.hypot(pointData.x - x, pointData.y - y);

		if(distance < closestPointData.distance) {
			closestPointData.distance = distance;
			closestPointData.closestPointId = pointData.id;
			return true;
		}

		return false;
	}

	private void initClosestPointMap() {
		closestPointSizeX = getCellDistance(rectangle.getSize().getX()) + 1;
		closestPointSizeY = getCellDistance(rectangle.getSize().getY()) + 1;

		log.info("initClosestPointMap():  size={} resolution={} -> {}*{}",
				rectangle.getSize(), closestPointResolution, closestPointSizeX, closestPointSizeY);

		closestPointMap = new ClosestPointData[closestPointSizeX][closestPointSizeY];

		for (int x = 0; x < closestPointSizeX; x++) {
			for (int y = 0; y < closestPointSizeY; y++) {
				closestPointMap[x][y] = new ClosestPointData();
			}
		}
	}

	private int getCellDistance(double value) {
		return (int) Math.round(value * closestPointResolution);
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
