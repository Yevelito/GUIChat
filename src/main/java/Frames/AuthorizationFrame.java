package Frames;

import Client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Authorization frame.
 * Contains 3 option: login, recovery password by email, registration.
 */
public class AuthorizationFrame extends JFrame implements ActionListener {
    private JButton loginButton;
    private JButton registrationButton;
    private JButton emailRecoverButton;
    private JPanel loginPanel;
    private JPanel registrationPanel;
    private JTextField usernameLogField;
    private JTextField emailRecoverField;
    private JTextField passwordLogField;
    private JTextField usernameRegField;
    private JTextField passwordRegField;
    private JTextField emailRegField;
    private Client client;

    /**
     * Contains 3 panels:
     * loginPanel (consist of usernameLogField, passwordLogField, loginButton, emailRecoverField, emailRecoverButton)
     * registrationPanel (consist of usernameRegField, passwordRegField, emailRegField and registrationButton)
     *
     * @param client user client
     */
    public AuthorizationFrame(Client client) {
        this.client = client;

        this.usernameLogField = new JTextField();
        this.usernameLogField.setPreferredSize(new Dimension(245, 40));
        this.usernameLogField.setText("username");
        this.usernameLogField.setBackground(Color.WHITE);
        this.usernameLogField.setCaretColor(Color.BLACK);

        this.passwordLogField = new JTextField();
        this.passwordLogField.setPreferredSize(new Dimension(120, 40));
        this.passwordLogField.setText("password");
        this.passwordLogField.setBackground(Color.WHITE);
        this.passwordLogField.setCaretColor(Color.BLACK);


        this.loginButton = new JButton();
        this.loginButton.setPreferredSize(new Dimension(120, 40));
        this.loginButton.setHorizontalAlignment((int) CENTER_ALIGNMENT);
        this.loginButton.setText("login");
        this.loginButton.addActionListener(this);
        this.loginButton.setForeground(Color.WHITE);
        this.loginButton.setBackground(Color.darkGray);
        this.loginButton.setFocusable(false);


        this.emailRecoverField = new JTextField();
        this.emailRecoverField.setPreferredSize(new Dimension(120, 40));
        this.emailRecoverField.setText("email");
        this.emailRecoverField.setBackground(Color.WHITE);
        this.emailRecoverField.setCaretColor(Color.BLACK);

        this.emailRecoverButton = new JButton();
        this.emailRecoverButton.setPreferredSize(new Dimension(120, 40));
        this.emailRecoverButton.setHorizontalAlignment((int) CENTER_ALIGNMENT);
        this.emailRecoverButton.setText("recover");
        this.emailRecoverButton.addActionListener(this);
        this.emailRecoverButton.setForeground(Color.WHITE);
        this.emailRecoverButton.setBackground(Color.darkGray);
        this.emailRecoverButton.setFocusable(false);

        this.loginPanel = new JPanel();
        this.loginPanel.setBounds(0, 0, 280, 210);
        this.loginPanel.setBackground(Color.black);
        this.loginPanel.add(usernameLogField);
        this.loginPanel.add(passwordLogField);
        this.loginPanel.add(emailRecoverField);
        this.loginPanel.add(loginButton);
        this.loginPanel.add(emailRecoverButton);


        this.usernameRegField = new JTextField();
        this.usernameRegField.setPreferredSize(new Dimension(120, 40));
        this.usernameRegField.setText("username");
        this.usernameRegField.setBackground(Color.WHITE);
        this.usernameRegField.setCaretColor(Color.BLACK);

        this.passwordRegField = new JTextField();
        this.passwordRegField.setPreferredSize(new Dimension(120, 40));
        this.passwordRegField.setText("password");
        this.passwordRegField.setBackground(Color.WHITE);
        this.passwordRegField.setCaretColor(Color.BLACK);

        this.emailRegField = new JTextField();
        this.emailRegField.setPreferredSize(new Dimension(120, 40));
        this.emailRegField.setText("email");
        this.emailRegField.setBackground(Color.WHITE);
        this.emailRegField.setCaretColor(Color.BLACK);

        this.registrationButton = new JButton();
        this.registrationButton.setPreferredSize(new Dimension(120, 40));
        this.registrationButton.setText("registration");
        this.registrationButton.addActionListener(this);
        this.registrationButton.setForeground(Color.WHITE);
        this.registrationButton.setBackground(Color.darkGray);
        this.registrationButton.setFocusable(false);

        this.registrationPanel = new JPanel();
        this.registrationPanel.setBounds(280, 0, 140, 210);
        this.registrationPanel.setBackground(Color.WHITE);
        this.registrationPanel.add(usernameRegField);
        this.registrationPanel.add(passwordRegField);
        this.registrationPanel.add(emailRegField);
        this.registrationPanel.add(registrationButton);


        this.setSize(420, 240);
        this.setResizable(false);
        this.setTitle("Chat client login");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.getContentPane().setBackground(Color.BLACK);
        this.setVisible(true);


        this.add(this.loginPanel);
        this.add(this.registrationPanel);
    }


    /**
     * 3 options of user choice:
     * loginButton:
     * 1 - check if text in fields is not default.
     * 2 - set username to clientHandler by SetClientHandlerUsername command (shortname "u").
     * 3 - check if login and password are correct by Password command (shortname "p").
     * <p>
     * registrationButton:
     * 1 - check if text in fields is not default.
     * 2 - append username, password and email to one string with "|" symbol.
     * 3 - send AddUser command (shortname "c") to clientHandler
     * <p>
     * emailRecoverButton:
     * 1 - check if text in fields is not default.
     * 2 - send Email command (shortname "e") for password recovery via gmail
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            if (!this.usernameLogField.getText().equals("username") && !this.passwordLogField.getText().equals("password")) {
                this.client.setUsername(this.usernameLogField.getText());
                this.client.sendMessageForm("p:" + this.passwordLogField.getText());
            }

        } else if (e.getSource() == registrationButton) {
            if (!this.usernameRegField.getText().equals("username") &
                    !this.passwordRegField.getText().equals("password") &
                    !this.emailRegField.getText().equals("email")) {
                String reg = this.usernameRegField.getText() + "|" +
                        this.passwordRegField.getText() + "|" +
                        this.emailRegField.getText();
                this.client.sendMessageForm("c:" + reg);
            }

        } else if (e.getSource() == emailRecoverButton) {
            if (!this.usernameLogField.getText().equals("username") && !this.emailRecoverField.getText().equals("email")) {
                this.client.setUsername(this.usernameLogField.getText());
                this.client.sendMessageForm("e:" + this.emailRecoverField.getText());
            }
        }
    }
}
