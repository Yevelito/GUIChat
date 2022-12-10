public class DirectMessage extends Command {
    Handlers handlers;

    public DirectMessage() {
        super();
        this.shortname = "d";
        this.handlers = Handlers.getInstance();
    }

    @Override
    public void action(String msg, ClientObject clientObject) {
        String[] txt = msg.split("@");
        for (ClientHandler h : handlers.getHandlers()) {
            if (h.getUsername().equals(txt[0]) && h.getClientObject().isOnline ) {
                h.msgToClient(txt[0]);
                h.msgToClient(txt[1]);
                h.msgToClient("Direct message from " + clientObject.username + ": " + txt[1]);
            }
        }
    }
}
