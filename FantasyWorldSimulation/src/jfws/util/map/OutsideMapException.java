package jfws.util.map;

import lombok.AllArgsConstructor;
import lombok.Getter;

// Thrown to indicate that a cell outside the map was tried to be accessed.
@AllArgsConstructor
@Getter
public class OutsideMapException extends Exception {

	private final Map2d map;
	private final int x, y;
}
