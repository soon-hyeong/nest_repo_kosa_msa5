package org.kosa.nest.network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;

import org.kosa.nest.common.ClientConfig;

public class ReceiveWorker {

    private Socket socket;

    // 임시 IP
    private String ip = "127.0.0.1";
    private String command;

    /**
     * ReceiveWorker 생성자로 socket 생성해 서버와 연결 <br>
     * 
     * @throws UnknownHostException
     * @throws IOException
     */
    // getNetwork() 필요없고 이렇게 하면 되지 않나?
    public ReceiveWorker() throws UnknownHostException, IOException {
        socket = new Socket(ip, 1234);
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

    // 이거 불린에 관련된 것도 이야기해 봐야 겠다. -> void로 향후 변경
    /**
     * 클라이언트가 입력한 명령어를 서버로 전달 <br>
     * @param command
     * @return
     * @throws IOException
     */
    public boolean sendCommand(String command) throws IOException {
        
        this.command = command;  // 명령어 가져다 쓸 수 있게 세팅
        BufferedWriter bw = null;

        try {
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write(command);
        } finally {
            if (bw != null)
                bw.close();
        }

        return true;
    }

    /**
     * 클라이언트가 download 명령어로 다운로드 받을 파일 제목을 명령하면 <br>
     * 파일을 서버로부터 받아 클라이언트의 컴퓨터에 다운로드 하는 기능
     * 
     * @return
     * @throws IOException
     */
    // 이거 불린에 관련된 것도 이야기해 봐야 겠다. -> void로 향후 변경
    public boolean downloadFile() throws IOException {

        /*
         * 1. 파일들이 저장될 폴더가 디렉토리에 있는지 확인 후, 없으면 만들어야
         * 2. 파일을 서버로부터 받아와서
         * 3. 내 컴퓨터 경로에 다운로드,
         * 이 때 byteStream 사용 8192
         */

        makeDir();

        OutputStream fos = null;
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;

        try {
            bis = new BufferedInputStream(socket.getInputStream());
            // tmpFileName은 나중에 서버에서 받아서 처리
            bos = new BufferedOutputStream(
                    new FileOutputStream(ClientConfig.REPOPATH + File.separator + "tmpFileName"));

            int data = bis.read();
            while (data != -1) {
                bos.write(data);
                data = bis.read();
            }
        } finally {
            if (bos != null)
                bos.close();
            if (fos != null)
                fos.close();
        }
        return true;
    }
}
