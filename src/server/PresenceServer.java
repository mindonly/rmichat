package server;

import presence.PresenceService;
import presence.RegistrationInfo;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;
import java.util.Hashtable;

public class PresenceServer implements PresenceService {

    private Hashtable<String, RegistrationInfo> BuddyList;

    public PresenceServer() {
        this.BuddyList = new Hashtable<>();
    }

    public boolean register(RegistrationInfo reg) {
        String userid = reg.getUserName();
        RegistrationInfo ri = BuddyList.putIfAbsent(userid, reg);

        return ri == null;
    }

    public boolean updateRegistrationInfo(RegistrationInfo reg) {
        String userid = reg.getUserName();
        if (BuddyList.containsKey(userid)) {
            BuddyList.replace(userid, reg);

            return true;
        } else { return false; }
    }

    public void unregister(String userid) {
        if (BuddyList.containsKey(userid)) {
            BuddyList.remove(userid);
        } else {
            System.out.println("unregister() failed: user " + userid + " non-existent.");
        }
    }

    public RegistrationInfo lookup(String userid) {
        return BuddyList.getOrDefault(userid, null);
    }

    public Vector<RegistrationInfo> listRegisteredUsers() {
        if (! BuddyList.isEmpty()) {
            Vector<RegistrationInfo> buddyVector = new Vector<>();
            for (RegistrationInfo ri : BuddyList.values())
                buddyVector.add(ri);

            return buddyVector;
        } else { return null; }
    }

    public void printBuddies() {
        if (BuddyList.isEmpty()) {
            System.out.println("Sorry, no buddies found!");
            return;
        }
        System.out.println("-----------------");
        for (String key : BuddyList.keySet()) {
            System.out.println(key + ": " +
                    BuddyList.get(key).getHost() + " " +
                    BuddyList.get(key).getPort() + " " +
                    BuddyList.get(key).getStatus());
        }
        System.out.println("-----------------\nBuddyList size: " + this.getBuddyListSize());
    }

    private int getBuddyListSize() {
        return BuddyList.size();
    }

    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "Presence";
            PresenceService chatServer = new PresenceServer();
            PresenceService stub =
                    (PresenceService) UnicastRemoteObject.exportObject(chatServer, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("PresenceServer bound successfully.");
        } catch (Exception e) {
            System.err.println("PresenceServer exception:");
            e.printStackTrace();
        }
    }
}
