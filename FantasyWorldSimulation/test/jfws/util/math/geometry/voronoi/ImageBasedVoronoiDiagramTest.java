package jfws.util.math.geometry.voronoi;

import jfws.util.math.geometry.Point2d;
import jfws.util.math.geometry.Rectangle;
import jfws.util.math.geometry.mesh.Face;
import jfws.util.math.geometry.mesh.Mesh;
import jfws.util.math.geometry.mesh.NoData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class ImageBasedVoronoiDiagramTest {

	public static final double CLOSEST_POINT_RESOLUTION = 100.0;
	public static final double MAX_DISTANCE = 0.02;

	private static final Point2d CORNER0 =  new Point2d(1.0, 2.0);
	private static final Point2d CORNER1 =  new Point2d(2.0, 2.0);
	private static final Point2d CORNER2 =  new Point2d(2.0, 4.0);
	private static final Point2d CORNER3 =  new Point2d(1.0, 4.0);

	private static final Point2d BOTTOM_HALF =  new Point2d(1.5, 2.0);
	private static final Point2d TOP_HALF =  new Point2d(1.5, 4.0);
	private static final Point2d LEFT_HALF =  new Point2d(1.0, 3.0);
	private static final Point2d RIGHT_HALF =  new Point2d(2.0, 3.0);

	private static final Point2d CENTER =  new Point2d(1.5, 3.0);
	private static final Point2d OUTSIDE =  new Point2d(0, 0);

	private static final Point2d POINT0 =  new Point2d(1.25, 2.5);
	private static final Point2d POINT1 =  new Point2d(1.75, 2.5);
	private static final Point2d POINT2 =  new Point2d(1.75, 3.5);
	private static final Point2d POINT3 =  new Point2d(1.25, 3.5);

	public static final Rectangle RECTANGLE = Rectangle.fromStartAndEnd(CORNER0, CORNER2);

	private ImageBasedVoronoiDiagram<NoData, NoData, NoData> voronoiDiagram;
	private Mesh<NoData, NoData, NoData> mesh;

	@BeforeEach
	public void setUp() {
		voronoiDiagram = new ImageBasedVoronoiDiagram<>(RECTANGLE, CLOSEST_POINT_RESOLUTION);
		mesh = voronoiDiagram.getMesh();
	}

	@Test
	public void testPointIsOutside() {
		assertThrows(IllegalArgumentException.class, () -> voronoiDiagram.update(List.of(OUTSIDE)));
	}

	@Test
	public void testResetMesh() {
		voronoiDiagram.update(List.of(POINT0, POINT1));
		voronoiDiagram.update(List.of(CENTER));

		assertNumberOfFaces(1);
		assertFaceIsFullRectangle();
	}

	@Test
	public void testOnePoint() {
		voronoiDiagram.update(List.of(CENTER));

		assertNumberOfFaces(1);
		assertFaceIsFullRectangle();
	}

	@Test
	public void testDifferentPoint() {
		voronoiDiagram.update(List.of(POINT0));

		assertNumberOfFaces(1);
		assertFaceIsFullRectangle();
	}

	@Test
	public void testPointOnBottomBorder() {
		voronoiDiagram.update(List.of(BOTTOM_HALF));

		assertNumberOfFaces(1);
		assertFaceIsFullRectangle();
	}

	@Test
	public void testPointOnTopBorder() {
		voronoiDiagram.update(List.of(TOP_HALF));

		assertNumberOfFaces(1);
		assertFaceIsFullRectangle();
	}

	@Test
	public void testPointOnLeftBorder() {
		voronoiDiagram.update(List.of(LEFT_HALF));

		assertNumberOfFaces(1);
		assertFaceIsFullRectangle();
	}

	@Test
	public void testPointOnRightBorder() {
		voronoiDiagram.update(List.of(RIGHT_HALF));

		assertNumberOfFaces(1);
		assertFaceIsFullRectangle();
	}

	@Test
	public void testTwoPointsWithSameX() {
		voronoiDiagram.update(List.of(POINT0, POINT3));

		assertNumberOfFaces(2);
		assertFaceIsBottomHalf(0);
		assertFaceIsTopHalf(1);
	}

	@Test
	public void testReversedTwoPointsWithSameX() {
		voronoiDiagram.update(List.of(POINT3, POINT0));

		assertNumberOfFaces(2);
		assertFaceIsTopHalf(0);
		assertFaceIsBottomHalf(1);
	}

	@Test
	public void testTwoPointsWithSameY() {
		voronoiDiagram.update(List.of(POINT0, POINT1));

		assertNumberOfFaces(2);
		assertFaceIsLeftHalf(0);
		assertFaceIsRightHalf(1);
	}

	@Test
	public void testReversedTwoPointsWithSameY() {
		voronoiDiagram.update(List.of(POINT1, POINT0));

		assertNumberOfFaces(2);
		assertFaceIsRightHalf(0);
		assertFaceIsLeftHalf(1);
	}

	@Test
	public void testDifferentTwoPointsWithSameY() {
		voronoiDiagram.update(List.of(POINT2, POINT3));

		assertNumberOfFaces(2);
		assertFaceIsRightHalf(0);
		assertFaceIsLeftHalf(1);
	}

	@Test
	public void testFourPoints() {
		voronoiDiagram.update(List.of(POINT0, POINT1, POINT2, POINT3));

		assertNumberOfFaces(4);
		assertFaceIsBottomLeft(0);
		assertFaceIsBottomRight(1);
		assertFaceIsTopRight(2);
		assertFaceIsTopLeft(3);
	}

	private void assertNumberOfFaces(int size) {
		assertThat(mesh.getFaces(), hasSize(size));
	}

	// full

	private void assertFaceIsFullRectangle() {
		assertFace(0, List.of(CORNER0, CORNER1, CORNER2, CORNER3));
	}

	// halves

	private void assertFaceIsBottomHalf(int i) {
		assertFace(i, List.of(CORNER0, CORNER1, RIGHT_HALF, LEFT_HALF));
	}

	private void assertFaceIsTopHalf(int i) {
		assertFace(i, List.of(CORNER2, CORNER3, LEFT_HALF, RIGHT_HALF));
	}

	private void assertFaceIsLeftHalf(int i) {
		assertFace(i, List.of(CORNER0, BOTTOM_HALF, TOP_HALF, CORNER3));
	}

	private void assertFaceIsRightHalf(int i) {
		assertFace(i, List.of(CORNER1, CORNER2, TOP_HALF, BOTTOM_HALF));
	}

	// quarter

	private void assertFaceIsBottomLeft(int i) {
		assertFace(i, List.of(CORNER0, BOTTOM_HALF, CENTER, LEFT_HALF));
	}

	private void assertFaceIsBottomRight(int i) {
		assertFace(i, List.of(CORNER1, RIGHT_HALF, CENTER, BOTTOM_HALF));
	}

	private void assertFaceIsTopLeft(int i) {
		assertFace(i, List.of(CORNER3, LEFT_HALF, CENTER, TOP_HALF));
	}

	private void assertFaceIsTopRight(int i) {
		assertFace(i, List.of(CORNER2, TOP_HALF, CENTER, RIGHT_HALF));
	}

	private void assertFace(int i, List<Point2d> corners) {
		Face<NoData, NoData, NoData> face = mesh.getFace(i);

		assertNotNull(face);
		assertThat(face.getId(), is(equalTo(i)));

		List<Point2d> pointsInCCW = face.getPointsInCCW();

		assertSamePoint(pointsInCCW, corners, MAX_DISTANCE);
	}

	private void assertSamePoint(List<Point2d> a, List<Point2d> b, double maxDistance) {
		for (Point2d point : a) {
			assertPointIsContained(b, point, maxDistance);
		}
	}

	private void assertPointIsContained(List<Point2d> points, Point2d pointToCheck, double maxDistance) {
		for (Point2d point : points) {
			if(point.getDistanceTo(pointToCheck) < maxDistance) {
				return;
			}
		}

		fail(String.format("%s not contained in %s!", pointToCheck, points));
	}

}