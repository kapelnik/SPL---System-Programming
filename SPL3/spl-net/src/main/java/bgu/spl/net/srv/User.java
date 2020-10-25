package bgu.spl.net.srv;

import java.util.HashMap;
import java.util.List;

public class User {
    private String name,password;
    private int connectionId;
    private ConnectionHandler handler;
    private boolean isActive;
    private HashMap<Integer,String> subscriptionMap; // subsscription id - topic

    public User(String name, String password, int connectionId, ConnectionHandler handler, HashMap<Integer, String> subscriptionMap) {
        this.name = name;
        this.password = password;
        this.connectionId = connectionId;
        this.handler = handler;
        this.isActive=false;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }

    public ConnectionHandler getHandler() {
        return handler;
    }

    public void setHandler(ConnectionHandler handler) {
        this.handler = handler;
    }

    public HashMap<Integer, String> getSubscriptionMap() {
        return subscriptionMap;
    }

    public void setSubscriptionMap(HashMap<Integer, String> subscriptionMap) {
        this.subscriptionMap = subscriptionMap;
    }
}
