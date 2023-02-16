package Client;

import Frames.MainChatFrame;

import java.io.*;
import java.net.Socket;


public class Client {
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private String username;
    private boolean auth;
    private MainChatFrame mcf;

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


    public void setUsername(String username) {
        this.username = username;
        sendMessageForm("u:" + this.username);
    }

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

    public void sendMessageForm(String msgToSend) {
        try {
            bufferedWriter.write(msgToSend);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            closeEverything(this.socket, bufferedWriter, bufferedReader);
        }
    }


    public void listenerForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromHandler;
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



                while (socket.isConnected()) {
                    try {
                        if (isAuth()) {
                            messageFromHandler = bufferedReader.readLine();
                            System.out.println("DEBUG: message from server(auth):\n" + messageFromHandler);
                            if ((messageFromHandler.startsWith("a:")) & (!messageFromHandler.equals("a:"))) {
                                String[] users = messageFromHandler.split(":")[1].split("\\|");
                                mcf.refreshOnlineUsers(users);
                            } else {
                                if (messageFromHandler.equals("a:")) {
                                    addOutputLine("Only you in chat");
                                } else {
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
