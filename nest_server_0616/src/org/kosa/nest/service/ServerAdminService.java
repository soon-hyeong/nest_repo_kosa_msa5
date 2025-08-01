package org.kosa.nest.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;
import java.util.StringTokenizer;

import org.kosa.nest.command.AdminCommand;
import org.kosa.nest.exception.AdminNotLoginException;
import org.kosa.nest.exception.FileNotDeletedInDatabase;
import org.kosa.nest.exception.LoginException;
import org.kosa.nest.exception.NoCommandLineException;
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
    
    public List<Object> executeCommand(String commnadLine) throws FileNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, SQLException, RegisterAdminFailException, LoginException, AdminNotLoginException, UpdateAdminInfoFailException, PasswordNotCorrectException, UploadFileFailException, IOException, FileNotDeletedInDatabase, org.kosa.nest.exception.FileNotFoundException, SearchDatabaseException, NoCommandLineException{
    	StringTokenizer st = new StringTokenizer(commnadLine);
    	String command = st.nextToken();
    	String keyword = null;
    	if(st.hasMoreTokens())
    		keyword = st.nextToken();
        AdminCommand adminCommand = (AdminCommand)AdminCommandHandlerMapping.getInstance().create(command);
        if(adminCommand == null) {
        	throw new NoCommandLineException("해당하는 명령어가 존재하지 않습니다");
        }
        List<Object> list = adminCommand.handleRequest(keyword);
        return list;
    }

}
