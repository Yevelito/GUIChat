package Commands;

import Server.ClientHandler;
import Server.Handlers;

/**
 * Show all online users command.
 */
public class ShowAllUsers extends Command {
    private Handlers handlers;

    public ShowAllUsers() {
        super();
        this.shortname = "showOnlineUsers";
        this.handlers = Handlers.getInstance();
    }

    /**
     * Runs on clientHandlers array, and append username if it's not equal to client username.
     * Send result to client.
     * @param msg equal '1', because it can't be empty.
     * @param clientObject contains: DB connection, username, online and authorization statuses of clientHandler.
     */
    @Override
    public void action(String msg, ClientObject clientObject) {
        StringBuilder res = new StringBuilder("a:");
        ClientHandler h = this.handlers.getHandlers().get(clientObject.getUsername());
        if (clientObject.isAuthorized()) {
            for (ClientHandler clientHandler : this.handlers.getHandlers().values()) {
                if ((clientHandler.getClientObject().isOnline()) &
                        (!clientHandler.getClientObject().getUsername().equals(clientObject.getUsername()))) {
                    res.append(clientHandler.getClientObject().getUsername()).append("|");
                }
            }
        }
        h.msgToClient(res.toString());
        res = new StringBuilder();
    }
}
