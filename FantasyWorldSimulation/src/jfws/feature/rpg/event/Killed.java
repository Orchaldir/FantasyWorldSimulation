package jfws.feature.rpg.event;

import jfws.feature.rpg.rpg.Damage;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Killed {
	public final int killerId;
	public final int targetId;
}
