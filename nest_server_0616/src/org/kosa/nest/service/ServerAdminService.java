package org.kosa.nest.service;

import org.kosa.nest.model.AdminDao;
import org.kosa.nest.model.AdminVO;
import org.kosa.nest.model.FileDao;

public class ServerAdminService {

	private static ServerAdminService instance;
	private FileDao fileDao = FileDao.getInstance();
	private AdminDao adminDao = AdminDao.getInstance();
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

}
