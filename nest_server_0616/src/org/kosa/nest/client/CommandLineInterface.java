package org.kosa.nest.client;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;
import java.util.StringTokenizer;

import org.kosa.nest.exception.AdminNotLoginException;
import org.kosa.nest.exception.FileNotDeletedInDatabase;
import org.kosa.nest.exception.FileNotFoundException;
import org.kosa.nest.exception.LoginException;
import org.kosa.nest.exception.NoCommandLineException;
import org.kosa.nest.exception.PasswordNotCorrectException;
import org.kosa.nest.exception.RegisterAdminFailException;
import org.kosa.nest.exception.SearchDatabaseException;
import org.kosa.nest.exception.UpdateAdminInfoFailException;
import org.kosa.nest.exception.UploadFileFailException;
import org.kosa.nest.network.NetworkWorker;
import org.kosa.nest.service.ServerAdminService;

public class CommandLineInterface {

	private static CommandLineInterface instance;
	NetworkWorker networkWorker;
	
	private CommandLineInterface() {
		networkWorker = NetworkWorker.getInstance();
	}
	
	public static CommandLineInterface getInstance() {
		if(instance == null)
			instance = new CommandLineInterface();
		return instance;
	}

	public String getCommand(String commandLine) throws LoginException, AdminNotLoginException, SQLException, IOException,
			PasswordNotCorrectException, UpdateAdminInfoFailException, FileNotFoundException, SearchDatabaseException,
			RegisterAdminFailException, UploadFileFailException, FileNotDeletedInDatabase, NoCommandLineException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		StringTokenizer st = new StringTokenizer(commandLine);
		if (!st.hasMoreTokens()) {
			throw new NoCommandLineException("Please enter commandline!");
		}
		String command = st.nextToken();
		
		List<Object> list = null;

		list = ServerAdminService.getInstance().executeCommand(commandLine);

		resultToString(list);
		return command;
	}

	public void resultToString(List<Object> list) {
		for(Object object : list) {
			System.out.println(object);
		}
	}

}
