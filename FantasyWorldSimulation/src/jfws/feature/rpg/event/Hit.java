package jfws.feature.rpg.event;

import jfws.feature.rpg.rules.Damage;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Hit {
	public final Damage damage;
	public final int attackerId;
	public final int targetId;
}
