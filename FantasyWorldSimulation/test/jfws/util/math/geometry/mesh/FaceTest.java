package jfws.util.math.geometry.mesh;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FaceTest {

	private HalfEdge edge0;
	private HalfEdge edge1;
	private HalfEdge edge2;
	private HalfEdge edge3;

	private Face face;

	@BeforeEach
	public void setUp() {
		edge0 = mock(HalfEdge.class);
		edge1 = mock(HalfEdge.class);
		edge2 = mock(HalfEdge.class);
		edge3 = mock(HalfEdge.class);

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

	@Test
	public void testGetEdges() {
		setUpTriangle();

		List<HalfEdge> edges = face.getEdgesInCCW();

		assertThat(edges, contains(edge0, edge1, edge2));
	}

	@Test
	public void testGetEdgeCount() {
		setUpQuad();

		assertThat(face.getEdgeCount(), equalTo(4));
	}

	@Nested
	class TestCanBeRendered {

		@Test
		public void testFaceWithOneEdge() {
			setUpOneEdge();

			assertFalse(face.canBeRendered());
		}

		@Test
		public void testFaceWithTwoEdges() {
			setUpTwoEdges();

			assertFalse(face.canBeRendered());
		}

		@Test
		public void testTriangle() {
			setUpTriangle();

			assertTrue(face.canBeRendered());
		}

		@Test
		public void testQuad() {
			setUpQuad();

			assertTrue(face.canBeRendered());
		}
	}

}