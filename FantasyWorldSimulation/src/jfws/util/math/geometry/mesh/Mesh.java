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

	public Vertex getVertex(int index) {
		for(Vertex vertex : vertices) {
			if(vertex.getId() == index) {
				return vertex;
			}
		}

		throw new IndexOutOfBoundsException(index);
	}

	public HalfEdge createEdge(int startIndex, int endIndex) {
		if(startIndex == endIndex) {
			throw new IllegalArgumentException("Start & end index are the same!");
		}

		Vertex start = getVertex(startIndex);
		Vertex end = getVertex(endIndex);

		HalfEdge edge = new HalfEdge(nextEdgeId++, end);
		HalfEdge oppositeEdge = new HalfEdge(nextEdgeId++, start);

		edge.oppositeEdge = oppositeEdge;
		oppositeEdge.oppositeEdge = edge;

		edges.add(edge);
		edges.add(oppositeEdge);

		return edge;
	}

	public HalfEdge getEdge(int index) {
		for(HalfEdge edge : edges) {
			if(edge.getId() == index) {
				return edge;
			}
		}

		throw new IndexOutOfBoundsException(index);
	}
}
