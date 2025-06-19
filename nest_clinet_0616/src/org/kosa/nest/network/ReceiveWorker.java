package org.kosa.nest.network;

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

    /**
     * ReceiveWorker 생성자로 socket 생성해 서버와 연결 <br>
     * 
     * @throws UnknownHostException
     * @throws IOException
     */
    // getNetwork() 필요없고 이렇게 하면 되지 않나?
    public ReceiveWorker() throws UnknownHostException, IOException {
//        socket = new Socket(ip, 9876);
    }


    /**
     * 클라이언트가 입력한 명령어를 서버로 전달 <br>
     * @param command
     * @return
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public void sendCommand(String command) throws IOException, ClassNotFoundException {
        
        BufferedWriter bw = null;

            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write(command);
            
            if(command.equalsIgnoreCase("download")) {
                this.downloadFile();
            } else if(command.equalsIgnoreCase("search") || command.equalsIgnoreCase("info")) {
                this.receiveFileList();
            }
        
    }
    
    /**
     * 명령어 : search, info
     * 클라이언트가 search, info 명령을 내리면 <br>
     * 파일 list를 server로부터 받아 FileVO로 만들고 list에 넣어서 반환 <br>
     * @return
     * @throws IOException 
     * @throws ClassNotFoundException 
     */
    // 메서드 이름 receiveResult 에서 receiveFileList로 변경
    public List<FileVO> receiveFileList() throws IOException, ClassNotFoundException{
        List<FileVO> list = new ArrayList<>();
        ObjectInputStream ois = null;
        
        try {
            ois = new ObjectInputStream(socket.getInputStream());
            list = (ArrayList<FileVO>)ois.readObject();
        }finally {
            if(ois != null)
                ois.close();
        }
        
        return list;
    }

    /**
     * 명령어 : download
     * 클라이언트가 download 명령어로 다운로드 받을 파일 제목을 명령하면 <br>
     * 파일을 서버로부터 받아 클라이언트의 컴퓨터에 다운로드 하는 기능 <br>
     * 
     * @return
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public void downloadFile() throws IOException, ClassNotFoundException {

        /*
         * 1. 파일들이 저장될 폴더가 디렉토리에 있는지 확인 후, 없으면 만들어야 -> Service로 옮김
         * 2. 파일을 서버로부터 받아와서
         * 3. 내 컴퓨터 경로에 다운로드, 이 때 byteStream 사용 
         */

        BufferedOutputStream bos = null;
        ObjectInputStream ois = null;
        
        try {
            // 파일 이름 : 서버에서 객체로 들어온 FileVO에서 얻음
            ois = new ObjectInputStream(socket.getInputStream());
            FileVO file = (FileVO)ois.readObject();
            String filename = file.getSubject();
            
            // 실제 파일을 받음
            // OutputStream을 두번 감싸지 말고 ObjectOutputStream을 그대로 사용하라. 객체 받을 때도 어차피 바이트 스트림 사용하므로.
            // bis = new BufferedInputStream(socket.getInputStream());
            
            // 파일을 클라이언트 컴퓨터에 저장
            bos = new BufferedOutputStream(new FileOutputStream(ClientConfig.REPOPATH + File.separator + filename)); 
            int data = ois.read();
            while (data != -1) {
                bos.write(data);
                data =  ois.read();
            }
            
        } finally {
            if (bos != null)
                bos.close();
            if (ois != null)
                ois.close();
        }
    }
}
