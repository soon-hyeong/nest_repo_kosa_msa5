package org.kosa.nest.command.user;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.List;

import org.kosa.nest.model.FileDao;

public class InfoCommand extends UserCommand {

	private static InfoCommand instance;
	
	private InfoCommand(ObjectOutputStream oos) {
		super(oos);
	}
	
	public static InfoCommand getInstance(ObjectOutputStream oos) {
		if(instance == null)
			instance = new InfoCommand(oos);
		return instance;
	}
	@Override
	public List<Object> handleRequest(String keyword) throws SQLException, IOException {
		// 반환할 FileVO
		//반환할 FileVO
		List<Object> resultFileInfoList = null;
		
		//fileDao를 이용하여 키워드에 해당하는 파일의 정보를 찾아옴
		resultFileInfoList = FileDao.getInstance().getFileInfo(keyword);

        System.out.println("[info] Initiating object transfer.");
		oos.writeObject(resultFileInfoList);
		oos.flush();
        System.out.println("[info] Object transfer completed successfully.");
		return resultFileInfoList;
	}

}
