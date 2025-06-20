package org.kosa.nest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.kosa.nest.exception.DataProcessException;
import org.kosa.nest.exception.FileNotFoundException;
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

    /**
     * @throws IOException
     * @throws FileNotFoundException
     * @throws DataProcessException 
     */
    public void executeProgram() throws IOException, FileNotFoundException, DataProcessException {
        String commandLine = scanner.nextLine();
        getCommand(commandLine);
    }

    /**
     * @param commandLine
     * @throws IOException
     * @throws FileNotFoundException
     * @throws DataProcessException 
     */
    public void getCommand(String commandLine) throws IOException, FileNotFoundException, DataProcessException {
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
            default : 
                System.out.println("Wrong command. If you need help, enter 'nest help'");
        }
    }

    /**
     * @param command
     * @param keyword
     * @throws DataProcessException 
     * @throws FileNotFoundException 
     */
    public void resultToString(String command, String keyword) throws FileNotFoundException, DataProcessException {
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
