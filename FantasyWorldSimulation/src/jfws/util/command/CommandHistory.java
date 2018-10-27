package jfws.util.command;

import java.util.ArrayList;
import java.util.List;

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

	public boolean canUnExecute() {
		return index >= 0;
	}

	public void unExecute() {
		if (!canUnExecute()) {
			return;
		}

		Command command = history.get(index);
		command.unExecute();
		index--;
	}

	public boolean canReExecute() {
		return index < getLastIndex();
	}

	public void reExecute() {
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
