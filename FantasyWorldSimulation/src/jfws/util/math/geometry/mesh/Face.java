package jfws.util.math.geometry.mesh;

import jfws.util.math.geometry.Point2d;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Slf4j
@ToString(of = {"id"})
public class Face<V,E,F> {

	private final int id;
	private HalfEdge<V,E,F> edge;
	@Setter
	private F data;

	public Face(int id, HalfEdge<V,E,F> edge) {
		this.id = id;
		this.edge = edge;
	}

	// edges

	public List<HalfEdge<V,E,F>> getEdgesInCCW() {
		List<HalfEdge<V,E,F>> edges = new ArrayList<>();
		HalfEdge<V,E,F> current = edge;

		do {
			edges.add(current);
			current = current.getNextEdge();
		}
		while(current != edge);

		return edges;
	}

	public boolean canBeRendered() {
		return getEdgesInCCW().size() >= 3;
	}

	// vertices

	public List<Vertex<V>> getVerticesInCCW() {
		return getEdgesInCCW().
				stream().
				map(HalfEdge::getEndVertex).
				collect(Collectors.toList());
	}

	public List<Point2d> getPointsInCCW() {
		return getVerticesInCCW().
				stream().
				map(Vertex::getPoint).
				collect(Collectors.toList());
	}

	// neighbors

	public List<Face<V,E,F>> getNeighborsInCCW() {
		return getEdgesInCCW().
				stream().
				map(HalfEdge::getOppositeFace).
				filter(Objects::nonNull).
				collect(Collectors.toList());
	}
}
