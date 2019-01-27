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
			mesh.createVertex(point2);
			mesh.createVertex(point3);

			Vertex vertex = mesh.getVertex(2);

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

}