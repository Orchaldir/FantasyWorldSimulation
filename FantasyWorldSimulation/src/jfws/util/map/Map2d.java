package jfws.util.map;

import java.util.Optional;

public interface Map2d<T> {

	CellMap2d<T> getCellMap();

	ToCellMapper<T> getToCellMapper();

	<U>
	Optional<Map2d<U>> getParentMap();
}
