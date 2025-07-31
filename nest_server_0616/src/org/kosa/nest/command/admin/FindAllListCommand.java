package org.kosa.nest.command.admin;

import java.sql.SQLException;
import java.util.List;

import org.kosa.nest.command.Command;
import org.kosa.nest.exception.AdminNotLoginException;
import org.kosa.nest.exception.SearchDatabaseException;
import org.kosa.nest.model.FileDao;
import org.kosa.nest.service.ServerAdminService;

public class FindAllListCommand implements Command {

	@Override
	public List<Object> handleRequest(String command) throws AdminNotLoginException, SearchDatabaseException {
		if (ServerAdminService.getInstance().getCurrentLoginAdmin() == null)
			throw new AdminNotLoginException("Permission denied");
		else {
			List<Object> list = null;
			try {
				list = FileDao.getInstance().getAllFileInfoList();
			} catch (SQLException e) {
				throw new SearchDatabaseException("File not found in database:" + e.getMessage());
			}
			return list;
		}
	}

}
