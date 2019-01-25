package jfws.util.math.geometry.mesh;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class HalfEdge {

	private Face face;
	private Vertex endVertex;
	private HalfEdge oppositeEdge;
	private HalfEdge nextEdge;

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

}
