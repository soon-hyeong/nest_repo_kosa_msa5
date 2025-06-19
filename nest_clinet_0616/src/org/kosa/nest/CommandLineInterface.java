package org.kosa.nest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.kosa.nest.model.FileVO;
import org.kosa.nest.service.ClientService;

public class CommandLineInterface {

    private ClientService clientService;
    private Scanner scanner;

    public CommandLineInterface() {
        try {
            this.clientService = new ClientService();
            this.scanner = new Scanner(System.in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void executeProgram() throws IOException {
        String commandLine = scanner.nextLine();
        getCommand(commandLine);
    }

    // nest download 파일이름
    // nest search 키워드
    public void getCommand(String commandLine) throws IOException {
        StringTokenizer st = new StringTokenizer(commandLine);
        st.nextToken();
        String command = st.nextToken();
        String keyword = null;
        if (st.hasMoreTokens()) {
            keyword = st.nextToken();
        }
        String reuniteCommandLine = command + " " + keyword;

        switch (command) {
        case "download":
            clientService.download(reuniteCommandLine);
            break;
        case "delete":
            clientService.delete(keyword);
            break;
        case "list":
            resultToString(command, keyword);
            break;
        case "search":
            resultToString(command, keyword);
            break;
        case "info":
            resultToString(command, keyword);
            break;
        case "help":
            // 만들어야함
            break;
        case "exit":
            if (scanner != null)
                scanner.close();
            return;
        }
    }

    // info만 전부, list랑 search는 세개(제목, 태그, 생성시간)
    public void resultToString(String command, String keyword) {

        switch (command) {
        case "list":
            ArrayList<FileVO> list = (ArrayList<FileVO>) clientService.list();
            for (FileVO files : list) {
                FileVO file = new FileVO();
                file = files;
                System.out.println(file.getSubject() + " " + file.getTag() + " " + file.getCreatedAt());
            }
            break;
        case "search":
            ArrayList<FileVO> search = (ArrayList<FileVO>) clientService.search(keyword);
            for (FileVO files : search) {
                FileVO file = new FileVO();
                file = files;
                System.out.println(file.getSubject() + " " + file.getTag() + " " + file.getCreatedAt());
            }
            break;
        case "info":
            FileVO info = (FileVO) clientService.info(keyword);
            System.out.println(info.getSubject() + " " + info.getTag() + " " + info.getCreatedAt() + " "
                    + info.getFileLocation() + " " + info.getDescription() + " " + info.getUploadAt());
            break;
        }

    }
}
