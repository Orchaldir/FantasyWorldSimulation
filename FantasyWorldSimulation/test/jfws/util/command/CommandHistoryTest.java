package jfws.util.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class CommandHistoryTest {

	private ICommand command0, command1, command2;
	private CommandHistory commandHistory;

	// execute()

	@BeforeEach
	public void setup() {
		command0 = Mockito.mock(ICommand.class);
		command1 = Mockito.mock(ICommand.class);
		command2 = Mockito.mock(ICommand.class);

		commandHistory = new CommandHistory();
	}

	@Test
	public void testExecuteCommand() {
		commandHistory.execute(command0);

		verify(command0).execute();
		verify(command0, never()).unExecute();
	}

	@Test
	public void testExecute2Commands() {
		commandHistory.execute(command0);
		commandHistory.execute(command1);

		verify(command0).execute();
		verify(command1).execute();

		verify(command0, never()).unExecute();
		verify(command1, never()).unExecute();
	}

	@Test
	public void testExecute3Commands() {
		commandHistory.execute(command0);
		commandHistory.execute(command1);
		commandHistory.execute(command2);

		verify(command0).execute();
		verify(command1).execute();
		verify(command2).execute();

		verify(command0, never()).unExecute();
		verify(command1, never()).unExecute();
		verify(command2, never()).unExecute();
	}

	// canUnExecute()

	@Test
	public void testCanNotUnExecuteWithoutExecute() {
		assertFalse(commandHistory.canUnExecute());
	}

	@Test
	public void testCanUnExecuteAfterExecute() {
		commandHistory.execute(command0);

		reset(command0);

		assertTrue(commandHistory.canUnExecute());

		verify(command0, never()).execute();
		verify(command0, never()).unExecute();
	}

	@Test
	public void testCanUnExecuteAfterMultipleExecutes() {
		commandHistory.execute(command0);
		commandHistory.execute(command1);
		commandHistory.execute(command2);

		assertTrue(commandHistory.canUnExecute());
	}

	// unExecute()

	@Test
	public void testUnExecuteWithoutExecutes() {
		commandHistory.unExecute();
	}

	@Test
	public void testUnExecute() {
		commandHistory.execute(command0);

		reset(command0);

		commandHistory.unExecute();

		verify(command0, never()).execute();
		verify(command0).unExecute();
	}

	@Test
	public void testUnExecuteFirstCommandTwice() {
		commandHistory.execute(command0);
		commandHistory.unExecute();

		reset(command0);

		verify(command0, never()).execute();
		verify(command0, never()).unExecute();
	}

	@Test
	public void testUnExecuteWithMultipleExecutes() {
		commandHistory.execute(command0);
		commandHistory.execute(command1);
		commandHistory.execute(command2);

		reset(command0);
		reset(command1);
		reset(command2);

		commandHistory.unExecute();

		verify(command0, never()).execute();
		verify(command1, never()).execute();
		verify(command2, never()).execute();

		verify(command0, never()).unExecute();
		verify(command1, never()).unExecute();
		verify(command2).unExecute();
	}

	@Test
	public void testUnExecuteTwiceWithMultipleExecutes() {
		commandHistory.execute(command0);
		commandHistory.execute(command1);
		commandHistory.execute(command2);

		commandHistory.unExecute();

		reset(command0);
		reset(command1);
		reset(command2);

		commandHistory.unExecute();

		verify(command0, never()).execute();
		verify(command1, never()).execute();
		verify(command2, never()).execute();

		verify(command0, never()).unExecute();
		verify(command1).unExecute();
		verify(command2, never()).unExecute();
	}

}