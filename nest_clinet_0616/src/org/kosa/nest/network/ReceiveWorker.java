package org.kosa.nest.network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.kosa.nest.common.ClientConfig;

public class ReceiveWorker {
    
    private Socket socket;
    
    /**
     * 클라이언트가 파일 다운로드를 시작할 때 <br>
     * 파일을 다운로드받을 경로 있는지 체크 후 <br>
     * 없으면 경로 생성 <br>
     */
    public void makeDir() {
        File file = new File(ClientConfig.REPOPATH);
        if(!file.isDirectory())
            file.mkdirs();
    }
    
    /**
     * 클라이언트가 download 명령어로 다운로드 받을 파일 제목을 명령하면 <br>
     * 파일을 서버로부터 받아 클라이언트의 컴퓨터에 다운로드 하는 기능
     * @param command
     * @return
     * @throws IOException 
     */
    public boolean downloadFile(String command) throws IOException {
        
        /*
         * 1. 파일들이 저장될 폴더가 디렉토리에 있는지 확인 후, 없으면 만들어야
         * 2. 파일을 서버로부터 받아와서
         * 3. 내 컴퓨터 경로에 다운로드, 이 때 byteStream 사용
         * 8192
         */
        
        makeDir();  // 1.
        
        OutputStream fos = null;
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;

        try {
            bis = new BufferedInputStream(socket.getInputStream());
            // tmpFileName은 
            bos = new BufferedOutputStream(new FileOutputStream(ClientConfig.REPOPATH + File.separator + "tmpFileName"));
            
            int data = bis.read();
            while(data != -1) {
                bos.write(data);
                data = bis.read();
            }
        }finally {
            if(bos != null)
                bos.close();
            if(fos != null)
                fos.close();
        }
        return false;
    }
}
