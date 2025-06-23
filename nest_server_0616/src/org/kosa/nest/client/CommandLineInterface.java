package org.kosa.nest.client;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

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

	Scanner scanner;
	NetworkWorker networkWorker;
	ServerAdminService serverAdminService;
	
	public CommandLineInterface() {
		networkWorker = new NetworkWorker();
		scanner = new Scanner(System.in);
		serverAdminService = new ServerAdminService(scanner); 
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
			while(true) {
				try {
					String commandLine = getCommand();
					if(commandLine.equalsIgnoreCase("quit"))
						break;
				}catch (RegisterAdminFailException e) {
					System.err.println(e.getMessage());	
				} catch (LoginException e) {
					System.err.println(e.getMessage());	
				} catch (AdminNotLoginException e) {
					System.err.println(e.getMessage());	
				} catch (PasswordNotCorrectException e) {
					System.err.println(e.getMessage());
				} catch (UpdateAdminInfoFailException e) {
					System.err.println(e.getMessage());
				} catch (FileNotFoundException e) {
					System.err.println(e.getMessage());
				} catch (SearchDatabaseException e) {
					System.err.println(e.getMessage());
				} catch (SQLException e) {
					System.err.println(e.getMessage());
				} catch (IOException e) {
					System.err.println(e.getMessage());
				} catch (UploadFileFailException e) {
					System.err.println(e.getMessage());
				} catch (FileNotDeletedInDatabase e) {
					System.err.println(e.getMessage());
				} catch (NoCommandLineException e) {
					System.err.println(e.getMessage());
				}
			}
		}
		
		public String getCommand() throws LoginException, AdminNotLoginException, SQLException, IOException, PasswordNotCorrectException, UpdateAdminInfoFailException, FileNotFoundException, SearchDatabaseException, RegisterAdminFailException, UploadFileFailException, FileNotDeletedInDatabase, NoCommandLineException {
			
			String commandLine = scanner.nextLine();
			StringTokenizer st = new StringTokenizer(commandLine);
			if(!st.hasMoreTokens()) {
				throw new NoCommandLineException("Please enter commandline!");
			}
			String command = st.nextToken();
			String keyword = null;
			if(st.hasMoreTokens())
				keyword = st.nextToken();
			
			if(command.equalsIgnoreCase("quit") || command.equalsIgnoreCase("exit")){
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
				resultToString(serverAdminService.search(keyword));
			}
			else if(command.equalsIgnoreCase("info")) {
				resultToString(serverAdminService.info(keyword));
			}
			else if(command.equalsIgnoreCase("help")) {
				serverAdminService.help();
			}
			else if(command.equalsIgnoreCase("findAllList")) {
				resultToString(serverAdminService.findAllList());
			}
			else {
				System.out.println("wrong command. If you need help, enter 'nest help'");
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
	
	public void resultToString(List<FileVO> fileList) {
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
