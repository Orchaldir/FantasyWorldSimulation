package jfws.feature.rpg.rules.action.selector;

import jfws.feature.rpg.rules.unit.Trait;

import java.util.Optional;

public class SelfSelector implements TargetSelector {

	@Override
	public boolean canSelectEntity(TargetSituation situation) {
		return situation.entityId == situation.targetId;
	}

	@Override
	public boolean requiresToHitCheck() {
		return false;
	}

	@Override
	public int getToHitModifier(TargetSituation situation) {
		return 0;
	}

	@Override
	public Optional<Trait> getToHitTrait() {
		return Optional.empty();
	}

}
