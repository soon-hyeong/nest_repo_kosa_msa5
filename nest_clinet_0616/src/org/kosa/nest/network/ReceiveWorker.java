package org.kosa.nest.network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.kosa.nest.common.ClientConfig;
import org.kosa.nest.model.FileVO;

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
     * 클라이언트가 입력한 명령어를 서버로 전달 <br>
     * @param command
     * @return
     * @throws IOException
     */
    // 이거 불린에 관련된 것도 이야기해 봐야 겠다. -> void로 향후 변경
    // 이 명령어를 보내서 명령어에 대한 값을 받을 때 download인지 receiveFileList인지
    // 선택해야하는데 어디서?
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
     * 명령어 : search, info
     * 클라이언트가 search, info 명령을 내리면 <br>
     * 파일 list를 server로부터 받아 FileVO로 만들고 list에 넣어서 반환 <br>
     * @return
     * @throws IOException 
     * @throws ClassNotFoundException 
     */
    // 메서드 이름 receiveResult 에서 receiveFileList로
    public ArrayList<FileVO> receiveFileList() throws IOException, ClassNotFoundException{
        List<FileVO> list = new ArrayList<>();
        ObjectInputStream ois = null;
        
        try {
            ois = new ObjectInputStream(socket.getInputStream());
            list = (ArrayList)ois.readObject();
        }finally {
            if(ois != null)
                ois.close();
        }
        
        return (ArrayList)list;
    }

    /**
     * 명령어 : download
     * 클라이언트가 download 명령어로 다운로드 받을 파일 제목을 명령하면 <br>
     * 파일을 서버로부터 받아 클라이언트의 컴퓨터에 다운로드 하는 기능 <br>
     * 
     * @return
     * @throws IOException
     */
    // 이거 불린에 관련된 것도 이야기해 봐야 겠다. -> void로 향후 변경
    public boolean downloadFile() throws IOException {

        /*
         * 1. 파일들이 저장될 폴더가 디렉토리에 있는지 확인 후, 없으면 만들어야 -> Service로 옮김
         * 2. 파일을 서버로부터 받아와서
         * 3. 내 컴퓨터 경로에 다운로드,
         * 이 때 byteStream 사용 8192
         */

        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;

        try {
            // 서버에서 다운로드 받을 파일을 받아옴
            bis = new BufferedInputStream(socket.getInputStream(), 8192);
            // tmpFileName은 나중에 서버에서 받아서 처리
            // 내 컴퓨터에 파일로 저장.....
            bos = new BufferedOutputStream(new FileOutputStream(ClientConfig.REPOPATH + File.separator + "tmpFileName"), 8192); 

            // 바이트코드인 파일을 한 줄 씩 읽어서 쓰기
            int data = bis.read();
            while (data != -1) {
                bos.write(data);
                data = bis.read();
            }
        } finally {
            if (bos != null)
                bos.close();
            if (bis != null)
                bis.close();
        }
        return true;
    }
}
