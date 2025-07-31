package org.kosa.nest.client;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.kosa.nest.common.ScannerWrapper;
import org.kosa.nest.exception.AdminNotLoginException;
import org.kosa.nest.exception.FileNotDeletedInDatabase;
import org.kosa.nest.exception.FileNotFoundException;
import org.kosa.nest.exception.LoginException;
import org.kosa.nest.exception.NoCommandLineException;
import org.kosa.nest.exception.PasswordNotCorrectException;
import org.kosa.nest.exception.RegisterAdminFailException;
import org.kosa.nest.exception.SearchDatabaseException;
import org.kosa.nest.exception.UpdateAdminInfoFailException;
import org.kosa.nest.exception.UploadFileFailException;
import org.kosa.nest.model.AdminVO;
import org.kosa.nest.model.FileVO;
import org.kosa.nest.network.NetworkWorker;
import org.kosa.nest.service.ServerAdminService;

public class CommandLineInterface {

	private static CommandLineInterface instance;
	NetworkWorker networkWorker;
	
	private CommandLineInterface() {
		networkWorker = NetworkWorker.getInstance();
	}
	
	public static CommandLineInterface getInstance() {
		return instance = new CommandLineInterface();
	}

	public String getCommand() throws LoginException, AdminNotLoginException, SQLException, IOException,
			PasswordNotCorrectException, UpdateAdminInfoFailException, FileNotFoundException, SearchDatabaseException,
			RegisterAdminFailException, UploadFileFailException, FileNotDeletedInDatabase, NoCommandLineException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		String commandLine = ScannerWrapper.getInstance().nextLine();
		StringTokenizer st = new StringTokenizer(commandLine);
		if (!st.hasMoreTokens()) {
			throw new NoCommandLineException("Please enter commandline!");
		}
		String command = st.nextToken();
		String keyword = null;
		if (st.hasMoreTokens())
			keyword = st.nextToken();
		
		List<Object> list = ServerAdminService.getInstance().executeCommand(command);

//			System.out.println("wrong command. If you need help, enter 'nest help'"); exception 확인 후 처리

		return command;
	}

	public void result(List<Object> list) {
	    if(list.size() == 1 && list.get(0) instanceof AdminVO) {
	        resultToString((AdminVO) list.get(0));
	    }else if(list.size() == 1 && list.get(0) instanceof FileVO) {
	        resultToString((FileVO) list.get(0));
	    }else if(list.size() > 1 && list.get(0) instanceof FileVO) {
	        List<FileVO> fileList  = new ArrayList<FileVO>();
	        for(Object obj : list) {
	            fileList.add((FileVO)obj);
	        }
	        resultToString(fileList);
	    }
	}

	public void resultToString(AdminVO adminVO) {
		if (adminVO == null) {
			System.out.println("관리자 정보가 없습니다.");
			return;
		}

		System.out.println("admin id: " + adminVO.getId());
		System.out.println("email: " + adminVO.getEmail());
		System.out.println("password: " + adminVO.getPassword());
	}

	public void resultToString(List<FileVO> fileList) {
		for (FileVO vo : fileList)
			System.out.println(vo.getSubject());
	}

	public void resultToString(FileVO fileVO) {
		if (fileVO == null)
			return;
		System.out.println("subject: " + fileVO.getSubject());
		System.out.println("tag: " + fileVO.getTag());
		System.out.println("lastModifed time: " + fileVO.getCreatedAt());
		System.out.println("file address: " + fileVO.getFileLocation());
		System.out.println("description: " + fileVO.getDescription());
		System.out.println("server upload time: " + fileVO.getUploadAt());
	}

}
