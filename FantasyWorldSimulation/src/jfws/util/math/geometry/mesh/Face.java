package jfws.util.math.geometry.mesh;

import jfws.util.math.geometry.Point2d;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@ToString(of = {"id"})
public class Face {

	private final int id;
	private HalfEdge edge;

	// edges

	public List<HalfEdge> getEdgesInCCW() {
		List<HalfEdge> edges = new ArrayList<>();
		HalfEdge current = edge;

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

	public List<Vertex> getVerticesInCCW() {
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

	public List<Face> getNeighborsInCCW() {
		return getEdgesInCCW().
				stream().
				map(HalfEdge::getOppositeFace).
				filter(Objects::nonNull).
				collect(Collectors.toList());
	}
}
