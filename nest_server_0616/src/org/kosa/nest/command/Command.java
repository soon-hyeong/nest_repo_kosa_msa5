package org.kosa.nest.command;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;

import org.kosa.nest.exception.AdminNotLoginException;
import org.kosa.nest.exception.LoginException;
import org.kosa.nest.exception.RegisterAdminFailException;

public interface Command {
	
	List<Object> handleRequest(String command) throws SQLException, FileNotFoundException, RegisterAdminFailException, LoginException, AdminNotLoginException;
}
