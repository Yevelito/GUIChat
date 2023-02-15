package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
//        new Server().startServer();
        Server server = new Server();
        server.startServer();
    }
}
