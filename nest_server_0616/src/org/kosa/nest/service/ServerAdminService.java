package org.kosa.nest.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
import org.kosa.nest.handlerMapping.AdminCommandHandlerMapping;
import org.kosa.nest.model.AdminVO;

public class ServerAdminService {

	private static ServerAdminService instance;
	private AdminVO currentLoginAdmin;

	private ServerAdminService() {
	}
	
	public static ServerAdminService getInstance() {
	    if(instance == null)
	        instance = new ServerAdminService();
		return instance;
	}

    public AdminVO getCurrentLoginAdmin() {
        return currentLoginAdmin;
    }

    public void setCurrentLoginAdmin(AdminVO currentLoginAdmin) {
        this.currentLoginAdmin = currentLoginAdmin;
    }
    
    public List<Object> executeCommand(String commnad) throws FileNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, SQLException, RegisterAdminFailException, LoginException, AdminNotLoginException, UpdateAdminInfoFailException, PasswordNotCorrectException, UploadFileFailException, IOException, FileNotDeletedInDatabase, org.kosa.nest.exception.FileNotFoundException, SearchDatabaseException{
        List<Object> list = AdminCommandHandlerMapping.getInstance().create(commnad).handleRequest(commnad);
        return list;
    }

}
