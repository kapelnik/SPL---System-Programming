package bgu.spl.mics.example.publishers;

import java.util.concurrent.TimeUnit;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleEvent;

public class ExampleMessageSender extends Subscriber {

    private boolean broadcast;

    public ExampleMessageSender(String name, String[] args) {
        super(name);

        if (args.length != 1 || !args[0].matches("broadcast|event")) {
            throw new IllegalArgumentException("expecting a single argument: broadcast/event");
        }

        this.broadcast = args[0].equals("broadcast");
    }

    @Override
    protected void initialize() {
        if (broadcast) {
            getSimplePublisher().sendBroadcast(new ExampleBroadcast(getName()));
            terminate();
        }
        else {
            Future<String> futureObject = getSimplePublisher().sendEvent(new ExampleEvent(getName()));
            if (futureObject != null) {
                String resolved = futureObject.get(100, TimeUnit.MILLISECONDS);
            }
            terminate();
        }
    }

}
