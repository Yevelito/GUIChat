import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    private Handlers handlers = null;
    private CommandsLibrary commandsLibrary;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;
    private MySQLOperator mySQLOperator;
    private ClientObject clientObject;


    public ClientHandler(Socket socket, MySQLOperator sql) {
        try {
            this.mySQLOperator = sql;
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.username = bufferedReader.readLine();
            this.commandsLibrary = new CommandsLibrary();
            this.handlers = Handlers.getInstance();
            this.clientObject = new ClientObject(this.mySQLOperator, this.username, this);

        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public String getUsername() {
        return username;
    }

    public ClientObject getClientObject() {
        return clientObject;
    }

    public boolean authorization() throws Exception {
        String[] password;
        String[] email;
        String msgFromClient;

        msgToClient("Authorization...");

        while (!this.clientObject.isAuthorized) {
            if (this.mySQLOperator.checkIfUserExist(username)) {
                msgToClient("For authorization by password please write p:{{password}} \n" +
                        "Recover your password by email please write e:{{email}}");

                msgFromClient = bufferedReader.readLine();
                purposeAction(msgFromClient);

            } else {
                msgToClient("User doesn't exist. You need to register. Please enter your email:");
                email = bufferedReader.readLine().split(":");

                msgToClient("Please enter your password");
                password = bufferedReader.readLine().split(":");

                mySQLOperator.AddUser(username, password[0], email[0]);
                msgToClient("You're welcome!");

                this.clientObject.setAuthorized(true);
            }
        }
        return true;
    }


    @Override
    public void run() {
        try {
            if (authorization()) {
                this.handlers.addHandler(this);

                msgToClient("SERVER: authorization successful");
                commandsLibrary.commands.get("b").action("SERVER: " + this.clientObject.username + " has been connected to the chat", this.clientObject);
                printAllMessages();

                String msgFromClient;
                while (socket.isConnected()) {
                    msgFromClient = bufferedReader.readLine();
                    purposeAction(msgFromClient);
                }
            }
        } catch (Exception e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
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
            bufferedWriter.write(messageToSend);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeClientHandler() {
        handlers.removeHandler(this);
        this.clientObject.setOnline(false);
        commandsLibrary.commands.get("b").action("SERVER: " + this.clientObject.username + " has left the chat.", this.clientObject);
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
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
}