package bgu.spl.mics;
import java.util.*;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Squad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SquadTest {
    private Squad squadTest = new Squad();
    @BeforeEach
    public void setUp(){
        Agent avraam = new Agent();
        avraam.setName("avraam");
        avraam.setSerialNumber("001");
        Agent itzhak = new Agent();
        itzhak.setName("itzhak");
        itzhak.setSerialNumber("002");
        Agent yaakov = new Agent();
        yaakov.setName("yaakov");
        yaakov.setSerialNumber("003");
        Agent[] agents = {avraam,itzhak,yaakov};
        squadTest.load(agents);
    }

    @Test
    public void testGetAgentsNames(){
        List<String> serials = Arrays.asList(new String[]{"001", "003"});
        List<String> names = squadTest.getAgentsNames(serials);
        for (String name : names) {
            assertTrue((name=="avraam")|(name=="yaakov"));
        }
    }
    @Test
    public void testGetAgent(){
        Agent ishmael = new Agent();
        ishmael.setName("ishmael");
        ishmael.setSerialNumber("004");
        Agent lavan = new Agent();
        lavan.setName("lavan");
        lavan.setSerialNumber("005");
        Agent[] agents = {ishmael,lavan};
        squadTest.load(agents);
        List<String> serials = Arrays.asList(new String[]{"004", "005"});
        assertTrue(squadTest.getAgents(serials));

        assertFalse(ishmael.isAvailable());
        assertFalse(lavan.isAvailable());

        assertFalse(squadTest.getAgents(serials));

        serials = Arrays.asList(new String[]{"001", "003"});
        assertFalse(squadTest.getAgents(serials));
    }
    @Test
    public void testSendAgents(){
        List<String> serials1 = Arrays.asList(new String[]{"001", "002"});
        squadTest.sendAgents(serials1,1000);
        assertFalse(squadTest.getAgents(serials1));

        List<String> serials2 = Arrays.asList(new String[]{"003"});
        assertTrue(squadTest.getAgents(serials2));

        squadTest.releaseAgents(serials1);
        assertTrue(squadTest.getAgents(serials1));

    }
}
