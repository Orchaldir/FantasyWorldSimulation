package jfws.util.command;

public interface ICommand {
	String getName();

	void execute();

	void unExecute();
}
