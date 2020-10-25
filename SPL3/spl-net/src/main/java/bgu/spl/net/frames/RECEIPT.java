package bgu.spl.net.frames;

import bgu.spl.net.srv.ConnectionsImp;

public class RECEIPT extends Frame {

    private int connectionId;
    public RECEIPT(int connectionId, String[] headers, String body) {
        super(headers, body);
        this.connectionId=connectionId;
    }

    @Override
    public void process() {
        ConnectionsImp.getInstance().send(connectionId,this);
    }
}
