package jfws.util.math.geometry.mesh;

import jfws.util.math.geometry.Point2d;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Vertex {

	private Point2d point;
	private HalfEdge edge;

}
