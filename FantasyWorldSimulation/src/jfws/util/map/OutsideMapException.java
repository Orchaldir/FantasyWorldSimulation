package jfws.util.map;

import lombok.AllArgsConstructor;
import lombok.Getter;

// Thrown to indicate that a cell outside the map was tried to be accessed.
@AllArgsConstructor
@Getter
public class OutsideMapException extends RuntimeException {

	private final CellMap2d map;
	private final int x, y, index;
	private final boolean usedIndex;
}
