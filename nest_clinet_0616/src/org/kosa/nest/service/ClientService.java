package org.kosa.nest.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.kosa.nest.common.ClientConfig;
import org.kosa.nest.network.ReceiveWorker;

public class ClientService {
    
    private ReceiveWorker receiveWorker;
    // 임시 명령어
    private String command = "download";
    
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
     *  파일 다운로드가 성공하면 boolean값 받아 리턴
     * @param command
     * @return
     * @throws IOException
     */
    public boolean download() throws IOException {
        makeDir();
        receiveWorker.sendCommand(command);
        return receiveWorker.downloadFile();
    }

}
