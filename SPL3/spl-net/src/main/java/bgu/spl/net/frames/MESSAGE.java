package bgu.spl.net.frames;

import bgu.spl.net.srv.ConnectionsImp;

    public class MESSAGE extends Frame {

    public MESSAGE(String[] headers, String body) {
        super(headers, body);
    }

    @Override
    public void process() {
        ConnectionsImp.getInstance().send(headers[0],this);
    }
}
