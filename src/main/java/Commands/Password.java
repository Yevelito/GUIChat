package Commands;

import java.sql.SQLException;

/**
 * Password check command.
 */
public class Password extends Command {

    public Password() {
        this.shortname = "passwordCheck";
    }

    /**
     * Check if received password is equal for password in 'users' table (DB),
     * and change authorization status to true in clientObject if it's ok.
     * @param password     password to check.
     * @param clientObject contains: DB connection, username, online and authorization statuses of clientHandler.
     */
    @Override
    public void action(String password, ClientObject clientObject) {
        try {
            if (!clientObject.getMysqlConnection().getOnlineStatus(clientObject.getUsername())) {
                boolean passwordOK = clientObject.getMysqlConnection().checkIfPasswordAndLoginCorrect(clientObject.getUsername(), password);
                if (passwordOK && !clientObject.isAuthorized()) {
                    System.out.println("DEBUG: clientObject auth status is: " + clientObject.isAuthorized());
                    clientObject.setAuthorized(true);
                    System.out.println("DEBUG: clientObject auth status is: " + clientObject.isAuthorized());
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
