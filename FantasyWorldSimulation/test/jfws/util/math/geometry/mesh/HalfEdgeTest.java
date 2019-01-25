package jfws.util.math.geometry.mesh;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;

class HalfEdgeTest {

	private Face face;
	private Vertex start;
	private Vertex end;
	private HalfEdge oppositeEdge;
	private HalfEdge nextEdge;
	private HalfEdge middleEdge;
	private HalfEdge previousEdge;

	private HalfEdge edge;

	@BeforeEach
	public void setUp() {
		face = mock(Face.class);
		start = mock(Vertex.class);
		end = mock(Vertex.class);
		oppositeEdge = mock(HalfEdge.class);
		nextEdge = mock(HalfEdge.class);
		middleEdge = mock(HalfEdge.class);
		previousEdge = mock(HalfEdge.class);

		edge = new HalfEdge(face, end, oppositeEdge, nextEdge);
	}

	@Test
	public void testGetter() {
		assertThat(edge.getFace(), equalTo(face));
		assertThat(edge.getEndVertex(), equalTo(end));
		assertThat(edge.getOppositeEdge(), equalTo(oppositeEdge));
		assertThat(edge.getNextEdge(), equalTo(nextEdge));
	}

	@Test
	public void testGetStartVertex() {
		when(oppositeEdge.getEndVertex()).thenReturn(start);

		assertThat(edge.getStartVertex(), equalTo(start));

		verify(oppositeEdge, times(1)).getEndVertex();
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