package org.kosa.nest.common;

import java.util.Scanner;

public class ScannerWrapper {
    
    private static Scanner scanner;
    
    public static Scanner getInstance() {
        if(scanner == null)
            scanner = new Scanner(System.in);
        return scanner;
    }
    
    public void close() {
        if(scanner != null)
            scanner.close();
    }
}
