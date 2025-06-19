package org.kosa.nest.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.kosa.nest.common.ServerConfig;
import org.kosa.nest.exception.AdminNotLoginException;
import org.kosa.nest.exception.FileDeleteDatabaseException;
import org.kosa.nest.exception.FileNotFoundException;
import org.kosa.nest.exception.LoginException;
import org.kosa.nest.exception.PasswordNotCorrectException;
import org.kosa.nest.exception.RegisterException;
import org.kosa.nest.exception.SearchDatabaseException;
import org.kosa.nest.exception.UpdateAdminInfoFailException;
import org.kosa.nest.model.AdminDao;
import org.kosa.nest.model.AdminVO;
import org.kosa.nest.model.FileDao;
import org.kosa.nest.model.FileVO;
import org.kosa.nest.network.NetworkWorker;

public class ServerAdminService {
	
	private NetworkWorker networkWorker;
	private FileDao fileDao = new FileDao();
	private AdminDao adminDao = new AdminDao();
	private AdminVO currentLoginAdmin;
//	private AdminVO currentLoginAdmin = new AdminVO(1, "aaa@aaa", "pwd"); // 단위 테스트를 위해 작성, 추후 삭제 예정
	
	public ServerAdminService(){
		this.networkWorker = new NetworkWorker();
	}
	/**

		 * scanner를 통해 회원 가입하는 메서드 <br>
		 * 회원 가입시 이메일, 비밀번호를 입력해 회원가입 진행 <br>
		 * 예외로 등록이 불가능 하다면 예외 발생한 메세지를 띄움 <br>
	 * @throws  
		 * 
		 * 
		 * @throws CreateRegisterException 
		 */
		public void register() throws RegisterException {
			Scanner scanner = new Scanner(System.in);
			try {
				System.out.print("email:");
				String email = scanner.nextLine();
				System.out.print("password:");
				String password = scanner.nextLine();
				
				AdminVO vo = new AdminVO(email, password);
				adminDao.register(vo); //Id는 시스템이 제공
			} catch (SQLException sqle) {
				throw new RegisterException("register failed"); // 사용자 정의 예외 전달
			}
		}
		
		/**
		 * scanner를 통한 로그인 메서드 <br>
		 * 로그인 시에 아이디와 비밀번호를 입력하면 로그인 하지만 비밀번호가 일치하지 않거나 이메일이 틀릴경우 <br>
		 * 로그인 실패 메세지를 띄움<br>
		 * 로그인 성공시에 ID가 아닌 email을 넣은 이유는 여러 사용자가 테스트 하기에 편리하고, 이메일처럼 <br>
		 * 유니크한 정보를 기록에 남겨 Id로 구분이 불가한걸 이메일로 확인이 가능하게 함.<br>
		 * email 부분은 삭제가 가능하지만 혹시 몰라 넣어봄
		 * @throws loginException 
		 */
		public void login() throws LoginException {
			Scanner scanner = new Scanner(System.in);
			
			AdminVO admin = null;
			try {
				System.out.print("email:");
				String email = scanner.nextLine();
				
				System.out.print("password:");
				String password = scanner.nextLine();
				
				admin = adminDao.getAdminInfo(email); // Id로 정보 추출
				
				if(admin == null || !admin.getPassword().equals(password)) {
					throw new LoginException("비밀번호가 일치하지 않거나 존재하지 않는 관계자 입니다.");
				}
				// 로그인 성공 시 나오는 메세지
				currentLoginAdmin = admin;
				System.out.println("login success:" + currentLoginAdmin.getEmail());
				
			}catch (LoginException e) {
				throw e; // 사용자 정의 예외 전달
			}catch (Exception e) {
				throw new LoginException("로그인 중 오류 발생: "+ e.getMessage());
			}
		}
		
		/**
		 * 로그아웃 하는 메서드 <br>
		 * 로그아웃을 하게 된다면 어떤 관리자가 로그아웃을 했는지 이메일로 나오게 함 <br>
		 * 관리자가 존재하지 않을 때 로그아웃을 한다고 하면 관리자가 없다는 메세지를 띄움 <br>
		 * @return
		 * @throws AdminNotLoginException 
		 */
		
		public void logout() throws AdminNotLoginException {
			if(currentLoginAdmin == null)
				throw new AdminNotLoginException("Permission denied");
			else {
				String email = currentLoginAdmin.getEmail();
				currentLoginAdmin = null;
				System.out.println("log out success" + email);
			}
		}
		/**
		 * 현재 로그인 된 관리자의 정보 확인 메서드 <br>
		 * 현재 로그인 된 관리자에 대한 정보를 가져올때 <br>
		 * AdminVO에 있는 관리자 정보를 가져옴 <br>
		 * @return 관리자 정보
		 * @throws AdminNotLoginException 
		 */
		public AdminVO getMyInformation() throws AdminNotLoginException {
			if(currentLoginAdmin == null)
				throw new AdminNotLoginException("Permission denied");
			else {
				return currentLoginAdmin;
			}
		}
		/**
		 * 현재 로그인 된 관리자의 정보 수정 메서드 <br>
		 * 현재 로그인이 되었는지 아닌지 확인하고 <br>
		 * 현재 로그인 된 관리자에 대한 정보를 보여주고 <br>
		 * email과 password를 둘 다 수정 가능하게 구현하고 <br>
		 * 만약 둘 다 입력을 안했다면 원래 가지고 있던 관리자 정보로 두게 함 <br>
		 * 
		 * @return currentLoginAdmin
		 * @throws SQLException 
		 * @throws AdminNotLoginException 
		 * @throws PasswordNotCorrectException 
		 * @throws UpdateAdminInfoFailException 
		 */
		public AdminVO updateMyInformation() throws SQLException, AdminNotLoginException, PasswordNotCorrectException, UpdateAdminInfoFailException {
			if(currentLoginAdmin == null)
				throw new AdminNotLoginException("Permission denied");
			else {
				Scanner scanner = new Scanner(System.in);
				
				System.out.print("enter password:");
				String password = scanner.nextLine();
				if(!adminDao.getAdminInfo(currentLoginAdmin.getEmail()).getPassword().equals(password)) {
					throw new PasswordNotCorrectException("password not correct");
				}
				System.out.print("new email:");
				String newEmail = scanner.nextLine();
				
				System.out.print("new password:");
				String newPassword = scanner.nextLine();

				AdminVO updatedAdmin = new AdminVO(
						currentLoginAdmin.getId(),
						newEmail,
						newPassword
						);
				// DAO에 updateAdminInfo 존재
				try {
					adminDao.updateAdminInfo(updatedAdmin);
				} catch(SQLException sqle){
					throw new UpdateAdminInfoFailException("Update Admin Information failed");
				}
				return updatedAdmin;
			}
		}

	
	/**
	 * nest Server의 파일 저장소에 파일을 저장하는 메서드
	 * 사용자에게 파일의 정보를 입력받고, 지정되 저장소에 파일을 업로드한다.
	 * @return
	 * @throws IOException
	 * @throws AdminNotLoginException 
	 * @throws SQLException 
	 */
	public void uploadFile() throws IOException, AdminNotLoginException, SQLException {
		if(currentLoginAdmin == null)
			throw new AdminNotLoginException("Permission denied");
		else {
			FileVO inputFileInfo = getFileInformation();

			File nestServerDir = new File(ServerConfig.REPOPATH);
			if(!nestServerDir.isDirectory())
				nestServerDir.mkdirs();
			
			File inputFile = new File(inputFileInfo.getFileLocation());
			File outputFile = new File(ServerConfig.REPOPATH + File.separator + inputFile.getName());
			
			// 파일 입출력
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inputFile), 8192);
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile), 8192);
			
			int data = bis.read();
			while(data != -1) {
				bos.write(data);
				data = bis.read();
			}
			
			fileDao.createFileInfo(inputFileInfo);
			System.out.println("File upload sucess!");
			bis.close();
			bos.close();
		}
	}
	
	/**
	 * 저장소에 저장된 파일을 삭제하는 메서드 <br>
	 * @param command
	 * @return
	 * @throws SQLException 
	 * @throws AdminNotLoginException 
	 * @throws FileDeleteDatabaseException 
	 * @throws FileNotFoundException 
	 */
    public void deleteFile(String fileName) throws SQLException, AdminNotLoginException, FileDeleteDatabaseException, FileNotFoundException {
		if(currentLoginAdmin == null)
			throw new AdminNotLoginException("Permission denied");
		else {
	        File file = new File(ServerConfig.REPOPATH + File.separator + fileName);
	        
	        if(!file.exists())
	        	throw new FileNotFoundException("File doesn't exist in repository!");
	        	
	        file.delete();
	        try {
		        fileDao.deleteFileInfo(fileName);
	        } catch(SQLException e) {
	        	throw new FileDeleteDatabaseException("File information database error occured!");
	        }
	        System.out.println("File delete success!");
		}
    }
	
	/**
	 * 파일 업로드에 필요한 정보들을 입력받아 fileVO를 생성하고 반환하는 클래스.
	 * uploadFile() 메서드에서 사용한다.
	 * @return
	 * @throws AdminNotLoginException 
	 */
	public FileVO getFileInformation() throws AdminNotLoginException {
		if(currentLoginAdmin == null)
			throw new AdminNotLoginException("Permission denied");
		else {
			Scanner scanner = new Scanner(System.in);
			
			System.out.print("file address:");
			String fileAddress = scanner.nextLine();
					
			System.out.print("tag:");
			String fileTag = scanner.nextLine();
			System.out.println("dscription:");
			String fileDescription = scanner.nextLine();
			
			File inputFile = new File(fileAddress);
			LocalDateTime lastModifed =  LocalDateTime.ofInstant(Instant.ofEpochMilli(inputFile.lastModified()), ZoneId.systemDefault());

			FileVO fileVO = new FileVO(fileAddress, lastModifed, currentLoginAdmin.getId(), inputFile.getName(), fileTag, fileDescription);
			
			return fileVO;
		}
	}
	
	public ArrayList<FileVO> findAllList() throws SQLException, AdminNotLoginException {
		if(currentLoginAdmin == null)
			throw new AdminNotLoginException("Permission denied");
		else {
			ArrayList<FileVO>list = (ArrayList<FileVO>)fileDao.getAllFileInfoList();
			return list;
		}

	}
	/**
	 * search : 파일의 일부 정보만 
	 * @param keyword
	 * @return
	 * @throws AdminNotLoginException 
	 * @throws SearchDatabaseException 
	 */
	public List<FileVO> search(String keyword) throws AdminNotLoginException, SearchDatabaseException {
		if(currentLoginAdmin == null)
			throw new AdminNotLoginException("Permission denied");
		else {
		    List<FileVO> resultList = new ArrayList<>();

		    try {
		    	resultList = fileDao.getFileInfoList(keyword); // 전체 파일 목록 조회

		    } catch (SQLException e) {
//		        throw new SearchDatabaseException("File information databaseasf error occured!");
		    	e.printStackTrace();
		    }
		    return resultList;
		}
	}
	/**
	 * info: 파일 상세 정보 확인 
	 * @param keyword
	 * @return
	 * @throws AdminNotLoginException 
	 */
	public FileVO info(String keyword) throws AdminNotLoginException {
		if(currentLoginAdmin == null)
			throw new AdminNotLoginException("Permission denied");
		else {
		    List<FileVO> resultList = new ArrayList<>();
		    FileVO resultFileVO = null;

		    try {
		        FileDao dao = new FileDao();
		        resultList = dao.getFileInfo(keyword); // 제목, 태그, 날짜 중 하나라도 키워드 포함 시 반환

		    } catch (SQLException e) {
		        System.out.println("파일 상세 정보 조회 중 오류 발생: " + e.getMessage());
		    }
		    if(resultList.size() > 0)
		    	resultFileVO = resultList.get(0);
		    return resultFileVO;
		}

	}
	/**
	 * 도움말을 출력합니다
	 */
	public void help() {
		System.out.println();
	}
	
	
	
	
}
