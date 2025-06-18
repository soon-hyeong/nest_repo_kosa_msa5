package org.kosa.nest.test;

import java.io.IOException;
import java.net.UnknownHostException;

import org.kosa.nest.service.ClientService;

public class TestMain {
    public static void main(String[] args) throws UnknownHostException, IOException {
        ClientService clientServer = new ClientService();

        try {
            clientServer.download("download");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
