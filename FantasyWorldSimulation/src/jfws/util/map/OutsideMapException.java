package jfws.util.map;

import lombok.AllArgsConstructor;
import lombok.Getter;

// Thrown to indicate that a cell outside the map was tried to be accessed.
@AllArgsConstructor
@Getter
public class OutsideMapException extends RuntimeException {

	private final int x;
	private final int y;
	private final int index;
	private final boolean usedIndex;
}
