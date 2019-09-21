package jfws.feature.rpg.rules.action.selector;

import jfws.feature.rpg.rules.unit.Trait;

import java.util.Optional;

public class SelfSelector implements TargetSelector {

	@Override
	public boolean canSelectEntity(TargetSituation situation) {
		return situation.entityId == situation.targetId;
	}

	@Override
	public boolean requiresTraitCheck() {
		return false;
	}

	@Override
	public int getModifier(TargetSituation situation) {
		return 0;
	}

	@Override
	public Optional<Trait> getTrait() {
		return Optional.empty();
	}

}
