package jfws.util.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

class CommandHistoryTest {

	private Command command0, command1, command2;
	private CommandHistoryForTesting commandHistory;

	@BeforeEach
	public void setUp() {
		command0 = mock(Command.class);
		command1 = mock(Command.class);
		command2 = mock(Command.class);

		commandHistory = new CommandHistoryForTesting();
	}

	// getCommandsToUnExecute()

	@Test
	public void testGetCommandsToUnExecute() {
		assertThat(commandHistory.getCommandsToUnExecute(), equalTo(0));
		commandHistory.execute(command0);
		assertThat(commandHistory.getCommandsToUnExecute(), equalTo(1));
		commandHistory.execute(command1);
		assertThat(commandHistory.getCommandsToUnExecute(), equalTo(2));
		commandHistory.execute(command2);
		assertThat(commandHistory.getCommandsToUnExecute(), equalTo(3));
	}

	// getCommandsToReExecute()

	@Test
	public void testGetCommandsToReExecute() {
		assertThat(commandHistory.getCommandsToReExecute(), equalTo(0));

		commandHistory.execute(command0);
		assertThat(commandHistory.getCommandsToReExecute(), equalTo(0));

		commandHistory.execute(command1);
		assertThat(commandHistory.getCommandsToReExecute(), equalTo(0));

		commandHistory.execute(command2);
		assertThat(commandHistory.getCommandsToReExecute(), equalTo(0));

		commandHistory.unExecute();
		assertThat(commandHistory.getCommandsToReExecute(), equalTo(1));

		commandHistory.unExecute();
		assertThat(commandHistory.getCommandsToReExecute(), equalTo(2));

		commandHistory.unExecute();
		assertThat(commandHistory.getCommandsToReExecute(), equalTo(3));
	}

	// execute()

	@Test
	public void testExecuteCommand() {
		commandHistory.execute(command0);

		verify(command0).execute();
		verify(command0, never()).unExecute();

		assertThat(commandHistory.getCommands(), contains(command0));
	}

	@Test
	public void testExecute2Commands() {
		commandHistory.execute(command0);
		commandHistory.execute(command1);

		verify(command0).execute();
		verify(command1).execute();

		verify(command0, never()).unExecute();
		verify(command1, never()).unExecute();

		assertThat(commandHistory.getCommands(), contains(command0, command1));
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
	public void testCanUnExecuteWithoutExecute() {
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

		verify(command0, never()).execute();
		verify(command0, never()).unExecute();
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

		commandHistory.unExecute();

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

	// canReExecute()

	@Test
	public void testCanReExecuteWithoutExecuteAndUnExecute() {
		assertFalse(commandHistory.canReExecute());
	}

	@Test
	public void testCanReExecuteWithoutAndUnExecute() {
		commandHistory.execute(command0);

		reset(command0);

		assertFalse(commandHistory.canReExecute());

		verify(command0, never()).execute();
		verify(command0, never()).unExecute();
	}

	@Test
	public void testCanReExecute() {
		commandHistory.execute(command0);
		commandHistory.unExecute();

		reset(command0);

		assertTrue(commandHistory.canReExecute());

		verify(command0, never()).execute();
		verify(command0, never()).unExecute();
	}

	@Test
	public void testCanReExecuteAfterMultipleUnExecutes() {
		commandHistory.execute(command0);
		commandHistory.execute(command1);
		commandHistory.execute(command2);
		commandHistory.unExecute();
		commandHistory.unExecute();
		commandHistory.unExecute();

		assertTrue(commandHistory.canReExecute());
	}

	// reExecute()

	@Test
	public void testReExecuteWithoutExecutes() {
		commandHistory.reExecute();

		verify(command0, never()).execute();
		verify(command0, never()).unExecute();
	}

	@Test
	public void testReExecuteWithoutUnExecutes() {
		commandHistory.execute(command0);

		reset(command0);

		commandHistory.reExecute();

		verify(command0, never()).execute();
		verify(command0, never()).unExecute();
	}

	@Test
	public void testReExecute() {
		commandHistory.execute(command0);
		commandHistory.unExecute();

		reset(command0);

		commandHistory.reExecute();

		verify(command0).execute();
		verify(command0, never()).unExecute();
	}

	@Test
	public void testReExecuteFirstCommandTwice() {
		commandHistory.execute(command0);
		commandHistory.unExecute();
		commandHistory.reExecute();

		reset(command0);

		commandHistory.reExecute();

		verify(command0, never()).execute();
		verify(command0, never()).unExecute();
	}

	@Test
	public void testReExecuteMultipleCommands() {
		commandHistory.execute(command0);
		commandHistory.execute(command1);
		commandHistory.execute(command2);
		commandHistory.unExecute();
		commandHistory.unExecute();
		commandHistory.unExecute();
		commandHistory.reExecute();

		reset(command0);
		reset(command1);
		reset(command2);

		commandHistory.reExecute();

		verify(command0, never()).execute();
		verify(command1).execute();
		verify(command2, never()).execute();

		verify(command0, never()).unExecute();
		verify(command1, never()).unExecute();
		verify(command2, never()).unExecute();

		assertThat(commandHistory.getCommands(), contains(command0, command1, command2));
	}

	@Test
	public void testExecuteAfterReExecute() {
		commandHistory.execute(command0);
		commandHistory.execute(command1);
		commandHistory.execute(command2);
		commandHistory.unExecute();
		commandHistory.unExecute();
		commandHistory.unExecute();
		commandHistory.reExecute();

		reset(command0);
		reset(command1);
		reset(command2);

		commandHistory.execute(command2);

		verify(command0, never()).execute();
		verify(command1, never()).execute();
		verify(command2).execute();

		verify(command0, never()).unExecute();
		verify(command1, never()).unExecute();
		verify(command2, never()).unExecute();

		assertTrue(commandHistory.canUnExecute());
		assertFalse(commandHistory.canReExecute());

		assertThat(commandHistory.getCommands(), contains(command0, command2));
	}

	public class CommandHistoryForTesting extends CommandHistory {

		public List<Command> getCommands() {
			return Collections.unmodifiableList(history);
		}
	}
}