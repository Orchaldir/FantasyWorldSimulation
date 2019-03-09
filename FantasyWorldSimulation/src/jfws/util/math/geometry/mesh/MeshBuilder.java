package jfws.util.math.geometry.mesh;

import jfws.util.math.geometry.Point2d;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MeshBuilder<V,E,F> implements Mesh<V,E,F> {

	@Getter
	private final List<Vertex<V>> vertices = new ArrayList<>();

	@Getter
	private final List<HalfEdge<V,E,F>> edges = new ArrayList<>();

	@Getter
	private final List<Face<V,E,F>> faces = new ArrayList<>();

	private int nextVertexId = 0;
	private int nextEdgeId = 0;
	private int nextFaceId = 0;

	public void clear() {
		vertices.clear();
		edges.clear();
		faces.clear();

		nextVertexId = 0;
		nextEdgeId = 0;
		nextFaceId = 0;
	}

	// vertices

	public Vertex<V> createVertex(Point2d point) {
		Vertex<V> vertex = new Vertex(nextVertexId++, point, null);
		vertices.add(vertex);
		return vertex;
	}

	public Vertex<V> createVertex(double x, double y) {
		return createVertex(new Point2d(x, y));
	}

	@Override
	public Vertex<V> getVertex(int id) {
		for(Vertex<V> vertex : vertices) {
			if(vertex.getId() == id) {
				return vertex;
			}
		}

		throw new IndexOutOfBoundsException(id);
	}

	// edges

	public HalfEdge<V,E,F> createEdge(int startVertexId, int endVertexId) {
		if(startVertexId == endVertexId) {
			throw new IllegalArgumentException("Start & end vertex id are the same!");
		}
		else if(getEdge(startVertexId, endVertexId) != null) {
			throw new IllegalArgumentException("Edge already exists!");
		}

		Vertex<V> start = getVertex(startVertexId);
		Vertex<V> end = getVertex(endVertexId);

		HalfEdge<V,E,F> edge = new HalfEdge<>(nextEdgeId++, end);
		HalfEdge<V,E,F> oppositeEdge = new HalfEdge<>(nextEdgeId++, start);

		edge.oppositeEdge = oppositeEdge;
		oppositeEdge.oppositeEdge = edge;

		edges.add(edge);
		edges.add(oppositeEdge);

		return edge;
	}

	@Override
	public HalfEdge<V,E,F> getEdge(int id) {
		for(HalfEdge<V,E,F> edge : edges) {
			if(edge.getId() == id) {
				return edge;
			}
		}

		throw new IndexOutOfBoundsException(id);
	}

	@Override
	public HalfEdge<V,E,F> getEdge(int startVertexId, int endVertexId) {
		for(HalfEdge<V,E,F> edge : edges) {
			if(edge.getEndVertex().getId() == endVertexId &&
					edge.getStartVertex().getId() == startVertexId) {
				return edge;
			}
		}

		return  null;
	}

	private HalfEdge<V,E,F> getOrCreateEdge(int startVertexId, int endVertexId) {
		HalfEdge<V,E,F> edge = getEdge(startVertexId, endVertexId);

		if(edge == null) {
			return createEdge(startVertexId, endVertexId);
		}

		return edge;
	}

	public HalfEdge<V,E,F> splitEdge(int edgeId, int vertexId) {
		HalfEdge<V,E,F> edge = getEdge(edgeId);
		Vertex<V> vertex = getVertex(vertexId);
		HalfEdge<V,E,F> oppositeEdge = edge.getOppositeEdge();

		HalfEdge<V,E,F> newEdge = new HalfEdge(nextEdgeId++, edge);
		edge.nextEdge = newEdge;
		edge.endVertex = vertex;

		HalfEdge<V,E,F> newOppositeEdge = new HalfEdge(nextEdgeId++, oppositeEdge);
		oppositeEdge.nextEdge = newOppositeEdge;
		oppositeEdge.endVertex = vertex;
		oppositeEdge.oppositeEdge = newEdge;

		edge.oppositeEdge = newOppositeEdge;

		return newEdge;
	}

	private void mergeAtEndVertex(HalfEdge<V,E,F> edge) {
		HalfEdge<V,E,F> edgeAfterEnd = edge.getNextEdge();
		HalfEdge<V,E,F> edgeBeforeEnd = edge.getOppositeEdge().getPreviousEdge();

		edgeBeforeEnd.nextEdge = edgeAfterEnd;
	}

	public void mergeEdge(int edgeId) {
		HalfEdge<V,E,F> edge = getEdge(edgeId);
		Face<V,E,F> oppositeFace = edge.getOppositeFace();

		if(edge.getFace() == null || oppositeFace == null) {
			throw new IllegalArgumentException("Edge requires 2 faces to merge.");
		}

		for (HalfEdge<V,E,F> edge0 : oppositeFace.getEdgesInCCW()) {
			edge0.face = edge.face;
		}

		mergeAtEndVertex(edge);
		mergeAtEndVertex(edge.getOppositeEdge());

		faces.remove(oppositeFace);
	}

	// faces

	public Face createTriangle(int vertexId0, int vertexId1, int vertexId2) {
		HalfEdge<V,E,F> edge0 = getOrCreateEdge(vertexId0, vertexId1);
		HalfEdge<V,E,F> edge1 = getOrCreateEdge(vertexId1, vertexId2);
		HalfEdge<V,E,F> edge2 = getOrCreateEdge(vertexId2, vertexId0);

		Face<V,E,F> triangle = new Face(nextFaceId++, edge0);

		edge0.face = triangle;
		edge1.face = triangle;
		edge2.face = triangle;

		edge0.nextEdge = edge1;
		edge1.nextEdge = edge2;
		edge2.nextEdge = edge0;

		faces.add(triangle);

		return triangle;
	}

	public Face<V,E,F> createFace(List<Integer> vertices) {
		List<HalfEdge<V,E,F>> edgesOfFace = createEdgesOfFace(vertices);

		Face<V,E,F> face = new Face(nextFaceId++, edgesOfFace.get(0));

		edgesOfFace.forEach(edge -> edge.face = face);

		log.info("createFace(): id={} vertices={}", face.getId(), vertices);

		connectEdgesOfFace(edgesOfFace);

		faces.add(face);

		return face;
	}

	private List<HalfEdge<V,E,F>> createEdgesOfFace(List<Integer> vertices) {
		final int numberOfVertices = vertices.size();

		if(numberOfVertices < 3) {
			throw new IllegalArgumentException("Less than 3 vertices!");
		}

		List<HalfEdge<V,E,F>> edgesOfFace = new ArrayList<>();
		int startVertexId = vertices.get(0);

		for(int startIndex = 0; startIndex < numberOfVertices; startIndex++) {
			int endIndex = (startIndex + 1) % numberOfVertices;
			int endVertexId = vertices.get(endIndex);

			edgesOfFace.add(getOrCreateEdge(startVertexId, endVertexId));

			startVertexId = endVertexId;
		}

		return edgesOfFace;
	}

	private void connectEdgesOfFace(List<HalfEdge<V,E,F>> edges) {
		HalfEdge<V,E,F> current = edges.get(0);
		int size = edges.size();

		for(int index = 0; index < size; index++) {
			int nextIndex = (index + 1) % size;
			HalfEdge<V,E,F> next = edges.get(nextIndex);

			if(current.nextEdge != null) {
				log.warn("connectEdgesOfFace(): edge={} next={} overwritten={}", current.id, next.id, current.nextEdge.id);
			}
			else {
				log.debug("connectEdgesOfFace(): edge={} next={}", current.id, next.id);
			}

			current.nextEdge = next;

			current = next;
		}
	}

	@Override
	public Face<V,E,F> getFace(int id) {
		for(Face<V,E,F> face : faces) {
			if(face.getId() == id) {
				return face;
			}
		}

		throw new IndexOutOfBoundsException(id);
	}

	public void splitFace() {

	}
}
