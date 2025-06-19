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
    
    public void resultToString(ArrayList<FileVO> fileList) {
        for(FileVO vo : fileList)
            System.out.println(vo.getSubject());
    }
    
    public void resultToString(FileVO fileVO) {
        if(fileVO == null)
            return ;

    }

    // info만 전부, list랑 search는 세개(제목, 태그, 생성시간)
    public void resultToString(String command, String keyword) {
        String reuniteCommandLine = command + " " + keyword;
        switch (command) {
        case "list":
            ArrayList<FileVO> list = (ArrayList<FileVO>) clientService.list();
            for (FileVO file : list) {
                System.out.println(file.getSubject());
            }
            break;
        case "search":
            ArrayList<FileVO> searchList = (ArrayList<FileVO>) clientService.search(reuniteCommandLine);
            for (FileVO file : searchList) {
                System.out.println(file.getSubject());
            }
            break;
        case "info":
            ArrayList<FileVO> infoList = (ArrayList<FileVO>) clientService.info(reuniteCommandLine);
            for (FileVO file : infoList) {
                System.out.println("subject: " + file.getSubject());
                System.out.println("tag: " + file.getTag());
                System.out.println("lastModifed time: " + file.getCreatedAt());
                System.out.println("file address: " + file.getFileLocation());
                System.out.println("description: " + file.getDescription());
                System.out.println("server upload time: " + file.getUploadAt());
            }
            break;
        }

    }
}
