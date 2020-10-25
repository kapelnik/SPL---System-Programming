package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class FutureTest {
    private Future<String> futureTst = new Future();
    @BeforeEach
    public void setUp(){

    }

    @Test
    public void test(){
        assertFalse(futureTst.isDone());
        futureTst.resolve("Banana");
        assertTrue(futureTst.isDone());

        String tst = futureTst.get();
        assertTrue(tst=="Banana");


    }
}
