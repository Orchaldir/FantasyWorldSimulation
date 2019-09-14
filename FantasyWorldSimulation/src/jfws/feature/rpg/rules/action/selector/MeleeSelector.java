package jfws.feature.rpg.rules.action.selector;

import jfws.feature.rpg.rules.unit.Trait;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public class MeleeSelector implements TargetSelector {

	private final int reach;
	private final int toHitModifier;
	private final Optional<Trait> toHitTrait;

	@Override
	public boolean canSelectEntity(TargetSituation situation) {
		boolean notSelf = situation.entityId != situation.targetId;
		boolean withinReach = situation.distance <= reach;

		return notSelf && withinReach;
	}

	@Override
	public boolean requiresToHitCheck() {
		return true;
	}

	@Override
	public int getToHitModifier(TargetSituation situation) {
		return toHitModifier;
	}

	@Override
	public Optional<Trait> getToHitTrait() {
		return toHitTrait;
	}
}
