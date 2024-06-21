package src;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;

public class chat implements ActionListener, Runnable, KeyListener {
    private JFrame frame, MAIN;
    private DataOutputStream DO;
    private URL url_icon = getClass().getResource("/Images/chat_icon.png");
    private ImageIcon icon = new ImageIcon(url_icon);
    private DataInputStream DI;
    private Socket S;
    public String NAME, ID, CHAT_CODE, CHAT_NAME;
    private JLabel chatname, send_mes_la;
    private JTextField mes_type;
    private JButton back, send_mes;
    private JTextArea Mes_pane;
    private JLabel head_chat, main_la;
    private Border chat_border = BorderFactory.createTitledBorder("Chat"),
            onl_border = BorderFactory.createLineBorder(Color.BLACK, 1);
    private SimpleAttributeSet attributeSet_online;
    private JTextPane online_Pane;
    private Document doc_onl;
    private JScrollPane mes_cs, online_sc;
    private boolean check = true;
    private ArrayList<String> online;
    Thread t;

    public chat(JFrame main_menu, Socket s, String name_chat, String chat_code, String name,
            DataOutputStream Do, DataInputStream Di) {
        this.S = s;
        this.DO = Do;
        this.DI = Di;
        this.MAIN = main_menu;
        this.NAME = name;
        // this.ID = id;
        this.CHAT_CODE = chat_code;
        this.CHAT_NAME = name_chat;
        this.check = true;

        this.back = new JButton("Back");
        this.back.setFocusable(false);
        this.back.addActionListener(this);

        this.chatname = new JLabel(this.CHAT_NAME, SwingConstants.CENTER);
        this.chatname.setBackground(Color.white);
        this.chatname.setOpaque(true);
        this.chatname.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));

        this.head_chat = new JLabel();
        this.head_chat.setLayout(new BorderLayout());
        this.head_chat.add(this.back, BorderLayout.WEST);
        this.head_chat.add(this.chatname, BorderLayout.CENTER);
        this.head_chat.setPreferredSize(new Dimension(100, 30));

        this.Mes_pane = new JTextArea();
        this.Mes_pane.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        this.Mes_pane.setEditable(false);
        this.Mes_pane.setLineWrap(true);
        this.Mes_pane.setWrapStyleWord(true);
        this.Mes_pane.setBackground(Color.WHITE);
        this.Mes_pane.setOpaque(true);

        this.mes_cs = new JScrollPane(Mes_pane);
        this.mes_cs.setBorder(this.chat_border);
        this.mes_cs.setBackground(Color.white);
        this.mes_cs.setOpaque(true);

        this.online_Pane = new JTextPane();
        this.online_Pane.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        this.online_Pane.setEditable(false);
        this.doc_onl = this.online_Pane.getStyledDocument();
        this.attributeSet_online = new SimpleAttributeSet();
        this.online_Pane.setCharacterAttributes(attributeSet_online, true);
        this.online_Pane.setPreferredSize(new Dimension(250, 0));
        this.online_Pane.setBackground(Color.LIGHT_GRAY);
        this.online_Pane.setOpaque(true);

        this.online_sc = new JScrollPane(this.online_Pane);
        this.online_sc.setBorder(this.onl_border);
        this.online_sc.setBackground(Color.LIGHT_GRAY);
        this.online_sc.setOpaque(true);

        this.mes_type = new JTextField();
        this.mes_type.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        this.mes_type.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        this.mes_type.addKeyListener(this);

        this.send_mes = new JButton("Send");
        this.send_mes.setPreferredSize(new Dimension(100, 30));
        this.send_mes.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        this.send_mes.setFocusable(false);
        this.send_mes.addActionListener(this);
        this.send_mes.addKeyListener(this);

        this.send_mes_la = new JLabel();
        this.send_mes_la.setLayout(new BorderLayout());
        this.send_mes_la.setBackground(Color.white);
        this.send_mes_la.setOpaque(true);
        this.send_mes_la.setPreferredSize(new Dimension(100, 30));
        this.send_mes_la.add(this.mes_type, BorderLayout.CENTER);
        this.send_mes_la.add(this.send_mes, BorderLayout.EAST);

        this.main_la = new JLabel();
        this.main_la.setLayout(new BorderLayout());
        this.main_la.add(this.mes_cs, BorderLayout.CENTER);
        this.main_la.add(this.head_chat, BorderLayout.NORTH);
        this.main_la.add(this.online_sc, BorderLayout.EAST);
        this.main_la.add(this.send_mes_la, BorderLayout.SOUTH);
        this.main_la.setBackground(Color.WHITE);
        this.main_la.setOpaque(true);

        this.frame = new JFrame("Chat " + CHAT_NAME);
        this.frame.setSize(1000, 650);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);
        this.frame.setResizable(false);
        this.frame.setIconImage(this.icon.getImage());
        this.frame.setContentPane(this.main_la);
        this.frame.addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    DO = new DataOutputStream(S.getOutputStream());
                    DO.writeUTF("::OUTCHAT::");
                    DO = new DataOutputStream(S.getOutputStream());
                    DO.writeUTF("::close::");
                    check = false;
                    S.close();
                    System.exit(0);
                    DO.close();
                    DI.close();

                } catch (Exception Ex) {
                    Ex.printStackTrace();
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }

        });
    }

    public void setThread(Thread T) {
        this.t = T;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.back) {
            try {
                this.DO = new DataOutputStream(this.S.getOutputStream());
                this.DO.writeUTF("::OUTCHAT::");
                this.DO.flush();
            } catch (Exception E) {
                // E.printStackTrace();
                JOptionPane.showMessageDialog(null, "Cannot connect to server!", "Alert!",
                        JOptionPane.ERROR_MESSAGE);
                this.check = false;
            }
        } else if (e.getSource() == this.send_mes) {
            try {
                if (mes_type.getText().length() != 0) {
                    DO = new DataOutputStream(S.getOutputStream());
                    DO.writeUTF("SENDMES:" + mes_type.getText());
                    DO.flush();
                    mes_type.setText("");
                } else {

                }
            } catch (Exception E) {
                JOptionPane.showMessageDialog(null, "Cannot connect to server!", "Alert!",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public int split(String submes) {
        int count = 0;
        while (count != submes.length()) {
            if (submes.charAt(count) == ' ') {
                break;
            }
            count++;
        }
        return count;
    }

    public void run() {
        try {
            this.DO = new DataOutputStream(this.S.getOutputStream());
            this.DO.writeUTF("getHis:" + CHAT_CODE);
            this.DO.flush();
            ArrayList<String> chat = null;
            ObjectInputStream his = new ObjectInputStream(this.S.getInputStream());
            chat = (ArrayList<String>) his.readObject();
            if (chat != null) {
                for (String CHAT : chat) {
                    String mes = CHAT;
                    if (mes.startsWith(this.NAME)) {
                        String submes = mes.substring(this.NAME.length() + 2, mes.length());
                        String mes_appear = "You: " + submes;
                        Mes_pane.append(mes_appear);
                        Mes_pane.append("\n");
                        Mes_pane.append("-----------------------------");
                        Mes_pane.append("\n");
                        Mes_pane.append("\n");
                        this.Mes_pane.setCaretPosition(this.Mes_pane.getDocument().getLength());
                    } else {
                        Mes_pane.append(mes);
                        Mes_pane.append("\n");
                        Mes_pane.append("-----------------------------");
                        Mes_pane.append("\n");
                        Mes_pane.append("\n");
                        this.Mes_pane.setCaretPosition(this.Mes_pane.getDocument().getLength());
                    }
                }

            } else {
                Mes_pane.append("Welcome to the chat!");
                Mes_pane.append("\n");
                Mes_pane.append("-----------------------------");
                Mes_pane.append("\n");
                Mes_pane.append("\n");
            }

            this.DO = new DataOutputStream(this.S.getOutputStream());
            this.DO.writeUTF("getOnl:" + CHAT_CODE);
            this.DO.flush();

            ObjectInputStream Online = new ObjectInputStream(this.S.getInputStream());
            Object client_onl = Online.readObject();

            if (client_onl != null) {
                this.online = (ArrayList<String>) client_onl;
                this.online_Pane.setText("");
                this.online_Pane.setText("");
                this.doc_onl = this.online_Pane.getStyledDocument();
                this.attributeSet_online = new SimpleAttributeSet();
                this.online_Pane.setCharacterAttributes(attributeSet_online, true);
                this.attributeSet_online = new SimpleAttributeSet();
                this.doc_onl.insertString(doc_onl.getLength(), "ID: " + this.CHAT_CODE,
                        attributeSet_online);
                this.attributeSet_online = new SimpleAttributeSet();
                this.doc_onl.insertString(doc_onl.getLength(), "\n", attributeSet_online);
                for (String onl : this.online) {
                    this.doc_onl = this.online_Pane.getStyledDocument();
                    this.attributeSet_online = new SimpleAttributeSet();
                    this.online_Pane.setCharacterAttributes(attributeSet_online, true);
                    this.attributeSet_online = new SimpleAttributeSet();
                    this.doc_onl.insertString(doc_onl.getLength(), onl, attributeSet_online);
                    this.attributeSet_online = new SimpleAttributeSet();
                    this.doc_onl.insertString(doc_onl.getLength(), "\n", attributeSet_online);
                }
            }
            this.DO = new DataOutputStream(this.S.getOutputStream());
            this.DO.writeUTF("startChat:" + CHAT_CODE);
            while (this.check != false) {
                this.DI = new DataInputStream(this.S.getInputStream());
                String mes = this.DI.readUTF();
                if (mes.startsWith("ONLINE")) {
                    String online_cl = mes.substring(7, mes.length());
                    if (this.online.contains(online_cl) != true) {
                        this.online.add(online_cl);
                        // try {
                        this.online_Pane.setText("");
                        this.online_Pane.setText("");
                        this.doc_onl = this.online_Pane.getStyledDocument();
                        this.attributeSet_online = new SimpleAttributeSet();
                        this.online_Pane.setCharacterAttributes(attributeSet_online, true);
                        this.attributeSet_online = new SimpleAttributeSet();
                        this.doc_onl.insertString(doc_onl.getLength(), "ID: " + this.CHAT_CODE,
                                attributeSet_online);
                        this.attributeSet_online = new SimpleAttributeSet();
                        this.doc_onl.insertString(doc_onl.getLength(), "\n", attributeSet_online);
                        for (String onl_cl : this.online) {
                            this.doc_onl = this.online_Pane.getStyledDocument();
                            this.attributeSet_online = new SimpleAttributeSet();
                            this.online_Pane.setCharacterAttributes(attributeSet_online, true);
                            this.attributeSet_online = new SimpleAttributeSet();
                            this.doc_onl.insertString(doc_onl.getLength(), onl_cl, attributeSet_online);
                            this.attributeSet_online = new SimpleAttributeSet();
                            this.doc_onl.insertString(doc_onl.getLength(), "\n", attributeSet_online);
                        }
                    }
                } else if (mes.startsWith("OFFLINE")) {
                    String off_cl = mes.substring(8, mes.length());
                    System.out.println(off_cl);
                    if (!this.NAME.equals(off_cl)) {
                        if (this.online.contains(off_cl)) {
                            this.online.remove(off_cl);
                            // try {
                            this.online_Pane.setText("");
                            this.doc_onl = this.online_Pane.getStyledDocument();
                            this.attributeSet_online = new SimpleAttributeSet();
                            this.online_Pane.setCharacterAttributes(attributeSet_online, true);
                            this.attributeSet_online = new SimpleAttributeSet();
                            this.doc_onl.insertString(doc_onl.getLength(), "ID: " + this.CHAT_CODE,
                                    attributeSet_online);
                            this.attributeSet_online = new SimpleAttributeSet();
                            this.doc_onl.insertString(doc_onl.getLength(), "\n", attributeSet_online);
                            for (String onl_cl : this.online) {
                                this.doc_onl = this.online_Pane.getStyledDocument();
                                this.attributeSet_online = new SimpleAttributeSet();
                                this.online_Pane.setCharacterAttributes(attributeSet_online, true);
                                this.attributeSet_online = new SimpleAttributeSet();
                                this.doc_onl.insertString(doc_onl.getLength(), onl_cl, attributeSet_online);
                                this.attributeSet_online = new SimpleAttributeSet();
                                this.doc_onl.insertString(doc_onl.getLength(), "\n", attributeSet_online);
                            }
                        }
                    } else {
                        System.out.println("Exit");
                        this.check = false;
                        this.online = null;
                        this.frame.dispose();
                        this.MAIN.setVisible(true);
                        break;
                    }
                } else if (mes.startsWith(this.NAME)) {
                    String submes = mes.substring(this.NAME.length(), mes.length());
                    String appearmes = "You" + submes;
                    Mes_pane.append(appearmes);
                    Mes_pane.append("\n");
                    Mes_pane.append("-----------------------------");
                    Mes_pane.append("\n");
                    Mes_pane.append("\n");
                    this.Mes_pane.setCaretPosition(this.Mes_pane.getDocument().getLength());
                } else if (mes.startsWith(this.NAME) != true) {
                    Mes_pane.append(mes);
                    Mes_pane.append("\n");
                    Mes_pane.append("-----------------------------");
                    Mes_pane.append("\n");
                    Mes_pane.append("\n");
                    this.Mes_pane.setCaretPosition(this.Mes_pane.getDocument().getLength());
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Cannot connect to server!", "Alert!",
                    JOptionPane.ERROR_MESSAGE);
            this.check = false;
            e.printStackTrace();
            this.frame.dispose();
            System.exit(0);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                if (mes_type.getText().length() != 0) {
                    DO = new DataOutputStream(S.getOutputStream());
                    DO.writeUTF("SENDMES:" + mes_type.getText());
                    DO.flush();
                    mes_type.setText("");
                } else {

                }
            } catch (Exception E) {
                JOptionPane.showMessageDialog(null, "Cannot connect to server!", "Alert!",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                if (mes_type.getText().length() != 0) {
                    DO = new DataOutputStream(S.getOutputStream());
                    DO.writeUTF("SENDMES:" + mes_type.getText());
                    DO.flush();
                    mes_type.setText("");
                } else {

                }
            } catch (Exception E) {
                JOptionPane.showMessageDialog(null, "Cannot connect to server!", "Alert!",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
