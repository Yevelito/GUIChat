public class ShowAllUsers extends Command {
    private Handlers handlers;

    public ShowAllUsers() {
        super();
        this.shortname = "a";
        this.handlers = Handlers.getInstance();
    }

    @Override
    public void action(String msg, ClientObject clientObject) {
        if (clientObject.isAuthorized) {
            for (ClientHandler clientHandler : this.handlers.getHandlers()) {
                if (clientHandler.getUsername().equals(clientObject.username)) {
                    for (ClientHandler h : this.handlers.getHandlers()) {
                        if (h.getClientObject().isOnline && !h.getUsername().equals(clientObject.username)) {
                            clientHandler.msgToClient(h.getUsername() + " is online");
                        }
                    }
                }
            }
        }
    }
}
