package jfws.feature.world.generation;

import jfws.feature.world.WorldCell;
import jfws.util.map.ToCellMapper;
import jfws.util.math.geometry.mesh.Mesh;

public interface WorldGenerationStep {

	void generate(ToCellMapper<WorldCell> mapper);

	void generate(Mesh<Void, Void, WorldCell> mesh);

}
