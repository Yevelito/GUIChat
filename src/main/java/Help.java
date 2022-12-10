public class Help extends Command {
    Handlers handlers;

    public Help() {
        super();
        this.shortname = "h";
        this.handlers = Handlers.getInstance();
    }

    @Override
    public void action(String msg, ClientObject clientObject) {
        for (ClientHandler h: this.handlers.getHandlers()) {
            if (h.getUsername().equals(clientObject.username)){
                h.msgToClient("""
                For broadcast message just type the message
                For direct message type d:username@message
                To show all online users just type a:a
                """);
            }
        }
    }
}
