package org.kosa.nest.network;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.kosa.nest.common.ClientConfig;
import org.kosa.nest.common.NestConfig;
import org.kosa.nest.exception.FileNotFoundException;
import org.kosa.nest.model.FileVO;

public class ReceiveWorker {

    private Socket socket;
    private PrintWriter pw;
    private ObjectInputStream ois;
    private BufferedOutputStream bos;

    private String ip = "192.168.210.6"; // 서버 ip

    /**
     * ReceiveWorker 생성자 <br>
     * socket 생성해 서버와 연결 <br>
     * @throws UnknownHostException
     * @throws IOException
     */
    public ReceiveWorker() throws UnknownHostException, IOException {
        socket = new Socket(NestConfig.ip, NestConfig.port);
    }

    /**
     * 명령어 전달 메서드 <br>
     * 클라이언트가 입력한 명령어를 스트림을 열고 <br>
     * 서버로 전달하고<br>
     * 명령어에 따라 해당하는 각 메서드 호출, <br>
     * 각 Input, Output 스트림들을 닫아줌 <br>
     * @param command
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws FileNotFoundException 
     */
    public List<FileVO> sendCommand(String commandLine) throws IOException, ClassNotFoundException, FileNotFoundException {

        StringTokenizer st = new StringTokenizer(commandLine);
        String command = st.nextToken();

        ArrayList<FileVO> resultList = null;
        try {
            pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            pw.println(commandLine);

            if (command.equalsIgnoreCase("download")) {
                this.downloadFile();
            } else if (command.equalsIgnoreCase("search") || command.equalsIgnoreCase("info")) {
                resultList = (ArrayList<FileVO>) this.receiveFileList();
            }
        } finally {
            if (ois != null)
                ois.close();
            if (pw != null)
                pw.close();
            if (bos != null)
                bos.close();
            if (socket != null)
                socket.close();
        }
        return resultList;
    }

    /**
     * 서버에서 실제 파일을 다운로드 받는 메서드 <br>
     * 유저가 download 명령을 내렸을 때 <br>
     * 실제 파일을 서버로부터 받아 유저의 컴퓨터에 다운로드 하는 기능 <br>
     * 서버에서 FileVO와 실제 파일을 각각 받는데 <br>
     * 이는 실제 파일을 저장할 때 파일 이름을 얻을 수 없기 때문에 <br>
     * 저장할 파일 이름을 FileVO에서 얻음 <br>
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws FileNotFoundException 
     */
    @SuppressWarnings("unchecked")
    public void downloadFile() throws IOException, ClassNotFoundException, FileNotFoundException {

        ArrayList<FileVO> list = new ArrayList<>();
        ois = new ObjectInputStream(socket.getInputStream());
        list = (ArrayList<FileVO>) ois.readObject();
        
        String filename = null;
        if (list.size() < 1)
            throw new FileNotFoundException("File doesn't exist in server repository!");  // 다운로드 메서드에서만 예외적으로 exception throw
        else {
            filename = list.get(0).getSubject();
            bos = new BufferedOutputStream(new FileOutputStream(ClientConfig.REPOPATH + File.separator + filename));
            
            int data = ois.read();
            while (data != -1) {
                bos.write(data);
                data = ois.read();
            }
            bos.flush();
        }
    }

    /**
     * 서버에서 파일(FileVO) 리스트를 받는 메서드 <br>
     * 유저가 search, info 명령을 내렸을 때 <br>
     * 서버로부터 파일리스트를 받아 FileVO로 만들고 <br>
     * ArrayList에 넣어 서비스로 반환 <br>
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    public ArrayList<FileVO> receiveFileList() throws IOException, ClassNotFoundException {
        ois = new ObjectInputStream(socket.getInputStream());
        ArrayList<FileVO> list = new ArrayList<>();
        list = (ArrayList<FileVO>) ois.readObject();
        return list;
    }
}
