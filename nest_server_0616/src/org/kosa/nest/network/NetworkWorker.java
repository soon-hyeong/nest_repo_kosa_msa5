package org.kosa.nest.network;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.List;
import java.util.StringTokenizer;

import org.kosa.nest.command.Command;
import org.kosa.nest.command.user.DownloadCommand;
import org.kosa.nest.command.user.InfoCommand;
import org.kosa.nest.command.user.SearchCommand;
import org.kosa.nest.common.NestConfig;
import org.kosa.nest.handlerMapping.CommandHandlerMapping;
import org.kosa.nest.service.ServerUserService;

public class NetworkWorker {

	private static NetworkWorker instance;
	private ServerUserService serverUserService;
	
	//생성자로 ServerUserService를 받아옴
	private NetworkWorker() {
		this.serverUserService = ServerUserService.getInstance();
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
		 */
		public void sendResult(String commandLine) throws IOException, SQLException, SocketException {
			
			BufferedInputStream bis = null;

			try {
				StringTokenizer st = new StringTokenizer(commandLine);
				String command = st.nextToken();
				String keyword = st.nextToken();
				
				if(command.equalsIgnoreCase("download")) {
					List<Object> downloadFileList = DownloadCommand.getInstance(bis, oos).handleRequest(commandLine);
                    System.out.println("[info] Initiating object transfer.");
					oos.writeObject(downloadFileList);
					oos.flush();
                    System.out.println("[info] Object transfer completed successfully.");
					if(downloadFileList.size() > 0) {
						bis = new BufferedInputStream(new FileInputStream(downloadFileList.get(0).getFileLocation()), 8192);
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
					
				}
				else if(command.equalsIgnoreCase("list") || command.equals("search")) {
					List<Object> searchFileList = SearchCommand.getInstance().handleRequest(commandLine);
                    System.out.println("[info] Initiating object transfer.");
                    oos.writeObject(searchFileList);
					oos.flush();
                    System.out.println("[info] Object transfer completed successfully.");
				}
				else if(command.equalsIgnoreCase("info")) {
					List<Object> infoFileList = InfoCommand.getInstance().handleRequest(commandLine);
                    System.out.println("[info] Initiating object transfer.");
					oos.writeObject(infoFileList);
					oos.flush();
                    System.out.println("[info] Object transfer completed successfully.");
				}
			} finally {
				if(socket!= null)
					socket.close();
			}

		}
	}
}











