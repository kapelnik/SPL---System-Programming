package bgu.spl.net.frames;

import bgu.spl.net.srv.DataBase;
import bgu.spl.net.srv.User;
import javafx.util.Pair;

public class UNSUBSCRIBE extends Frame {
    private User user;
    public UNSUBSCRIBE(User user, String[] headers, String body) {
        super(headers, body);
        this.user = user;
    }
    @Override
    public void process() {
        String topic = user.getSubscriptionMap().remove(headers[0]);
        DataBase.getInstance().getTopics().get(topic).remove(user);
    }
}
