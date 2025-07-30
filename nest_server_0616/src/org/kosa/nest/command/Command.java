package org.kosa.nest.command;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;

public interface Command {
	
	List<Object> handleRequest(String command) throws SQLException, FileNotFoundException;
}
