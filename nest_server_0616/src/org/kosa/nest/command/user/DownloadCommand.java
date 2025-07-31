package org.kosa.nest.command.user;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.StringTokenizer;

import org.kosa.nest.model.FileDao;
import org.kosa.nest.model.FileVO;

public class DownloadCommand extends UserCommand {

	private static DownloadCommand instance;
	
	private DownloadCommand(BufferedInputStream bis, ObjectOutputStream oos) {
		super(bis, oos);
	}
	
	public static DownloadCommand getInstance(BufferedInputStream bis, ObjectOutputStream oos) {
		if(instance == null)
			instance = new DownloadCommand(bis, oos);
		return instance;
	}
	@Override
	public List<Object> handleRequest(String command) throws SQLException, IOException {
	
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
		
        System.out.println("[info] Initiating object transfer.");
		oos.writeObject(resultFileInfoList);
		oos.flush();
        System.out.println("[info] Object transfer completed successfully.");
		if(resultFileInfoList.size() > 0) {
			bis = new BufferedInputStream(new FileInputStream(((FileVO)resultFileInfoList.get(0)).getFileLocation()), 8192);
            System.out.println("[info] Initiating file transfer.");
			int data = bis.read();
			while(data != -1) {
				oos.write(data);
				data = bis.read();
			}
			oos.flush();
            System.out.println("[info] File transfer completed successfully.");
		} else {
			oos.write(-1);
			oos.flush();
            System.out.println("[info] No files found.");
		}
	return resultFileInfoList;
	}

}
