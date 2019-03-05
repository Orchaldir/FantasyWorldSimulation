package jfws.util.math.geometry.mesh;

import jfws.util.math.geometry.Point2d;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class MeshBuilderTest {

	private Point2d point0;
	private Point2d point1;
	private Point2d point2;
	private Point2d point3;

	private MeshBuilder mesh;

	@BeforeEach
	public void setUp() {
		point0 = mock(Point2d.class);
		point1 = mock(Point2d.class);
		point2 = mock(Point2d.class);
		point3 = mock(Point2d.class);

		mesh = new MeshBuilder();
	}

	private void assertVertex(Vertex vertex, int id, double x, double y, HalfEdge edge) {
		assertNotNull(vertex);
		assertThat(vertex.getId(), is(equalTo(id)));
		assertThat(vertex.getPoint().getX(), is(equalTo(x)));
		assertThat(vertex.getPoint().getY(), is(equalTo(y)));
		assertThat(vertex.getEdge(), is(equalTo(edge)));
	}

	private void assertVertex(Vertex vertex, int id, Point2d point, HalfEdge edge) {
		assertVertex(vertex, id, point.getX(), point.getY(), edge);
	}

	private void assertEdge(HalfEdge edge, int id, Vertex endVertex, Face face, HalfEdge next) {
		assertNotNull(edge);
		assertThat(edge.getId(), is(equalTo(id)));
		assertThat(edge.getEndVertex(), is(equalTo(endVertex)));
		assertThat(edge.getFace(), is(equalTo(face)));
		assertThat(edge.getNextEdge(), is(equalTo(next)));
	}

	@Nested
	class TestCreateVertex {

		@Test
		public void createVertexFromPoint() {
			Vertex vertex = mesh.createVertex(point0);

			assertVertex(vertex, 0, point0, null);
		}

		@Test
		public void createVertexFromDoubles() {
			Vertex vertex = mesh.createVertex(point0.getX(), point0.getY());

			assertVertex(vertex, 0, point0, null);
		}

		@Test
		public void createMultipleVertices() {
			Vertex vertex0 = mesh.createVertex(point0);
			Vertex vertex1 = mesh.createVertex(point1);

			assertVertex(vertex0, 0, point0, null);
			assertVertex(vertex1, 1, point1, null);
		}
	}

	@Nested
	class TestGetVertex {

		@Test
		public void getExistingVertex() {
			mesh.createVertex(point0);
			mesh.createVertex(point1);
			Vertex vertex2 = mesh.createVertex(point2);
			mesh.createVertex(point3);

			Vertex vertex = mesh.getVertex(2);

			assertThat(vertex, is(equalTo(vertex2)));
			assertVertex(vertex, 2, point2, null);
		}

		@Test
		public void getVertexWithNegativeIndex() {
			mesh.createVertex(point0);

			assertThrows(IndexOutOfBoundsException.class, () ->  mesh.getVertex(-1));
		}

		@Test
		public void getVertexWithTooHighIndex() {
			mesh.createVertex(point0);

			assertThrows(IndexOutOfBoundsException.class, () ->  mesh.getVertex(1));
		}
	}

	@Nested
	class TestEdge {

		private Vertex vertex0;
		private Vertex vertex1;
		private Vertex vertex2;
		private Vertex vertex3;

		@BeforeEach
		public void setUp() {
			vertex0 = mesh.createVertex(point0);
			vertex1 = mesh.createVertex(point1);
			vertex2 = mesh.createVertex(point2);
			vertex3 = mesh.createVertex(point3);
		}

		@Nested
		class TestCreateEdge {

			@Test
			public void createEdge() {
				HalfEdge edge = mesh.createEdge(0, 1);

				assertEdge(edge, 0, vertex1, null, null);
				assertEdge(edge.getOppositeEdge(), 1, vertex0, null, null);
			}

			@Test
			public void createEdgeTwice() {
				mesh.createEdge(0, 1);

				assertThrows(IllegalArgumentException.class, () -> mesh.createEdge(0, 1));
			}

			@Test
			public void createEdgeWithSameIndex() {
				assertThrows(IllegalArgumentException.class, () -> mesh.createEdge(0, 0));
			}

			@Test
			public void createEdgeWithNegativeStartIndex() {
				assertThrows(IndexOutOfBoundsException.class, () -> mesh.createEdge(-1, 0));
			}

			@Test
			public void createEdgeWithTooHighStartIndex() {
				assertThrows(IndexOutOfBoundsException.class, () -> mesh.createEdge(4, 0));
			}

			@Test
			public void createEdgeWithNegativeEndIndex() {
				assertThrows(IndexOutOfBoundsException.class, () -> mesh.createEdge(0, -1));
			}

			@Test
			public void createEdgeWithTooHighEndIndex() {
				assertThrows(IndexOutOfBoundsException.class, () -> mesh.createEdge(0, 4));
			}
		}

		@Nested
		class TestGetEdge {

			private HalfEdge edge0;
			private HalfEdge edge1;
			private HalfEdge edge2;

			@BeforeEach
			public void setUp() {
				edge0 = mesh.createEdge(0, 1);
				edge1 = mesh.createEdge(1, 2);
				edge2 = mesh.createEdge(2, 3);
			}

			@Test
			public void getExistingEdge() {
				assertThat(mesh.getEdge(2), is(equalTo(edge1)));
				assertThat(mesh.getEdge(3), is(equalTo(edge1.oppositeEdge)));
			}

			@Test
			public void getEdgeWithNegativeIndex() {
				assertThrows(IndexOutOfBoundsException.class, () -> mesh.getEdge(-1));
			}

			@Test
			public void getEdgeWithTooHighIndex() {
				assertThrows(IndexOutOfBoundsException.class, () -> mesh.getEdge(6));
			}
		}

		@Nested
		class TestSplitEdge {

			private Point2d point4;
			private Vertex splitAtVertex;
			private Face triangle0;
			private Face triangle1;

			@BeforeEach
			public void setUp() {
				point4 = mock(Point2d.class);
				splitAtVertex = mesh.createVertex(point4);

				triangle0 = mesh.createTriangle(0, 1, 2);
				triangle1 = mesh.createTriangle(0, 2, 3);
			}

			@Test
			public void testSplitEdgeWithoutOppositeFace() {
				HalfEdge edge = mesh.getEdge(0);
				HalfEdge oppositeEdge = edge.getOppositeEdge();
				HalfEdge nextEdge = edge.getNextEdge();

				HalfEdge newEdge = mesh.splitEdge(0, 4);

				assertEdge(edge, 0, splitAtVertex, triangle0, newEdge);
				assertEdge(newEdge, 10, vertex1, triangle0, nextEdge);

				assertEdge(oppositeEdge, 1, splitAtVertex, null, edge.getOppositeEdge());
				assertEdge(edge.getOppositeEdge(), 11, vertex0, null, null);
			}

			@Test
			public void testSplitOppositeEdgeWithoutFace() {
				HalfEdge edge = mesh.getEdge(1);
				HalfEdge oppositeEdge = edge.getOppositeEdge();
				HalfEdge nextEdge = edge.getNextEdge();

				HalfEdge newEdge = mesh.splitEdge(1, 4);

				assertEdge(edge, 1, splitAtVertex, null, newEdge);
				assertEdge(newEdge, 10, vertex0, null, nextEdge);

				assertEdge(oppositeEdge, 0 , splitAtVertex, triangle0, edge.getOppositeEdge());
				assertEdge(edge.getOppositeEdge(), 11, vertex1, triangle0, mesh.getEdge(2));
			}
		}

		@Nested
		class TestMergeEdge {

			private Face triangle0;
			private Face triangle1;

			@BeforeEach
			public void setUp() {
				triangle0 = mesh.createTriangle(0, 1, 2);
				triangle1 = mesh.createTriangle(0, 2, 3);
			}

			private void assertEdge(HalfEdge edge, int id, Vertex startVertex, Vertex endVertex, Face face) {
				assertNotNull(edge);
				assertThat(edge.getId(), is(equalTo(id)));
				assertThat(edge.getStartVertex(), is(equalTo(startVertex)));
				assertThat(edge.getEndVertex(), is(equalTo(endVertex)));
				assertThat(edge.getFace(), is(equalTo(face)));
			}

			@Test
			public void testMergeEdge() {
				mesh.mergeEdge(4);

				assertThat(mesh.getFaces().size(), is(equalTo(1)));
				assertThat(mesh.getFace(0), is(equalTo(triangle0)));

				HalfEdge edge = triangle0.getEdge();
				assertEdge(edge, 0, vertex0, vertex1,  triangle0);
				assertEdge(edge.getOppositeEdge(), 1, vertex1, vertex0,  null);

				edge = edge.getNextEdge();
				assertEdge(edge, 2, vertex1, vertex2,  triangle0);
				assertEdge(edge.getOppositeEdge(), 3, vertex2, vertex1,  null);

				edge = edge.getNextEdge();
				assertEdge(edge, 6, vertex2, vertex3,  triangle0);
				assertEdge(edge.getOppositeEdge(), 7, vertex3, vertex2,  null);

				edge = edge.getNextEdge();
				assertEdge(edge, 8, vertex3, vertex0,  triangle0);
				assertEdge(edge.getOppositeEdge(), 9, vertex0, vertex3,  null);
			}

			@Test
			public void testMergeEdgeWithoutSecondFace() {
				assertThrows(IllegalArgumentException.class, () -> mesh.mergeEdge(0));
			}
		}
	}

	@Nested
	class TestFace {

		private Vertex vertex0;
		private Vertex vertex1;
		private Vertex vertex2;
		private Vertex vertex3;

		@BeforeEach
		public void setUp() {
			vertex0 = mesh.createVertex(point0);
			vertex1 = mesh.createVertex(point1);
			vertex2 = mesh.createVertex(point2);
			vertex3 = mesh.createVertex(point3);
		}

		@Nested
		class TestCreateTriangle {

			@Test
			public void createTriangle() {
				Face triangle = mesh.createTriangle(2, 3, 0);

				assertThat(triangle.getId(), is(equalTo(0)));

				HalfEdge edge0 = triangle.getEdge();
				HalfEdge edge1 = edge0.getNextEdge();
				HalfEdge edge2 = edge1.getNextEdge();

				assertEdge(edge0, 0, vertex3, triangle, edge1);
				assertEdge(edge1, 2, vertex0, triangle, edge2);
				assertEdge(edge2, 4, vertex2, triangle, edge0);

				HalfEdge opposite0 = edge0.getOppositeEdge();
				HalfEdge opposite1 = edge1.getOppositeEdge();
				HalfEdge opposite2 = edge2.getOppositeEdge();

				assertEdge(opposite0, 1, vertex2, null, null);
				assertEdge(opposite1, 3, vertex3, null, null);
				assertEdge(opposite2, 5, vertex0, null, null);
			}

			@Test
			public void createTriangleWhileReusingAnEdge() {
				Face triangle0 = mesh.createTriangle(0, 1, 2);
				Face triangle1 = mesh.createTriangle(0, 2, 3);

				assertThat(triangle0.getId(), is(equalTo(0)));
				assertThat(triangle1.getId(), is(equalTo(1)));

				HalfEdge edge0 = triangle0.getEdge().getPreviousEdge();

				assertThat(edge0.getFace(), is(equalTo(triangle0)));
				assertThat(edge0.getOppositeFace(), is(equalTo(triangle1)));
				assertThat(edge0.getOppositeEdge(), is(equalTo(triangle1.getEdge())));
			}

			@Test
			public void createTriangleWithNegativeIndex() {
				assertThrows(IndexOutOfBoundsException.class, () -> mesh.createTriangle(-1, 1, 2));
				assertThrows(IndexOutOfBoundsException.class, () -> mesh.createTriangle(0, -1, 2));
				assertThrows(IndexOutOfBoundsException.class, () -> mesh.createTriangle(0, 1, -2));
			}

			@Test
			public void createTriangleWithTooHighIndex() {

				assertThrows(IndexOutOfBoundsException.class, () -> mesh.createTriangle(6, 1, 2));
				assertThrows(IndexOutOfBoundsException.class, () -> mesh.createTriangle(0, 6, 2));
				assertThrows(IndexOutOfBoundsException.class, () -> mesh.createTriangle(0, 1, 6));
			}
		}

		@Nested
		class TestCreateFace {

			@Test
			public void createFace() {
				Face face = mesh.createFace(Arrays.asList(0, 1, 2, 3));

				assertNotNull(face);
				assertThat(face.getId(), is(equalTo(0)));

				HalfEdge edge0 = face.getEdge();
				HalfEdge edge1 = edge0.getNextEdge();
				HalfEdge edge2 = edge1.getNextEdge();
				HalfEdge edge3 = edge2.getNextEdge();

				assertEdge(edge0, 0, vertex1, face, edge1);
				assertEdge(edge1, 2, vertex2, face, edge2);
				assertEdge(edge2, 4, vertex3, face, edge3);
				assertEdge(edge3, 6, vertex0, face, edge0);

				HalfEdge opposite0 = edge0.getOppositeEdge();
				HalfEdge opposite1 = edge1.getOppositeEdge();
				HalfEdge opposite2 = edge2.getOppositeEdge();
				HalfEdge opposite3 = edge3.getOppositeEdge();

				assertEdge(opposite0, 1, vertex0, null, null);
				assertEdge(opposite1, 3, vertex1, null, null);
				assertEdge(opposite2, 5, vertex2, null, null);
				assertEdge(opposite3, 7, vertex3, null, null);
			}

			@Test
			public void createTwoFaces() {
				Face face0 = mesh.createFace(Arrays.asList(0, 1, 2));
				Face face1 = mesh.createFace(Arrays.asList(0, 2, 3));

				assertThat(face0.getId(), is(equalTo(0)));
				assertThat(face1.getId(), is(equalTo(1)));
			}

			@Test
			public void createFaceWithNoVertices() {
				assertThrows(IllegalArgumentException.class, () -> mesh.createFace(Collections.emptyList()));
			}

			@Test
			public void createFaceWithOneVertex() {
				assertThrows(IllegalArgumentException.class, () -> mesh.createFace(Arrays.asList(0)));
			}

			@Test
			public void createFaceWithTwoVertices() {
				assertThrows(IllegalArgumentException.class, () -> mesh.createFace(Arrays.asList(0, 1)));
			}

			@Test
			public void createFaceWithNegativeIndex() {
				assertThrows(IndexOutOfBoundsException.class, () -> mesh.createFace(Arrays.asList(0, -1, 2, 3)));
			}

			@Test
			public void createFaceWithTooHighIndex() {
				assertThrows(IndexOutOfBoundsException.class, () -> mesh.createFace(Arrays.asList(0, 1, 6, 3)));
			}
		}

		@Nested
		class TestGetFace {

			private Face triangle;
			private Face face;

			@BeforeEach
			public void setUp() {
				triangle = mesh.createTriangle(0, 1, 2);
				face = mesh.createFace(Arrays.asList(0, 2, 3));
			}

			@Test
			public void getExistingTriangle() {
				assertThat(mesh.getFace(0), is(equalTo(triangle)));
			}

			@Test
			public void getExistingFace() {
				assertThat(mesh.getFace(1), is(equalTo(face)));
			}

			@Test
			public void getFaceWithNegativeIndex() {
				assertThrows(IndexOutOfBoundsException.class, () ->  mesh.getFace(-1));
			}

			@Test
			public void getEdgeWithTooHighIndex() {
				assertThrows(IndexOutOfBoundsException.class, () ->  mesh.getFace(2));
			}
		}
	}

}