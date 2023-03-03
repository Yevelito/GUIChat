package Commands;

import Server.ClientHandler;
import Server.Handlers;

/**
 * Direct message command.
 * Send message straight to needed user, without write this message to 'message' table (DB).
 */
public class DirectMessage extends Command {
    Handlers handlers;

    public DirectMessage() {
        this.shortname = "directMessage";
        this.handlers = Handlers.getInstance();
    }

    /**
     * Split received message with "@" to get receivers username and text of message.
     * Send message to receiver and sender clients.
     * @param msg message received from client.
     * @param clientObject contains: DB connection, username, online and authorization statuses of clientHandler.
     */
    @Override
    public void action(String msg, ClientObject clientObject) {
        String[] txt = msg.split("#");
        ClientHandler receiver = this.handlers.getHandlers().get(txt[0]);
        ClientHandler sender = this.handlers.getHandlers().get(clientObject.getUsername());

        if (receiver.getClientObject().isOnline()) {
            receiver.msgToClient("Direct message from '" + clientObject.getUsername() + "': " + txt[1]);
            sender.msgToClient("Direct message to '" + txt[0] + "': " + txt[1]);

        }
    }
}
