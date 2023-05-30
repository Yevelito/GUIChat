package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

/**
 * Server.
 * Contains: MySQL operator, socket and list of handlers.
 */
public class Server {
    private final int serverPort = Integer.parseInt(System.getenv("SERVERPORT"));
    ServerSocket serverSocket;
    MySQLOperator mySQLOperator;
    Handlers handlers;


    /**
     *Open connection socket, create MySQL operator, drop and create 'messages' table (DB), get Handlers list instance.
     * If needed drop 'users' table (DB) too, it's possible if uncommit committed lines.
     */
    public Server() {
        System.out.println("INFO: Starting server on port: " + serverPort + " ...");
        try {
            this.serverSocket = new ServerSocket(serverPort);
            this.handlers = Handlers.getInstance();

            this.mySQLOperator = new MySQLOperator();
            this.mySQLOperator.DropTableMessages();
            this.mySQLOperator.CreateTableMessages();
          this.mySQLOperator.DropTableUsers();
          this.mySQLOperator.CreateTableUsers();
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Wait for client connection to the server socket while socket is open, and open new ClientHandler when it's happen.
     */
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
            } catch (SQLException e) {
                throw new RuntimeException(e);
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
