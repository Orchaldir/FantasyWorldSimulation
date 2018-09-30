package jfws.util.command;

import java.util.ArrayList;
import java.util.List;

public class CommandHistory {

	private final List<ICommand> history = new ArrayList<>();
	private int index = getLastIndex();

	public void execute(ICommand command) {
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

		ICommand command = history.get(index);
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

		ICommand command = history.get(index+1);
		command.execute();
		index++;
	}

	private int getLastIndex() {
		return history.size() - 1;
	}
}
