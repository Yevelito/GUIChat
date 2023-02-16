package Server;

import Commands.ClientObject;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    private Handlers handlers;
    private CommandsLibrary commandsLibrary;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;
    private MySQLOperator mySQLOperator;
    private ClientObject clientObject;


    private String threadName = ""; // name for server debug printing

    public ClientHandler(Socket socket, MySQLOperator sql) throws SQLException {
        try {
            this.mySQLOperator = sql;
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            this.handlers = Handlers.getInstance();
            this.commandsLibrary = CommandsLibrary.getInstance();

            this.clientObject = new ClientObject(this.mySQLOperator);
            this.threadName = "|N:" + Thread.currentThread().getName() + "|P:" + socket.getPort() + "|";

            System.out.println(threadName + "INFO: Client handler successfully created");

        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }


    @Override
    public void run() {
        String msgFromClient = "";
        while (!this.clientObject.isAuthorized()) {
            try {
                msgFromClient = bufferedReader.readLine();
                System.out.println(threadName + "DEBUG: msg from client: " + msgFromClient);
                purposeAction(msgFromClient);

                if (this.clientObject.isAuthorized()) {
                    setUsername(this.clientObject.getUsername());
                    System.out.println(threadName + "INFO: client was authorized successfully");
                    msgToClient("SERVER: authorization successful");
                    commandsLibrary.commands.get("b").action("Hi, everyone! Happy join to the chat", this.clientObject);
                    this.handlers.addHandler(this.clientObject.getUsername(), this);
                    this.clientObject.getMysqlConnection().setOnlineStatus(this.clientObject.getUsername(), true);
                    printAllMessages();
                }

            } catch (Exception e) {
                System.out.println("ERROR: details: \n" + e + " \nclosing and leaving the server.");
                System.out.println("-------");
                e.printStackTrace();
                System.out.println("-------");
                try {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                } catch (SQLException ex) {
                    System.out.println(ex);
                    ex.printStackTrace();
                    throw new RuntimeException(ex);
                }
            }
        }


        while (this.clientObject.isOnline()) {
            try {
                msgFromClient = bufferedReader.readLine();
                purposeAction(msgFromClient);
            } catch (Exception e) {
                System.out.println(e);
                e.printStackTrace();
                try {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                } catch (SQLException ex) {
                    System.out.println(ex);
                    ex.printStackTrace();
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    public void purposeAction(String msgFromClient) {
        String[] msg = msgFromClient.split(":");
        if (msg.length > 1) {
            this.commandsLibrary.getCommands().get(msg[0]).action(msg[1], this.clientObject);
        } else {
            this.commandsLibrary.getCommands().get("b").action(msg[0], this.clientObject);
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ClientObject getClientObject() {
        return clientObject;
    }


    public void printAllMessages() {
        try {
            ArrayList<String> messages = mySQLOperator.getAllMessagesSortByTime();
            for (String msg : messages) {
                msgToClient(msg);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void msgToClient(String messageToSend) {
        try {
            //check if stream is available
            bufferedWriter.write(messageToSend);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeClientHandler() throws SQLException {
        commandsLibrary.commands.get("b").action(" Bye, everyone! I left the chat.", this.clientObject);
        this.clientObject.setOnline(false);
        this.clientObject.getMysqlConnection().setOnlineStatus(this.clientObject.getUsername(), false);
        handlers.removeHandler(this);
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) throws SQLException {
        removeClientHandler();
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

    @Override
    public String toString() {
        return this.clientObject.getUsername();
    }
}