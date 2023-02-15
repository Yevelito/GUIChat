package Commands;

import Server.ClientHandler;
import Server.Handlers;

public class ShowAllUsers extends Command {
    private Handlers handlers;

    public ShowAllUsers() {
        super();
        this.shortname = "a";
        this.handlers = Handlers.getInstance();
    }

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
