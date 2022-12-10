import com.sendemail.EmailSender;

import java.sql.SQLException;

public class Email extends Command {
    public Email() {
        super();
        this.shortname = "e";
    }

    @Override
    public void action(String msg, ClientObject clientObject) {
        try {
            new EmailSender().sendEmail(msg, clientObject.mysql.getPasswordByLoginAndEmail(clientObject.username, msg));
            System.out.println("Email sent");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
