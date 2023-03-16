package Commands;

/**
 * Set clientHandler username command.
 */
public class SetClientHandlerUsername extends Command{

    public SetClientHandlerUsername() {
        this.shortname = "setUsername";
    }

    @Override
    public void action(String msg, ClientObject clientObject) {
        clientObject.setUsername(msg);
    }
}
