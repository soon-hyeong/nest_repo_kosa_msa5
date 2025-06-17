package org.kosa.nest.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Scanner;

import org.kosa.nest.common.ServerConfig;
import org.kosa.nest.model.AdminDao;
import org.kosa.nest.model.AdminVO;
import org.kosa.nest.model.FileDao;
import org.kosa.nest.model.FileVO;
import org.kosa.nest.network.NetworkWorker;

public class ServerAdminService {
	
	private NetworkWorker networkWorker = new NetworkWorker();
	private FileDao fileDao = new FileDao();
	private AdminDao adminDao = new AdminDao();
	private AdminVO currentLoginAdmin;
//	private AdminVO currentLoginAdmin = new AdminVO(1, "aaa@aaa", "pwd"); // 단위 테스트를 위해 작성, 추후 삭제 예정
	
	/**
	 * nest Server의 파일 저장소에 파일을 저장하는 메서드
	 * 사용자에게 파일의 정보를 입력받고, 지정되 저장소에 파일을 업로드한다.
	 * @return
	 * @throws IOException
	 */
	public boolean uploadFile() throws IOException {
		
		FileVO inputFileInfo = getFileInformation();

		File nestServerDir = new File(ServerConfig.REPOPATH);
		if(!nestServerDir.isDirectory())
			nestServerDir.mkdirs();
		
		File inputFile = new File(inputFileInfo.getFileLocation());
		File outputFile = new File(ServerConfig.REPOPATH + File.separator + inputFile.getName());
		
		// 파일 입출
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inputFile), 8192);
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile), 8192);
		
		
		int data = bis.read();
		while(data != -1) {
			bos.write(data);
			data = bis.read();
		}
		
		bis.close();
		bos.close();
		return true;
	}
	
	/**
	 * 파일 업로드에 필요한 정보들을 입력받아 fileVO를 생성하고 반환하는 클래스.
	 * uploadFile() 메서드에서 사용한다.
	 * @return
	 */
	public FileVO getFileInformation() {
		
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("파일 주소를 입력하세요: ");
		String fileAddress = scanner.nextLine();
				
		System.out.println("태그를 입력하세요:");
		String fileTag = scanner.nextLine();
		System.out.println("설명을 입력하세요:");
		String fileDescription = scanner.nextLine();
		
		File inputFile = new File(fileAddress);
		LocalDateTime lastModifed =  LocalDateTime.ofInstant(Instant.ofEpochMilli(inputFile.lastModified()), ZoneId.systemDefault());

		FileVO fileVO = new FileVO(fileAddress, lastModifed, currentLoginAdmin.getId(), inputFile.getName(), fileTag, fileDescription);
		
		scanner.close();
		return fileVO;
	}
}
