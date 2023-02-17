package Commands;

import Server.Handlers;

public class AddUser extends Command {

    public AddUser() {this.shortname = "c";}


    @Override
    public void action(String msg, ClientObject clientObject) {
        try {
//            add new user
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
