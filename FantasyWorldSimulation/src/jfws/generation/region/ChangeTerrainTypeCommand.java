package jfws.generation.region;

import jfws.generation.region.terrain.TerrainType;
import jfws.util.command.Command;
import jfws.util.map.OutsideMapException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChangeTerrainTypeCommand implements Command {

	private final AbstractRegionMap map;
	private final int index;
	private final TerrainType newTerrainType;
	private TerrainType oldTerrainType;

	public ChangeTerrainTypeCommand(AbstractRegionMap map, int index, TerrainType newTerrainType) {
		this.map = map;
		this.index = index;
		this.newTerrainType = newTerrainType;
	}

	@Override
	public String getName() {
		return "ChangeTerrainType";
	}

	@Override
	public void execute() {
		log.info("execute(): index={} newTerrainType={}", index, newTerrainType);
		try {
			AbstractRegionCell cell = map.getCells().getCell(index);
			oldTerrainType = cell.getTerrainType();
			cell.setTerrainType(newTerrainType);
		} catch (OutsideMapException e) {
			log.error("execute(): ", e);
		}
	}

	@Override
	public void unExecute() {
		if(oldTerrainType == null) {
			throw new IllegalStateException("Can not unexecute!");
		}

		log.info("unExecute(): index={} oldTerrainType={}", index, oldTerrainType);

		try {
			AbstractRegionCell cell = map.getCells().getCell(index);
			cell.setTerrainType(oldTerrainType);
		} catch (OutsideMapException e) {
			log.error("unExecute(): ", e);
		}
	}
}
