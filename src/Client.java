import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client extends JFrame {
    private String userName;
    private JTextField sendText;
    private JTextArea receivedText;
    private JButton sendButton;
    private PrintWriter writer;
    private Scanner reader;

    public Client (String name) {
        super(name + "'s Chat");
        this.userName = name;
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setBounds(100, 100, 400, 400);

        startComponents();
        startConnection();
        this.setVisible(true);
    }

    private void startComponents() {
        JPanel sendPanel = new JPanel(new BorderLayout());
        sendText = new JTextField();
        receivedText = new JTextArea();
        sendText.addActionListener(new SendMessage());
        sendPanel.add(sendText, BorderLayout.PAGE_END);
        sendButton = new JButton(">");
        sendButton.addActionListener(new SendMessage());
        sendPanel.add(sendButton, BorderLayout.LINE_END);
        sendPanel.add(receivedText, BorderLayout.CENTER);
        this.add(sendPanel, BorderLayout.CENTER);
    }

    private void startConnection() {
        try {
            Socket socket = new Socket("localhost", 6789);
            writer = new PrintWriter(socket.getOutputStream());
            reader = new Scanner(socket.getInputStream());
            new Thread(new GetServer()).start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class GetServer implements Runnable {

        @Override
        public void run() {
            String text1;
            while ((text1 = reader.nextLine()) != null) {
                receivedText.append(text1+"\n");
            }
        }
    }

    private class SendMessage implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            writer.println(userName+": "+sendText.getText());
            writer.flush();
            sendText.setText("");
            sendText.setRequestFocusEnabled(true);
        }
    }

    public static void main(String args[]) {
        new Client(JOptionPane.showInputDialog("Digite seu nome:"));
    }

}
