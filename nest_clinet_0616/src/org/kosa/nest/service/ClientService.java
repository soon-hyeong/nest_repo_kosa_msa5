package org.kosa.nest.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.kosa.nest.common.ClientConfig;
import org.kosa.nest.network.ReceiveWorker;

public class ClientService {
    
    private ReceiveWorker receiveWorker;
 
    /**
     *  파일 다운로드가 성공하면 boolean값 받아 리턴
     * @param command
     * @return
     * @throws IOException
     */
    public boolean download(String command) throws IOException {
        return receiveWorker.downloadFile(command);
    }

}
