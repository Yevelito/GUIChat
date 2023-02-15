package Client;

import java.io.IOException;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
//        new ClientMain().run();
        ClientMain client = new ClientMain();
        client.start();
    }
}
