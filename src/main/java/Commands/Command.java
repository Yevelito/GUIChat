package Commands;

import org.json.JSONObject;

/**
 * Abstract class for commands between client and clientHandler.
 * Contains shortname and action method.
 */
public abstract class Command {
    String shortname;

    public Command() {
    }

    public String getShortname() {
        return shortname;
    }



    public abstract void action(String msg, ClientObject clientObject);

    public JSONObject convertMSGToJSON (String msg){
        return new JSONObject(msg);
    }
}
