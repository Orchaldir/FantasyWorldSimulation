package jfws.util.math.geometry.mesh;

import jfws.util.math.geometry.Point2d;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class MeshTest {

	private Point2d point0;
	private Point2d point1;
	private Point2d point2;
	private Point2d point3;

	private Mesh mesh;

	@BeforeEach
	public void setUp() {
		point0 = mock(Point2d.class);
		point1 = mock(Point2d.class);
		point2 = mock(Point2d.class);
		point3 = mock(Point2d.class);

		mesh = new Mesh();
	}

	private void assertVertex(Vertex vertex, int id, Point2d point, HalfEdge edge) {
		assertNotNull(vertex);
		assertThat(vertex.getId(), is(equalTo(id)));
		assertThat(vertex.getPoint(), is(equalTo(point)));
		assertThat(vertex.getEdge(), is(equalTo(edge)));
	}

	@Nested
	class TestCreateVertex {

		@Test
		public void createVertexOnce() {
			Vertex vertex = mesh.createVertex(point0);

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
	class TestCreateEdge {

		@Test
		public void createEdge() {
			Vertex vertex0 = mesh.createVertex(point0);
			Vertex vertex1 = mesh.createVertex(point1);

			HalfEdge edge = mesh.createEdge(0, 1);

			assertNotNull(edge);
			assertThat(edge.getId(), is(equalTo(0)));
			assertThat(edge.getEndVertex(), is(equalTo(vertex1)));
			assertNull(edge.getFace());

			HalfEdge oppositeEdge = edge.getOppositeEdge();

			assertNotNull(oppositeEdge);
			assertThat(oppositeEdge.getId(), is(equalTo(1)));
			assertThat(oppositeEdge.getEndVertex(), is(equalTo(vertex0)));
			assertNull(oppositeEdge.getFace());
		}

		@Test
		public void createEdgeWithSameIndex() {
			mesh.createVertex(point0);

			assertThrows(IllegalArgumentException.class, () -> mesh.createEdge(0, 0));
		}

		@Test
		public void createEdgeWithNegativeStartIndex() {
			mesh.createVertex(point0);

			assertThrows(IndexOutOfBoundsException.class, () -> mesh.createEdge(-1, 0));
		}

		@Test
		public void  createEdgeWithTooHighStartIndex() {
			mesh.createVertex(point0);

			assertThrows(IndexOutOfBoundsException.class, () ->  mesh.createEdge(1, 0));
		}

		@Test
		public void createEdgeWithNegativeEndIndex() {
			mesh.createVertex(point0);

			assertThrows(IndexOutOfBoundsException.class, () -> mesh.createEdge(0, -1));
		}

		@Test
		public void  createEdgeWithTooHighEndIndex() {
			mesh.createVertex(point0);

			assertThrows(IndexOutOfBoundsException.class, () ->  mesh.createEdge(0, 1));
		}
	}

	@Nested
	class TestGetEdge {

		@Test
		public void getExistingEdge() {
			mesh.createVertex(point0);
			mesh.createVertex(point1);
			mesh.createVertex(point2);
			mesh.createVertex(point3);
			mesh.createEdge(0, 1);
			HalfEdge edge = mesh.createEdge(1, 2);
			mesh.createEdge(2, 3);

			assertThat(mesh.getEdge(2), is(equalTo(edge)));
			assertThat(mesh.getEdge(3), is(equalTo(edge.oppositeEdge)));
		}

		@Test
		public void getEdgeWithNegativeIndex() {
			mesh.createVertex(point0);
			mesh.createVertex(point1);
			mesh.createEdge(0, 1);

			assertThrows(IndexOutOfBoundsException.class, () ->  mesh.getEdge(-1));
		}

		@Test
		public void getEdgeWithTooHighIndex() {
			mesh.createVertex(point0);
			mesh.createVertex(point1);
			mesh.createEdge(0, 1);

			assertThrows(IndexOutOfBoundsException.class, () ->  mesh.getEdge(2));
		}

	}

}