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
            for (ClientHandler h: this.handlers.getHandlers()) {
                if (!h.getUsername().equals(clientObject.username)) {
                    h.msgToClient(clientObject.username + "==>" + msg);
                }
            }
            String time = String.valueOf(Instant.now().getEpochSecond());
            clientObject.mysql.AddMessage(clientObject.username, msg, time);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
