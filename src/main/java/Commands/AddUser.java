package Commands;

/**
 * Add user command.
 * Add user to table 'users'(DB).
 */
public class AddUser extends Command {

    public AddUser() {this.shortname = "c";}

    /**
     * Add user method.
     * Parse received message from client and add this data to 'users' DB.
     * @param msg message contains data (username, email, password).
     * @param clientObject contains: DB connection, username, online and authorization statuses of clientHandler.
     */
    @Override
    public void action(String msg, ClientObject clientObject) {
        try {
            String[] user = msg.split("\\|");
            if (!clientObject.getMysqlConnection().checkIfUserExist(user[0])){
                clientObject.getMysqlConnection().AddUser(user[0], user[1], user[2]);
                clientObject.setUsername(user[0]);
                clientObject.setAuthorized(true);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
