package src;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
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
import java.util.ArrayList;

public class sign_in implements ActionListener {
    public Socket client;
    public DataOutputStream op;
    public DataInputStream ip;
    public JFrame frame;
    public JTextField email_field;
    public JTextArea email_text, pass_text, new_acc, chat;
    public URL url_icon = getClass().getResource("/Images/chat_icon.png");
    public ImageIcon icon = new ImageIcon(url_icon),
            image = new ImageIcon(getClass().getResource("/Images/chat_icon.png"));
    public JPasswordField pass_field;
    public JButton signin_btn, si;
    public JLabel signin_la, img_la;
    public Border border;
    public JCheckBox show_pass;
    public Check_input check = new Check_input();
    public String name, EMAIL, ID;

    public sign_in() {
        try {
            this.client = new Socket("192.168.1.7", 1234);
            this.chat = new JTextArea("Chat");
            this.chat.setBackground(Color.white);
            this.chat.setForeground(Color.BLACK);
            this.chat.setFocusable(false);
            this.chat.setBounds(150, 150, 130, 40);
            this.chat.setFont(new Font(Font.MONOSPACED, Font.BOLD, 30));
            this.chat.setEditable(false);

            this.new_acc = new JTextArea("New account");
            this.new_acc.setBackground(Color.white);
            this.new_acc.setForeground(Color.BLACK);
            this.new_acc.setFocusable(false);
            this.new_acc.setBounds(125, 415, 130, 30);
            this.new_acc.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
            this.new_acc.setEditable(false);
            this.new_acc.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // TODO Auto-generated method stub
                    // throw new UnsupportedOperationException("Unimplemented method
                    // 'mouseClicked'");

                }

                @Override
                public void mousePressed(MouseEvent e) {
                    // TODO Auto-generated method stub
                    // throw new UnsupportedOperationException("Unimplemented method
                    // 'mousePressed'");
                    new New_account(client, op, ip, frame);
                    frame.setVisible(false);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    // TODO Auto-generated method stub
                    // throw new UnsupportedOperationException("Unimplemented method
                    // 'mouseReleased'");
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    // TODO Auto-generated method stub
                    // throw new UnsupportedOperationException("Unimplemented method
                    // 'mouseEntered'");
                    Border line = BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK);
                    new_acc.setBorder(line);
                    new_acc.setCursor(new Cursor(12));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    // TODO Auto-generated method stub
                    // throw new UnsupportedOperationException("Unimplemented method
                    // 'mouseExited'");
                    new_acc.setBorder(null);
                }

            });

            this.signin_btn = new JButton("Log in");
            this.signin_btn.setBackground(Color.black);
            this.signin_btn.setForeground(Color.white);
            this.signin_btn.setFocusable(false);
            this.signin_btn.setBounds(140, 380, 100, 30);
            this.signin_btn.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
            this.signin_btn.addActionListener(this);
            this.signin_btn.addMouseListener(new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    // TODO Auto-generated method stub
                    // throw new UnsupportedOperationException("Unimplemented method
                    // 'mouseClicked'");

                }

                @Override
                public void mousePressed(MouseEvent e) {
                    // TODO Auto-generated method stub
                    // throw new UnsupportedOperationException("Unimplemented method
                    // 'mousePressed'");

                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    // TODO Auto-generated method stub
                    // throw new UnsupportedOperationException("Unimplemented method
                    // 'mouseReleased'");

                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    // TODO Auto-generated method stub
                    // throw new UnsupportedOperationException("Unimplemented method
                    // 'mouseEntered'");
                    signin_btn.setBackground(Color.white);
                    signin_btn.setForeground(Color.black);
                    signin_btn.setBorder(null);
                    signin_btn.setCursor(new Cursor(12));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    // TODO Auto-generated method stub
                    // throw new UnsupportedOperationException("Unimplemented method
                    // 'mouseExited'");
                    signin_btn.setBackground(Color.black);
                    signin_btn.setForeground(Color.white);
                    signin_btn.setBorder(null);
                }

            });

            this.show_pass = new JCheckBox("Show password");
            this.show_pass.setBounds(72, 340, 200, 20);
            this.show_pass.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
            this.show_pass.setFocusable(false);
            this.show_pass.setVisible(true);
            this.show_pass.setForeground(Color.black);
            this.show_pass.setBackground(Color.white);
            this.show_pass.setOpaque(true);
            this.show_pass.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (show_pass.isSelected()) {
                        pass_field.setEchoChar((char) 0);
                    } else {
                        pass_field.setEchoChar('*');
                    }
                }

            });

            this.img_la = new JLabel();
            this.img_la.setBounds(110, 3, 170, 170);
            Image i = image.getImage();
            Image img = (i).getScaledInstance(img_la.getWidth(), img_la.getHeight(), Image.SCALE_SMOOTH);
            ImageIcon scale = new ImageIcon(img);
            this.img_la.setIcon(scale);
            this.img_la.setBackground(new Color(0, 0, 0, 0));
            this.img_la.setVisible(true);
            this.img_la.setOpaque(true);

            this.email_text = new JTextArea("Email");
            this.email_text.setBounds(72, 207, 100, 25);
            this.email_text.setForeground(Color.BLACK);
            this.email_text.setBackground(new Color(0, 0, 0, 0));
            this.email_text.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
            this.email_text.setEditable(false);

            this.pass_text = new JTextArea("Password");
            this.pass_text.setBounds(72, 270, 100, 25);
            this.pass_text.setForeground(Color.BLACK);
            this.pass_text.setBackground(new Color(0, 0, 0, 0));
            this.pass_text.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
            this.pass_text.setVisible(true);
            this.pass_text.setOpaque(true);
            this.pass_text.setEditable(false);

            this.border = BorderFactory.createMatteBorder(0, 0, 2, 0, Color.black);

            this.email_field = new JTextField();
            this.email_field.setBounds(72, 230, 240, 30);
            this.email_field.setBorder(this.border);
            this.email_field.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
            this.email_field.setBackground(Color.WHITE);
            this.email_field.setForeground(Color.black);

            this.pass_field = new JPasswordField();
            this.pass_field.setBounds(72, 295, 240, 30);
            this.pass_field.setBorder(this.border);
            this.pass_field.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
            this.pass_field.setBackground(Color.white);
            this.pass_field.setForeground(Color.black);
            this.pass_field.setEchoChar('*');

            this.signin_la = new JLabel();
            this.signin_la.setLayout(null);
            this.signin_la.setBackground(Color.white);
            this.signin_la.add(this.email_field);
            this.signin_la.add(this.pass_field);
            this.signin_la.add(this.email_text);
            this.signin_la.add(this.pass_text);
            this.signin_la.add(this.img_la);
            this.signin_la.add(this.show_pass);
            this.signin_la.add(this.signin_btn);
            this.signin_la.add(this.new_acc);
            this.signin_la.add(this.chat);
            this.signin_la.setOpaque(true);

            this.frame = new JFrame("Sign in");
            this.frame.setIconImage(this.icon.getImage());
            this.frame.setSize(400, 500);
            this.frame.setLayout(null);
            this.frame.setVisible(true);
            this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.frame.setBackground(Color.white);
            this.frame.setLocationRelativeTo(null);
            this.frame.setContentPane(this.signin_la);
            this.frame.setResizable(false);
            this.frame.addWindowListener(new WindowListener() {

                @Override
                public void windowOpened(WindowEvent e) {
                    // TODO Auto-generated method stub
                    // throw new UnsupportedOperationException("Unimplemented method
                    // 'windowOpened'");
                }

                @Override
                public void windowClosing(WindowEvent e) {
                    // TODO Auto-generated method stub
                    // throw new UnsupportedOperationException("Unimplemented method
                    // 'windowClosing'");
                    try {
                        op = new DataOutputStream(client.getOutputStream());
                        op.writeUTF("::close::");
                        client.close();
                        System.exit(0);
                        op.close();
                        ip.close();
                    } catch (Exception Ex) {
                        Ex.printStackTrace();
                    }
                }

                @Override
                public void windowClosed(WindowEvent e) {
                    // TODO Auto-generated method stub
                    // throw new UnsupportedOperationException("Unimplemented method
                    // 'windowClosed'");
                }

                @Override
                public void windowIconified(WindowEvent e) {
                    // TODO Auto-generated method stub
                    // throw new UnsupportedOperationException("Unimplemented method
                    // 'windowIconified'");
                }

                @Override
                public void windowDeiconified(WindowEvent e) {
                    // TODO Auto-generated method stub
                    // throw new UnsupportedOperationException("Unimplemented method
                    // 'windowDeiconified'");
                }

                @Override
                public void windowActivated(WindowEvent e) {
                    // TODO Auto-generated method stub
                    // throw new UnsupportedOperationException("Unimplemented method
                    // 'windowActivated'");
                }

                @Override
                public void windowDeactivated(WindowEvent e) {
                    // TODO Auto-generated method stub
                    // throw new UnsupportedOperationException("Unimplemented method
                    // 'windowDeactivated'");
                }

            });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Cannot connect to server!", "Alert!", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.signin_btn) {
            if (this.check.Email(this.email_field.getText())) {
                if (this.check.Password(this.pass_field.getText())) {
                    System.out.println("Hello");
                    try {
                        String Sign_in = "Login:" + this.email_field.getText() + " " + this.pass_field.getText();
                        this.op = new DataOutputStream(this.client.getOutputStream());
                        this.op.writeUTF(Sign_in);
                        this.op.flush();
                        this.ip = new DataInputStream(this.client.getInputStream());
                        String getInf = this.ip.readUTF();
                        if (getInf.equals("false")) {
                            JOptionPane.showMessageDialog(null, "Wrong email or password, please try again!", "Alert!",
                                    JOptionPane.ERROR_MESSAGE);
                        } else {
                            ObjectInputStream get_inf = new ObjectInputStream(client.getInputStream());
                            try {
                                Object object = get_inf.readObject();
                                ArrayList<String> inf = null;
                                inf = (ArrayList<String>) object;
                                this.name = inf.get(0);
                                this.EMAIL = inf.get(1);
                                System.out.println(this.name + " " + this.EMAIL);
                                this.frame.dispose();
                                new In_App(client, this.op, this.ip, this.name, this.EMAIL);
                            } catch (Exception EX) {
                                JOptionPane.showMessageDialog(null, "Cannot Login!", "Alert!",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }

                    } catch (Exception E) {
                        JOptionPane.showMessageDialog(null, "Cannot login!", "Alert!", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Wrong password!", "Allert!", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Wrong Email!", "Allert!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
