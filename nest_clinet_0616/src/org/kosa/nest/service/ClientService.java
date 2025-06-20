package org.kosa.nest.service;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

import org.kosa.nest.common.ClientConfig;
import org.kosa.nest.exception.DataProcessException;
import org.kosa.nest.exception.FileNotFoundException;
import org.kosa.nest.exception.ServerConnectException;
import org.kosa.nest.model.FileVO;
import org.kosa.nest.network.ReceiveWorker;

public class ClientService {
    
    private ReceiveWorker receiveWorker;
    
    /**
     * 클라이언트가 파일 다운로드를 시작할 때 <br>
     * 파일을 다운로드받을 경로 있는지 체크 후 <br>
     * 없으면 경로 생성 <br>
     */
    public void makeDir() {
        File file = new File(ClientConfig.REPOPATH);
        if (!file.isDirectory())
            file.mkdirs();
    }

    /**
     *  파일 다운로드가 성공하면 boolean값 받아 리턴 <br>
     * @param command
     * @return
     * @throws IOException
     * @throws DataProcessException 
     * @throws FileNotFoundException 
     * @throws ServerConnectException 
     */
    public void download(String command) throws DataProcessException, FileNotFoundException, ServerConnectException {
        makeDir();
        try {
            receiveWorker = new ReceiveWorker();
            receiveWorker.sendCommand(command);
        } catch (ClassNotFoundException e) {
            throw new DataProcessException("Failed to process data!");
        } catch (IOException e) {
            throw new ServerConnectException("An unexpected error occurred while trying to connect to the server");
        }
    }

	/**
	 * list
	 * @return
	 * @throws FileNotFoundException 
	 */
	public ArrayList<FileVO> list() throws FileNotFoundException {
	    ArrayList<FileVO> fileList = new ArrayList<>();
        File folder = new File(ClientConfig.REPOPATH);

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
	 * search : 파일의 일부 정보만 
	 * 명령어가 search 또는 info일 때
	 * @param keyword
	 * @return
	 * @throws FileNotFoundException 
	 * @throws DataProcessException 
	 * @throws UnknownHostException 
	 * @throws ServerConnectException 
	 */
	public ArrayList<FileVO> search(String reuniteCommandLine) throws FileNotFoundException, DataProcessException, UnknownHostException, ServerConnectException {
	    ArrayList<FileVO> resultList = new ArrayList<>();
	    try {
	        receiveWorker = new ReceiveWorker();
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
	 * info : 파일의 상세정보 가져오기
	 * @param keyword
	 * @return
	 * @throws FileNotFoundException 
	 * @throws DataProcessException 
	 * @throws UnknownHostException 
	 * @throws ServerConnectException 
	 */
	public ArrayList<FileVO> info(String reuniteCommandLine) throws FileNotFoundException, DataProcessException, UnknownHostException, ServerConnectException {
	    ArrayList<FileVO> resultList = new ArrayList<>();
	    
	    try {
	        receiveWorker = new ReceiveWorker();
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
     * 클라이언트의 컴퓨터에 있는 다운로드 받은 파일 삭제 <br>
     * @return
     * @throws FileNotFoundException 
     * @throws Exception 
     */
    public void delete(String title) throws FileNotFoundException {
        File file = new File(ClientConfig.REPOPATH + File.separator + title);
        if(!file.exists())
            throw new FileNotFoundException("File doesn't exist in your directory!");
        else {
            file.delete();
            System.out.println("File delete success!");
        }
    }

}
