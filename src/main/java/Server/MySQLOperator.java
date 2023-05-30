package Server;

import java.sql.*;
import java.util.ArrayList;

/**
 * MySQL operator.
 * Responsible for all MySQL operations.
 */
public class MySQLOperator {
    Statement stmt;
    String SERVERHOST;
    String USER;
    String PASSWORD;
    Connection con;
    int id;

    /**
     * Connection to localhost chat database 'chat_base'.
     */
    public MySQLOperator() {
        this.SERVERHOST = System.getenv("DBHOST");
        this.USER = System.getenv("USER");
        this.PASSWORD = System.getenv("DBPASSWORD");
        this.id = 0;
        try {

            Class.forName("com.mysql.jdbc.Driver");
            this.con = DriverManager.getConnection(SERVERHOST, USER, PASSWORD);
            this.stmt = this.con.createStatement();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Create 'users' table with columns:
     * ID - (int) - user id, auto increment - can't be empty,
     * USERNAME - (varchar 255) - username - can't be empty,
     * EMAIL - (varchar 255) - email - can't be empty,
     * PASSWORD - (varchar 255) - password - can't be empty,
     * ISONLINE - (bit) - 0 as default
     * Primary key is ID.
     * @throws SQLException exception
     */
    public void CreateTableUsers() throws SQLException {
        this.stmt.execute("create table users (" + "id int NOT NULL AUTO_INCREMENT,"
                + "username varchar(255) NOT NULL,"
                + "email varchar(255) NOT NULL,"
                + "password varchar(255) NOT NULL,"
                + "isOnline BIT NULL,"
                + "PRIMARY KEY (id));");
    }

    /**
     * Create table 'messages' with columns:
     * ID - (int) - user id, auto increment - can't be empty,
     * USERNAME - (varchar 255) - username of sender - can't be empty,
     * MESSAGE - (varchar 255) - text of message - can't be empty,
     * TIME - (varchar 255) - unix epoch time - can't be empty.
     * Primary key is ID.
     * @throws SQLException exception
     */
    public void CreateTableMessages() throws SQLException {
        this.stmt.execute("create table messages (" + "id int NOT NULL AUTO_INCREMENT,"
                + "username varchar(255) NOT NULL,"
                + "message varchar(255) NOT NULL,"
                + "time varchar(255) NOT NULL,"
                + "PRIMARY KEY (id));");
    }

    /**
     * Add row to 'message' table (DB).
     * @param username - username
     * @param message  - message text
     * @param time     - time when message was sent
     * @throws SQLException
     */
    public void AddMessage(String username, String message, String time) throws SQLException {
        this.stmt.execute("INSERT INTO messages (username, message, time) VALUES ('"
                + username + "','" + message + "','" + time + "');");
    }

    /**
     * Add user to 'users' table (DB).
     * @param username username
     * @param password password
     * @param email    email
     * @throws SQLException
     */
    public void AddUser(String username, String password, String email) throws SQLException {
        if (!checkIfUserExist(username)) {
            this.stmt.execute("INSERT INTO users (username, email, password, isOnline) VALUES ('"
                    + username + "','" + email + "','" + password + "',1);");
        }
    }

    public void setOnlineStatus(String username, boolean status) throws SQLException {
        if (checkIfUserExist(username)) {
            if (status) {
                this.stmt.execute("UPDATE `users` SET isOnline=1 WHERE username='" + username + "';");
            } else {
                this.stmt.execute("UPDATE `users` SET isOnline=0 WHERE username='" + username + "';");
            }
        }

    }

    public boolean getOnlineStatus(String username) throws SQLException {
        ResultSet rs = null;
        boolean status = false;
        if (checkIfUserExist(username)) {
            rs = this.stmt.executeQuery("SELECT isOnline FROM `users` WHERE username='" + username + "';");
            if (rs.next()) {
                if (rs.getString("isOnline").equals("1")) {
                    status = true;
                }
            }
        }
        return status;
    }


    public void ConClose() throws SQLException {
        this.con.close();
    }

    public String returnUserIdByName(String name) throws SQLException {
        ResultSet rs = null;
        rs = this.stmt.executeQuery("SELECT * FROM `users` WHERE username='" + name + "';");
        return rs.getString(1);
    }

    public boolean checkIfUserExist(String username) throws SQLException {
        ResultSet rs = null;
        int n = 0;
        rs = this.stmt.executeQuery("SELECT * FROM `users` WHERE username='" + username + "';");
        while (rs.next()) {
            n++;
        }
        return n > 0;
    }

    public boolean checkIfPasswordAndLoginCorrect(String username, String password) throws SQLException {
        int n = 0;
        if (checkIfUserExist(username)) {
            ResultSet rs = null;
            rs = this.stmt.executeQuery("SELECT * FROM `users` WHERE username='" + username + "' and password='" + password + "';");
            while (rs.next()) {
                n++;
            }
        }
        return n > 0;
    }

    public String getPasswordByLoginAndEmail(String username, String email) throws SQLException {
        ResultSet rs = null;
        rs = this.stmt.executeQuery("SELECT * FROM `users` WHERE username='" + username + "' and email='" + email + "';");
        if (rs.next()) {
            return rs.getString(4);
        } else {
            return "Your email is not correct";
        }
    }

    public ArrayList<String> getAllMessagesSortByTime() throws SQLException {
        ResultSet rs = null;
        ArrayList<String> messages = new ArrayList<>();
        rs = this.stmt.executeQuery("SELECT * FROM `messages` ORDER BY time;");
        while (rs.next()) {
            messages.add(rs.getString(2) + " <=> " + rs.getString(3));
        }
        return messages;
    }

    public void DropTableUsers() throws SQLException {
        this.stmt.execute("DROP TABLE users;");
    }

    public void DropTableMessages() throws SQLException {
        this.stmt.execute("DROP TABLE messages;");
    }
}

