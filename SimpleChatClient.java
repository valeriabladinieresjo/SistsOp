package hfj.connection15.chat;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import sun.audio.*;
import java.awt.event.*;
import java.util.ArrayList;


public class SimpleChatClient
{
    JTextArea incoming;
    JTextArea nombresclientes;
    JTextField outgoing;
    BufferedReader reader;
    PrintWriter writer;
    Socket sock;
    InetAddress ip;
    boolean imp=true;
    String nick;
    JFrame frame = new JFrame("Ludicrously Simple Chat Client");
    ArrayList<String> nombresarray = new ArrayList<String>();
    JButton sendButton4 = new JButton("Startup");
    JButton sendButton = new JButton("Send");
    JButton sendButton2 = new JButton("Borrar");
    JButton sendButton3 = new JButton("Desconectar");
    JPanel mainPanel = new JPanel();
        
    public void go()
    {     
        incoming = new JTextArea(15, 50);
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(false);
        nombresclientes = new JTextArea(5,15);
        nombresclientes.setLineWrap(true);
        nombresclientes.setWrapStyleWord(true);
        nombresclientes.setEditable(false);
        JScrollPane qScroller = new JScrollPane(incoming);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        outgoing = new JTextField(20);
        

        sendButton4.addActionListener(new SendButtonListener4());
        sendButton3.addActionListener(new SendButtonListener3());
        sendButton2.addActionListener(new SendButtonListener2());
        sendButton.addActionListener(new SendButtonListener());
        
        mainPanel.add(qScroller);
        mainPanel.add(sendButton4);

        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        nick = JOptionPane.showInputDialog("Nickname");  

        frame.setSize(650, 500);
        frame.setVisible(true);

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

    public class SendButtonListener3 implements ActionListener
    {
        public void actionPerformed(ActionEvent ev) {
            try {
                for ( int ini = 0;  ini < nombresarray.size(); ini++){
                    String tempName = nombresarray.get(ini);
                    if(tempName.equals(nick)){
                    nombresarray.remove(ini);
                    }
                }
                nombresclientes.setText("");
                for(String nom : nombresarray){
                    nombresclientes.append(nom + "\n");
                }
                System.exit(0);
            }
            
            catch (Exception ex) {
                ex.printStackTrace();
            }  
        }                   
        
    }

    public class SendButtonListener4 implements ActionListener
    {
        public void actionPerformed(ActionEvent ev) {
            try {
                mainPanel.add(outgoing);
                mainPanel.add(sendButton);
                mainPanel.add(sendButton2);
                mainPanel.add(sendButton3);
                mainPanel.add(nombresclientes);
                sock = new Socket("192.168.1.70", 5000);
                InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(streamReader);
                writer = new PrintWriter(sock.getOutputStream());
                System.out.println("networking established");
                nombresarray.add(nick);
                for(String nom : nombresarray){
                    nombresclientes.append(nom + "\n");
                }
                Thread readerThread = new Thread(new IncomingReader());
                readerThread.start();
                sendButton4.setVisible(false);
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
