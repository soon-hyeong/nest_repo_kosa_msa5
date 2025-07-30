package org.kosa.nest.command.user;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;
import java.util.StringTokenizer;

import org.kosa.nest.command.Command;
import org.kosa.nest.model.FileDao;
import org.kosa.nest.model.FileVO;

public class DownloadCommand implements Command {

	private static DownloadCommand instance;
	
	public static DownloadCommand getInstance() {
		if(instance == null)
			instance = new DownloadCommand();
		return instance;
	}
	@Override
	public List<Object> handleRequest(String command) throws SQLException, FileNotFoundException {
	
		//반환할 FileVO
		List<Object> resultFileInfoList = null;
		
		//StringTokenizer를 이용하여 명령어에서 키워드를 분리
		StringTokenizer st = new StringTokenizer(command);
		st.nextToken();
		String keyword = st.nextToken();
		
		//fileDao를 이용하여 키워드에 해당하는 파일의 정보를 찾아옴
		resultFileInfoList = FileDao.getInstance().getFileInfo(keyword);

		//저장소에 해당 파일이 없다면 fildDao를 이용하여 db에 파일 정보 삭제하고 null값 반환
		if(resultFileInfoList.size() > 0 && !new File(((FileVO)resultFileInfoList.get(0)).getFileLocation()).isFile()) {
			throw new FileNotFoundException();
		}
		return resultFileInfoList;
	}

}
