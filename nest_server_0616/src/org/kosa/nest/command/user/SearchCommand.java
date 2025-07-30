package org.kosa.nest.command.user;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.kosa.nest.command.Command;
import org.kosa.nest.model.FileDao;
import org.kosa.nest.model.FileVO;

public class SearchCommand implements Command {
	
	private static SearchCommand instance;
	
	private SearchCommand() {
		
	}
	
	public static SearchCommand getInstance() {
		if(instance == null)
			instance = new SearchCommand();
		return instance;
	}

	@Override
	public List<Object> handleRequest(String command) throws SQLException, FileNotFoundException {

		StringTokenizer st = new StringTokenizer(command);
		st.nextToken();
		String keyword = st.nextToken();
	    ArrayList<Object> resultList = new ArrayList<>();

	    try {
	        List<Object> allFiles = FileDao.getInstance().getAllFileInfoList(); // 전체 파일 목록 조회

	        for (Object file : allFiles) {
	            if (((FileVO) file).getSubject() != null && ((FileVO) file).getSubject().contains(keyword)) {
	                resultList.add(file); // 제목에 키워드 포함 시 추가
	            }
	        }
	    } catch (SQLException e) {
	        System.out.println("DB 검색 중 오류 발생: " + e.getMessage());
	    }

	    return resultList;
	}
}
