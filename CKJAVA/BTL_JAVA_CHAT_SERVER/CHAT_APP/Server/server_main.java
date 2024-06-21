package Server;

import java.net.ServerSocket;

public class server_main {
    public static void main(String[] args) {
        server se = new server();
        Thread t1 = new Thread(se);
        t1.start();
    }
}
