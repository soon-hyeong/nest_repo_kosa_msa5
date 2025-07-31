package org.kosa.nest.command;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.kosa.nest.exception.AdminNotLoginException;
import org.kosa.nest.exception.FileNotDeletedInDatabase;
import org.kosa.nest.exception.LoginException;
import org.kosa.nest.exception.PasswordNotCorrectException;
import org.kosa.nest.exception.RegisterAdminFailException;
import org.kosa.nest.exception.SearchDatabaseException;
import org.kosa.nest.exception.UpdateAdminInfoFailException;
import org.kosa.nest.exception.UploadFileFailException;

public interface Command {
	
	List<Object> handleRequest(String command) throws SQLException, FileNotFoundException, RegisterAdminFailException, LoginException, AdminNotLoginException, UpdateAdminInfoFailException, PasswordNotCorrectException, UploadFileFailException, IOException, FileNotDeletedInDatabase, org.kosa.nest.exception.FileNotFoundException, SearchDatabaseException;
}
