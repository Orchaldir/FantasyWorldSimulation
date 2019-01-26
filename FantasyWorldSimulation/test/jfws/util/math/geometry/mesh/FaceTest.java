package jfws.util.math.geometry.mesh;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FaceTest {

	private HalfEdge edge0;
	private HalfEdge edge1;
	private HalfEdge edge2;
	private HalfEdge edge3;

	private Vertex vertex0;
	private Vertex vertex1;
	private Vertex vertex2;
	private Vertex vertex3;

	private Face face;

	@BeforeEach
	public void setUp() {
		edge0 = mock(HalfEdge.class);
		edge1 = mock(HalfEdge.class);
		edge2 = mock(HalfEdge.class);
		edge3 = mock(HalfEdge.class);

		vertex0 = mock(Vertex.class);
		vertex1 = mock(Vertex.class);
		vertex2 = mock(Vertex.class);
		vertex3 = mock(Vertex.class);

		when(edge0.getEndVertex()).thenReturn(vertex0);
		when(edge1.getEndVertex()).thenReturn(vertex1);
		when(edge2.getEndVertex()).thenReturn(vertex2);
		when(edge3.getEndVertex()).thenReturn(vertex3);

		face = new Face(edge0);
	}

	private void setUpOneEdge() {
		when(edge0.getNextEdge()).thenReturn(edge0);
	}

	private void setUpTwoEdges() {
		when(edge0.getNextEdge()).thenReturn(edge1);
		when(edge1.getNextEdge()).thenReturn(edge0);
	}

	private void setUpTriangle() {
		when(edge0.getNextEdge()).thenReturn(edge1);
		when(edge1.getNextEdge()).thenReturn(edge2);
		when(edge2.getNextEdge()).thenReturn(edge0);
	}

	private void setUpQuad() {
		when(edge0.getNextEdge()).thenReturn(edge1);
		when(edge1.getNextEdge()).thenReturn(edge2);
		when(edge2.getNextEdge()).thenReturn(edge3);
		when(edge3.getNextEdge()).thenReturn(edge0);
	}

	@Nested
	class TestEdges {

		@Nested
		class TestGetEdgesInCCW {

			@Test
			public void getEdgesWithOneEdge() {
				setUpOneEdge();

				List<HalfEdge> edges = face.getEdgesInCCW();

				assertThat(edges, contains(edge0));
			}

			@Test
			public void getEdgesWithTwoEdges() {
				setUpTwoEdges();

				List<HalfEdge> edges = face.getEdgesInCCW();

				assertThat(edges, contains(edge0, edge1));
			}

			@Test
			public void getEdgesWithTriangle() {
				setUpTriangle();

				List<HalfEdge> edges = face.getEdgesInCCW();

				assertThat(edges, contains(edge0, edge1, edge2));
			}

			@Test
			public void getEdgesWithQuad() {
				setUpQuad();

				List<HalfEdge> edges = face.getEdgesInCCW();

				assertThat(edges, contains(edge0, edge1, edge2, edge3));
			}

		}

		@Nested
		class TestCanBeRendered {

			@Test
			public void canBeRenderedWithOneEdge() {
				setUpOneEdge();

				assertFalse(face.canBeRendered());
			}

			@Test
			public void canBeRenderedWithTwoEdges() {
				setUpTwoEdges();

				assertFalse(face.canBeRendered());
			}

			@Test
			public void canBeRenderedWithTriangle() {
				setUpTriangle();

				assertTrue(face.canBeRendered());
			}

			@Test
			public void canBeRenderedWithQuad() {
				setUpQuad();

				assertTrue(face.canBeRendered());
			}
		}
	}

	@Nested
	class TestVertices {

		@Nested
		class TestGetVerticesInCCW {

			@Test
			public void getVerticesWithOneEdge() {
				setUpOneEdge();

				List<Vertex> vertices = face.getVerticesInCCW();

				assertThat(vertices, contains(vertex0));
			}

			@Test
			public void getVerticesWithTwoEdges() {
				setUpTwoEdges();

				List<Vertex> vertices = face.getVerticesInCCW();

				assertThat(vertices, contains(vertex0, vertex1));
			}

			@Test
			public void getVerticesWithTriangle() {
				setUpTriangle();

				List<Vertex> vertices = face.getVerticesInCCW();

				assertThat(vertices, contains(vertex0, vertex1, vertex2));
			}

			@Test
			public void getVerticesWithQuad() {
				setUpQuad();

				List<Vertex> vertices = face.getVerticesInCCW();

				assertThat(vertices, contains(vertex0, vertex1, vertex2, vertex3));
			}

		}
	}

}