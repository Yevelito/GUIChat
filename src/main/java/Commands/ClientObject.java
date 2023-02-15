package Commands;

import Server.MySQLOperator;

public class ClientObject {
    private MySQLOperator mysql;
    private String username;
    private Boolean isOnline;
    private Boolean isAuthorized;

    public ClientObject(MySQLOperator mysql) {
        this.mysql = mysql;
        this.username = "";
        this.isAuthorized = false;
        this.isOnline = true;
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
