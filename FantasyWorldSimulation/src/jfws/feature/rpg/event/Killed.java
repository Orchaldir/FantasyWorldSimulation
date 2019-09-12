package jfws.feature.rpg.event;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Killed {
	public final int killerId;
	public final int targetId;
}
