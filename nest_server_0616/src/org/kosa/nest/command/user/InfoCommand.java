package org.kosa.nest.command.user;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.kosa.nest.command.Command;
import org.kosa.nest.model.FileDao;

public class InfoCommand extends UserCommand {

	private static InfoCommand instance;
	
	private InfoCommand(BufferedInputStream bis, ObjectOutputStream oos) {
		super(bis, oos);
	}
	
	public static InfoCommand getInstance(BufferedInputStream bis, ObjectOutputStream oos) {
		if(instance == null)
			instance = new InfoCommand(bis, oos);
		return instance;
	}
	@Override
	public List<Object> handleRequest(String command) throws SQLException, IOException {
		// 반환할 FileVO
		//반환할 FileVO
		List<Object> resultFileInfoList = null;
		
		//StringTokenizer를 이용하여 명령어에서 키워드를 분리
		StringTokenizer st = new StringTokenizer(command);
		st.nextToken();
		String keyword = st.nextToken();
		
		//fileDao를 이용하여 키워드에 해당하는 파일의 정보를 찾아옴
		resultFileInfoList = FileDao.getInstance().getFileInfo(keyword);

        System.out.println("[info] Initiating object transfer.");
		oos.writeObject(resultFileInfoList);
		oos.flush();
        System.out.println("[info] Object transfer completed successfully.");
		return resultFileInfoList;
	}

}
