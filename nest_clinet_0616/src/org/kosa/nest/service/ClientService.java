package org.kosa.nest.service;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.kosa.nest.common.NestConfig;
import org.kosa.nest.exception.DataProcessException;
import org.kosa.nest.exception.FileNotFoundException;
import org.kosa.nest.exception.ServerConnectException;
import org.kosa.nest.model.FileVO;
import org.kosa.nest.network.ReceiveWorker;

public class ClientService {
    
    @SuppressWarnings("unused")
    private static ClientService instance;
    private ReceiveWorker receiveWorker;
    
    private ClientService() throws UnknownHostException, IOException {
        receiveWorker = ReceiveWorker.getInstance();
    }
    
    public static ClientService getInstance() throws UnknownHostException, IOException {
        return instance = new ClientService();
    }
    
    /**
     * 경로 생성 메서드 <br>
     * 클라이언트가 파일 다운로드를 시작할 때, <br>
     * 폴더를 만들어주는 메서드 <br>
     * 파일을 다운로드 받을 경로 체크 후 <br>
     * 없으면 경로 및 폴더 생성 <br>
     */
    private void makeDir() {
        File file = new File(NestConfig.REPOPATH);
        if (!file.isDirectory())
            file.mkdirs();
    }

    /**
     *  파일 다운로드 메서드 <br>
     *  ReceiveWorker에 서버에 있는 파일을 받아달라고 요청 <br>
     *  파일 다운로드에 문제가 생기면 Exception을 발생시킴 <br>
     * @param command
     * @return
     * @throws IOException
     * @throws DataProcessException 
     * @throws FileNotFoundException 
     * @throws ServerConnectException 
     */
    public void download(String command) throws DataProcessException, FileNotFoundException, ServerConnectException, IOException {
        makeDir();
        try {
            receiveWorker.sendCommand(command);
            System.out.println("File download success");
        } catch (ClassNotFoundException e) {
            throw new DataProcessException("Failed to process data!");
//        } catch (IOException e) {
//            throw new ServerConnectException("An unexpected error occurred while trying to connect to the server");
        }
        System.out.println("download completed!");
    }

	/**
	 * 클라이언트의 로컬컴퓨터에서 다운로드한 파일 목록을 가져옵니다
	 * @return
	 * @throws FileNotFoundException 
	 */
	public List<FileVO> list() throws FileNotFoundException {
	    ArrayList<FileVO> fileList = new ArrayList<>();
        File folder = new File(NestConfig.REPOPATH);

        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        // 마지막 수정 시간을 기반, 생성일시 추정
                        LocalDateTime createdAt = LocalDateTime.ofInstant(																					
                            Instant.ofEpochMilli(file.lastModified()),
                            ZoneId.systemDefault()
                        );

                        FileVO vo = new FileVO(file.getAbsolutePath(), createdAt, file.getName());
                        fileList.add(vo);
                    }
                }
            } else {
                throw new FileNotFoundException("File doesn't exist in directory!");
            }
        }
        return fileList;
    }
	
	/**
	 * 서버에 있는 파일의 일부 정보(파일제목)를 가져옵니다
	 * 명령어가 search 또는 info일 때
	 * @param keyword
	 * @return
	 * @throws FileNotFoundException 
	 * @throws DataProcessException 
	 * @throws UnknownHostException 
	 * @throws ServerConnectException 
	 */
	public List<FileVO> search(String reuniteCommandLine) throws FileNotFoundException, DataProcessException, UnknownHostException, ServerConnectException {
	    List<FileVO> resultList = new ArrayList<>();
	    try {
	        resultList = receiveWorker.sendCommand(reuniteCommandLine);
	        if(resultList.size() < 1) {
	            throw new FileNotFoundException("File doesn't exist in server!");
	        }
        } catch (IOException e) {
            throw new ServerConnectException("An unexpected error occurred while trying to connect to the server");
        } catch (ClassNotFoundException e) {
            throw new DataProcessException("Failed to process data!");
        }
	    return resultList;
	}
	
	/**
	 * 서버에 있는 파일의 모든 정보를 가져옵니다.
	 * @param keyword
	 * @return
	 * @throws FileNotFoundException 
	 * @throws DataProcessException 
	 * @throws UnknownHostException 
	 * @throws ServerConnectException 
	 */
	public List<FileVO> info(String reuniteCommandLine) throws FileNotFoundException, DataProcessException, UnknownHostException, ServerConnectException {
	    List<FileVO> resultList = new ArrayList<>();
	    
	    try {
	        resultList = receiveWorker.sendCommand(reuniteCommandLine);
	       if(resultList.size() < 1) {
	            throw new FileNotFoundException("File doesn't exist in server!");
           }
	    } catch (IOException e) {
            throw new ServerConnectException("An unexpected error occurred while trying to connect to the server!");
	    } catch (ClassNotFoundException e) {
	        throw new DataProcessException("Failed to process data!");
	    }
	    return resultList;
	}
	

    /**
     *  파일 삭제 메서드 <br>
     *  서버에서 다운로드 받은 파일 삭제 <br>
     * @param title
     * @throws FileNotFoundException
     */
    public void delete(String title) throws FileNotFoundException {
        File file = new File(NestConfig.REPOPATH + File.separator + title);
        if(!file.exists())
            throw new FileNotFoundException("File doesn't exist in your directory!");
        else {
            file.delete();
            System.out.println("File delete success");
        }
    }

    /**
     * nest 프로그램에 관한 설명 및 명령어 도움말<br>
     * 간략하게 nest 프로그램에 관한 설명을 하고 사용되는 명령어들을 출력하게 했다<br>
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
        System.out.println("  Download   - 서버에서 파일을 다운로드합니다");
        System.out.println("  List       - 내 컴퓨터에 있는 파일 목록을 확인합니다");
        System.out.println("  Search     - 서버에서 파일 이름을 검색합니다");
        System.out.println("  Info       - 서버에 있는 파일의 상세 정보를 확인합니다");
        System.out.println("  Delete     - 내 컴퓨터에서 파일을 삭제합니다");
    }
}
