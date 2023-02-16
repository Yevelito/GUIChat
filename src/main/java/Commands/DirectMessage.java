package Commands;

import Server.ClientHandler;
import Server.Handlers;

import java.util.Set;

public class DirectMessage extends Command {
    Handlers handlers;

    public DirectMessage() {
//        super();gin executions to the default lifecycle
        this.shortname = "d";
        this.handlers = Handlers.getInstance();
    }

    @Override
    public void action(String msg, ClientObject clientObject) {
        System.out.println(msg);
        String[] txt = msg.split("@");
        System.out.println(txt[0]);
        System.out.println(txt[1]);
        ClientHandler receiver = this.handlers.getHandlers().get(txt[0]);
        ClientHandler sender = this.handlers.getHandlers().get(clientObject.getUsername());

        if (receiver.getClientObject().isOnline()) {
            receiver.msgToClient("Direct message from '" + clientObject.getUsername() + "': " + txt[1]);
            sender.msgToClient("Direct message to '" + txt[0] + "': " + txt[1]);

        }
    }
}
