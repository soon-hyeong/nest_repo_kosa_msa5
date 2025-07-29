package org.kosa.nest;

import java.io.IOException;

import org.kosa.nest.client.CommandLineInterface;
import org.kosa.nest.exception.DataProcessException;
import org.kosa.nest.exception.FileNotFoundException;
import org.kosa.nest.exception.NoCommandLineException;
import org.kosa.nest.exception.ServerConnectException;

public class NestMain {
    /**
     * nest program main class <br>
     * nest 프로그램을 실행하는 실행점<br>
     * @param args
     */
    public static void main(String[] args) {
        try {
            CommandLineInterface CLI = CommandLineInterface.getInstatnce(args);
            
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
            CLI.executeProgram();
        } catch (IOException e) {
            System.err.print(e.getMessage());
        } catch (FileNotFoundException e) {
            System.err.print(e.getMessage());
        } catch (DataProcessException e) {
            System.err.print(e.getMessage());
        } catch (ServerConnectException e) {
            System.err.print(e.getMessage());
        } catch (NoCommandLineException e) {
        	System.err.println(e.getMessage());
		}
    }
}
