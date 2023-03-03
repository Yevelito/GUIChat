package Server;

import Commands.ClientObject;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Made when client have connection to te server.
 * Responsible for all logic part of application.
 * Contains:
 * Handlers list instance,
 * Commands list instance,
 * Socket
 * Buffer reader and writer
 * Username
 * MySQL operator instance
 * Client object
 */
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

    /**
     * Receive from server socket and MySQL operator.
     * Create Buffered reader and writer based on socket connection.
     * Get handlers list instance and CommandsLibrary instance.
     *
     * @param socket socket
     * @param sql    MySQL operator instance
     * @throws SQLException
     */
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


    /**
     * Main logic of project.
     * Consist of 2 steps:
     * 1 - AUTHORIZATION:
     * wait till clientObject "isAuthorized" status will be true,
     * and when send "SERVER: authorization successful" message to the Client for their authorization,
     * and broadcast message to the chat about it.
     * Add this ClientHandler to Handlers list.
     * Set online status "1" in 'users' table (DB).
     * 2 - CHAT RUNNING:
     * receive messages from Client till "isOnline" status in clientObject is true
     */
    @Override
    public void run() {
        String msgFromClient = "";
        try {
            /**
             * AUTHORIZATION PART
             */
            while (!this.clientObject.isAuthorized()) {
                msgFromClient = bufferedReader.readLine();
                System.out.println(threadName + "DEBUG: msg from client: " + msgFromClient);
                System.out.println(threadName + "DEBUG: clientObject auth status: " + this.clientObject.isAuthorized());
                purposeAction(msgFromClient);


                if (this.clientObject.isAuthorized()) {
                    setUsername(this.clientObject.getUsername());
                    System.out.println(threadName + "INFO: client was authorized successfully");
                    msgToClient("SERVER: authorization successful");
                    commandsLibrary.commands.get("broadcastMessage").action("Hi, everyone! Happy join to the chat", this.clientObject);
                    this.handlers.addHandler(this.clientObject.getUsername(), this);
                    this.clientObject.getMysqlConnection().setOnlineStatus(this.clientObject.getUsername(), true);
                    printAllMessages();
                }

            }
        } catch (Exception e) {
            System.out.println("ERROR: details: \n" + e + " \nclosing and leaving the server.");
            System.out.println("-------");
            e.printStackTrace();
            System.out.println("-------");
            try {
                closeEverything(socket, bufferedReader, bufferedWriter);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        }

        /**
         * RUNNING CHAT PART
         */
        try {
            while (this.clientObject.isOnline()) {
                msgFromClient = bufferedReader.readLine();
                purposeAction(msgFromClient);
            }
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

    /**
     * Convert received string to JSONObject,
     * check if "message" value contains '#' symbol,
     * and turn command to DirectMessage command if yes.
     *
     * @param msgFromClient JSONObject converted to string. Came from Client.
     */
    public void purposeAction(String msgFromClient) {
        JSONObject msg = new JSONObject(msgFromClient);
        if (msg.get("message").toString().contains("#")) {
            msg.put("commandName", "directMessage");
        }
        this.commandsLibrary.getCommands().get(msg.get("commandName").toString())
                .action(msg.get("message").toString(), this.clientObject);
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
        if (this.clientObject.isAuthorized()) {
            commandsLibrary.commands.get("broadcastMessage").action("Bye, everyone! I left the chat.", this.clientObject);
            this.clientObject.setOnline(false);
            this.clientObject.setAuthorized(false);
            this.clientObject.getMysqlConnection().setOnlineStatus(this.clientObject.getUsername(), false);
        }
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