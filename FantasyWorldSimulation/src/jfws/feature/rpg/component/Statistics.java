package jfws.feature.rpg.component;

import jfws.feature.rpg.rules.unit.Trait;
import jfws.util.ecs.component.ComponentStorage;

import java.util.HashMap;
import java.util.Map;

public class Statistics {

	public static final int DEFAULT_RANK = 0;

	private Map<Trait, Integer> traitRank = new HashMap<>();

	public void setRank(Trait trait, int rank) {
		traitRank.put(trait, rank);
	}

	public int getRank(Trait trait) {
		return traitRank.getOrDefault(trait, DEFAULT_RANK);
	}

	public static int getRank(ComponentStorage<Statistics> storage, int entityId, Trait trait) {
		return storage.
				get(entityId).
				map(s -> s.getRank(trait)).
				orElse(DEFAULT_RANK);
	}

}
