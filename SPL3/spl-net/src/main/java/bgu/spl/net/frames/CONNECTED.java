package bgu.spl.net.frames;

import bgu.spl.net.srv.ConnectionHandler;
import bgu.spl.net.srv.ConnectionsImp;

public class CONNECTED extends Frame {
    private int connectionId;
    public CONNECTED(int connectionId , String[] headers, String body) {
        super(headers, body);
        this.connectionId=connectionId;
    }

    public CONNECTED(String[] headers, String body, int connectionId) {
        super(headers, body);
        this.connectionId = connectionId;
    }

    @Override
    public void process() {
        ConnectionsImp.getInstance().send(connectionId,this);
    }
}
