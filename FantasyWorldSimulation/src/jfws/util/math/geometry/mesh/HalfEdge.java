package jfws.util.math.geometry.mesh;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString(of = {"id"})
public class HalfEdge<V,E,F> {

	protected final int id;
	protected Face<V,E,F> face;
	protected Vertex<V> endVertex;
	protected HalfEdge<V,E,F> oppositeEdge;
	protected HalfEdge<V,E,F> nextEdge;
	protected E data;

	public HalfEdge(int id, Face<V,E,F> face, Vertex<V> endVertex,
					HalfEdge<V,E,F> oppositeEdge, HalfEdge<V,E,F> nextEdge) {
		this.id = id;
		this.face = face;
		this.endVertex = endVertex;
		this.oppositeEdge = oppositeEdge;
		this.nextEdge = nextEdge;
	}

	public HalfEdge(int id, HalfEdge<V,E,F> edge) {
		this.id = id;
		face = edge.face;
		endVertex = edge.endVertex;
		oppositeEdge = edge.oppositeEdge;
		nextEdge = edge.nextEdge;
	}

	public HalfEdge(int id, Vertex<V> endVertex) {
		this.id = id;
		this.endVertex = endVertex;
	}

	public Vertex<V> getStartVertex() {
		return oppositeEdge.getEndVertex();
	}

	public Face<V,E,F> getOppositeFace() {
		return oppositeEdge.face;
	}

	public HalfEdge<V,E,F> getPreviousEdge() {
		HalfEdge<V,E,F> currentEdge = this;

		while(currentEdge.getNextEdge() != this) {
			currentEdge = currentEdge.getNextEdge();
		}

		return currentEdge;
	}

}
