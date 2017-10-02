package client;

import presence.PresenceService;
import presence.RegistrationInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Vector;

public class ChatClient {

    private Registry registry;
    private PresenceService pres;
    private String clientHost;

    private String presenceServer;
    private int RMIPort;

    private String userName;
    private boolean status;
    private int talkPort;

    public ChatClient() {
        super();
    }

    public ChatClient(String uname, String pserver, int port) {
        try {
            this.registry = LocateRegistry.getRegistry(presenceServer);
        } catch (Exception e) {
            System.err.println("ChatClient getRegistry() exception.");
            e.printStackTrace();
        }
        try {
            this.pres = (PresenceService) this.registry.lookup("Presence");
        } catch (Exception e) {
            System.err.println("ChatClient registry.lookup() exception.");
            e.printStackTrace();
        }
        try {
            this.clientHost = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            System.err.println("ChatClient getHostName() exception.");
            e.printStackTrace();
        }
        this.presenceServer = pserver;
        this.RMIPort = port;
        this.userName = uname;
        this.status = true;
        this.talkPort = 0;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getPresenceServer() {
        return this.presenceServer;
    }

    public String getClientHost() {
        return this.clientHost;
    }

    public boolean getStatus() {
        return this.status;
    }

    public int getRMIPort() {
        return this.RMIPort;
    }

    public int getTalkPort() {
        return this.talkPort;
    }

    public void setTalkPort(int port) {
        this.talkPort = port;
    }

    public void setStatus(boolean mode) {
        this.status = mode;
    }

    public void join(int talkPort) {
        try {
            if (pres.lookup(this.userName) == null) {
                RegistrationInfo ri = new RegistrationInfo(this.getUserName(), this.getClientHost(),
                                                           talkPort, this.getStatus());
                pres.register(ri);
                this.setTalkPort(talkPort);
            } else {
                System.err.println("join() failed, ChatClient user " +
                        this.userName + " is already registered.");
                System.exit(-1);
            }
        } catch (Exception e) {
            System.err.println("ChatClient.join() exception:");
            e.printStackTrace();
        }
    }

    public void updateStatus(boolean mode) {
        RegistrationInfo ri;
        try {
            if (pres.lookup(this.userName) != null)
                ri = pres.lookup(this.userName);
            else {
                System.err.println("updateStatus() failed, ChatClient user " +
                        this.userName + " is not registered.");
                return;
            }

            String avail;
            if (mode) avail = "available";
            else avail = "busy";

            if (ri.getStatus() == mode)
                System.out.println("updateStatus() failed, ChatClient user " + this.userName + " is already " + avail);
            else {
                ri.setStatus(mode);
                this.setStatus(mode);
                pres.updateRegistrationInfo(ri);
            }
        } catch (Exception e) {
            System.err.println("ChatClient.updateStatus() exception:");
            e.printStackTrace();
        }
    }

    public void exit() {
        try {
            if (pres.lookup(this.userName) != null)
                pres.unregister(this.userName);
            else System.out.println("exit() failed, " + this.userName + " is not registered.");
        } catch (Exception e) {
            System.err.println("ChatClient.exit() exception:");
            e.printStackTrace();
        }
    }

    public void whoami() {
        System.out.println();
        System.out.println("     userid: " + this.getUserName());
        System.out.println("        RMI: " + this.getPresenceServer());
        System.out.println("   RMI port: " + this.getRMIPort());
        System.out.println("client host: " + this.getClientHost());
        System.out.println("  talk port: " + this.getTalkPort());
        System.out.println();
    }

    public void showFriends() {
        try {
            if (pres.listRegisteredUsers() != null) {
                Vector<RegistrationInfo> buddyVector = pres.listRegisteredUsers();

                System.out.println("\n Buddy List:\n---------------------------");
                for (RegistrationInfo ri : buddyVector) {

                    if (ri.getUserName().equals(this.getUserName()))
                        continue;

                    String avail;
                    if (ri.getStatus()) avail = "available";
                    else avail = "busy";

                    System.out.printf("%-12s %9s %d\n", ri.getUserName(), avail, ri.getPort());
                }
                System.out.println();
            } else System.err.println("showFriends() failed, there are no users currently registered.");
        } catch (Exception e) {
            System.err.println("ChatClient.showFriends() exception:");
            e.printStackTrace();
        }
    }

    public RegistrationInfo find(String userName) {
        try {
            if (pres.listRegisteredUsers() != null) {
                Vector<RegistrationInfo> buddyVector = pres.listRegisteredUsers();

                for (RegistrationInfo ri : buddyVector) {
                    if (ri.getUserName().equals(userName))
                        return ri;
                }
            } else System.err.println("find() failed, user " + userName + " is not registered.");
        } catch(Exception e) {
            System.err.println("ChatClient.find() exception:");
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        String uname, pserver;
        int port;
        ChatClient cc = new ChatClient();

        switch (args.length) {
            case 1:
                uname = args[0];
                cc = new ChatClient(uname, "localhost", 1099);
                break;
            case 2:
                uname = args[0];
                pserver = args[1];
                cc = new ChatClient(uname, pserver, 1099);
                break;
            case 3:
                uname = args[0];
                pserver = args[1];
                port = Integer.parseInt(args[2]);
                cc = new ChatClient(uname, pserver, port);
                break;
            default:
                System.err.println("Invalid number of arguments.");
                break;
        }

        ChatListener cl = new ChatListener(cc);
        Thread t = new Thread(cl);
        t.start();

        try {
            Thread.currentThread().sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

            // join the chat server
        cc.join(cl.getListenPort());
        cc.whoami();

        String line;
        BufferedReader is;
        is = new BufferedReader(new InputStreamReader(System.in));

        while(true) {
            try {
                System.out.print("rmichat> ");
                line = is.readLine();
                if (line == null) {
                    break;
                }
                String[] tokens = line.split("\\s+");
                switch (tokens[0]) {
                    case "friends":
                        cc.showFriends();
                        break;
                    case "whoami":
                        cc.whoami();
                        break;
                    case "talk":
                        String funame = tokens[1];
                        RegistrationInfo tri = cc.find(funame);
                        PrintStream tos;
                        if (tri != null && tri.getStatus()) {
                            Socket talkSocket = new Socket(tri.getHost(), tri.getPort());
                            tos = new PrintStream(talkSocket.getOutputStream());
                            tos.print(cc.getUserName() + ": ");
                            for (int i = 2; i < tokens.length; i++)
                                tos.println(tokens[i] + " ");
                        } else System.out.println("talk() error: user " + funame + " busy or not found.");
                        break;
                    case "broadcast":
                        try {
                            if (cc.pres.listRegisteredUsers() != null) {
                                Vector<RegistrationInfo> buddyVector = cc.pres.listRegisteredUsers();
                                for (RegistrationInfo bri : buddyVector) {
                                    if (! bri.getUserName().equals(cc.getUserName()) && bri.getStatus()) {
                                        Socket bcastSocket = new Socket(bri.getHost(), bri.getPort());
                                        PrintStream bos = new PrintStream(bcastSocket.getOutputStream());
                                        bos.print(cc.getUserName() + ": ");
                                        for (int j = 1; j < tokens.length; j++)
                                            bos.println(tokens[j] + " ");
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case "status":
                        if (cc.getStatus())
                            System.out.println("available");
                        else System.out.println("busy");
                        break;
                    case "busy":
                        cc.updateStatus(false);
                        break;
                    case "available":
                        cc.updateStatus(true);
                        break;
                    case "help":
                        System.out.println("usage:");
                        System.out.println("    help\t\t\tthis message");
                        System.out.println("    friends\t\t\tshow friends");
                        System.out.println("    status\t\t\tcurrent status");
                        System.out.println("    whoami\t\t\tconnection details");
                        System.out.println("    busy\t\t\tupdate status 'busy' ");
                        System.out.println("    available\t\t\tupdate status 'available' ");
                        System.out.println("    broadcast {message}\t\tsend message to all available users");
                        System.out.println("    talk {username} {message}\tsend message to an available user");
                        System.out.println("    exit\t\t\tquit rmichat");
                        break;
                    case "exit":
                        cc.exit();
                        System.exit(0);
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
