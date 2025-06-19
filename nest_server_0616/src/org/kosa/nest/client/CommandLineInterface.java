package org.kosa.nest.client;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.kosa.nest.exception.AdminNotLoginException;
import org.kosa.nest.exception.FileDeleteDatabaseException;
import org.kosa.nest.exception.FileNotFoundException;
import org.kosa.nest.exception.LoginException;
import org.kosa.nest.exception.PasswordNotCorrectException;
import org.kosa.nest.exception.RegisterException;
import org.kosa.nest.exception.SearchDatabaseException;
import org.kosa.nest.exception.UpdateAdminInfoFailException;
import org.kosa.nest.model.AdminVO;
import org.kosa.nest.model.FileVO;
import org.kosa.nest.network.NetworkWorker;
import org.kosa.nest.service.ServerAdminService;

public class CommandLineInterface {

	NetworkWorker networkWorker;
	ServerAdminService serverAdminService;
	
	public CommandLineInterface() {
		networkWorker = new NetworkWorker();
		serverAdminService = new ServerAdminService(); 
	}
	
	public void executeProgram() {
		
		Thread serverThread = new Thread(new ServerThread());
		Thread clientThread = new Thread(new clientThread());
		
		serverThread.setDaemon(true);
		
		clientThread.start();
		serverThread.start();
	}
	
	class ServerThread implements Runnable{
		
		
		@Override
		public void run(){
			try {
				networkWorker.go();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	class clientThread implements Runnable{
		
		@Override
		public void run() {
			
			try {
				while(true) {
					String commandLine = getCommand();
					if(commandLine.equalsIgnoreCase("quit"))
						break;
				}
				
			} catch (LoginException e) {
				e.printStackTrace();
			} catch (AdminNotLoginException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (RegisterException e) {
				e.printStackTrace();
			} catch (PasswordNotCorrectException e) {
				e.printStackTrace();
			} catch (UpdateAdminInfoFailException e) {
				e.printStackTrace();
			} catch (FileDeleteDatabaseException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (SearchDatabaseException e) {
				e.printStackTrace();
			}
		}
		
		public String getCommand() throws LoginException, AdminNotLoginException, SQLException, IOException, RegisterException, PasswordNotCorrectException, UpdateAdminInfoFailException, FileDeleteDatabaseException, FileNotFoundException, SearchDatabaseException {
			
			Scanner scanner = new Scanner(System.in);
			String commandLine = scanner.nextLine();
			StringTokenizer st = new StringTokenizer(commandLine);
			st.nextToken();
			String command = st.nextToken();
			String keyword = null;
			if(st.hasMoreTokens())
				keyword = st.nextToken();
			
			if(command.equalsIgnoreCase("quit")){
				scanner.close();
			}
			else if(command.equalsIgnoreCase("register")) {
				serverAdminService.register();
			}
			else if(command.equalsIgnoreCase("login")) {
				serverAdminService.login();
			}
			else if(command.equalsIgnoreCase("logout")) {
				serverAdminService.logout();
			}
			else if(command.equalsIgnoreCase("getMyInformation")) {
				resultToString(serverAdminService.getMyInformation());
			}
			else if(command.equalsIgnoreCase("updateMyInformation")) {
				serverAdminService.updateMyInformation();
			}
			else if(command.equalsIgnoreCase("uploadFile")) {
				serverAdminService.uploadFile();
			}
			else if(command.equalsIgnoreCase("deleteFile")) {
				serverAdminService.deleteFile(keyword);
			}
			else if(command.equalsIgnoreCase("search")) {
				resultToString((ArrayList<FileVO>)serverAdminService.search(keyword));
			}
			else if(command.equalsIgnoreCase("info")) {
				resultToString(serverAdminService.info(keyword));
			}
			else if(command.equalsIgnoreCase("help")) {
				serverAdminService.help();
			}
			else if(command.equalsIgnoreCase("findAllList")) {
				resultToString((ArrayList<FileVO>)serverAdminService.findAllList());
			}
			return command;
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
	
	public void resultToString(ArrayList<FileVO> fileList) {
		for(FileVO vo : fileList)
			System.out.println(vo.getSubject());
	}
	
	public void resultToString(FileVO fileVO) {
		if(fileVO == null)
			return ;
		System.out.println("subject: " + fileVO.getSubject());
		System.out.println("tag: " + fileVO.getTag());
		System.out.println("lastModifed time: " + fileVO.getCreatedAt());
		System.out.println("file address: " + fileVO.getFileLocation());
		System.out.println("description: " + fileVO.getDescription());
		System.out.println("server upload time: " + fileVO.getUploadAt());
	}
	
}
