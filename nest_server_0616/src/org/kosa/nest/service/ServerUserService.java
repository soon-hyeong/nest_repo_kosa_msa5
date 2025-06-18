package org.kosa.nest.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.kosa.nest.model.FileDao;
import org.kosa.nest.model.FileVO;

public class ServerUserService {
	
	FileDao fileDao;
	
	public ServerUserService() {
		this.fileDao = new FileDao();
	}
	
	/**
	 * 유저 클라이언트가 전달한 명령어를 분석하고, <br>
	 * 다운로드 요청한 파일의 정보를 전달하는 메서드
	 * @param commandLine
	 * @return
	 * @throws FileNotFoundException 
	 * @throws SQLException 
	 */
	public FileVO download(String commandLine) throws FileNotFoundException, SQLException {
		
		//반환할 FileVO
		FileVO resultFileInfo = null;
		
		//StringTokenizer를 이용하여 명령어에서 키워드를 분리
		StringTokenizer st = new StringTokenizer(commandLine);
		st.nextToken();
		String keyword = st.nextToken();
		
		//fileDao를 이용하여 키워드에 해당하는 파일의 정보를 찾아옴
		resultFileInfo = fileDao.getFileInfoList(keyword).get(0);
		//저장소에 해당 파일이 없다면 fildDao를 이용하여 db에 파일 정보 삭제하고 null값 반환
		if(resultFileInfo != null && !new File(resultFileInfo.getFileLocation()).isFile()) {
			throw new FileNotFoundException();
		}
		return resultFileInfo;
	}
	/**
	 * search : 파일의 일부 정보만 
	 * @param keyword
	 * @return
	 */
	public List<FileVO> search(String command) {
		StringTokenizer st = new StringTokenizer(command);
		st.nextToken();
		String keyword = st.nextToken();
	    List<FileVO> resultList = new ArrayList<>();

	    try {
	        FileDao dao = new FileDao(); // DAO 객체 생성
	        List<FileVO> allFiles = dao.getAllFileInfoList(); // 전체 파일 목록 조회

	        for (FileVO file : allFiles) {
	            if (file.getSubject() != null && file.getSubject().contains(keyword)) {
	                resultList.add(file); // 제목에 키워드 포함 시 추가
	            }
	        }

	    } catch (SQLException e) {
	        System.out.println("DB 검색 중 오류 발생: " + e.getMessage());
	    }

	    return resultList;
	}
	/**
	 * info: 파일 상세 정보 확인 
	 * @param keyword
	 * @return
	 */
	public List<FileVO> info(String command) {
		StringTokenizer st = new StringTokenizer(command);
		st.nextToken();
		String keyword = st.nextToken();
	    List<FileVO> resultList = new ArrayList<>();

	    try {
	        FileDao dao = new FileDao();
	        resultList = dao.getFileInfoList(keyword); // 제목, 태그, 날짜 중 하나라도 키워드 포함 시 반환

	    } catch (SQLException e) {
	        System.out.println("파일 상세 정보 조회 중 오류 발생: " + e.getMessage());
	    }

	    return resultList;
	}


}
