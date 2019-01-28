package jfws.util.math.geometry.mesh;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString(of = {"id"})
public class HalfEdge {

	protected final int id;
	protected Face face;
	protected Vertex endVertex;
	protected HalfEdge oppositeEdge;
	protected HalfEdge nextEdge;

	public HalfEdge(int id, HalfEdge edge) {
		this.id = id;
		face = edge.face;
		endVertex = edge.endVertex;
		oppositeEdge = edge.oppositeEdge;
		nextEdge = edge.nextEdge;
	}

	public HalfEdge(int id, Vertex endVertex) {
		this.id = id;
		this.endVertex = endVertex;
	}

	public Vertex getStartVertex() {
		return oppositeEdge.getEndVertex();
	}

	public Face getOppositeFace() {
		return oppositeEdge.face;
	}

	public HalfEdge getPreviousEdge() {
		HalfEdge currentEdge = this;

		while(currentEdge.getNextEdge() != this) {
			currentEdge = currentEdge.getNextEdge();
		}

		return currentEdge;
	}

}
