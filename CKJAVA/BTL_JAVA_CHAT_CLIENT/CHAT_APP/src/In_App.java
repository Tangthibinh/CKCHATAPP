package src;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class In_App implements ActionListener {
    Socket s;
    JFrame frame;
    JLabel mes_list, op_list, mes_la, op_la, head, new_chat_la, op_la_left, new_chat_la_center, frame_la, head_new_la,
            head_op_la, right_change_US, right_change_pass;
    JButton option, mes, new_chat, search_chat, back_but_new, back_but_op, join_chat_but, make_new_but;
    JButton changePass, changeUsname, Logout, ok_ch_us, ok_ch_pass;
    String Name, EMAIL;
    URL url_icon = getClass().getResource("/Images/chat_icon.png");
    ImageIcon icon = new ImageIcon(url_icon),
            image = new ImageIcon(getClass().getResource("/Images/chat_icon.png"));
    DataOutputStream op;
    DataInputStream ip;
    JScrollPane mes_sc;
    JPanel mes_pane;
    JTextField search, ChangUSname;
    JPasswordField inpPass_changeUS, inpPass_changePass, changPass, rechangePass;
    JTextArea in_pas_us, ch_us, in_pas_pas, ch_pas, re_ch_pass, tt_ch_us, tt_ch_pass;
    JCheckBox show_pas_us, show_pas_pas;

    public In_App(Socket S, DataOutputStream OP, DataInputStream IP, String name, String email) {
        this.s = S;
        this.op = OP;
        this.ip = IP;
        this.Name = name;
        this.EMAIL = email;

        this.search = new JTextField();
        this.search.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));

        this.search_chat = new JButton("Search");
        this.search_chat.setFocusable(false);
        this.search_chat.setBackground(Color.BLACK);
        this.search_chat.setForeground(Color.WHITE);
        this.search_chat.setOpaque(true);
        this.search_chat.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        this.search_chat.addActionListener(this);

        this.head = new JLabel();
        this.head.setFont(new Font(Font.MONOSPACED, Font.BOLD, 25));
        this.head.setPreferredSize(new Dimension(100, 30));
        this.head.setLayout(new BorderLayout());
        this.head.add(this.search, BorderLayout.CENTER);
        this.head.add(this.search_chat, BorderLayout.EAST);

        this.mes = new JButton("Refresh");
        this.mes.setFocusable(false);
        this.mes.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        this.mes.setBackground(Color.black);
        this.mes.setForeground(Color.white);
        this.mes.setOpaque(true);
        this.mes.addActionListener(this);

        this.new_chat = new JButton("New");
        this.new_chat.setBackground(Color.black);
        this.new_chat.setForeground(Color.white);
        this.new_chat.setOpaque(true);
        this.new_chat.setFocusable(false);
        this.new_chat.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        this.new_chat.addActionListener(this);

        this.option = new JButton("Option");
        this.option.setBackground(Color.black);
        this.option.setForeground(Color.white);
        this.option.setOpaque(true);
        this.option.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        this.option.setFocusable(false);
        this.option.addActionListener(this);

        this.op_list = new JLabel();
        this.op_list.setLayout(new GridLayout(1, 3));
        this.op_list.add(this.mes);
        this.op_list.add(this.new_chat);
        this.op_list.add(this.option);
        this.op_list.setPreferredSize(new Dimension(0, 50));

        this.mes_list = new JLabel();
        this.mes_list.setLayout(null);
        this.mes_list.setBackground(Color.WHITE);
        this.mes_list.setPreferredSize(new Dimension(100, 0));
        this.mes_list.setOpaque(true);

        this.mes_pane = new JPanel(new BorderLayout());
        this.mes_pane.add(this.mes_list, BorderLayout.CENTER);
        this.mes_sc = new JScrollPane(this.mes_pane);

        new Thread(new getMes()).start();

        this.mes_la = new JLabel();
        this.mes_la.setLayout(new BorderLayout());
        this.mes_la.setVisible(true);
        this.mes_la.add(this.head, BorderLayout.NORTH);
        this.mes_la.add(this.mes_sc, BorderLayout.CENTER);
        this.mes_la.add(this.op_list, BorderLayout.SOUTH);

        this.back_but_new = new JButton("Back");
        this.back_but_new.setFocusable(false);
        this.back_but_new.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        this.back_but_new.addActionListener(this);

        this.head_new_la = new JLabel("New chat", SwingConstants.CENTER);
        this.head_new_la.setLayout(new BorderLayout());
        this.head_new_la.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        this.head_new_la.add(this.back_but_new, BorderLayout.WEST);
        this.head_new_la.setPreferredSize(new Dimension(30, 30));

        this.make_new_but = new JButton("Make new chat");
        this.make_new_but.setFocusable(false);
        this.make_new_but.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        this.make_new_but.setBounds(40, 250, 550, 30);
        this.make_new_but.addActionListener(this);

        this.join_chat_but = new JButton("Join chat");
        this.join_chat_but.setFocusable(false);
        this.join_chat_but.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        this.join_chat_but.setBounds(40, 310, 550, 30);
        this.join_chat_but.addActionListener(this);

        this.new_chat_la_center = new JLabel();
        this.new_chat_la_center.setVisible(true);
        this.new_chat_la_center.setLayout(null);
        this.new_chat_la_center.add(this.make_new_but);
        this.new_chat_la_center.add(this.join_chat_but);

        this.new_chat_la = new JLabel();
        this.new_chat_la.setVisible(false);
        this.new_chat_la.setBackground(Color.white);
        this.new_chat_la.setOpaque(true);
        this.new_chat_la.setLayout(new BorderLayout());
        this.new_chat_la.add(this.head_new_la, BorderLayout.NORTH);
        this.new_chat_la.add(this.new_chat_la_center, BorderLayout.CENTER);

        this.back_but_op = new JButton("Back");
        this.back_but_op.setFocusable(false);
        this.back_but_op.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        this.back_but_op.addActionListener(this);

        this.ok_ch_us = new JButton("Change");
        this.ok_ch_us.setFocusable(false);
        this.ok_ch_us.setBounds(125, 370, 130, 30);
        this.ok_ch_us.setBackground(Color.BLACK);
        this.ok_ch_us.setForeground(Color.WHITE);
        this.ok_ch_us.setOpaque(true);
        this.ok_ch_us.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        this.ok_ch_us.addActionListener(this);

        this.tt_ch_us = new JTextArea("Change username");
        this.tt_ch_us.setFont(new Font(Font.MONOSPACED, Font.BOLD, 35));
        this.tt_ch_us.setBackground(Color.WHITE);
        this.tt_ch_us.setEditable(false);
        this.tt_ch_us.setBounds(30, 110, 350, 45);

        this.in_pas_us = new JTextArea("Enter password");
        this.in_pas_us.setEditable(false);
        this.in_pas_us.setBounds(30, 180, 220, 30);
        this.in_pas_us.setFont(new Font(Font.MONOSPACED, Font.BOLD, 22));
        this.in_pas_us.setBackground(Color.WHITE);
        this.in_pas_us.setFocusable(false);

        this.show_pas_us = new JCheckBox("Show password");
        this.show_pas_us.setBounds(30, 245, 250, 20);
        this.show_pas_us.setFont(new Font(Font.MONOSPACED, Font.BOLD, 17));
        this.show_pas_us.setBackground(Color.white);

        this.ch_us = new JTextArea("New username");
        this.ch_us.setEditable(false);
        this.ch_us.setBounds(30, 270, 220, 30);
        this.ch_us.setFont(new Font(Font.MONOSPACED, Font.BOLD, 22));
        this.ch_us.setBackground(Color.WHITE);
        this.ch_us.setFocusable(false);

        this.inpPass_changeUS = new JPasswordField();
        this.inpPass_changeUS.setBounds(30, 210, 330, 30);
        this.inpPass_changeUS.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
        this.inpPass_changeUS.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        this.inpPass_changeUS.setEchoChar('*');
        this.show_pas_us.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (show_pas_us.isSelected()) {
                    inpPass_changeUS.setEchoChar((char) 0);
                } else {
                    inpPass_changeUS.setEchoChar('*');
                }
            }

        });

        this.ChangUSname = new JTextField();
        this.ChangUSname.setBounds(30, 300, 330, 30);
        this.ChangUSname.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
        this.ChangUSname.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));

        this.right_change_US = new JLabel();
        this.right_change_US.setLayout(null);
        this.right_change_US.setPreferredSize(new Dimension(384, 0));
        this.right_change_US.add(this.inpPass_changeUS);
        this.right_change_US.add(this.ChangUSname);
        this.right_change_US.add(this.in_pas_us);
        this.right_change_US.add(this.ch_us);
        this.right_change_US.add(this.show_pas_us);
        this.right_change_US.add(this.tt_ch_us);
        this.right_change_US.add(this.ok_ch_us);
        this.right_change_US.setBackground(Color.WHITE);
        this.right_change_US.setOpaque(true);
        this.right_change_US.setVisible(false);

        this.ok_ch_pass = new JButton("Change");
        this.ok_ch_pass.setFocusable(false);
        this.ok_ch_pass.setBounds(125, 430, 130, 30);
        this.ok_ch_pass.setBackground(Color.BLACK);
        this.ok_ch_pass.setForeground(Color.WHITE);
        this.ok_ch_pass.setOpaque(true);
        this.ok_ch_pass.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        this.ok_ch_pass.addActionListener(this);

        this.tt_ch_pass = new JTextArea("Change password");
        this.tt_ch_pass.setFont(new Font(Font.MONOSPACED, Font.BOLD, 35));
        this.tt_ch_pass.setBackground(Color.WHITE);
        this.tt_ch_pass.setEditable(false);
        this.tt_ch_pass.setBounds(30, 110, 350, 45);

        this.in_pas_pas = new JTextArea("Enter password");
        this.in_pas_pas.setEditable(false);
        this.in_pas_pas.setBounds(30, 180, 220, 30);
        this.in_pas_pas.setFont(new Font(Font.MONOSPACED, Font.BOLD, 22));
        this.in_pas_pas.setBackground(Color.WHITE);
        this.in_pas_pas.setFocusable(false);

        this.ch_pas = new JTextArea("New password");
        this.ch_pas.setEditable(false);
        this.ch_pas.setBounds(30, 250, 220, 30);
        this.ch_pas.setFont(new Font(Font.MONOSPACED, Font.BOLD, 22));
        this.ch_pas.setBackground(Color.WHITE);
        this.ch_pas.setFocusable(false);

        this.re_ch_pass = new JTextArea("Re-enter new password");
        this.re_ch_pass.setEditable(false);
        this.re_ch_pass.setBounds(30, 320, 350, 30);
        this.re_ch_pass.setFont(new Font(Font.MONOSPACED, Font.BOLD, 22));
        this.re_ch_pass.setBackground(Color.WHITE);
        this.re_ch_pass.setFocusable(false);

        this.inpPass_changePass = new JPasswordField();
        this.inpPass_changePass.setBounds(30, 210, 330, 30);
        this.inpPass_changePass.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
        this.inpPass_changePass.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        this.inpPass_changePass.setEchoChar('*');

        this.changPass = new JPasswordField();
        this.changPass.setBounds(30, 280, 330, 30);
        this.changPass.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
        this.changPass.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        this.changPass.setEchoChar('*');

        this.rechangePass = new JPasswordField();
        this.rechangePass.setBounds(30, 350, 330, 30);
        this.rechangePass.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
        this.rechangePass.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        this.rechangePass.setEchoChar('*');

        this.show_pas_pas = new JCheckBox("Show password");
        this.show_pas_pas.setBounds(30, 390, 250, 20);
        this.show_pas_pas.setFont(new Font(Font.MONOSPACED, Font.BOLD, 17));
        this.show_pas_pas.setBackground(Color.white);
        this.show_pas_pas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (show_pas_pas.isSelected()) {
                    inpPass_changePass.setEchoChar((char) 0);
                    changPass.setEchoChar((char) 0);
                    rechangePass.setEchoChar((char) 0);
                } else {
                    inpPass_changePass.setEchoChar('*');
                    changPass.setEchoChar('*');
                    rechangePass.setEchoChar('*');
                }
            }

        });

        this.right_change_pass = new JLabel();
        this.right_change_pass.setLayout(null);
        this.right_change_pass.setPreferredSize(new Dimension(384, 0));
        this.right_change_pass.setBackground(Color.WHITE);
        this.right_change_pass.setOpaque(true);
        this.right_change_pass.add(this.tt_ch_pass);
        this.right_change_pass.add(this.ok_ch_pass);
        this.right_change_pass.add(this.in_pas_pas);
        this.right_change_pass.add(this.ch_pas);
        this.right_change_pass.add(this.re_ch_pass);
        this.right_change_pass.add(this.inpPass_changePass);
        this.right_change_pass.add(this.changPass);
        this.right_change_pass.add(this.rechangePass);
        this.right_change_pass.add(this.show_pas_pas);
        this.right_change_pass.setVisible(false);

        this.head_op_la = new JLabel("Option", SwingConstants.CENTER);
        this.head_op_la.setLayout(new BorderLayout());
        this.head_op_la.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        this.head_op_la.add(this.back_but_op, BorderLayout.WEST);
        this.head_op_la.setPreferredSize(new Dimension(30, 30));

        this.changeUsname = new JButton("Change username");
        this.changeUsname.setFocusable(false);
        this.changeUsname.setBounds(0, 0, 250, 50);
        this.changeUsname.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        this.changeUsname.addActionListener(this);

        this.changePass = new JButton("Change password");
        this.changePass.setFocusable(false);
        this.changePass.setBounds(0, 50, 250, 50);
        this.changePass.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        this.changePass.addActionListener(this);

        this.Logout = new JButton("Log out");
        this.Logout.setFocusable(false);
        this.Logout.setBounds(0, 100, 250, 50);
        this.Logout.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        this.Logout.setBackground(Color.RED);
        this.Logout.setForeground(Color.BLACK);
        this.Logout.setOpaque(true);
        this.Logout.addActionListener(this);

        this.op_la_left = new JLabel();
        this.op_la_left.setLayout(null);
        this.op_la_left.setPreferredSize(new Dimension(250, 100));
        this.op_la_left.add(this.changeUsname);
        this.op_la_left.add(this.changePass);
        this.op_la_left.add(this.Logout);

        this.op_la = new JLabel();
        this.op_la.setLayout(new BorderLayout());
        this.op_la.add(this.head_op_la, BorderLayout.NORTH);
        this.op_la.add(this.op_la_left, BorderLayout.WEST);
        this.op_la.setVisible(false);

        this.frame_la = new JLabel();
        this.frame_la.setLayout(new BorderLayout());
        this.frame_la.add(this.mes_la, BorderLayout.CENTER);

        this.frame = new JFrame(Name);
        this.frame.setSize(650, 700);
        this.frame.setVisible(true);
        this.frame.setLocationRelativeTo(null);
        this.frame.setContentPane(this.frame_la);
        this.frame.setResizable(false);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setIconImage(this.icon.getImage());
        this.frame.addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    op = new DataOutputStream(s.getOutputStream());
                    op.writeUTF("::close::");
                    s.close();
                    System.exit(0);
                    op.close();
                    ip.close();
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

    class getMes implements Runnable {

        public void run() {
            // while (true) {
            try {
                search.setText("");
                op = new DataOutputStream(s.getOutputStream());
                op.writeUTF("getMes");
                Map<String, String> chat_list = null;
                ObjectInputStream get_chat_list = new ObjectInputStream(s.getInputStream());
                Object list_chat = get_chat_list.readObject();
                if (list_chat == null) {
                    if (mes_list.getComponentCount() == 0) {
                        System.out.println("null");
                    } else {
                        mes_list.setVisible(false);
                        mes_list.removeAll();
                        mes_list.setPreferredSize(new Dimension(100, 0));
                        mes_list.setVisible(true);
                    }
                } else {
                    if (mes_list.getComponentCount() == 0) {
                        mes_list.setVisible(false);
                        chat_list = (Map<String, String>) list_chat;
                        Set<String> ID_chat = chat_list.keySet();
                        int position = 0;
                        int high = 80;
                        for (String getID : ID_chat) {
                            String chat_name = chat_list.get(getID);
                            JLabel chat = new JLabel();
                            chat.setText("   " + chat_name);
                            chat.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
                            chat.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                            chat.setBounds(0, position, 625, 70);
                            mes_list.setPreferredSize(new Dimension(100, high));
                            position = position + 75;
                            high = high + 80;
                            chat.addMouseListener(new MouseListener() {
                                @Override
                                public void mouseClicked(MouseEvent e) {
                                }

                                @Override
                                public void mousePressed(MouseEvent e) {
                                    frame.setVisible(false);
                                    Thread t = new Thread(new chat(frame, s, chat_name, getID, Name, op, ip));
                                    t.start();

                                }

                                @Override
                                public void mouseReleased(MouseEvent e) {
                                }

                                @Override
                                public void mouseEntered(MouseEvent e) {
                                }

                                @Override
                                public void mouseExited(MouseEvent e) {
                                }

                            });
                            mes_list.add(chat);
                        }

                        mes_list.setVisible(true);
                        JScrollBar vertical = mes_sc.getVerticalScrollBar();
                        vertical.setValue(vertical.getMinimum());
                    } else {
                        mes_list.setVisible(false);
                        mes_list.removeAll();
                        mes_list.setPreferredSize(new Dimension(100, 0));
                        chat_list = (Map<String, String>) list_chat;
                        Set<String> ID_chat = chat_list.keySet();
                        int position = 0;
                        int high = 80;
                        for (String getID : ID_chat) {
                            JLabel chat = new JLabel();
                            String get_Name = chat_list.get(getID);
                            chat.setText("   " + get_Name);
                            chat.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
                            chat.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                            chat.setBounds(0, position, 625, 70);
                            mes_list.setPreferredSize(new Dimension(100, high));
                            position = position + 75;
                            high = high + 80;
                            chat.addMouseListener(new MouseListener() {

                                @Override
                                public void mouseClicked(MouseEvent e) {
                                }

                                @Override
                                public void mousePressed(MouseEvent e) {
                                    frame.setVisible(false);
                                    new Thread(new chat(frame, s, get_Name, getID, Name, op, ip)).start();
                                }

                                @Override
                                public void mouseReleased(MouseEvent e) {
                                }

                                @Override
                                public void mouseEntered(MouseEvent e) {
                                }

                                @Override
                                public void mouseExited(MouseEvent e) {
                                }

                            });

                            mes_list.add(chat);
                        }
                        mes_list.setVisible(true);
                        JScrollBar vertical = mes_sc.getVerticalScrollBar();
                        vertical.setValue(vertical.getMinimum());
                    }
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Cannot connect to server!", "Alert!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.mes) {
            new Thread(new getMes()).start();
        } else if (e.getSource() == this.search_chat) {
            if (this.search.getText().length() != 0) {
                try {
                    this.op = new DataOutputStream(this.s.getOutputStream());
                    this.op.writeUTF("SEARCH:" + this.search.getText());
                    Map<String, String> chat_search_arr = null;
                    ObjectInputStream chat_search = new ObjectInputStream(this.s.getInputStream());
                    Object search = chat_search.readObject();
                    chat_search_arr = (Map<String, String>) search;
                    if (chat_search_arr != null) {
                        this.mes_list.setVisible(false);
                        this.mes_list.removeAll();
                        this.mes_list.setPreferredSize(new Dimension(100, 0));
                        int position = 0;
                        int high = 80;
                        Set<String> ID_chat = chat_search_arr.keySet();
                        for (String getID : ID_chat) {
                            JLabel chat = new JLabel();
                            String get_Name = chat_search_arr.get(getID);
                            chat.setText("   " + get_Name);
                            chat.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
                            chat.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                            chat.setBounds(0, position, 625, 70);
                            mes_list.setPreferredSize(new Dimension(100, high));
                            position = position + 75;
                            high = high + 80;
                            chat.addMouseListener(new MouseListener() {
                                @Override
                                public void mouseClicked(MouseEvent e) {
                                }

                                @Override
                                public void mousePressed(MouseEvent e) {
                                    frame.setVisible(false);
                                    new Thread(new chat(frame, s, get_Name, getID, Name, op, ip)).start();
                                }

                                @Override
                                public void mouseReleased(MouseEvent e) {
                                }

                                @Override
                                public void mouseEntered(MouseEvent e) {
                                }

                                @Override
                                public void mouseExited(MouseEvent e) {
                                }

                            });
                            mes_list.add(chat);
                        }
                        mes_list.setVisible(true);
                        JScrollBar vertical = mes_sc.getVerticalScrollBar();
                        vertical.setValue(vertical.getMinimum());
                    } else {
                        mes_list.setVisible(false);
                        mes_list.setPreferredSize(new Dimension(100, 300));
                        JLabel chatname = new JLabel("There are no chat group", SwingConstants.CENTER);
                        chatname.setBackground(Color.white);
                        chatname.setOpaque(true);
                        chatname.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
                        mes_list.add(chatname);
                        mes_list.setVisible(true);
                    }
                } catch (Exception E) {
                    E.printStackTrace();
                }
            } else {
                new Thread(new getMes()).start();
            }
        } else if (e.getSource() == this.new_chat) {
            this.frame_la.remove(this.mes_la);
            this.frame_la.add(this.new_chat_la, BorderLayout.CENTER);
            this.new_chat_la.setVisible(true);
        } else if (e.getSource() == this.back_but_new) {
            this.new_chat_la.setVisible(false);
            this.frame_la.remove(this.new_chat_la);
            this.frame_la.add(this.mes_la, BorderLayout.CENTER);
            new Thread(new getMes()).start();
        } else if (e.getSource() == this.make_new_but) {
            boolean check = true;
            while (check != false) {
                String newchat = JOptionPane.showInputDialog(null, "Enter new chat name!",
                        "New chat", JOptionPane.INFORMATION_MESSAGE);
                if (newchat == null) {
                    System.out.println("ahihi");
                    check = false;
                } else {
                    if (newchat.length() == 0) {
                        JOptionPane.showMessageDialog(null, "Please input chat name!", "Alert!",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        check = false;
                        try {
                            this.op = new DataOutputStream(this.s.getOutputStream());
                            this.op.writeUTF("NEWCHAT:" + newchat);
                            this.ip = new DataInputStream(this.s.getInputStream());
                            String chat_code = this.ip.readUTF();
                            this.new_chat_la.setVisible(false);
                            this.frame.remove(this.new_chat_la);
                            this.frame_la.add(this.mes_la, BorderLayout.CENTER);
                            new Thread(new getMes()).start();
                            frame.setVisible(false);
                            JOptionPane.showMessageDialog(null,
                                    "Chat name " + newchat + " with chat code : " + chat_code, "Alert!",
                                    JOptionPane.INFORMATION_MESSAGE);
                            new Thread(new chat(frame, s, newchat, chat_code, Name, op, ip)).start();
                        } catch (Exception Ex) {
                            JOptionPane.showMessageDialog(null, "Cannot connect to server!", "Alert!",
                                    JOptionPane.ERROR_MESSAGE);
                            check = false;
                        }
                    }
                }
            }
        } else if (e.getSource() == this.join_chat_but) {
            boolean check = true;
            while (check != false) {
                String join_chat = JOptionPane.showInputDialog(null, "Enter chat code to join!",
                        "Join chat", JOptionPane.INFORMATION_MESSAGE);
                if (join_chat == null) {
                    System.out.println("ahihi");
                    check = false;
                } else {
                    if (join_chat.length() == 0) {
                        JOptionPane.showMessageDialog(null, "Please input chat code!", "Alert!",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        try {
                            this.op = new DataOutputStream(this.s.getOutputStream());
                            this.op.writeUTF("JOINCHAT:" + join_chat);
                            this.ip = new DataInputStream(this.s.getInputStream());
                            String get_access = this.ip.readUTF();
                            if (get_access.equals("cantgetchat")) {
                                JOptionPane.showMessageDialog(null, "Invalid chat code, please try again!", "Alert!",
                                        JOptionPane.ERROR_MESSAGE);
                            } else if (get_access.equals("exinchat")) {
                                JOptionPane.showMessageDialog(null, "You already in chat, please try again!", "Alert!",
                                        JOptionPane.ERROR_MESSAGE);
                            } else if (get_access.startsWith("Nametojoin:")) {
                                check = false;
                                String subname = get_access.substring(11, get_access.length());
                                this.new_chat_la.setVisible(false);
                                this.frame.remove(this.new_chat_la);
                                this.frame_la.add(this.mes_la, BorderLayout.CENTER);
                                new Thread(new getMes()).start();
                                frame.setVisible(false);
                                JOptionPane.showMessageDialog(null,
                                        "You have been joined the chat " + subname, "Alert!",
                                        JOptionPane.INFORMATION_MESSAGE);
                                new Thread(new chat(frame, s, subname, join_chat, Name, op, ip)).start();
                            }
                        } catch (Exception Ex) {
                            JOptionPane.showMessageDialog(null, "Cannot connect to server!", "Alert!",
                                    JOptionPane.ERROR_MESSAGE);
                            check = false;
                        }
                    }
                }
            }
        } else if (e.getSource() == this.option) {
            this.frame_la.remove(this.mes_la);
            this.frame_la.add(this.op_la, BorderLayout.CENTER);
            this.op_la.setVisible(true);
        } else if (e.getSource() == this.back_but_op) {
            this.op_la.setVisible(false);
            this.frame_la.remove(this.op_la);
            this.frame_la.add(this.mes_la, BorderLayout.CENTER);
        } else if (e.getSource() == this.Logout) {
            try {
                this.op = new DataOutputStream(this.s.getOutputStream());
                this.op.writeUTF("LOGOUT:");
                this.frame.dispose();
                this.s.close();
                new sign_in();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == this.changeUsname) {
            this.changePass.setEnabled(true);
            this.right_change_pass.setVisible(false);
            this.op_la.remove(this.right_change_pass);
            this.changeUsname.setEnabled(false);
            this.op_la.add(this.right_change_US, BorderLayout.EAST);
            this.right_change_US.setVisible(true);
        } else if (e.getSource() == this.ok_ch_us) {
            if (this.inpPass_changeUS.getText().length() != 0) {
                Check_input input = new Check_input();
                if (input.Password(this.inpPass_changeUS.getText()) == true) {
                    if (this.ChangUSname.getText().length() >= 7) {
                        try {
                            this.op = new DataOutputStream(this.s.getOutputStream());
                            this.op.writeUTF("CHANGUSNAME:" + this.inpPass_changeUS.getText() + " "
                                    + this.ChangUSname.getText());
                            this.ip = new DataInputStream(this.s.getInputStream());
                            String getAcc = this.ip.readUTF();
                            if (getAcc.equals("CANNOTCHANGECAUSEUSNAMENOTCHANGE")) {
                                JOptionPane.showMessageDialog(null, "Cannot change your username because not change!",
                                        "Alert!",
                                        JOptionPane.ERROR_MESSAGE);
                            } else if (getAcc.equals("CANNOTCHANGECAUSEWRONGPASS")) {
                                JOptionPane.showMessageDialog(null,
                                        "Cannot change your username because wrong password!",
                                        "Alert!",
                                        JOptionPane.ERROR_MESSAGE);
                            } else if (getAcc.equals("CANNOTCHANGECAUSEUSNAMEEXIST")) {
                                JOptionPane.showMessageDialog(null,
                                        "Cannot change your username because username in use by other user!",
                                        "Alert!",
                                        JOptionPane.ERROR_MESSAGE);
                            } else if (getAcc.equals("CHANGEUSNAMESUCCESS")) {
                                this.Name = this.ChangUSname.getText();
                                JOptionPane.showMessageDialog(null, "Change username success!", "Notification!",
                                        JOptionPane.INFORMATION_MESSAGE);
                                this.ChangUSname.setText("");
                                this.inpPass_changeUS.setText("");
                                this.frame.setTitle(Name);
                            }
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Cannot connect to server!", "Alert!",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Your username is too short!", "Alert!",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Wrong password format!", "Alert!",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please enter your password!", "Alert!",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == this.changePass) {
            this.changePass.setEnabled(false);
            this.changeUsname.setEnabled(true);
            this.right_change_US.setVisible(false);
            this.op_la.remove(this.right_change_US);
            this.op_la.add(this.right_change_pass, BorderLayout.EAST);
            this.right_change_pass.setVisible(true);
        } else if (e.getSource() == this.ok_ch_pass) {
            Check_input check = new Check_input();
            if (check.Password(this.inpPass_changePass.getText())) {
                if (check.Password(this.changPass.getText())) {
                    if (check.Password(this.rechangePass.getText())) {
                        if (this.changPass.getText().equals(rechangePass.getText())) {
                            try {
                                this.op = new DataOutputStream(this.s.getOutputStream());
                                this.op.writeUTF("CHANGEPASS:" + this.inpPass_changePass.getText() + " "
                                        + this.changPass.getText());
                                this.ip = new DataInputStream(this.s.getInputStream());
                                String getAcc = this.ip.readUTF();
                                if (getAcc.equals("CHANGEPASSSUCCESS")) {
                                    JOptionPane.showMessageDialog(null, "Change password success!", "Notification!",
                                            JOptionPane.INFORMATION_MESSAGE);
                                    this.inpPass_changePass.setText("");
                                    this.changPass.setText("");
                                    this.rechangePass.setText("");
                                } else if (getAcc.equals("CANNOTCHANGEPASSCAUSEWRONGPASS")) {
                                    JOptionPane.showMessageDialog(null,
                                            "Cannot change password because wrong password!",
                                            "Alert!",
                                            JOptionPane.ERROR_MESSAGE);
                                } else if (getAcc.equals("CANNOTCHANGEPASSCAUSEPASSNOTCHANGE")) {
                                    JOptionPane.showMessageDialog(null,
                                            "Cannot change password because password not change!",
                                            "Alert!",
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(null, "Cannot connect to server!", "Alert!",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Re-enter password not correct!", "Alert!",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Wrong re-enter password format!", "Alert!",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Wrong new password format!", "Alert!",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Wrong password format!", "Alert!",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
