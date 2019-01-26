package jfws.util.math.geometry.mesh;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public class Face {

	private HalfEdge edge;

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

	public int getEdgeCount() {
		return getEdgesInCCW().size();
	}

	public boolean canBeRendered() {
		return getEdgeCount() >= 3;
	}

}
