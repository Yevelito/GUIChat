package Server;

import java.sql.*;
import java.util.ArrayList;

public class MySQLOperator {
    Statement stmt;
    String SERVERHOST;
    String USER;
    String PASSWORD;
    Connection con;
    int id;

    public MySQLOperator() {
        this.SERVERHOST = "jdbc:mysql://localhost:3306/chat_base";
        this.USER = "root";
        this.PASSWORD = "Password12321!!";
        this.id = 0;
        try {

            Class.forName("com.mysql.jdbc.Driver");
            this.con = DriverManager.getConnection(SERVERHOST, USER, PASSWORD);
            this.stmt = this.con.createStatement();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void CreateTableUsers() throws SQLException {
        this.stmt.execute("create table users (" + "id int NOT NULL AUTO_INCREMENT,"
                + "username varchar(255) NOT NULL,"
                + "email varchar(255) NOT NULL,"
                + "password varchar(255) NOT NULL,"
                + "isOnline BIT NULL,"
                + "PRIMARY KEY (id));");
    }

    public void CreateTableMessages() throws SQLException {
        this.stmt.execute("create table messages (" + "id int NOT NULL AUTO_INCREMENT,"
                + "username varchar(255) NOT NULL,"
                + "message varchar(255) NOT NULL,"
                + "time varchar(255) NOT NULL,"
                + "PRIMARY KEY (id));");
    }

    public void AddMessage(String username, String message, String time) throws SQLException {
        this.stmt.execute("INSERT INTO messages (username, message, time) VALUES ('"
                + username + "','" + message + "','" + time + "');");
    }

    public void AddUser(String username, String password, String email) throws SQLException {
        if (!checkIfUserExist(username)){
            this.stmt.execute("INSERT INTO users (username, email, password, isOnline) VALUES ('"
                    + username + "','" + email + "','" + password + "',1);");
        }
    }

//    public void DeleteLineById(Integer id) throws SQLException {
//        this.stmt.execute("delete from  users where id=" + id + ";");
//    }

//    public void ShowLines() throws SQLException {
//        ResultSet rs = null;
//        rs = this.stmt.executeQuery("select * from items");
//
//        while (rs.next()) {
//            System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3));
//        }
//    }

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
        rs = this.stmt.executeQuery("SELECT * FROM users WHERE username='" + username + "';");
        while (rs.next()) {
            n++;
        }
        return n > 0;
    }

    public boolean checkIfPasswordAndLoginCorrect(String username, String password) throws SQLException {
        int n = 0;
        ResultSet rs = null;
        rs = this.stmt.executeQuery("SELECT * FROM `users` WHERE username='" + username + "' and password='" + password + "';");
        while (rs.next()) {
            n++;
        }
        return n > 0;
    }

    public String getPasswordByLoginAndEmail(String username, String email) throws SQLException {
        ResultSet rs = null;
        rs = this.stmt.executeQuery("SELECT * FROM `users` WHERE username='" + username + "' and email='" + email + "';");
        if(rs.next()){
            return rs.getString(4);
        }else {
            return "Your email is not correct";
        }
    }

    public ArrayList<String> getAllMessagesSortByTime() throws SQLException {
        ResultSet rs = null;
        ArrayList<String>messages = new ArrayList<>();
        rs = this.stmt.executeQuery("SELECT * FROM `messages` ORDER BY time;");
        while(rs.next()){
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

