package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Date;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class server implements Runnable {
    private ServerSocket ss;
    public ArrayList<ClientConnect> Cc;
    private Socket s;
    private Connection conn;
    public static String DB_URL  = "jdbc:sqlserver://localhost:3306;"
    + "database=chat;"
    + "user=sa;"
    + "password=123456789;"
    + "encrypt=true;"
    + "trustServerCertificate=true;"
    + "loginTimeout=30;";
    private Statement stmt;
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
    Date date = new Date();

    public server() {
        Cc = new ArrayList<>();
    }

    public void run() {
        try {
            this.ss = new ServerSocket(1234);
            this.conn = DriverManager.getConnection(DB_URL);
            this.stmt = conn.createStatement();
            while (true) {
                s = ss.accept();
                ClientConnect client = new ClientConnect(s);
                this.Cc.add(client);
                new Thread(client).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ClientConnect implements Runnable {
        private static final String InnetAdress = null;
        public Socket c;
        public DataInputStream DI;
        public DataOutputStream DO;
        public String NAME = "", EMAIL = " ", ID = "", PASS = "";
        public boolean check;
        public int existun, existmail;
        public String in_chat = "0";
        public boolean status = false;

        public ClientConnect(Socket s) {
            this.c = s;
            this.check = true;
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
            while (this.check != false) {
                try {
                    DI = new DataInputStream(this.c.getInputStream());
                    String mes = DI.readUTF();
                    if (mes.startsWith("Login:")) {
                        String submes = mes.substring(6, mes.length());
                        int i = split(submes);
                        String Email = submes.substring(0, i);
                        String Pass = submes.substring(i + 1, submes.length());
                        ResultSet select = stmt.executeQuery(
                                "SELECT [ID] , [Name], [Email] FROM [chat].[dbo].[user] WHERE [Email]='" + Email + "' AND [Pass] = '"
                                        + Pass + "';");
                        if (select.isBeforeFirst()) {
                            DO = new DataOutputStream(this.c.getOutputStream());
                            DO.writeUTF("true");
                            while (select.next()) {
                                this.ID = select.getString(1);
                                this.NAME = select.getString(2);
                                this.EMAIL = select.getString(3);
                            }
                            this.PASS = Pass;
                            ArrayList<String> info_send = new ArrayList<>();
                            info_send.add(this.NAME);
                            info_send.add(this.EMAIL);
                            ObjectOutputStream send_inf = new ObjectOutputStream(c.getOutputStream());
                            send_inf.writeObject(info_send);
                            // Broadcast(submes);
                        } else {
                            DO = new DataOutputStream(this.c.getOutputStream());
                            DO.writeUTF("false");
                        }
                    } else if (mes.startsWith("NewAcc:")) {
                        String submes = mes.substring(7, mes.length());
                        int getusname = split(submes);
                        String usname = submes.substring(0, getusname);
                        submes = submes.substring(getusname + 1, submes.length());
                        int getemail = split(submes);
                        String email = submes.substring(0, getemail);
                        String pass = submes.substring(getemail + 1, submes.length());
                        ResultSet select = stmt
                                .executeQuery("Select COUNT([Email]) From [chat].[dbo].[user] WHERE [Email] = '" + email + "';");
                        this.existmail = 0;
                        while (select.next()) {
                            this.existmail = Integer.parseInt(select.getString(1));
                        }
                        ResultSet select1 = stmt
                                .executeQuery("Select COUNT([Name]) From [chat].[dbo].[user] WHERE [Name] = '" + usname + "';");
                        this.existun = 0;
                        while (select1.next()) {
                            this.existun = Integer.parseInt(select1.getString(1));
                        }
                        if (this.existmail > 0) {
                            DO = new DataOutputStream(c.getOutputStream());
                            DO.writeUTF("Existemail");
                        } else {
                            if (this.existun > 0) {
                                DO = new DataOutputStream(c.getOutputStream());
                                DO.writeUTF("Existusname");
                            } else {
                                stmt.executeUpdate(
                                        "INSERT INTO [chat].[dbo].[user](Name, Email , Pass) VALUES ('" + usname + "','"
                                                + email + "','"
                                                + pass + "');");
                                ResultSet select_id = stmt.executeQuery("SELECT [ID] FROM [chat].[dbo].[user] WHERE [Name] = '"
                                        + usname + "' AND [Email]='" + email + "' AND [Pass] = '"
                                        + pass + "';");
                                while (select_id.next()) {
                                    this.ID = select_id.getString(1);
                                }
                                this.NAME = usname;
                                this.EMAIL = email;
                                this.PASS = pass;
                                DO = new DataOutputStream(c.getOutputStream());
                                DO.writeUTF("Loginacpt");
                            }
                        }
                    } else if (mes.equals("::close::")) {
                        Cc.remove(this);
                        this.check = false;
                        this.c.close();
                    } else if (mes.equals("getMes")) {
                        ResultSet chat = stmt.executeQuery(
                                "SELECT [ID], [Name_chat] FROM [chat].[dbo].[chat_box] WHERE [ID_User_in_chat] = " + this.ID);
                        if (chat.isBeforeFirst()) {
                            Map<String, String> chat_list = new HashMap<String, String>();
                            while (chat.next()) {
                                chat_list.put(chat.getString(1), chat.getString(2));
                            }
                            ObjectOutputStream sen_chat_list = new ObjectOutputStream(this.c.getOutputStream());
                            sen_chat_list.writeObject(chat_list);
                        } else {
                            ObjectOutputStream sen_chat_list = new ObjectOutputStream(this.c.getOutputStream());
                            sen_chat_list.writeObject(null);
                        }
                    } else if (mes.startsWith("getHis:")) {
                        this.status = true;
                        String submes = mes.substring(7, mes.length());
                        this.in_chat = submes;
                        ResultSet his_chat = stmt.executeQuery(
                                "SELECT [user_name] , [message], [TIME_send] FROM [chat].[dbo].[chat_his] WHERE [chat_id] = "
                                        + submes + ";");
                        ArrayList<String> get_chat_his = null;
                        if (his_chat.isBeforeFirst()) {
                            get_chat_his = new ArrayList<String>();
                            while (his_chat.next()) {
                                get_chat_his.add(his_chat.getString(1) + ": " +
                                        his_chat.getString(2) + "\n" + his_chat.getString(3));

                            }
                            ObjectOutputStream sen_chat_his = new ObjectOutputStream(this.c.getOutputStream());
                            sen_chat_his.writeObject(get_chat_his);
                        } else {
                            ObjectOutputStream chat_his = new ObjectOutputStream(this.c.getOutputStream());
                            chat_his.writeObject(get_chat_his);
                        }
                    } else if (mes.startsWith("getOnl:")) {
                        String submes = mes.substring(7, mes.length());
                        this.in_chat = submes;
                        ResultSet on = stmt.executeQuery(
                                "SELECT [user_name] FROM [chat].[dbo].[chat_box] WHERE [ID] = "
                                        + submes + ";");
                        ArrayList<String> online = null;
                        if (on.isBeforeFirst()) {
                            online = new ArrayList<>();
                            while (on.next()) {
                                for (ClientConnect cc : Cc) {
                                    String name_onl = on.getString(1);
                                    if (cc.getName().equals(name_onl)) {
                                        if (cc.status == true) {
                                            if (cc.getInchat().equals(submes)) {
                                                if (online.contains(name_onl) != true) {
                                                    online.add(name_onl);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            ObjectOutputStream sen_onl = new ObjectOutputStream(this.c.getOutputStream());
                            sen_onl.writeObject(online);
                        } else {
                            ObjectOutputStream sen_onl = new ObjectOutputStream(this.c.getOutputStream());
                            sen_onl.writeObject(online);
                        }
                    } else if (mes.startsWith("startChat:")) {
                        String submes = mes.substring(10, mes.length());
                        String tosend = "ONLINE " + this.NAME;
                        Broadcast(tosend, submes);
                    } else if (mes.equals("::OUTCHAT::")) {
                        String tosend = "OFFLINE " + this.NAME;
                        sendOffbroadcast(tosend, this.NAME, this.in_chat);
                        this.in_chat = "0";
                        this.status = false;
                    } else if (mes.startsWith("SENDMES:")) {
                        String submes = mes.substring(8, mes.length());
                        stmt.executeUpdate("INSERT INTO [chat].[dbo].[chat_his] (chat_id,user_id,user_name,message,TIME_send) VALUES ("
                                + this.in_chat + ", " + this.ID + ",'" + this.NAME + "','" + submes + "',CONVERT(datetime, '"+ formatter.format(date)+"', 103));");
                        submes = this.NAME + ": " + submes + "\n" + formatter.format(date);
                        Broadcast(submes, this.in_chat);
                    } else if (mes.startsWith("SEARCH:")) {
                        String submes = mes.substring(7, mes.length());
                        Map<String, String> chat_list = null;
                        ResultSet chat = stmt.executeQuery(
                                "select [ID], [Name_chat] From [chat].[dbo].[chat_box] where [ID] Like '%" + submes
                                        + "%' and  [ID_User_in_chat] = '" + this.ID
                                        + "' OR [Name_chat] Like '%" + submes + "%' and  [ID_User_in_chat] = '" + this.ID
                                        + "' ; ");
                        if (chat.isBeforeFirst()) {
                            chat_list = new HashMap<String, String>();
                            while (chat.next()) {
                                chat_list.put(chat.getString(1), chat.getString(2));
                            }
                            ObjectOutputStream sen_chat_list = new ObjectOutputStream(this.c.getOutputStream());
                            sen_chat_list.writeObject(chat_list);
                        } else {
                            ObjectOutputStream sen_chat_list = new ObjectOutputStream(this.c.getOutputStream());
                            sen_chat_list.writeObject(chat_list);
                        }
                    } else if (mes.startsWith("NEWCHAT:")) {
                        String submes = mes.substring(8, mes.length());
                        String getRe = null;
                        boolean check_ex = true;
                        while (check_ex != false) {
                            Random rand = new Random();
                            int n = rand.nextInt(10000);
                            int last_ran = n * 10 + rand.nextInt(10);
                            n = last_ran;
                            ResultSet get_ex = stmt
                                    .executeQuery("SELECT count([ID]) FROM [chat].[dbo].[chat_box] WHERE [ID] = '" + n + "'; ");
                            while (get_ex.next()) {
                                getRe = get_ex.getString(1);
                            }
                            if (Integer.parseInt(getRe) == 0) {
                                check_ex = false;
                                stmt.executeUpdate(
                                        "INSERT INTO [chat].[dbo].[chat_box](ID,Name_chat, ID_User_in_chat,User_name,Permission) VALUES ('"
                                                + n + "', '" + submes + "','" + this.ID + "','" + this.NAME
                                                + "','Admin')");
                                this.DO = new DataOutputStream(this.c.getOutputStream());
                                this.DO.writeUTF("" + n);
                            }
                        }
                    } else if (mes.startsWith("JOINCHAT:")) {
                        String submes = mes.substring(9, mes.length());
                        String get = null;
                        ResultSet get_ex = stmt
                                .executeQuery("SELECT count([ID]) FROM [chat].[dbo].[chat_box] WHERE [ID] = '" + submes + "'; ");
                        while (get_ex.next()) {
                            get = get_ex.getString(1);
                        }
                        if (Integer.parseInt(get) > 0) {
                            String count = null;
                            ResultSet count_ex = stmt
                                    .executeQuery("SELECT count([ID]) FROM [chat].[dbo].[chat_box] WHERE [ID] = '" + submes
                                            + "' AND [ID_User_in_chat] = '" + this.ID + "'; ");
                            while (count_ex.next()) {
                                count = count_ex.getString(1);
                            }
                            if (Integer.parseInt(count) > 0) {
                                this.DO = new DataOutputStream(this.c.getOutputStream());
                                this.DO.writeUTF("exinchat");
                            } else {
                                String name_chat = null;
                                ResultSet get_namechat = stmt
                                        .executeQuery(
                                                "SELECT [Name_chat] FROM [chat].[dbo].[chat_box] Where [ID] = '" + submes + "'");
                                while (get_namechat.next()) {
                                    name_chat = get_namechat.getString(1);
                                }
                                stmt.execute(
                                        "INSERT INTO [chat].[dbo].[chat_box](ID,Name_chat, ID_User_in_chat,User_name,Permission) VALUES ('"
                                                + submes + "', '" + name_chat + "','" + this.ID + "','" + this.NAME
                                                + "','normal')");
                                this.DO = new DataOutputStream(this.c.getOutputStream());
                                this.DO.writeUTF("Nametojoin:" + name_chat);
                            }
                        } else {
                            this.DO = new DataOutputStream(this.c.getOutputStream());
                            this.DO.writeUTF("cantgetchat");
                        }
                    } else if (mes.startsWith("LOGOUT:")) {
                        this.check = false;
                        Cc.remove(this);
                        this.c.close();
                    } else if (mes.startsWith("CHANGUSNAME:")) {
                        String submes = mes.substring(12, mes.length());
                        int getpass = split(submes);
                        String oldname = this.NAME;
                        String pass = submes.substring(0, getpass);
                        String usname = submes.substring(getpass + 1, submes.length());
                        int getcountName = 0;
                        ResultSet countname = stmt
                                .executeQuery("SELECT count([Name]) FROM [chat].[dbo].[user] WHERE [Name] = '" + usname + "'");
                        while (countname.next()) {
                            getcountName = countname.getInt(1);
                        }
                        if (this.PASS.equalsIgnoreCase(pass)) {
                            if (this.NAME.equals(usname)) {
                                DO = new DataOutputStream(this.c.getOutputStream());
                                DO.writeUTF("CANNOTCHANGECAUSEUSNAMENOTCHANGE");
                            } else if (getcountName > 0) {
                                DO = new DataOutputStream(this.c.getOutputStream());
                                DO.writeUTF("CANNOTCHANGECAUSEUSNAMEEXIST");
                            } else {
                                stmt.executeUpdate(
                                        "UPDATE [chat].[dbo].[user] SET [Name] = '" + usname + "' WHERE [ID] = '" + this.ID + "'");
                                this.NAME = usname;
                                DO = new DataOutputStream(this.c.getOutputStream());
                                DO.writeUTF("CHANGEUSNAMESUCCESS");
                                // changeName(oldname, this.ID, this.NAME);
                            }
                        } else {
                            DO = new DataOutputStream(this.c.getOutputStream());
                            DO.writeUTF("CANNOTCHANGECAUSEWRONGPASS");
                        }
                    } else if (mes.startsWith("CHANGEPASS:")) {
                        String submes = mes.substring(11, mes.length());
                        int getpass = split(submes);

                        String pass = submes.substring(0, getpass);
                        String newpass = submes.substring(getpass + 1, submes.length());
                        if (this.PASS.equalsIgnoreCase(pass)) {
                            if (this.PASS.equalsIgnoreCase(newpass)) {
                                DO = new DataOutputStream(this.c.getOutputStream());
                                DO.writeUTF("CANNOTCHANGEPASSCAUSEPASSNOTCHANGE");
                            } else {
                                this.PASS = newpass;
                                stmt.executeUpdate(
                                        "UPDATE  [chat].[dbo].[user] SET [Name] = '" + newpass + "' WHERE [ID] = '" + this.ID + "'");
                                DO = new DataOutputStream(this.c.getOutputStream());
                                DO.writeUTF("CHANGEPASSSUCCESS");
                            }
                        } else {
                            DO = new DataOutputStream(this.c.getOutputStream());
                            DO.writeUTF("CANNOTCHANGEPASSCAUSEWRONGPASS");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    this.check = false;
                }
            }
        }

        public String getName() {
            return this.NAME;
        }

        public String getInchat() {
            return this.in_chat;
        }

        public boolean getStat() {
            return this.status;
        }

        public void changename(String namechange) {
            this.NAME = namechange;
        }

        // public void changeName(String old, String ID, String change) {
        // for (int i = 0; i < Cc.size(); i++) {
        // if (Cc.get(i).getName().equals(old) && Cc.get(i).ID.equals(ID)) {
        // Cc.get(i).changename(change);
        // if (Cc.get(i).getInchat().equals("0") != true) {
        // Cc.get(i).sendMes("CHANGENEWUSNAME:" + change);
        // }
        // }
        // }
        // }

        public void sendOffbroadcast(String mes, String Name, String Inchat) {
            try {
                int count = 0;
                for (int i = 0; i < Cc.size(); i++) {
                    if (Cc.get(i).getName().equals(Name) && Cc.get(i).in_chat.equals(Inchat)) {
                        count++;
                    }
                }
                if (count == 1) {
                    Broadcast(mes, Inchat);
                } else if (count > 1) {
                    this.DO = new DataOutputStream(this.c.getOutputStream());
                    this.DO.writeUTF(mes);
                    this.in_chat = "0";
                    this.status = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void Broadcast(String mes, String Inchat) {
            try {
                for (int i = 0; i < Cc.size(); i++) {
                    // System.out.println(Cc.get(i).getName());
                    if (Cc.get(i).getStat() != false) {
                        if (Cc.get(i).getInchat().equals(Inchat)) {
                            Cc.get(i).sendMes(mes);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void sendMes(String mes) {
            try {
                this.DO = new DataOutputStream(this.c.getOutputStream());
                this.DO.writeUTF(mes);
                DO.flush();
            } catch (Exception e) {
                // e.printStackTrace();
            }
        }
    }
}
