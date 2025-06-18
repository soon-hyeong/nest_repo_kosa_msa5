package org.kosa.nest.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.StringTokenizer;

import org.kosa.nest.model.FileDao;
import org.kosa.nest.model.FileVO;

public class ServerUserService {
	
	FileDao fileDao;
	
	public ServerUserService(FileDao fileDao) {
		this.fileDao = fileDao;
	}
	
	/**
	 * 유저 클라이언트가 전달한 명령어를 분석하고, <br>
	 * 다운로드 요청한 파일의 정보를 전달하는 메서드
	 * @param commandLine
	 * @return
	 * @throws FileNotFoundException 
	 */
	public FileVO download(String commandLine) throws FileNotFoundException {
		
		//반환할 FileVO
		FileVO resultFileInfo = null;
		
		//StringTokenizer를 이용하여 명령어에서 키워드를 분리
		StringTokenizer st = new StringTokenizer(commandLine);
		st.nextToken();
		String keyword = st.nextToken();
		
		//fileDao를 이용하여 키워드에 해당하는 파일의 정보를 찾아옴
		resultFileInfo = fileDao.getFileInfo(keyword);
		//저장소에 해당 파일이 없다면 fildDao를 이용하여 db에 파일 정보 삭제하고 null값 반환
		if(resultFileInfo != null && !new File(resultFileInfo.getFileLocation()).isFile()) {
			throw new FileNotFoundException();
		}
		return resultFileInfo;
	}
}
