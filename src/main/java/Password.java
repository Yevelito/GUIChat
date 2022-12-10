import java.sql.SQLException;
import java.util.ArrayList;

public class Password extends Command {

    Password() {
        this.shortname = "p";
    }


    @Override
    public void action(String password, ClientObject clientObject) {
        try {
            if (clientObject.mysql.checkIfPasswordAndLoginCorrect(clientObject.username, password)){
                clientObject.setAuthorized(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
