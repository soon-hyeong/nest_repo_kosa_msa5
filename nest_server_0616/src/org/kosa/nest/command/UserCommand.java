package org.kosa.nest.command;

import java.io.ObjectOutputStream;

public abstract class UserCommand implements Command{
	
	protected ObjectOutputStream oos;
	
	protected UserCommand(ObjectOutputStream oos) {
		this.oos = oos;
	}
}
