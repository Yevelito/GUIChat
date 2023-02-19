package Commands;

import Server.MySQLOperator;

/**
 * ClientObject class.
 * Contains inside MySQL connection, username, online and authorization statuses.
 */
public class ClientObject {
    private MySQLOperator mysql;
    private String username;
    private Boolean isOnline;
    private Boolean isAuthorized;

    /**
     * Constructor of clientObject.
     * Receive MySQL connection and create object with default parameters:
     * username - "",
     * online status - true,
     * authorization status - false.
     * @param mysql MySQL connection.
     */
    public ClientObject(MySQLOperator mysql) {
        this.mysql = mysql;
        this.username = "";
        this.isOnline = true;
        this.isAuthorized = false;
    }

    public void setOnline(Boolean online) {
        this.isOnline = online;
    }

    public Boolean isOnline() {
        return this.isOnline;
    }


    public void setAuthorized(Boolean authorized) {
        this.isAuthorized = authorized;
    }

    public boolean isAuthorized() {
        return this.isAuthorized;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public MySQLOperator getMysqlConnection() {
        return this.mysql;
    }
}
