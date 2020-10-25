package bgu.spl.net.srv;

import bgu.spl.net.frames.Frame;

import java.io.IOException;

public interface Connections {

    boolean send(int connectionId, Frame msg);

    void send(String channel, Frame msg);

    void disconnect(int connectionId);
}
