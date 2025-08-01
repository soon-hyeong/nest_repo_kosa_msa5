package org.kosa.nest.command.user;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.StringTokenizer;

import org.kosa.nest.model.FileDao;

public class SearchCommand extends UserCommand {

	private static SearchCommand instance;

	private SearchCommand(ObjectOutputStream oos) {
		super(oos);
	}

	public static SearchCommand getInstance(ObjectOutputStream oos) {
		if (instance == null)
			instance = new SearchCommand(oos);
		return instance;
	}

	@Override
	public List<Object> handleRequest(String keyword) throws SQLException, IOException {

		//반환할 FileVO
		List<Object> resultFileInfoList = null;
		
		//fileDao를 이용하여 키워드에 해당하는 파일의 정보를 찾아옴
		resultFileInfoList = FileDao.getInstance().getFileInfoList(keyword);
		System.out.println("사이즈:" + resultFileInfoList.size());
		for(Object file : resultFileInfoList)
			System.out.println(file);

        System.out.println("[info] Initiating object transfer.");
		oos.writeObject(resultFileInfoList);
		oos.flush();
        System.out.println("[info] Object transfer completed successfully.");

	return resultFileInfoList;
	}
}
