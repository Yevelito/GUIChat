package Commands;

import com.sendemail.EmailSender;

import java.sql.SQLException;

/**
 * Send email command.
 * Send email via gmail.
 */
public class Email extends Command {
    public Email() {
        super();
        this.shortname = "recoveryPasswordViaEmail";
    }

    /**
     * Get password from table 'users' (DB), and send it via EmailSender.
     *
     * @param msg          message to send
     * @param clientObject contains: DB connection, username, online and authorization statuses of clientHandler.
     */
    @Override
    public void action(String msg, ClientObject clientObject) {
        try {
            new EmailSender().sendEmail(msg, clientObject.getMysqlConnection().getPasswordByLoginAndEmail(clientObject.getUsername(), msg));
            System.out.println("Commands.Email sent");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
