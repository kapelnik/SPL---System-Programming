package bgu.spl.net.api;

import bgu.spl.net.frames.*;
import bgu.spl.net.srv.Connections;
import bgu.spl.net.srv.ConnectionsImp;
import bgu.spl.net.srv.DataBase;
import bgu.spl.net.srv.User;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Supplier;

public class StompMessagingProtocolImp implements StompMessagingProtocol, Supplier {
    private boolean shouldTerminate = false;
    private int connectionId;
    private User user;
    private ConnectionsImp connections;
    @Override
    public void start(int connectionId, ConnectionsImp connections) {
        this.connections = ConnectionsImp.getInstance(); //TODO: maybe change it to not instance
        this.connectionId=connectionId;
        this.user = DataBase.getInstance().getUserByConnectionId(connectionId);// user has only ConnectionId and Handler !
    }
    @Override
    public void process(Frame frame) {
        if(frame.getClass()== CONNECT.class)
        {
            new CONNECT(user,frame.getHeaders(),frame.getBody()).process();
            new CONNECTED(connectionId,new String[]{"1.2"},"").process();
        }
        else if (frame.getClass()== MESSAGE.class)
        {
            frame.process();
        }
        else if (frame.getClass()== SUBSCRIBE.class)
        {
            new SUBSCRIBE(user,frame.getHeaders(),frame.getBody()).process();
            String[] receipt = {frame.getHeaders()[1]}; // headers[1] == id
            new RECEIPT(connectionId,receipt,"").process();
        }
        else if (frame.getClass()== UNSUBSCRIBE.class)
        {
            frame.process();
            String[] receipt = {frame.getHeaders()[1]}; // headers[1] == id
            new RECEIPT(connectionId,receipt,"").process();
        }
        else if(frame.getClass()== DISCONNECT.class)
        {
            new RECEIPT(user.getConnectionId(),frame.getHeaders(),"");
            user.setActive(false);
            user.getSubscriptionMap().clear();
            DataBase.getInstance().getUsers().remove(user);
            for (ConcurrentLinkedQueue t:DataBase.getInstance().getTopics().values())
            {
                t.remove(user);
            }
//            terminate();
        }
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
    public void terminate(){ shouldTerminate=true;}



//__________________________________________________________________________________________________
    @Override // factory method
    public StompMessagingProtocolImp get() {
        return new StompMessagingProtocolImp();
    }

}
