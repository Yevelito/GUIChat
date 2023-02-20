package Server;

import java.sql.SQLException;

/**
 * Main class to run a server instance.
 */
public class Main {
    public static void main(String[] args){
        Server server = new Server();
        server.startServer();
    }
}
