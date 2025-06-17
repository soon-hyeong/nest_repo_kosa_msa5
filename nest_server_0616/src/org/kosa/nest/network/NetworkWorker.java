package org.kosa.nest.network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.kosa.nest.model.FileVO;
import org.kosa.nest.service.ServerUserService;

public class NetworkWorker {

	private ServerUserService serverUserService;
	
	/**
	 * 네트워크 접속을 시작하는 메서드 <br>
	 * ServerSocket을 생성하고 accept() 메서드를 실행하여 접속 가능한 상태로 만든 뒤, <br>
	 * socket을 생성하고 MultiThreading을 사용하여 여러 접속을 처리할 수 있도록 합니다.
	 * @throws IOException
	 */
	public void go() throws IOException {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(9876);
			while(true){
				Socket socket = serverSocket.accept();
				ReceiveWorker receiveWorker = new ReceiveWorker(socket);
				Thread receiveWorkerThread = new Thread(receiveWorker);
				receiveWorkerThread.start();
				socket.close();
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
		
		private ReceiveWorker(Socket socket) {
			this.socket = socket;
		}
		
		@Override
		public void run() {
			try {
				sendResult(getCommand());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * 유저가 보낸 명령어를 받아 반환하는 메서드입니다.
		 * @return
		 * @throws UnsupportedEncodingException
		 * @throws IOException
		 */
		public String getCommand() throws UnsupportedEncodingException, IOException {
			
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
			String commandLine  = br.readLine();
			br.close();
			
			return commandLine;
		}
		
		/**
		 * 입력받은 명령어에 따라 ServerUserService()의 메서드를 호출하여 결과값을 반환받고 <br>
		 * 반환받은 결과값을 stream을 통하여 user client에게 전송합니다.
		 * @param command
		 * @throws IOException
		 */
		public void sendResult(String command) throws IOException {
			
			BufferedOutputStream bfos = null;
			ObjectOutputStream oos = null;
			BufferedInputStream bis = null;

			try {
				if(command.equalsIgnoreCase("download")) {
					FileVO downloadFile = serverUserService.download(command);
					oos.writeObject(downloadFile);
					oos.close();
					bis = new BufferedInputStream(new FileInputStream(downloadFile.getFileLocation()), 8192);
					bfos = new BufferedOutputStream(socket.getOutputStream());
					int data = bis.read();
					while(data != -1) {
						bfos.write(data);
						bis.read();
					}
					bfos.close();
				}
				else if(command.equalsIgnoreCase("list") || command.equals("search")) {
					ArrayList<FileVO> searchFileList = serverUserService.search(command);
					oos = new ObjectOutputStream(socket.getOutputStream());
					oos.writeObject(searchFileList);
				}
				else if(command.equalsIgnoreCase("info")) {
					ArrayList<FileVO> infoFileList = serverUserService.info(command);
					oos = new ObjectOutputStream(socket.getOutputStream());
					oos.writeObject(infoFileList);
				}
			} finally {
				if(bfos != null)
					bfos.close();
				if(oos != null)
					oos.close();
				if(bis != null)
					bis.close();
			}

		}
	}
}











