package org.kosa.nest.command.admin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.kosa.nest.command.Command;
import org.kosa.nest.exception.AdminNotLoginException;
import org.kosa.nest.exception.SearchDatabaseException;
import org.kosa.nest.model.FileDao;
import org.kosa.nest.model.FileVO;

public class InfoCommand implements Command {
	

	@Override
	public List<Object> handleRequest(String keyword) throws SearchDatabaseException {
		
		List<Object> resultList = new ArrayList<>();
		FileVO resultFileVO = null;
		try {
			resultList = FileDao.getInstance().getFileInfo(keyword);
		} catch (SQLException e) {
			throw new SearchDatabaseException("File not found in database:" + e.getMessage());
		}
		return resultList;
	}

}
