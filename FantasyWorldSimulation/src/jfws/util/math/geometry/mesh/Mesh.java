package jfws.util.math.geometry.mesh;

import java.util.List;

public interface Mesh {

	// vertices

	List<Vertex> getVertices();

	Vertex getVertex(int id);

	// edges

	List<HalfEdge> getEdges();

	HalfEdge getEdge(int id);

	HalfEdge getEdge(int startVertexId, int endVertexId);

	// faces

	List<Face> getFaces();

	Face getFace(int id);
}
