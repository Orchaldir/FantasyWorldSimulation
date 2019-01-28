package jfws.util.math.geometry.mesh;

import jfws.util.math.geometry.Point2d;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Mesh {

	@Getter
	private final List<Vertex> vertices = new ArrayList<>();

	@Getter
	private final List<HalfEdge> edges = new ArrayList<>();

	@Getter
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
		else if(getEdge(startVertexId, endVertexId) != null) {
			throw new IllegalArgumentException("Edge already exists!");
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

	private HalfEdge getEdge(int startVertexId, int endVertexId) {
		for(HalfEdge edge : edges) {
			if(edge.getEndVertex().getId() == endVertexId &&
					edge.getStartVertex().getId() == startVertexId) {
				return edge;
			}
		}

		return  null;
	}

	private HalfEdge getOrCreateEdge(int startVertexId, int endVertexId) {
		HalfEdge edge = getEdge(startVertexId, endVertexId);

		if(edge == null) {
			return createEdge(startVertexId, endVertexId);
		}

		return  edge;
	}

	public HalfEdge splitEdge(int edgeId, int vertexId) {
		HalfEdge edge = getEdge(edgeId);
		Vertex vertex = getVertex(vertexId);
		HalfEdge oppositeEdge = edge.getOppositeEdge();

		HalfEdge newEdge = new HalfEdge(nextEdgeId++, edge);
		edge.nextEdge = newEdge;
		edge.endVertex = vertex;

		HalfEdge newOppositeEdge = new HalfEdge(nextEdgeId++, oppositeEdge);
		oppositeEdge.nextEdge = newOppositeEdge;
		oppositeEdge.endVertex = vertex;
		oppositeEdge.oppositeEdge = newEdge;

		edge.oppositeEdge = newOppositeEdge;

		return newEdge;
	}

	public void mergeAtEndVertex(HalfEdge edge) {
		HalfEdge edgeAfterEnd = edge.getNextEdge();
		HalfEdge edgeBeforeEnd = edge.getOppositeEdge().getPreviousEdge();

		edgeBeforeEnd.nextEdge = edgeAfterEnd;
		//edgeAfterEnd.getOppositeEdge().nextEdge = edgeBeforeEnd.oppositeEdge;
	}

	public void mergeEdge(int edgeId) {
		HalfEdge edge = getEdge(edgeId);
		Face oppositeFace = edge.getOppositeFace();

		if(edge.getFace() == null || oppositeFace == null) {
			throw new IllegalArgumentException("Edge requires 2 faces to merge.");
		}

		for (HalfEdge edge0 : oppositeFace.getEdgesInCCW()) {
			edge0.face = edge.face;
		}

		mergeAtEndVertex(edge);
		mergeAtEndVertex(edge.getOppositeEdge());

		faces.remove(oppositeFace);
	}

	public Face createTriangle(int vertexId0, int vertexId1, int vertexId2) {
		HalfEdge edge0 = getOrCreateEdge(vertexId0, vertexId1);
		HalfEdge edge1 = getOrCreateEdge(vertexId1, vertexId2);
		HalfEdge edge2 = getOrCreateEdge(vertexId2, vertexId0);

		Face triangle = new Face(nextFaceId++, edge0);

		edge0.face = triangle;
		edge1.face = triangle;
		edge2.face = triangle;

		edge0.nextEdge = edge1;
		edge1.nextEdge = edge2;
		edge2.nextEdge = edge0;

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
