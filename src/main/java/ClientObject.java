public class ClientObject {
    MySQLOperator mysql;
    String username;
    Boolean isOnline;
    Boolean isAuthorized;

    public ClientObject(MySQLOperator mysql, String username, ClientHandler handler) {
        this.mysql = mysql;
        this.username = username;
        this.isOnline = true;
        this.isAuthorized = false;
    }

    public void setOnline(Boolean online) {
        isOnline = online;
    }

    public void setAuthorized(Boolean authorized) {
        isAuthorized = authorized;
    }
    
}
