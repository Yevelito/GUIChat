import java.util.ArrayList;

public class Handlers {
    private static Handlers instance = null;
    private ArrayList< ClientHandler> handlers = null;
    Handlers(){
        this.handlers = new ArrayList<>();
    }


    public static Handlers getInstance() {
        if(instance == null) {
            instance = new Handlers();
        }

        return instance;
    }

    public void addHandler(ClientHandler handler) {
        this.handlers.add(handler);
    }

    public ArrayList<ClientHandler> getHandlers() {
        return this.handlers;
    }

    public void removeHandler(ClientHandler handler) {
        this.handlers.remove(handler);
    }
}
