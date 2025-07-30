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
import org.kosa.nest.model.AdminDao;
import org.kosa.nest.model.AdminVO;
import org.kosa.nest.model.FileDao;
import org.kosa.nest.model.FileVO;

public class ServerAdminService {

	private static ServerAdminService instance;
	private FileDao fileDao = FileDao.getInstance();
	private AdminDao adminDao = AdminDao.getInstance();
	private AdminVO currentLoginAdmin;

	private ServerAdminService() {
	}
	
	public static ServerAdminService getInstance() {
	    if(instance == null)
	        instance = new ServerAdminService();
		return instance;
	}

    public AdminVO getCurrentLoginAdmin() {
        return currentLoginAdmin;
    }

    public void setCurrentLoginAdmin(AdminVO currentLoginAdmin) {
        this.currentLoginAdmin = currentLoginAdmin;
    }

	/**
	 * 현재 로그인 된 관리자의 정보 수정 메서드 <br>
	 * 현재 로그인이 되었는지 아닌지 확인하고 <br>
	 * 현재 로그인 된 관리자에 대한 정보를 보여주고 <br>
	 * email과 password를 둘 다 수정 가능하게 구현하고 <br>
	 * 만약 둘 다 입력을 안했다면 원래 가지고 있던 관리자 정보로 두게 함 <br>
	 * 
	 * @return currentLoginAdmin
	 * @throws AdminNotLoginException 
	 * @throws PasswordNotCorrectException 
	 * @throws UpdateAdminInfoFailException 
	 */
	public AdminVO updateMyInformation() throws AdminNotLoginException, PasswordNotCorrectException, UpdateAdminInfoFailException {
		if(currentLoginAdmin == null)
			throw new AdminNotLoginException("Permission denied!");
		else {
			
			System.out.print("enter password:");
			String password = scanner.nextLine();
			
			if(!currentLoginAdmin.getPassword().equals(password)) {
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
			} catch(SQLException e){
				throw new UpdateAdminInfoFailException("Update Admin Information failed:" + e.getMessage());
			}
			System.out.println("Update Admin Information success");
			return updatedAdmin;
		}
	}

	/**
	 * nest Server의 파일 저장소에 파일을 저장하는 메서드 사용자에게 파일의 정보를 입력받고<br>
	 * 지정되 저장소에 파일을 업로드한다.
	 * 
	 * @return
	 * @throws IOException
	 * @throws AdminNotLoginException
	 * @throws UploadFileFailException
	 */
	public void uploadFile() throws AdminNotLoginException, UploadFileFailException, IOException {

		if(currentLoginAdmin == null)
			throw new AdminNotLoginException("Permission denied!");
		else {
			FileVO inputFileInfo = getFileInformation();

			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			
			File outputFile = null;
			try {				
				File nestServerDir = new File(NestConfig.REPOPATH);
				if(!nestServerDir.isDirectory())
					nestServerDir.mkdirs();

				File inputFile = new File(inputFileInfo.getFileLocation());
				String outputFileAddress = NestConfig.REPOPATH + File.separator + inputFile.getName();
				outputFile = new File(outputFileAddress);
				// 파일 입출력
				bis = new BufferedInputStream(new FileInputStream(inputFile), 8192);
				bos = new BufferedOutputStream(new FileOutputStream(outputFile), 8192);

				int data = bis.read();
				while (data != -1) {
					bos.write(data);
					data = bis.read();
				}
				inputFileInfo.setFileLocation(outputFileAddress);
				fileDao.createFileInfo(inputFileInfo);
				System.out.println("File upload success");
			} catch(IOException e) {
				throw new UploadFileFailException("File upload failed:" + e.getMessage());			
			} catch(SQLException e) {
				outputFile.delete();
				throw new UploadFileFailException("File upload failed:" + e.getMessage());			
			} finally {
				if (bis != null)
					bis.close();
				if (bos != null)
					bos.close();
			}

		}
	}

	/**
	 * 저장소에 저장된 파일을 삭제하는 메서드 <br>
	 * 
	 * @param command
	 * @return
	 * @throws SQLException
	 * @throws AdminNotLoginException
	 * @throws FileDeleteDatabaseException
	 * @throws FileNotFoundException
	 */
    public void deleteFile(String fileName) throws AdminNotLoginException, FileNotDeletedInDatabase, FileNotFoundException {
		if(currentLoginAdmin == null)
			throw new AdminNotLoginException("Permission denied!");
		else {
	        File file = new File(NestConfig.REPOPATH + File.separator + fileName);
	        
	        if(!file.exists())
	        	throw new FileNotFoundException("File doesn't exist in repository!");
	        	
	        file.delete();
	        try {
		        fileDao.deleteFileInfo(fileName);
	        } catch(SQLException e) {
	        	throw new FileNotDeletedInDatabase("File information database error occured:" + e.getMessage());
	        }
	        System.out.println("File delete success");
		}
	}

	/**
	 * 파일 업로드에 필요한 정보들을 입력받아 fileVO를 생성하고 반환하는 클래스. uploadFile() 메서드에서 사용한다.
	 * 
	 * @return
	 * @throws AdminNotLoginException
	 */
	private FileVO getFileInformation() throws AdminNotLoginException {
		System.out.print("file address:");
		String fileAddress = scanner.nextLine();

		System.out.print("tag:");
		String fileTag = scanner.nextLine();
		System.out.println("dscription:");
		String fileDescription = scanner.nextLine();
		File inputFile = new File(fileAddress);
		LocalDateTime lastModifed = LocalDateTime.ofInstant(Instant.ofEpochMilli(inputFile.lastModified()),
				ZoneId.systemDefault());

		FileVO fileVO = new FileVO(fileAddress, lastModifed, currentLoginAdmin.getId(), inputFile.getName(), fileTag,
				fileDescription);

		return fileVO;
	}


	/**
	 * 모든 파일들을 리스트 형식으로 불러옵니다 <br>
	 * 불러온 각각의 파일은 일부 정보(tag, title, created_at)들만 표시됩니다<br>
	 * @return
	 * @throws AdminNotLoginException
	 * @throws SearchDatabaseException
	 */
	public List<FileVO> findAllList() throws AdminNotLoginException, SearchDatabaseException {
		if(currentLoginAdmin == null)
			throw new AdminNotLoginException("Permission denied");
		else {
			List<FileVO> list = null;
			try {
				list = fileDao.getAllFileInfoList();
			} catch(SQLException e){
				throw new SearchDatabaseException("File not found in database:" + e.getMessage());
			}
			return (ArrayList<FileVO>) list;
		}

	}

	/**
	 * 개별 파일을 리스트 형식으로 불러옵니다 <br>
	 * 불러온 파일은 title 만 표시됩니다<br>
	 * 태그, 제목, 생성시간 중 하나만 입력해도 원하는 파일을 찾을 수 있습니다<br>
	 * @param keyword
	 * @return
	 * @throws AdminNotLoginException
	 * @throws SearchDatabaseException
	 */
	public List<FileVO> search(String keyword) throws AdminNotLoginException, SearchDatabaseException {
		if (currentLoginAdmin == null)
			throw new AdminNotLoginException("Permission denied");
		else {
			List<FileVO> resultList = new ArrayList<>();

			try {
				resultList = fileDao.getFileInfoList(keyword); // 전체 파일 목록 조회
			} catch (SQLException e) {
				throw new SearchDatabaseException("File not found in database:" + e.getMessage());
			}
			return resultList;
		}
	}

	/**
	 * 개별 파일의 상세정보를 확인합니다<br>
	 * 파일의 제목 뿐만 아니라 db 에 있는 모든 정보를 볼 수 있습니다 <br>
	 * 제목, 태그, 날짜 중 하나라도 키워드로 이용하여 파일의 정보를 조회할 수 있습니다 <br>
	 * info: 파일 상세 정보 확인
	 * @param keyword
	 * @return
	 * @throws AdminNotLoginException
	 * @throws SearchDatabaseException
	 */
	public FileVO info(String keyword) throws AdminNotLoginException, SearchDatabaseException {
		if (currentLoginAdmin == null)
			throw new AdminNotLoginException("Permission denied");
		else {
			List<FileVO> resultList = new ArrayList<>();
			FileVO resultFileVO = null;

			try {
				resultList = fileDao.getFileInfo(keyword); // 제목, 태그, 날짜 중 하나라도 키워드 포함 시 반환

			} catch (SQLException e) {
				throw new SearchDatabaseException("File not found in database:" + e.getMessage());
			}
			if (resultList.size() > 0)
				resultFileVO = resultList.get(0);
			return resultFileVO;
		}

	}

	/**
	 * 간략하게 nest 프로그램에 대한 소개를 적고, <br>
	 * 프로젝트에서 사용된 명령어와 관리자가 사용할 명령어들을 기입하여 help를 입력했을 때 도움말을 출력해<br>
	 * 명령어와 설명을 띄웁니다<br>
	 * 
	 * @param command
	 */
	public void help() {
		System.out.println("---- nest 프로그램 안내 ----");
		System.out.println("nest는 로컬과 서버의 파일을 명령어 한 줄로 손쉽게 관리할 수 있는 CLI 기반 파일 관리 프로그램입니다.");
		System.out.println("로컬 모드에서는 내 컴퓨터의 파일 목록 확인 및 삭제가 가능하며,");
		System.out.println("서버 모드에서는 파일 검색, 상세 정보 확인, 다운로드를 지원합니다.");
		System.out.println();
		System.out.println("모드를 선택한 후 다음 명령어를 사용할 수 있습니다:");
		System.out.println("  list             - 로컬 파일 목록 보기");
		System.out.println("  delete <파일명>   - 로컬 파일 삭제");
		System.out.println("  search <키워드>   - 서버에서 파일 검색");
		System.out.println("  info <파일명>     - 서버 파일 상세 정보 보기");
		System.out.println("  download <파일명> - 서버 파일 다운로드");
		System.out.println("  exit             - 프로그램 종료");
		System.out.println("  back             - 모드 선택 화면으로 돌아가기");
		System.out.println();
		System.out.println("---- 관리자 명령어 ----");
		System.out.println("  Register              - 관리자로 회원가입합니다");
		System.out.println("  Login                 - 로그인합니다");
		System.out.println("  Logout                - 로그아웃합니다");
		System.out.println("  GetMyInformation      - 로그인한 관리자의 정보를 조회합니다");
		System.out.println("  UpdateMyInformation   - 로그인한 관리자의 정보를 수정합니다");
		System.out.println("  UploadFile            - 파일을 업로드합니다");
		System.out.println("  DeleteFile            - 파일을 삭제합니다");
		System.out.println("  FindAllList           - 모든 파일 목록을 조회합니다");
		System.out.println("  Search                - 파일 이름으로 검색합니다 (파일 이름 필요)");
		System.out.println("  Info                  - 파일 상세 정보를 조회합니다 (파일 이름 필요)");
		System.out.println("  Delete                - 파일을 삭제합니다 (파일 이름 필요)");
	}

}
