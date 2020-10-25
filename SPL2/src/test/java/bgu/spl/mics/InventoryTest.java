package bgu.spl.mics;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import bgu.spl.mics.application.passiveObjects.Inventory;
import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {
    private Inventory inv = new Inventory();

    @BeforeEach
    public void setUp(){
        String[] gadg = {"gadget1","gadget2","gadget3"};
        inv.load(gadg);
    }

    @Test
    public void testLoad(){
        String[] gadg = {"gadget4","gadget5","gadget6"};
        inv.load(gadg);
        assertTrue(inv.getItem("gadget4"));
        assertTrue(inv.getItem("gadget5"));
        assertTrue(inv.getItem("gadget6"));
        assertFalse(inv.getItem("gadget1"));

    }

}
