package org.kosa.nest.client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import org.kosa.nest.exception.DataProcessException;
import org.kosa.nest.exception.FileNotFoundException;
import org.kosa.nest.exception.NoCommandLineException;
import org.kosa.nest.exception.ServerConnectException;
import org.kosa.nest.model.FileVO;
import org.kosa.nest.service.ClientService;

public class CommandLineInterface {

    private ClientService clientService;
    private String[] args;

    public CommandLineInterface(String[] args) {
    		this.args = args;
            this.clientService = new ClientService();
    }

    /**
     * @throws IOException
     * @throws FileNotFoundException
     * @throws DataProcessException
     * @throws ServerConnectException 
     * @throws NoCommandLineException 
     */
    public void executeProgram() throws IOException, FileNotFoundException, DataProcessException, ServerConnectException, NoCommandLineException {
    	if(args.length == 0)
    		throw new NoCommandLineException("Please enter commandline!");
        getCommand();
    }

    /**
     * @param commandLine
     * @throws IOException
     * @throws FileNotFoundException
     * @throws DataProcessException
     * @throws ServerConnectException 
     */
    public void getCommand() throws IOException, FileNotFoundException, DataProcessException, ServerConnectException {
        String command = args[0];
        String keyword = null;
        if (args.length > 1) {
            keyword = args[1];
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
            clientService.help();
        else {
            System.out.println("Wrong command. If you need help, enter 'nest help'");
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
        	List<FileVO> list = clientService.list();
            for (FileVO file : list) {
                System.out.println(file.getSubject());
            }
        } else if (command.equalsIgnoreCase("search")) {
            List<FileVO> searchList = clientService.search(reuniteCommandLine);
            for (FileVO file : searchList) {
                System.out.println(file.getSubject());
            }
        } else if (command.equalsIgnoreCase("info")) {
            List<FileVO> infoList = clientService.info(reuniteCommandLine);
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
