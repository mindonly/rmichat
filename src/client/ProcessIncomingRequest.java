package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ProcessIncomingRequest implements Runnable {
    private ChatClient client;
    private Socket clientSocket;

    public ProcessIncomingRequest(Socket clientSocket, ChatClient client) {
        super();
        this.clientSocket = clientSocket;
        this.client = client;
    }

    @Override
    public void run() {

        String line;
        BufferedReader is;
        PrintStream os;

        try {
            is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            while(true) {
                line = is.readLine();
                if(line == null) {
                    break;
                }
                System.out.print(line);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
