package Frames;

import Client.Client;
import Server.Handlers;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Main chat frame.
 * Created with java form.
 * Contains:
 *      inputPanel - text input area with send button.
 *      usersPanel - list of online users with option to send direct message when click on username).
 *      ChatPanel - area with chat message history.
 */
public class MainChatFrame extends JFrame {
    private final Handlers handlers;
    private JTextArea inputArea;
    private JPanel inputPanel;
    private JPanel usersPanel;
    private JPanel ChatPanel;
    private JButton sentButton;
    private JTextPane chatOutputFrame;
    private JButton uplOnline;
    private JPanel ChatMainPane;
    private JList<String> usersList;

    private final DefaultListModel<String> model;
    private final Client client;


    /**
     * Receive client.
     * Create 'Handlers' instance (list of all connected clientHandlers) and model for online users list.
     * @param c client
     */
    public MainChatFrame(Client c) {
        this.client = c;
        this.handlers = Handlers.getInstance();
        this.model = new DefaultListModel<>();
        this.usersList.setModel(this.model);


        this.setSize(600, 800);
        this.setResizable(false);
        this.setTitle("Chat client on-line");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.getContentPane().setBackground(Color.BLACK);
        this.setContentPane(this.ChatMainPane);


        /**
         * Send message to the client, and clear inputArea
         */
        sentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.sendMessageForm(inputArea.getText());
                inputArea.setText("");
            }
        });


        /**
         * Refresh online users list by sending ShowAllUsers command ("a:1").
         */
        uplOnline.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Main frame: required user list update");
                client.sendMessageForm("a:1");
            }
        });

        /**
         * Set start of direct message command ("d:username@") when click on username from list of online users
         */
        usersList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                inputArea.setText("d:"+ usersList.getSelectedValue() + "@");
            }
        });
    }


    /**
     * Receive online users set, and put it to the model.
     * @param users set of online users
     */
    public void refreshOnlineUsers(String[] users) {
        model.clear();

        for (int i = 0; i < users.length; i++) {
            model.addElement(users[i]);
        }
        usersList.setModel(model);
    }

    public void addOutputLine(String msg) {
        chatOutputFrame.setText(chatOutputFrame.getText() + "\n" + msg);
    }
}
