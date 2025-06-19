package org.kosa.nest.service;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.kosa.nest.common.ClientConfig;
import org.kosa.nest.model.FileVO;
import org.kosa.nest.network.ReceiveWorker;

public class ClientService {
    
    private ReceiveWorker receiveWorker;
    // 임시 명령어
    private String command = "download";
    
    public ClientService() throws UnknownHostException, IOException {
        this.receiveWorker = new ReceiveWorker();
    }
    
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
     */
    public void download(String command) throws IOException {
        makeDir();
        try {
            receiveWorker.sendCommand(command);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 클라이언트의 컴퓨터에 있는 다운로드 받은 파일 삭제 <br>
     * @return
     * @throws Exception 
     */
    public void delete(String title) {
        File file = new File(ClientConfig.REPOPATH + File.separator + title);
        boolean result = false;
        if(!file.exists())         // 존재 안하면 exception?
            System.out.println("temp exception");
        result = file.delete();
        System.out.println("file delete success");
    }

	public List<FileVO> list() {
        List<FileVO> fileList = new ArrayList<>();

        // 사용자 홈 디렉토리 하위의 nest 폴더를 기준 경로로 설정
        File folder = new File(ClientConfig.REPOPATH);

        // 해당 경로가 존재하고, 디렉토리일 경우에만 처리
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles(); // 디렉토리 내의 파일 리스트

            if (files != null) {
                for (File file : files) {
                    // 일반 파일인 경우에만 처리
                    if (file.isFile()) {
                        // 마지막 수정 시간을 기반으로 생성일시 추정
                        LocalDateTime createdAt = LocalDateTime.ofInstant(																					
                            Instant.ofEpochMilli(file.lastModified()),
                            ZoneId.systemDefault()
                        );

                        // 파일 정보를 담은 FileVO 객체 생성
                        FileVO vo = new FileVO(file.getAbsolutePath(), createdAt, file.getName());
                        fileList.add(vo); // 리스트에 추가
                    }
                }
            }
        }

        return fileList;
    }
	/**
	 * search : 파일의 일부 정보만 
	 * @param keyword
	 * @return
	 */
	public List<FileVO> search(String reuniteCommandLine) {
	    List<FileVO> resultList = new ArrayList<>();

	    try {
	        resultList = receiveWorker.sendCommand(reuniteCommandLine); // 명령어 전송

	        // 명령어가 search 또는 info일 때, 서버로부터 FileVO 목록 수신

	    } catch (IOException | ClassNotFoundException e) {
	        e.printStackTrace();
	    }

	    return resultList;
	}
	/**
	 * info : 파일의 상세정보 가져오기
	 * @param keyword
	 * @return
	 */
	public List<FileVO> info(String reuniteCommandLine) {
	    List<FileVO> resultList = new ArrayList<>();

	    try {
	        resultList = receiveWorker.sendCommand(reuniteCommandLine); // 서버에 info 명령어 전송
	    } catch (IOException | ClassNotFoundException e) {
	        e.printStackTrace();
	    }

	    return resultList;
	}




}
