package org.kosa.nest.command.user;

import java.io.BufferedInputStream;
import java.io.ObjectOutputStream;

import org.kosa.nest.command.Command;

public abstract class UserCommand implements Command{
	
	protected BufferedInputStream bis;
	protected ObjectOutputStream oos;
	
	protected UserCommand(BufferedInputStream bis, ObjectOutputStream oos) {
		this.bis = bis;
		this.oos = oos;
	}
}
