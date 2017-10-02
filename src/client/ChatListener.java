package client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;

public class ChatListener implements Runnable {
    private ChatClient client;
    private int listenPort;

    public ChatListener(ChatClient client) {
        this.client = client;
    }

    public int getListenPort() { return this.listenPort; }

    @Override
    public void run() {
        int randomPort = ThreadLocalRandom.current().nextInt(2000, 8000 + 1);
        try {
            ServerSocket server = new ServerSocket(randomPort);
            System.out.println("listening [" + randomPort + "]");
            this.listenPort = randomPort;

            while (true) {
                try {
                    Socket socket = server.accept();
                    Thread client = new Thread(new ProcessIncomingRequest(socket, this.client));
                    client.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
