package org.kosa.nest.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.StringTokenizer;

import org.kosa.nest.common.NestConfig;
import org.kosa.nest.exception.AdminNotLoginException;
import org.kosa.nest.exception.FileNotDeletedInDatabase;
import org.kosa.nest.exception.FileNotFoundException;
import org.kosa.nest.exception.LoginException;
import org.kosa.nest.exception.PasswordNotCorrectException;
import org.kosa.nest.exception.RegisterAdminFailException;
import org.kosa.nest.exception.SearchDatabaseException;
import org.kosa.nest.exception.UpdateAdminInfoFailException;
import org.kosa.nest.exception.UploadFileFailException;
import org.kosa.nest.handlerMapping.UserCommandHandlerMapping;

public class NetworkWorker {

	private static NetworkWorker instance;
	
	//생성자로 ServerUserService를 받아옴
	private NetworkWorker() {
	}
	
	public static NetworkWorker getInstance() {
		if(instance == null)
			instance = new NetworkWorker();
		return instance;	}
	
	/**
	 * 네트워크 접속을 시작하는 메서드 <br>
	 * ServerSocket을 생성하고 accept() 메서드를 실행하여 접속 가능한 상태로 만든 뒤, <br>
	 * socket을 생성하고 MultiThreading을 사용하여 여러 접속을 처리할 수 있도록 합니다.
	 * @throws IOException
	 */
	public void go() throws IOException {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(NestConfig.port);
            System.out.println("[info] Server start");
			while(true){
				Socket socket = serverSocket.accept();
                System.out.println("[info] " + socket.getInetAddress() + " connected");
				ReceiveWorker receiveWorker = new ReceiveWorker(socket);
				Thread receiveWorkerThread = new Thread(receiveWorker);
				receiveWorkerThread.start();
			}
		} finally {
			if(serverSocket != null)
				serverSocket.close();
		}
	}
	
	/**
	 * ReceiveWorker는 다중 접속을 처리하기 위해 Runnable 인터페이스를 적용하였습니다 <br>
	 * getCommand() 메서드를 이용하여 명령어를 받아온 뒤, sendResult() 메서드에 명령어를 입력하여 <br>
	 * 유저에게 명령어에 따른 결과값을 전송합니다
	 */
	private class ReceiveWorker implements Runnable{
		private Socket socket; // 서버 직원이 개별 클라이언트와 통신하기 위한 전화기 Socket
		private ObjectOutputStream oos;
		private BufferedReader br;
		
		private ReceiveWorker(Socket socket) {
			this.socket = socket;
		}
		
		@Override
		public void run() {
			try {
				String commandLine = getCommand();
				sendResult(commandLine);
			} catch (SocketException e) {
				System.err.println("[Warning] Connection reset by client.");
			} catch (IOException e) {
			    System.err.println("[Error] IOException occurred: " + e.getMessage());
			} catch (SQLException e) {
			    System.err.println("[Error] SQLException occurred: " + e.getMessage());
			} catch (InstantiationException e) {
			    System.err.println("[Error] InstantiationException occurred: " + e.getMessage());
			} catch (IllegalAccessException e) {
			    System.err.println("[Error] IllegalAccessException occurred: " + e.getMessage());
			} catch (IllegalArgumentException e) {
			    System.err.println("[Error] IllegalArgumentException occurred: " + e.getMessage());
			} catch (InvocationTargetException e) {
			    System.err.println("[Error] InvocationTargetException occurred: " + e.getMessage());
			} catch (NoSuchMethodException e) {
			    System.err.println("[Error] NoSuchMethodException occurred: " + e.getMessage());
			} catch (SecurityException e) {
			    System.err.println("[Error] SecurityException occurred: " + e.getMessage());
			} catch (RegisterAdminFailException e) {
			    System.err.println("[Error] RegisterAdminFailException occurred: " + e.getMessage());
			} catch (LoginException e) {
			    System.err.println("[Error] LoginException occurred: " + e.getMessage());
			} catch (AdminNotLoginException e) {
			    System.err.println("[Error] AdminNotLoginException occurred: " + e.getMessage());
			} catch (UpdateAdminInfoFailException e) {
			    System.err.println("[Error] UpdateAdminInfoFailException occurred: " + e.getMessage());
			} catch (PasswordNotCorrectException e) {
			    System.err.println("[Error] PasswordNotCorrectException occurred: " + e.getMessage());
			} catch (UploadFileFailException e) {
			    System.err.println("[Error] UploadFileFailException occurred: " + e.getMessage());
			} catch (FileNotDeletedInDatabase e) {
			    System.err.println("[Error] FileNotDeletedInDatabase occurred: " + e.getMessage());
			} catch (FileNotFoundException e) {
			    System.err.println("[Error] FileNotFoundException occurred: " + e.getMessage());
			} catch (SearchDatabaseException e) {
			    System.err.println("[Error] SearchDatabaseException occurred: " + e.getMessage());
			}
		}
		
		/**
		 * 유저가 보낸 명령어를 받아 반환하는 메서드입니다.
		 * @return
		 * @throws UnsupportedEncodingException
		 * @throws IOException
		 */
		public String getCommand() throws UnsupportedEncodingException, IOException, SocketException {
						
			br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
			oos = new ObjectOutputStream(socket.getOutputStream());
			String commandLine  = br.readLine();
			
            System.out.println("[info] " + socket.getInetAddress() + " request command: " + commandLine);
			return commandLine;
		}
		
		/**
		 * 입력받은 명령어에 따라 ServerUserService()의 메서드를 호출하여 결과값을 반환받고 <br>
		 * 반환받은 결과값을 stream을 통하여 user client에게 전송합니다.
		 * @param command
		 * @throws IOException
		 * @throws SQLException 
		 * @throws SearchDatabaseException 
		 * @throws FileNotFoundException 
		 * @throws FileNotDeletedInDatabase 
		 * @throws UploadFileFailException 
		 * @throws PasswordNotCorrectException 
		 * @throws UpdateAdminInfoFailException 
		 * @throws AdminNotLoginException 
		 * @throws LoginException 
		 * @throws RegisterAdminFailException 
		 * @throws SecurityException 
		 * @throws NoSuchMethodException 
		 * @throws InvocationTargetException 
		 * @throws IllegalArgumentException 
		 * @throws IllegalAccessException 
		 * @throws InstantiationException 
		 */
		public void sendResult(String commandLine) throws IOException, SQLException, SocketException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, RegisterAdminFailException, LoginException, AdminNotLoginException, UpdateAdminInfoFailException, PasswordNotCorrectException, UploadFileFailException, FileNotDeletedInDatabase, FileNotFoundException, SearchDatabaseException {
			
			try {
				StringTokenizer st = new StringTokenizer(commandLine);
				st.nextToken();
				String keyword = st.nextToken();
				
				UserCommandHandlerMapping.getInstance().create(commandLine, oos).handleRequest(keyword);
			} finally {
				if(socket!= null)
					socket.close();
			}

		}
	}
}











