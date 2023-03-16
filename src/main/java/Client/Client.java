package Client;

import Frames.MainChatFrame;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;

/**
 * Client class. User side of app.
 * Receive and send messages from/to server via bufferReader and bufferWriter.
 * Connection made via socket.
 * Contains GUI mainChatFrame.
 */
public class Client {
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private String username;
    private boolean auth;
    private MainChatFrame mcf;

    /**
     * Client constructor.
     * Receive socket, and create BufferedReader and BufferedWriter based on I/O streams.
     *
     * @param s - socket
     */
    public Client(Socket s) {
        try {
            this.socket = s;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.username = null;
            this.auth = false;
            this.listenerForMessage();
        } catch (IOException e) {
            closeEverything(socket, bufferedWriter, bufferedReader);
        }
    }

    /**
     * Set username to the client instance.
     * Set username to the clientHandler by SetClientHandlerUsername command via JSONObject.
     *
     * @param username - username to set.
     */
    public void setUsername(String username) {
        this.username = username;
        JSONObject msg = new JSONObject();
        msg.put("commandName", "setUsername");
        msg.put("message", username);
        sendMessageForm(msg.toString());
    }


    /**
     * Add line to chatOutputFrame in mainChatFrame.
     *
     * @param msg - message to add.
     */
    public void addOutputLine(String msg) {
        if (this.mcf != null) {
            this.mcf.addOutputLine(msg);
        } else {
            System.out.println("WARNING!: main chat frame wasn't created. Printing message:");
            System.out.println(msg);
        }
    }

    public void setMcf(MainChatFrame mcf) {
        this.mcf = mcf;
    }

    public String getUsername() {
        return this.username;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    public boolean isAuth() {
        return this.auth;
    }

    /**
     * Send message to client handler.
     *
     * @param msgToSend - message to send
     */
    public void sendMessageForm(String msgToSend) {
        try {
            bufferedWriter.write(msgToSend);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            closeEverything(this.socket, bufferedWriter, bufferedReader);
        }
    }

    /**
     * listen message from client handler
     */
    public void listenerForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromHandler;
                /**
                 * authorization
                 */
                while (!isAuth()) {
                    try {
                        messageFromHandler = bufferedReader.readLine();
                        System.out.println("DEBUG: message from server(no auth): " + messageFromHandler);
                        if (messageFromHandler.equals("SERVER: authorization successful")) {
                            setAuth(true);
                        }

                    } catch (IOException e) {
                        System.out.println("ERROR! GOT AN EXCEPTION! 1");
                        System.out.println(e);
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }

                /**
                 * receiving messages from clientHandler
                 */
                while (socket.isConnected()) {
                    try {
                        if (isAuth()) {
                            messageFromHandler = bufferedReader.readLine();
                            System.out.println("DEBUG: message from server(auth):\n" + messageFromHandler);

                            /**
                             * Check if received message says to show online users,
                             * and add line to chatOutputFrame in mainChatFrame if it's not.
                             */
                            if ((messageFromHandler.startsWith("a:")) & (!messageFromHandler.equals("a:"))) {
                                String[] users = messageFromHandler.split(":")[1].split("\\|");
                                mcf.refreshOnlineUsers(users);
                            } else {
                                if (messageFromHandler.equals("a:")) {
                                    String[] users = new String[1];
                                    users[0] = "null";
                                    mcf.refreshOnlineUsers(users);
                                }else{
                                    addOutputLine(messageFromHandler);
                                }
                            }
                        }
                    } catch (IOException e) {
                        System.out.println("ERROR! GOT AN EXCEPTION! 2");
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();


    }


    /**
     * Close all connections.
     *
     * @param socket
     * @param bufferedWriter
     * @param bufferedReader
     */
    public void closeEverything(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
