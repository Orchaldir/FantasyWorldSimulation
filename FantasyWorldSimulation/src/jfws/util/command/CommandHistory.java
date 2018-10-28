package jfws.util.command;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CommandHistory {

	protected final List<Command> history = new ArrayList<>();
	private int index = getLastIndex();

	public void execute(Command command) {
		int lastIndex = getLastIndex();

		for(int i = index; i < lastIndex; i++) {
			history.remove(getLastIndex());
		}

		command.execute();
		history.add(command);
		index = getLastIndex();
	}

	public int getCommandsToUnExecute() {
		return index + 1;
	}

	public boolean canUnExecute() {
		return index >= 0;
	}

	public void unExecute() {
		log.info("unExecute(): commands={}", getCommandsToUnExecute());

		if (!canUnExecute()) {
			return;
		}

		Command command = history.get(index);
		command.unExecute();
		index--;
	}

	public int getCommandsToReExecute() {
		return getLastIndex() - index;
	}

	public boolean canReExecute() {
		return index < getLastIndex();
	}

	public void reExecute() {
		log.info("reExecute(): commands={}", getCommandsToReExecute());

		if(!canReExecute()) {
			return;
		}

		Command command = history.get(index+1);
		command.execute();
		index++;
	}

	private int getLastIndex() {
		return history.size() - 1;
	}
}
