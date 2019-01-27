package jfws.util.math.geometry.mesh;

import jfws.util.math.geometry.Point2d;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class HalfEdgeTest {

	private Face face;
	private Face oppositeFace;
	private Vertex start;
	private Vertex end;
	private HalfEdge oppositeEdge;
	private HalfEdge nextEdge;
	private HalfEdge middleEdge;
	private HalfEdge previousEdge;
	private HalfEdge previousOppositeEdge;

	private HalfEdge edge;

	@BeforeEach
	public void setUp() {
		face = mock(Face.class);
		oppositeFace = mock(Face.class);
		start = mock(Vertex.class);
		end = mock(Vertex.class);
		nextEdge = mock(HalfEdge.class);
		middleEdge = mock(HalfEdge.class);
		previousEdge = mock(HalfEdge.class);
		previousOppositeEdge = mock(HalfEdge.class);

		edge = new HalfEdge(0, face, end, null, nextEdge);
		oppositeEdge = new HalfEdge(1, oppositeFace, start, edge, previousOppositeEdge);
		edge.oppositeEdge = oppositeEdge;
	}

	private void assertEdge(HalfEdge edge, Face face, Vertex endVertex, HalfEdge oppositeEdge, HalfEdge nextEdge) {
		assertThat(edge.getFace(), equalTo(face));
		assertThat(edge.getEndVertex(), equalTo(endVertex));
		assertThat(edge.getOppositeEdge(), equalTo(oppositeEdge));
		assertThat(edge.getNextEdge(), equalTo(nextEdge));
	}

	@Test
	public void testGetter() {
		assertEdge(edge, face, end, oppositeEdge, nextEdge);
	}

	@Test
	public void testGetStartVertex() {
		assertThat(edge.getStartVertex(), equalTo(start));
	}

	@Test
	public void testGetOppositeFace() {
		assertThat(edge.getOppositeFace(), equalTo(oppositeFace));
	}

	@Test
	public void testGetPreviousEdge() {
		when(nextEdge.getNextEdge()).thenReturn(middleEdge);
		when(middleEdge.getNextEdge()).thenReturn(previousEdge);
		when(previousEdge.getNextEdge()).thenReturn(edge);

		assertThat(edge.getPreviousEdge(), equalTo(previousEdge));

		verify(nextEdge, atLeastOnce()).getNextEdge();
		verify(middleEdge, atLeastOnce()).getNextEdge();
		verify(previousEdge, atLeastOnce()).getNextEdge();
	}

}