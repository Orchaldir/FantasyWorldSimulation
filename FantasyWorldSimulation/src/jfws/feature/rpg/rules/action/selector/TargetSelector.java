package jfws.feature.rpg.rules.action.selector;

import jfws.feature.rpg.rules.unit.Trait;

import java.util.Optional;

public interface TargetSelector {

	boolean canSelectEntity(TargetSituation situation);
	boolean requiresToHitCheck();
	int getToHitModifier(TargetSituation situation);
	Optional<Trait> getToHitTrait();

}
