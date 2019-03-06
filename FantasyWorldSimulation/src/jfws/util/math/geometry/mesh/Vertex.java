package jfws.util.math.geometry.mesh;

import jfws.util.math.geometry.Point2d;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString(of = {"id"})
public class Vertex<T> {

	private final int id;
	private final Point2d point;
	private HalfEdge edge;
	private T data;

	public Vertex(int id,  Point2d point, HalfEdge edge) {
		this.id = id;
		this.point = point;
		this.edge = edge;
	}
}
