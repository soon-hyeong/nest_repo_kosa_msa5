package org.kosa.nest.command.user;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.kosa.nest.command.Command;
import org.kosa.nest.model.FileDao;

public class InfoCommand implements Command {

	private static InfoCommand instance;
	
	public static InfoCommand getInstance() {
		if(instance == null)
			instance = new InfoCommand();
		return instance;
	}
	
	@Override
	public List<Object> handleRequest(String command) throws SQLException, FileNotFoundException {
		StringTokenizer st = new StringTokenizer(command);
		st.nextToken();
		String keyword = st.nextToken();
	    List<Object> resultList = new ArrayList<>();

	    try {
	        resultList = FileDao.getInstance().getFileInfo(keyword); // 제목, 태그, 날짜 중 하나라도 키워드 포함 시 반환

	    } catch (SQLException e) {
	        System.out.println("파일 상세 정보 조회 중 오류 발생: " + e.getMessage());
	    }

	    return resultList;
	}

}
