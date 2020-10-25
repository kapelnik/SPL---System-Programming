package bgu.spl.net.frames;

import bgu.spl.net.srv.DataBase;
import bgu.spl.net.srv.User;

public class DISCONNECT extends Frame {
    public DISCONNECT(String[] headers, String body) {
        super(headers, body);
    }

    @Override
    public void process() {

    }
}
