package jfws.util.math.geometry.mesh;

import jfws.util.math.geometry.Point2d;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class HalfEdge {

	protected Face face;
	protected Vertex endVertex;
	protected HalfEdge oppositeEdge;
	protected HalfEdge nextEdge;

	public HalfEdge(HalfEdge edge) {
		face = edge.face;
		endVertex = edge.endVertex;
		oppositeEdge = edge.oppositeEdge;
		nextEdge = edge.nextEdge;
	}

	public Vertex getStartVertex() {
		return oppositeEdge.getEndVertex();
	}

	public HalfEdge getPreviousEdge() {
		HalfEdge currentEdge = this;

		while(currentEdge.getNextEdge() != this) {
			currentEdge = currentEdge.getNextEdge();
		}

		return currentEdge;
	}

	public void insertPoint(Point2d point) {
		HalfEdge newEdge = new HalfEdge(this);
		nextEdge = newEdge;
		endVertex = new Vertex(point, newEdge);

		HalfEdge newOppositeEdge = new HalfEdge(oppositeEdge);
		oppositeEdge.nextEdge = newOppositeEdge;
		oppositeEdge.endVertex = endVertex;
		oppositeEdge.oppositeEdge = newEdge;

		oppositeEdge = newOppositeEdge;
	}

}
