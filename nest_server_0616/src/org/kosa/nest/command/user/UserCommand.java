package org.kosa.nest.command.user;

import java.io.ObjectOutputStream;

import org.kosa.nest.command.Command;

public abstract class UserCommand implements Command{
	
	protected ObjectOutputStream oos;
	
	protected UserCommand(ObjectOutputStream oos) {
		this.oos = oos;
	}
}
