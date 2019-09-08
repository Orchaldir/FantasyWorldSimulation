package jfws.feature.rpg.component;

import jfws.feature.rpg.rules.unit.Trait;

import java.util.HashMap;
import java.util.Map;

public class Statistics {

	private Map<Trait, Integer> traitRank = new HashMap<>();

	public void setRank(Trait trait, int rank) {
		traitRank.put(trait, rank);
	}

	public int getRank(Trait trait) {
		return traitRank.getOrDefault(trait, 0);
	}

}
