package Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Handler;

public class Handlers {
    private static Handlers instance = null;
    private HashMap<String, ClientHandler> handlers = null;
    Handlers(){
        this.handlers = new HashMap<String, ClientHandler>();
    }


    public static Handlers getInstance() {
        if(instance == null) {
            instance = new Handlers();
        }

        return instance;
    }

    public void addHandler(String username, ClientHandler handler) {
        this.handlers.put(username, handler);
    }

    public HashMap<String, ClientHandler> getHandlers() {
        return this.handlers;
    }

    public void removeHandler(ClientHandler handler) {
        this.handlers.remove(handler);
    }

}
