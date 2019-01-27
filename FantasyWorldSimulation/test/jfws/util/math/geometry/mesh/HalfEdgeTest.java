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

	/*
	@Nested
	class TestInsertPoint {

		private void assertInsertPoint(Point2d newPoint, Face desiredOppositeFace) {
			Vertex newEnd = edge.getEndVertex();
			assertThat(newEnd, is(not(equalTo(end))));
			assertThat(newEnd.getPoint(), is(equalTo(newPoint)));

			newEnd = oppositeEdge.getEndVertex();
			assertThat(newEnd, is(not(equalTo(end))));
			assertThat(newEnd.getPoint(), is(equalTo(newPoint)));

			HalfEdge newOpposite = edge.getOppositeEdge();
			assertThat(newOpposite, not(oppositeEdge));
			assertEdge(newOpposite, desiredOppositeFace, start, edge, previousOppositeEdge);

			HalfEdge newEdge = edge.getNextEdge();
			assertThat(newEdge, not(anyOf(is(nextEdge), is(middleEdge), is(previousEdge))));
			assertEdge(newEdge, face, end, oppositeEdge, nextEdge);
		}

		@Test
		public void testWithOppositeFace() {
			Point2d newPoint = mock(Point2d.class);

			edge.insertPoint(newPoint);

			assertInsertPoint(newPoint, oppositeFace);
		}

		@Test
		public void testWithoutOppositeFace() {
			oppositeEdge.face = null;
			Point2d newPoint = mock(Point2d.class);

			edge.insertPoint(newPoint);

			assertInsertPoint(newPoint, null);
		}
	}

	@Nested
	class TestCreateFace {

		@Test
		public void createFace() {
			edge.face = null;
			edge.nextEdge = null;

			Point2d newPoint = mock(Point2d.class);

			Face newFace = edge.createFace(newPoint);

			assertEdge(oppositeEdge, oppositeFace, start, edge, previousOppositeEdge);

			assertThat(newFace, is(not(oppositeFace)));
			assertThat(newFace, is(not(face)));
			assertThat(edge.face, is(newFace));
			assertThat(edge.face.getEdge(), is(edge));

			HalfEdge toPointEdge = edge.getNextEdge();
			assertThat(toPointEdge.face, is(newFace));
			assertThat(toPointEdge.endVertex, not(anyOf(is(start), is(end))));
			assertThat(toPointEdge.endVertex.getPoint(), is(newPoint));

			HalfEdge fromPointEdge = toPointEdge.getNextEdge();
			assertThat(fromPointEdge.face, is(newFace));
			assertThat(fromPointEdge.endVertex, is(start));
			assertThat(fromPointEdge.nextEdge, is(edge));

			assertThat(toPointEdge.endVertex.getEdge(), is(fromPointEdge));
		}

		@Test
		public void createFaceWithExistingFace() {
			Point2d newPoint = mock(Point2d.class);

			assertThrows(IllegalStateException.class, () ->  edge.createFace(newPoint));
		}
	}
	*/

}