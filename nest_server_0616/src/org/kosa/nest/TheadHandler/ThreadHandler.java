package org.kosa.nest.TheadHandler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import org.kosa.nest.client.CommandLineInterface;
import org.kosa.nest.common.ScannerWrapper;
import org.kosa.nest.exception.AdminNotLoginException;
import org.kosa.nest.exception.FileNotDeletedInDatabase;
import org.kosa.nest.exception.FileNotFoundException;
import org.kosa.nest.exception.LoginException;
import org.kosa.nest.exception.NoCommandLineException;
import org.kosa.nest.exception.PasswordNotCorrectException;
import org.kosa.nest.exception.RegisterAdminFailException;
import org.kosa.nest.exception.SearchDatabaseException;
import org.kosa.nest.exception.UpdateAdminInfoFailException;
import org.kosa.nest.exception.UploadFileFailException;
import org.kosa.nest.network.NetworkWorker;

public class ThreadHandler {
	
	@SuppressWarnings("unused")
	private static ThreadHandler instance;
	private NetworkWorker networkWorker;
	private CommandLineInterface commandLineInterface;
	
	private ThreadHandler() {
		networkWorker = NetworkWorker.getInstance();
		commandLineInterface = CommandLineInterface.getInstance();
	}
	
	public static ThreadHandler getInstance() {
		if(instance == null)
			instance = new ThreadHandler();
		return instance;	}

	public void executeProgram() {
        String logo = """
                ███╗   ██╗███████╗███████╗████████╗
                ████╗  ██║██╔════╝██╔════╝╚══██╔══╝
                ██╔██╗ ██║█████╗  ███████╗   ██║   
                ██║╚██╗██║██╔══╝  ╚════██║   ██║   
                ██║ ╚████║███████╗███████║   ██║   
                ╚═╝  ╚═══╝╚══════╝╚══════╝   ╚═╝   
                                                
                              	NEST 
                          
                    A neat place for your files.
                """;
        System.out.println(logo);
		Thread serverThread = new Thread(new ServerThread());
		Thread clientThread = new Thread(new clientThread());
		
		serverThread.setDaemon(true);
		
		clientThread.start();
		serverThread.start();
	}
	
	class ServerThread implements Runnable{
		
		
		@Override
		public void run(){
			try {
				networkWorker.go();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	class clientThread implements Runnable{
		
		@Override
		public void run() {
			while(true) {
				try {
					String commandLine = ScannerWrapper.getInstance().nextLine();
					if(commandLine.equalsIgnoreCase("quit") || commandLine.equalsIgnoreCase("exit"))
						break;
					commandLineInterface.getCommand(commandLine);
				}catch (RegisterAdminFailException e) {
					System.err.println(e.getMessage());	
				} catch (LoginException e) {
					System.err.println(e.getMessage());	
				} catch (AdminNotLoginException e) {
					System.err.println(e.getMessage());	
				} catch (PasswordNotCorrectException e) {
					System.err.println(e.getMessage());
				} catch (UpdateAdminInfoFailException e) {
					System.err.println(e.getMessage());
				} catch (FileNotFoundException e) {
					System.err.println(e.getMessage());
				} catch (SearchDatabaseException e) {
					System.err.println(e.getMessage());
				} catch (SQLException e) {
					System.err.println(e.getMessage());
				} catch (IOException e) {
					System.err.println(e.getMessage());
				} catch (UploadFileFailException e) {
					System.err.println(e.getMessage());
				} catch (FileNotDeletedInDatabase e) {
					System.err.println(e.getMessage());
				} catch (NoCommandLineException e) {
					System.err.println(e.getMessage());
				} catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
			}
		}
		
	}
}
