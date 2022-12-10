import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class Server {

    ServerSocket serverSocket;
    MySQLOperator mySQLOperator;
    Handlers handlers;

    public Server(ServerSocket serverSocket) throws SQLException {
        this.serverSocket = serverSocket;
        this.handlers = Handlers.getInstance();
        this.mySQLOperator = new MySQLOperator();
        this.mySQLOperator.DropTableMessages();
        this.mySQLOperator.CreateTableMessages();
//        this.mySQLOperator.DropTableUsers();
//        this.mySQLOperator.CreateTableUsers();

    }

    public void startServer() {
        while (!serverSocket.isClosed()) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Socket "+socket.getPort() + " hase connected");
                ClientHandler clientHandler = new ClientHandler(socket,this.mySQLOperator);

                Thread thread = new Thread(clientHandler);
                thread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException, SQLException {
        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        server.startServer();
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
