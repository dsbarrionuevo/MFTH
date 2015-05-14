package mfthclient.client;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Barrionuevo Diego
 */
public class Main {


    public static void main(String[] args) {
        try {
            System.out.println("CLIENT");
            Client client = new Client(new Socket(Client.DEFAULT_SERVER_HOST, Client.DEFAULT_PORT));
            new Thread(client).start();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
