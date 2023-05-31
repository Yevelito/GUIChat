package Client;

import Frames.AuthorizationFrame;
import Frames.MainChatFrame;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;

import static java.lang.Thread.sleep;

/**
 * ClientMain Class.
 * This class manage switch between authorizationFrame and mainChatFrame.
 */
public class ClientMain {
    private Socket serverSocket;
    private Client client;

    private AuthorizationFrame authorizationFrame;
    private MainChatFrame mainChatFrame;


    /**
     * Create socket, client, authorizationFrame and mainChatFrame (mainChatFrame not visible).
     * Data for socket connection come from ClientConfig object.
     */
    public ClientMain() {
        try {
            String serverHost = ClientConfig.getInstance().data.get("SERVERHOST");
            final int serverPort = Integer.parseInt(System.getenv("SERVERPORT"));

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
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Wait for authorization by user, and then it's done dispose authorization frame,
     * set username to mainChatFrame and make mainChatFrame visible.
     */
    public void start() {
        System.out.println("INFO: waiting for authorization...");
        while (!this.client.isAuth()) {
            try {
                sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        ;

        this.authorizationFrame.dispose();
        this.mainChatFrame.setTitle("Chat client for '" + this.client.getUsername() + "' is on-line");
        this.mainChatFrame.setVisible(true);

        /**
         * Starts auto refresh for users online list
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject msg = new JSONObject();
                msg.put("commandName", "showOnlineUsers");
                msg.put("message", "");
                while (client.isAuth()){
                    try {
                        client.sendMessageForm(msg.toString());
                        sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println(e);
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }
}

