package jfws.util.math.geometry.mesh;

import jfws.util.math.geometry.Point2d;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString(of = {"id"})
public class Vertex {

	private final int id;
	private Point2d point;
	private HalfEdge edge;

}
