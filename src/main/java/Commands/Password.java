package Commands;

import java.sql.SQLException;

public class Password extends Command {

    public Password() {
        this.shortname = "p";
    }


    @Override
    public void action(String password, ClientObject clientObject) {
        try {
            if (!clientObject.getMysqlConnection().getOnlineStatus(clientObject.getUsername())) {
                try {
                    // check if password is the same as in DB
                    boolean passwordOK = clientObject.getMysqlConnection().checkIfPasswordAndLoginCorrect(clientObject.getUsername(), password);
                    if (passwordOK && !clientObject.isAuthorized()) {
                        clientObject.setAuthorized(true);
                    }
                } catch (SQLException e) {
                    System.out.println(e);
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }

            }
        } catch (SQLException e) {
            System.out.println(e);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
