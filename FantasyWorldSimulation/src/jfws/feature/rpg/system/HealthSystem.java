package jfws.feature.rpg.system;

import jfws.feature.rpg.component.Health;
import jfws.feature.rpg.component.Statistics;
import jfws.feature.rpg.event.Hit;
import jfws.feature.rpg.event.Killed;
import jfws.feature.rpg.rules.unit.Trait;
import jfws.feature.rpg.rules.unit.TraitChecker;
import jfws.util.ecs.component.ComponentStorage;
import jfws.util.ecs.event.EventPublisher;
import jfws.util.ecs.event.EventSubscriber;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@AllArgsConstructor
@Slf4j
public class HealthSystem implements EventSubscriber<Hit> {

	private final ComponentStorage<Health> healthStorage;
	private final ComponentStorage<Statistics> statisticsStorage;
	private final TraitChecker checker;
	private final Trait toughnessTrait;
	private final EventPublisher<Killed> killedPublisher;

	@Override
	public void update(Hit event) {
		Optional<Health> health = healthStorage.get(event.targetId);

		if(!health.isPresent()) {
			log.warn("Entity '{}' has no health component!", event.targetId);
			return;
		}

		int toughness = getToughnessRank(event.targetId);

		handleHit(event, health.get(), toughness);
	}

	private int getToughnessRank(int entityId) {
		return Statistics.getRank(statisticsStorage, entityId, toughnessTrait);
	}

	private void handleHit(Hit event, Health health, int toughness) {
		int modifiedToughness = toughness - health.getToughnessPenalty();
		int damage = event.damage.rank;

		int result = checker.check(modifiedToughness, damage);

		if(result > 1) {
			log.info("Entity '{}' is not hurt by entity '{}' with '{}' damage.", event.targetId, event.attackerId, damage);
		}
		else if(result >= 0) {
			log.info("Entity '{}' is hurt by entity '{}' with '{}' damage.", event.targetId, event.attackerId, damage);

			health.increaseToughnessPenalty();
		}
		else {
			log.info("Entity '{}' is killed by entity '{}' with '{}' damage.", event.targetId, event.attackerId, damage);

			Killed killed = new Killed(event.attackerId, event.targetId);
			killedPublisher.publish(killed);
		}
	}
}
