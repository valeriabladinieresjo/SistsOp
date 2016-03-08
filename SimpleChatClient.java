package hfj.connection15.chat;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import sun.audio.*;
import java.awt.event.*;



public class SimpleChatClient
{
    JTextArea incoming;
    JTextField outgoing;
    BufferedReader reader;
    PrintWriter writer;
    Socket sock;
    InetAddress ip;
    boolean imp=true;
    String nick;
    JFrame frame = new JFrame("Ludicrously Simple Chat Client");

    public void go()
    {     
        JPanel mainPanel = new JPanel();
        incoming = new JTextArea(15, 50);
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(false);
        JScrollPane qScroller = new JScrollPane(incoming);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        outgoing = new JTextField(20);
        JButton sendButton = new JButton("Send");
        JButton sendButton2 = new JButton("Borrar");
        sendButton2.addActionListener(new SendButtonListener2());
        sendButton.addActionListener(new SendButtonListener());
        mainPanel.add(qScroller);
        mainPanel.add(outgoing);
        mainPanel.add(sendButton);
        mainPanel.add(sendButton2);
        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        setUpNetworking();

        Thread readerThread = new Thread(new IncomingReader());
        readerThread.start();

        frame.setSize(650, 500);
        frame.setVisible(true);

    }

    private void setUpNetworking()
    {
        try {
            nick = JOptionPane.showInputDialog("Nickname");  
            sock = new Socket("140.148.242.200", 5000);
            InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
            reader = new BufferedReader(streamReader);
            writer = new PrintWriter(sock.getOutputStream());
            System.out.println("networking established");
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public class SendButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent ev) {
            try {
                /*ip = InetAddress.getLocalHost();
                NetworkInterface network = NetworkInterface.getByInetAddress(ip);
                byte[] mac = network.getHardwareAddress();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < mac.length; i++) {
                    sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));        
                }
                if("78-CA-39-B8-8E-3A".equals(sb.toString())){
                    imp= false;
                }*/
                writer.println(nick + ": " + outgoing.getText());
                writer.flush();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            outgoing.setText("");
            outgoing.requestFocus();
        }
    }

    public class SendButtonListener2 implements ActionListener
    {
        public void actionPerformed(ActionEvent ev) {
            try {
               incoming.setText(null);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }                     
        }
    }

    public static void main(String[] args)
    {
        new SimpleChatClient().go();
    }

    class IncomingReader implements Runnable
    {
        public void run()
        {
            String message;
            try {
                while ((message = reader.readLine()) != null)
                {
                    Toolkit.getDefaultToolkit().beep();
                    System.out.println("client read " + message);
                    //if(imp){
                        incoming.append(message + "\n");
                    //}
                    //imp=true;
                }
            } 
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }
}
