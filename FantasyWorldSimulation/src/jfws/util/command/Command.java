package jfws.util.command;

public interface Command {
	String getName();

	void execute();

	void unExecute();
}
