package org.kosa.nest.command.user;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.kosa.nest.command.Command;
import org.kosa.nest.model.FileDao;
import org.kosa.nest.model.FileVO;

public class SearchCommand extends UserCommand {

	private static SearchCommand instance;

	private SearchCommand(BufferedInputStream bis, ObjectOutputStream oos) {
		super(bis, oos);
	}

	public static SearchCommand getInstance(BufferedInputStream bis, ObjectOutputStream oos) {
		if (instance == null)
			instance = new SearchCommand(bis, oos);
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

        System.out.println("[info] Initiating object transfer.");
		oos.writeObject(resultFileInfoList);
		oos.flush();
        System.out.println("[info] Object transfer completed successfully.");

	return resultFileInfoList;
	}
}
