package bgu.spl.net.srv;

import bgu.spl.net.frames.Frame;
import javafx.util.Pair;

import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsImp implements Connections{
    private int connectiontId=0;
    private ConcurrentHashMap<Integer,ConnectionHandler> connectionsHandlers;// ConHandler for each active user (by user's connectionId)
    private DataBase dataBase;
    private static ConnectionsImp instance = new ConnectionsImp();
    private ConnectionsImp() {
        dataBase= DataBase.getInstance();
        connectionsHandlers= new ConcurrentHashMap<>();
    }
    public static ConnectionsImp getInstance(){
        return instance;
    }

    @Override
    public boolean send(int connectionId, Frame msg) {
        if(!connectionsHandlers.containsKey(connectionId)) return false;
        connectionsHandlers.get(connectionId).send(msg);
        return true;
    }

    @Override
    public void send(String channel, Frame msg) {
        for(User u:dataBase.getTopics().get(channel))
        {
            send(u.getConnectionId(),msg);
        }
    }

    @Override
    public void disconnect(int connectionId) {
    }

    public synchronized int addHandler(ConnectionHandler handler){// TODO: check sync ?!?!?!?!
        connectionsHandlers.put(++connectiontId,handler);
        return connectiontId;
    }


}
