package org.kosa.nest.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.kosa.nest.model.FileDao;
import org.kosa.nest.model.FileVO;

public class ServerUserService {
	
	private static ServerUserService instance;
	private FileDao fileDao;
	
	private ServerUserService() {
		this.fileDao = FileDao.getInstance();
	}
	
	public static ServerUserService getInstance() {
		return instance = new ServerUserService();
	}
	
	/**
	 * 유저 클라이언트가 전달한 명령어를 분석하고, <br>
	 * 다운로드 요청한 파일의 정보를 전달하는 메서드<br>
	 * @param commandLine
	 * @return
	 * @throws FileNotFoundException 
	 * @throws SQLException 
	 */
	public List<FileVO> download(String commandLine) throws FileNotFoundException, SQLException {
		
		//반환할 FileVO
		List<FileVO> resultFileInfoList = null;
		
		//StringTokenizer를 이용하여 명령어에서 키워드를 분리
		StringTokenizer st = new StringTokenizer(commandLine);
		st.nextToken();
		String keyword = st.nextToken();
		
		//fileDao를 이용하여 키워드에 해당하는 파일의 정보를 찾아옴
		resultFileInfoList = fileDao.getFileInfo(keyword);

		//저장소에 해당 파일이 없다면 fildDao를 이용하여 db에 파일 정보 삭제하고 null값 반환
		if(resultFileInfoList.size() > 0 && !new File(resultFileInfoList.get(0).getFileLocation()).isFile()) {
			throw new FileNotFoundException();
		}
		return resultFileInfoList;
	}
	/**
	 * 모든 파일을 리스트 형식으로 불러옵니다 <br>
	 * 불러온 각각의 파일은 일부 정보(tag, title, created_at)들만 표시됩니다<br>
	 * @param keyword
	 * @return
	 */
	public List<FileVO> search(String command) {
		StringTokenizer st = new StringTokenizer(command);
		st.nextToken();
		String keyword = st.nextToken();
	    ArrayList<FileVO> resultList = new ArrayList<>();

	    try {
	        List<FileVO> allFiles = fileDao.getAllFileInfoList(); // 전체 파일 목록 조회

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
	 * 개별 파일의 상세정보를 확인합니다<br>
	 * 파일의 제목 뿐만 아니라 db에 있는 모든 정보를 볼 수 있습니다 <br>
	 * 제목, 태그, 날짜 중 하나라도 키워드로 이용하여 파일의 정보를 조회할 수 있습니다 <br>
	 * @param keyword
	 * @return
	 */
	public List<FileVO> info(String command) {
		StringTokenizer st = new StringTokenizer(command);
		st.nextToken();
		String keyword = st.nextToken();
	    List<FileVO> resultList = new ArrayList<>();

	    try {
	        resultList = fileDao.getFileInfo(keyword); // 제목, 태그, 날짜 중 하나라도 키워드 포함 시 반환

	    } catch (SQLException e) {
	        System.out.println("파일 상세 정보 조회 중 오류 발생: " + e.getMessage());
	    }

	    return resultList;
	}


}
