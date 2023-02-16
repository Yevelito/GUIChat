package Client;

import Frames.AuthorizationFrame;
import Frames.MainChatFrame;

import java.io.IOException;
import java.net.Socket;

public class ClientMain {
    private Socket serverSocket;
    private Client client;

    private AuthorizationFrame authorizationFrame;
    private MainChatFrame mainChatFrame;

    public ClientMain() {
        try {
            String serverHost = ClientConfig.getInstance().data.get("serverAddress");
            int serverPort = Integer.parseInt(ClientConfig.getInstance().data.get("serverPort"));

            System.out.println("INFO: Connecting to server " + serverHost + ":" + serverPort);
            this.serverSocket = new Socket(serverHost, serverPort);
            this.client = new Client(serverSocket);
            System.out.println("INFO: Connection - success");

            this.authorizationFrame = new AuthorizationFrame(this.client);
            this.mainChatFrame = new MainChatFrame(this.client);
            this.client.setMcf(mainChatFrame);

        } catch (IOException e) {
            System.out.println(
                    "ERROR: Cant connect to the server. Exiting.\nDetails: \n" +
                            e.getMessage());
//            e.printStackTrace();
            System.exit(-1);
        }


    }


    public void start() {
        System.out.println("INFO: waiting for authorization...");
        while (!this.client.isAuth())
        {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        this.authorizationFrame.dispose();
        this.mainChatFrame.setTitle("Chat client for '" + this.client.getUsername() + "' is on-line");
        this.mainChatFrame.setVisible(true);
    }
}

