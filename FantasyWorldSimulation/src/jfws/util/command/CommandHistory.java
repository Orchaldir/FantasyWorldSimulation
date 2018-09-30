package jfws.util.command;

import java.util.ArrayList;
import java.util.List;

public class CommandHistory {

	private final List<ICommand> history = new ArrayList<>();

	public void execute(ICommand command) {
		command.execute();
		history.add(command);
	}

	public boolean canUnExecute() {
		return !history.isEmpty();
	}
}
