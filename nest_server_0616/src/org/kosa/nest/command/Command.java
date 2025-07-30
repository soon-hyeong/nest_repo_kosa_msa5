package org.kosa.nest.command;

import java.util.List;

public interface Command {
	
	List<Object> handleRequest(String command);
}
