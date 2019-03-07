package jfws.util.math.geometry.voronoi;

import jfws.util.math.geometry.Point2d;
import jfws.util.math.geometry.Rectangle;
import jfws.util.math.geometry.mesh.Face;
import jfws.util.math.geometry.mesh.MeshBuilder.NoData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VoronoiDiagramTest {

	private static final Point2d CORNER0 =  new Point2d(1.0, 2.0);
	private static final Point2d CORNER1 =  new Point2d(2.0, 2.0);
	private static final Point2d CORNER2 =  new Point2d(2.0, 3.0);
	private static final Point2d CORNER3 =  new Point2d(1.0, 3.0);

	private static final Point2d CENTER =  new Point2d(1.5, 2.5);
	private static final Point2d OUTSIDE =  new Point2d(0, 0);

	public static final Rectangle RECTANGLE = Rectangle.fromStartAndEnd(CORNER0, CORNER2);

	private VoronoiDiagram voronoiDiagram;

	@Nested
	class TestAddFirstPoint {

		private Face<NoData, NoData, VoronoiFaceData> face;

		@BeforeEach
		public void setUp() {
			voronoiDiagram = new VoronoiDiagram(RECTANGLE);
		}

		@Test
		public void testAddFirstPoint() {
			face = voronoiDiagram.addFirstPoint(CENTER);

			assertNotNull(face);
			assertThat(face.getData().point, is(equalTo(CENTER)));
			assertThat(face.getPointsInCCW(), contains(CORNER1, CORNER2, CORNER3, CORNER0));
		}

		@Test
		public void testAddFirstPointOutside() {
			assertThrows(IllegalArgumentException.class, () -> voronoiDiagram.addFirstPoint(OUTSIDE));
		}

		@Test
		public void testAddFirstPointTwice() {
			voronoiDiagram.addFirstPoint(CENTER);
			assertThrows(IllegalStateException.class, () -> voronoiDiagram.addFirstPoint(OUTSIDE));
		}
	}

}