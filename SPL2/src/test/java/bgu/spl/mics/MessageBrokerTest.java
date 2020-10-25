package bgu.spl.mics;

import bgu.spl.mics.application.subscribers.*;
import bgu.spl.mics.example.messages.ExampleEvent;
import bgu.spl.mics.example.publishers.ExampleMessageSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class MessageBrokerTest {
    private MessageBrokerImpl msgbTst = new MessageBrokerImpl();

    @BeforeEach
    public void setUp(){
    }

    @Test
    public void testRegister(){
        ExampleEvent eventTst1 = new ExampleEvent("God");
        ExampleEvent eventTst2 = new ExampleEvent("Lucifer");
        assertTrue(eventTst1.getSenderName()=="God");



        SimplePublisher pub1 = new SimplePublisher();

        M M1 = new M();
        M M2 = new M();
        msgbTst.register(M1);
        msgbTst.register(M2);


        msgbTst.subscribeEvent(eventTst1.getClass(),M1);
        msgbTst.subscribeEvent(eventTst1.getClass(),M2);

        pub1.sendEvent(eventTst1);
        pub1.sendEvent(eventTst2);
        try{ Message msg = msgbTst.awaitMessage(M1);
        assertTrue((ExampleEvent)msg==eventTst1);
        }
        catch(Exception e){ fail("event didn't recieved by M1");}
        try{ Message msg = msgbTst.awaitMessage(M2);
            assertTrue((ExampleEvent)msg==eventTst2);
        }
        catch(Exception e){ fail("event didn't recieved by M2");}


    }

}
