package Server;

import Commands.*;

import java.util.HashMap;

public class CommandsLibrary {

    private static CommandsLibrary instance = null;

    HashMap<String, Command> commands = null;

    public CommandsLibrary() {
        this.commands = new HashMap<>();
        this.commands.put(new Password().getShortname(), new Password());
        this.commands.put(new Email().getShortname(), new Email());
        this.commands.put(new Broadcast().getShortname(), new Broadcast());
        this.commands.put(new ShowAllUsers().getShortname(), new ShowAllUsers());
        this.commands.put(new DirectMessage().getShortname(), new DirectMessage());
        this.commands.put(new SetClientHandlerUsername().getShortname(), new SetClientHandlerUsername());
        this.commands.put(new AddUser().getShortname(), new AddUser());
    }

    public HashMap<String, Command> getCommands() {
        return commands;
    }

    public static CommandsLibrary getInstance() {
        if(instance == null) {
            instance = new CommandsLibrary();
        }

        return instance;
    }
}
