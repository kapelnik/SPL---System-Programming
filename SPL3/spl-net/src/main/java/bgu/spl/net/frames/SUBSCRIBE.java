package bgu.spl.net.frames;

import bgu.spl.net.srv.DataBase;
import bgu.spl.net.srv.User;
import javafx.util.Pair;

import java.util.concurrent.ConcurrentLinkedQueue;

public class SUBSCRIBE  extends Frame{
    private User user;
    public SUBSCRIBE(User user, String[] headers, String body) {
        super(headers, body);
        this.user = user;
    }

    @Override
    public void process() {

        DataBase.getInstance().getTopics().putIfAbsent(headers[0],new ConcurrentLinkedQueue<>());
        DataBase.getInstance().getTopics().get(headers[0]).add(user);
    }
}
