package Commands;

import Server.ClientHandler;
import Server.Handlers;

import java.sql.SQLException;
import java.time.Instant;

/**
 * Broadcast message command.
 */
public class Broadcast extends Command{
    private Handlers handlers;
    public Broadcast() {
        super();
        this.shortname = "broadcastMessage";
        this.handlers = Handlers.getInstance();
    }

    /**
     * Send message to all online users, and write it to the table 'messages' (DB).
     * @param msg message to send
     * @param clientObject contains: DB connection, username, online and authorization statuses of clientHandler.
     */
    @Override
    public void action(String msg, ClientObject clientObject) {
        try {
            for (ClientHandler h: this.handlers.getHandlers().values()) {
                h.msgToClient(clientObject.getUsername() + " ==> " + msg);
            }
            String time = String.valueOf(Instant.now().getEpochSecond());
            clientObject.getMysqlConnection().AddMessage(clientObject.getUsername(), msg, time);
        } catch (SQLException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}
