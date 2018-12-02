package jfws.editor.map.tool;

import jfws.maps.sketch.ChangeTerrainTypeCommand;
import jfws.maps.sketch.SketchCell;
import jfws.maps.sketch.SketchMap;
import jfws.maps.sketch.terrain.TerrainType;
import jfws.util.command.CommandHistory;
import jfws.util.map.OutsideMapException;
import jfws.util.map.ToCellMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class ChangeTerrainTypeTool {

	private CommandHistory commandHistory;

	@Getter
	@Setter
	private SketchMap sketchMap;

	@Getter
	private TerrainType terrainType;

	public void changeTerrainType(TerrainType terrainType) {
		if(this.terrainType != terrainType) {
			log.info("changeTerrainType(): {} -> {}", this.terrainType.getName(), terrainType.getName());
			this.terrainType = terrainType;
		}
	}

	public boolean use(double worldX, double worldY) {
		try {
			ToCellMapper<SketchCell> toCellMapper = sketchMap.getToCellMapper();
			SketchCell cell = toCellMapper.getCell(worldX, worldY);
			int index = toCellMapper.getIndex(worldX, worldY);

			if(cell.getTerrainType() == terrainType) {
				log.info("use(): Cell {} is already {}.",
						index, cell.getTerrainType().getName());
				return false;
			}

			log.info("use(): {} oldTerrain={}", cell.getTerrainType().getName());

			ChangeTerrainTypeCommand command = new ChangeTerrainTypeCommand(sketchMap, index, terrainType);
			commandHistory.execute(command);

			return true;
		} catch (OutsideMapException e1) {
			log.info("use(): Outside map! world: x={} y={}", String.format("%.03f", worldX), String.format("%.03f", worldY));
		}

		return false;
	}
}
