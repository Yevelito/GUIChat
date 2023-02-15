package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class Server {

    private final int serverPort = 1234;

    ServerSocket serverSocket;
    MySQLOperator mySQLOperator;
    Handlers handlers;


    public Server() throws SQLException {
        System.out.println("INFO: Starting server on port: " + serverPort + " ...");
        try {
            this.serverSocket = new ServerSocket(serverPort);
            this.handlers = Handlers.getInstance();

            this.mySQLOperator = new MySQLOperator();
            this.mySQLOperator.DropTableMessages();
            this.mySQLOperator.CreateTableMessages();
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
//        this.mySQLOperator.DropTableUsers();
//        this.mySQLOperator.CreateTableUsers();

    }

    public void startServer() {
        while (!serverSocket.isClosed()) {
            try {
                System.out.println("INFO: Listening for a new connection...");
                Socket socket = serverSocket.accept();

                System.out.println("INFO: Socket has connected via port: " + socket.getPort());
                ClientHandler clientHandler = new ClientHandler(socket, this.mySQLOperator);
                Thread thread = new Thread(clientHandler);
                thread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
