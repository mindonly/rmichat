package tests;

import static org.junit.Assert.*;

import server.PresenceServer;
import org.junit.Test;
import presence.RegistrationInfo;

public class PresenceServerTest {

    @Test
    public void test_register() {
        PresenceServer ps = new PresenceServer();
        RegistrationInfo ri1 = new RegistrationInfo("sanchrob", "mindonly", 5050, true);
        RegistrationInfo ri2 = new RegistrationInfo("zsh", "halfmoon", 5050, true);

        assertTrue(ps.register(ri1));
        assertTrue(ps.register(ri2));
        ps.printBuddies();
    }

    @Test
    public void test_updateRegistrationInfo() {
        PresenceServer ps = new PresenceServer();

        RegistrationInfo ri1 = new RegistrationInfo("sanchrob", "mindonly", 5050, true);
        RegistrationInfo ri2 = new RegistrationInfo("sanchrob", "halfmoon", 4040, false);
        RegistrationInfo ri3 = new RegistrationInfo("roan", "halfmoon", 4040, false);

        ps.register(ri1);
        ps.printBuddies();
        System.out.println();

        assertTrue(ps.updateRegistrationInfo(ri2));
        ps.printBuddies();
        System.out.println();

        assertFalse(ps.updateRegistrationInfo(ri3));
        ps.printBuddies();
        System.out.println();
    }

    @Test
    public void test_listRegisteredUsers() {
        PresenceServer ps = new PresenceServer();

        RegistrationInfo ri1 = new RegistrationInfo("sanchrob", "mindonly", 5050, true);
        RegistrationInfo ri2 = new RegistrationInfo("sanchrob", "halfmoon", 4040, false);
        RegistrationInfo ri3 = new RegistrationInfo("roan", "halfmoon", 4040, false);

        ps.register(ri1);
        ps.register(ri2);
        ps.register(ri3);
    }
}
