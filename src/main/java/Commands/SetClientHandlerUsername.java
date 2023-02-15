package Commands;

public class SetClientHandlerUsername extends Command{

    public SetClientHandlerUsername() {
        this.shortname = "u";
    }

    @Override
    public void action(String msg, ClientObject clientObject) {
        clientObject.setUsername(msg);
    }
}
