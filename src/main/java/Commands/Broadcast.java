package Commands;

import Server.ClientHandler;
import Server.Handlers;

import java.sql.SQLException;
import java.time.Instant;

public class Broadcast extends Command{
    private Handlers handlers;
    public Broadcast() {
        super();
        this.shortname = "b";
        this.handlers = Handlers.getInstance();
    }

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
//            throw new RuntimeException(e);
        }

    }
}
