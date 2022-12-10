import java.util.HashMap;

public class CommandsLibrary {
    HashMap<String, Command> commands = null;

    public CommandsLibrary() {
        this.commands = new HashMap<>();
        this.commands.put(new Password().shortname, new Password());
        this.commands.put(new Email().shortname, new Email());
        this.commands.put(new Broadcast().shortname, new Broadcast());
        this.commands.put(new ShowAllUsers().shortname, new ShowAllUsers());
        this.commands.put(new Help().shortname, new Help());
        this.commands.put(new DirectMessage().shortname, new DirectMessage());

    }

    public HashMap<String, Command> getCommands() {
        return commands;
    }
}
