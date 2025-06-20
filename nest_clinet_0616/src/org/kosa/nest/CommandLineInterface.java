package org.kosa.nest;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.kosa.nest.exception.DataProcessException;
import org.kosa.nest.exception.FileNotFoundException;
import org.kosa.nest.exception.ServerConnectException;
import org.kosa.nest.model.FileVO;
import org.kosa.nest.service.ClientService;

public class CommandLineInterface {

    private ClientService clientService;
    private Scanner scanner;

    public CommandLineInterface() {
            this.clientService = new ClientService();
            this.scanner = new Scanner(System.in);
    }

    /**
     * @throws IOException
     * @throws FileNotFoundException
     * @throws DataProcessException
     * @throws ServerConnectException 
     */
    public void executeProgram() throws IOException, FileNotFoundException, DataProcessException, ServerConnectException {
        String commandLine = scanner.nextLine();
        getCommand(commandLine);
    }

    /**
     * @param commandLine
     * @throws IOException
     * @throws FileNotFoundException
     * @throws DataProcessException
     * @throws ServerConnectException 
     */
    public void getCommand(String commandLine) throws IOException, FileNotFoundException, DataProcessException, ServerConnectException {
        StringTokenizer st = new StringTokenizer(commandLine);
        String command = st.nextToken();
        String keyword = null;
        if (st.hasMoreTokens()) {
            keyword = st.nextToken();
        }
        String reuniteCommandLine = command + " " + keyword;

        if (command.equalsIgnoreCase("download"))
            clientService.download(reuniteCommandLine);
        else if (command.equalsIgnoreCase("list"))
            resultToString(command, keyword);
        else if (command.equalsIgnoreCase("search"))
            resultToString(command, keyword);
        else if (command.equalsIgnoreCase("info"))
            resultToString(command, keyword);
        else if (command.equalsIgnoreCase("delete"))
            clientService.delete(keyword);
        else if (command.equalsIgnoreCase("help"))
            System.out.println("help 내용 필요");
        else {
            System.out.println("Wrong command. If you need help, enter 'nest help'");
            scanner.close();
        }
    }

    /**
     * @param command
     * @param keyword
     * @throws DataProcessException
     * @throws FileNotFoundException
     * @throws UnknownHostException 
     * @throws ServerConnectException 
     */
    public void resultToString(String command, String keyword) throws FileNotFoundException, DataProcessException, UnknownHostException, ServerConnectException {
        String reuniteCommandLine = command + " " + keyword;
        if (command.equalsIgnoreCase("list")) {
            ArrayList<FileVO> list = (ArrayList<FileVO>) clientService.list();
            for (FileVO file : list) {
                System.out.println(file.getSubject());
            }
        } else if (command.equalsIgnoreCase("search")) {
            ArrayList<FileVO> searchList = (ArrayList<FileVO>) clientService.search(reuniteCommandLine);
            for (FileVO file : searchList) {
                System.out.println(file.getSubject());
            }
        } else if (command.equalsIgnoreCase("info")) {
            ArrayList<FileVO> infoList = (ArrayList<FileVO>) clientService.info(reuniteCommandLine);
            for (FileVO file : infoList) {
                System.out.println("subject: " + file.getSubject());
                System.out.println("tag: " + file.getTag());
                System.out.println("lastModifed time: " + file.getCreatedAt());
                System.out.println("file address: " + file.getFileLocation());
                System.out.println("description: " + file.getDescription());
                System.out.println("server upload time: " + file.getUploadAt());
            }
        }
    }
}
