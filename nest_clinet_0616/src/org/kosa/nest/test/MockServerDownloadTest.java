package org.kosa.nest.test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;

import org.kosa.nest.model.FileVO;

// 1. 클라이언트에게 명령어를 받아서 : download(ClienctService에 하드코딩 해놓음)
// 2. 다운로드할 파일을 전달
public class MockServerDownloadTest {
    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = null;

        BufferedReader br = null; // 클라이언트에서 들어오는 명령어 읽음
        ObjectOutputStream oos = null; // 클라이언트로 파일 이름 보냄 / 클라이언트로 파일 보냄
        BufferedInputStream bis = null; // 클라이언트로 보낼 파일 컴퓨터에서 읽어옴

        try {
            serverSocket = new ServerSocket(5252);
            Socket socket = serverSocket.accept();

            // 명령어를 받음
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 클라이언트로 파일 이름 보내기 위해 FileVO 보냄
            oos = new ObjectOutputStream(socket.getOutputStream());
            FileVO fileVO = new FileVO("test", LocalDateTime.parse("2025-06-16T16:33:12"), 3,
                    "database-study1-basic.sql", "test", "test");
            oos.writeObject(fileVO);
            oos.flush();

            File file = new File("C:" + File.separator + "Projects" + File.separator
                    + "lesson" + File.separator + "database-sql" + File.separator + "database-study1-basic.sql");
            bis = new BufferedInputStream(new FileInputStream(file));
            
            // OutputStream을 두번 감싸지 말고 ObjectOutputStream을 그대로 사용하라. 객체 보낼 때도 어차피 바이트 스트림 사용하므로.
            // bos = new BufferedOutputStream(socket.getOutputStream());
            int data = bis.read();
            while (data != -1) {
                oos.write(data);
                data = bis.read();
            }
            oos.flush();
            
        } finally {
            if (serverSocket != null)
                serverSocket.close();
            if (br != null)
                br.close();
            if (bis != null)
                bis.close();
        }
    }

}
