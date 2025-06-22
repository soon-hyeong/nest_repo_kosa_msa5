package org.kosa.nest;

import java.io.IOException;

import org.kosa.nest.client.CommandLineInterface;
import org.kosa.nest.exception.DataProcessException;
import org.kosa.nest.exception.FileNotFoundException;
import org.kosa.nest.exception.ServerConnectException;

public class NestMain {
    public static void main(String[] args) {
        CommandLineInterface CLI = new CommandLineInterface();
        try {
            CLI.executeProgram();
        } catch (IOException e) {
            System.err.print(e.getMessage());
        } catch (FileNotFoundException e) {
            System.err.print(e.getMessage());
        } catch (DataProcessException e) {
            System.err.print(e.getMessage());
        } catch (ServerConnectException e) {
            System.err.print(e.getMessage());
        }
    }
}
