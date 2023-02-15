package Commands;

import java.sql.SQLException;

public class Password extends Command {

    public Password() {
        this.shortname = "p";
    }


    @Override
    public void action(String password, ClientObject clientObject) {

        boolean passwordOK = false;
        try {
            // check if password is the same as in DB
            passwordOK = clientObject.getMysqlConnection().checkIfPasswordAndLoginCorrect(clientObject.getUsername(), password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (passwordOK && !clientObject.isAuthorized()) {
            clientObject.setAuthorized(true);
        }
//        else {
//            throw new Exception("VSE PLOHO!");
//        }

    }
}
