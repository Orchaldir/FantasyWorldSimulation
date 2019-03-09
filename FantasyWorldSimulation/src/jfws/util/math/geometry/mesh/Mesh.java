package jfws.util.math.geometry.mesh;

import jfws.util.math.geometry.Point2d;

import java.util.List;

public interface Mesh<V,E,F> {

	// vertices

	List<Vertex<V>> getVertices();

	Vertex<V> getVertex(int id);

	// edges

	List<HalfEdge<V,E,F>> getEdges();

	HalfEdge<V,E,F> getEdge(int id);

	HalfEdge<V,E,F> getEdge(int startVertexId, int endVertexId);

	// faces

	List<Face<V,E,F>> getFaces();

	Face<V,E,F> getFace(int id);
}
