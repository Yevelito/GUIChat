package Client;

import Server.Handlers;

import java.util.HashMap;
import java.util.Map;

/**
 * Config class.
 * Contains serverAddress and serverPort in hashMap data type.
 * Based on singleton pattern.
 */
public class ClientConfig {

    public Map<String, String > data;

    public ClientConfig(){
        data = new HashMap<>();
        data.put("serverAddress", "127.0.0.1");
        data.put("serverPort", "1234");
    }

    private static ClientConfig instance = null;


    public static ClientConfig getInstance() {
        if(instance == null) {
            instance = new ClientConfig();
        }

        return instance;
    }
}
