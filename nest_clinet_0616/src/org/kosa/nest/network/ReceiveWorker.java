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

    /**
     * ReceiveWorker 생성자로 socket 생성해 서버와 연결 <br>
     * 
     * @throws UnknownHostException
     * @throws IOException
     */
    public ReceiveWorker() throws UnknownHostException, IOException {
        socket = new Socket(NestConfig.ip, NestConfig.port);
    }

    /**
     * 클라이언트가 입력한 명령어를 서버로 전달 <br>
     * 
     * @param command
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws FileNotFoundException 
     */
    public ArrayList<FileVO> sendCommand(String commandLine) throws IOException, ClassNotFoundException, FileNotFoundException {

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
     * 명령어 : search, info 클라이언트가 search, info 명령을 내리면 <br>
     * 파일 list를 server로부터 받아 FileVO로 만들고 list에 넣어서 반환 <br>
     * 
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

    /**
     * 명령어 : download 클라이언트가 download 명령어로 다운로드 받을 파일 제목을 명령하면 <br>
     * 파일을 서버로부터 받아 클라이언트의 컴퓨터에 다운로드 하는 기능 <br>
     * 파일 이름 : 서버에서 객체로 들어온 FileVO에서 얻음<br>
     * 
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
            throw new FileNotFoundException("File doesn't exist in your directory!");  // 다운로드 메서드에서만 예외적으로 exception throw
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
}
