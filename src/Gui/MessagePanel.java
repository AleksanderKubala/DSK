package Gui;

import javax.swing.*;

public class MessagePanel extends JPanel {

    private JTextArea messageArea;

    public MessagePanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(messageArea);
        add(scroll);
    }

    public void message(String text) {
        messageArea.append(text);
    }

    public void messageNewLine(String text) {
        message(text + "\n");
    }

}
