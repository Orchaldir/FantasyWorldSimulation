package jfws.util.math.geometry.mesh;

import jfws.util.math.geometry.Point2d;

import java.util.ArrayList;
import java.util.List;

public class Mesh {

	private final List<Vertex> vertices = new ArrayList<>();
	private final List<HalfEdge> edges = new ArrayList<>();
	private final List<Face> faces = new ArrayList<>();

	private int nextVertexId = 0;
	private int nextEdgeId = 0;
	private int nextFaceId = 0;

	public Vertex createVertex(Point2d point) {
		Vertex vertex = new Vertex(nextVertexId++, point, null);
		vertices.add(vertex);
		return vertex;
	}

	public Vertex getVertex(int id) {
		for(Vertex vertex : vertices) {
			if(vertex.getId() == id) {
				return vertex;
			}
		}

		throw new IndexOutOfBoundsException(id);
	}

	public HalfEdge createEdge(int startVertexId, int endVertexId) {
		if(startVertexId == endVertexId) {
			throw new IllegalArgumentException("Start & end vertex id are the same!");
		}

		Vertex start = getVertex(startVertexId);
		Vertex end = getVertex(endVertexId);

		HalfEdge edge = new HalfEdge(nextEdgeId++, end);
		HalfEdge oppositeEdge = new HalfEdge(nextEdgeId++, start);

		edge.oppositeEdge = oppositeEdge;
		oppositeEdge.oppositeEdge = edge;

		edges.add(edge);
		edges.add(oppositeEdge);

		return edge;
	}

	public HalfEdge getEdge(int id) {
		for(HalfEdge edge : edges) {
			if(edge.getId() == id) {
				return edge;
			}
		}

		throw new IndexOutOfBoundsException(id);
	}

	public Face createTriangle(int vertexId0, int vertexId1, int vertexId2) {
		HalfEdge edge0 = createEdge(vertexId0, vertexId1);
		HalfEdge edge1 = createEdge(vertexId1, vertexId2);
		HalfEdge edge2 = createEdge(vertexId2, vertexId0);

		Face triangle = new Face(nextFaceId++, edge0);

		edge0.face = triangle;
		edge1.face = triangle;
		edge2.face = triangle;

		edge0.nextEdge = edge1;
		edge1.nextEdge = edge2;
		edge2.nextEdge = edge0;

		edge0.oppositeEdge.nextEdge = edge2.oppositeEdge;
		edge2.oppositeEdge.nextEdge = edge1.oppositeEdge;
		edge1.oppositeEdge.nextEdge = edge0.oppositeEdge;

		faces.add(triangle);

		return triangle;
	}

	public Face getFace(int id) {
		for(Face face : faces) {
			if(face.getId() == id) {
				return face;
			}
		}

		throw new IndexOutOfBoundsException(id);
	}
}
