abstract class Command {
    String shortname;

    public Command() {
    }

    public abstract void action(String msg, ClientObject clientObject);
}
