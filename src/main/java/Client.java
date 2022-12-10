import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class Client {
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private String username;
    private boolean auth;

    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.username = username;
            this.auth = false;
        } catch (IOException e) {
            closeEverything(socket, bufferedWriter, bufferedReader);
        }
    }


    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your nickname for the chat:");
        String username = scanner.nextLine();

        Socket socket = new Socket("127.0.0.1", 1234);
        Client client = new Client(socket, username);
        client.listenerForMessage();
        client.msgToHandler();
    }

    public void sendMessageForm(String msgToSend) {
        try {
            bufferedWriter.write(msgToSend);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            closeEverything(socket, bufferedWriter, bufferedReader);
        }
    }

    public void msgToHandler() {
        Scanner scanner = new Scanner(System.in);
        sendMessageForm(username);

        String messageToSend= scanner.nextLine();
        sendMessageForm(messageToSend);

        while (socket.isConnected()) {
            messageToSend = scanner.nextLine();
            sendMessageForm(messageToSend);
        }
    }

    public void listenerForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromHandler;
                while (socket.isConnected()) {
                    try {
                        messageFromHandler = bufferedReader.readLine();
                        if (!auth && messageFromHandler.equals("SERVER: authorization successful")) {
                            auth = true;
                        }
                        System.out.println(messageFromHandler);
                    } catch (IOException e) {
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
