package jfws.util.math.geometry;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Rectangle {

	private final Point2d start;
	private final Point2d end;
	private final Point2d size;

	private Rectangle(Point2d start, Point2d end, Point2d size) {
		this.start = start;
		this.end = end;
		this.size = size;
	}

	public static Rectangle fromStartAndEnd(Point2d start, Point2d end) {
		Point2d size = end.subtract(start);

		return new Rectangle(start, end, size);
	}

	public static Rectangle fromSize(Point2d start, Point2d size) {
		Point2d end = start.add(size);

		return new Rectangle(start, end, size);
	}

	public static Rectangle fromSize(Point2d size) {
		return new Rectangle(new Point2d(0,0), size, size);
	}

	public boolean isInside(Point2d point) {
		return  point.getX() >= start.getX() &&
				point.getY() >= start.getY() &&
				point.getX() <= end.getX() &&
				point.getY() <= end.getY();
	}

	public List<Point2d> getCorners() {
		List<Point2d> corners = new ArrayList<>(4);

		corners.add(start);
		corners.add(start.add(new Point2d(size.getX(), 0)));
		corners.add(end);
		corners.add(start.add(new Point2d(0, size.getY())));

		return corners;
	}
}
