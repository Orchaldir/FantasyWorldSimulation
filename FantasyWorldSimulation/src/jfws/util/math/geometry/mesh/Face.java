package jfws.util.math.geometry.mesh;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class Face {

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
}
