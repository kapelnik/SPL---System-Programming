package bgu.spl.mics.application;

import bgu.spl.mics.Event;

import java.util.List;

public class SendAgentsEvent implements Event<Boolean> { //first   List<string>, second int
    private List<String> serials;
    private int time;
    public SendAgentsEvent(List<String> serials,int time)
    {
        this.serials=serials;
        this.time=time;
    }


    public List<String> getSerials() {
        return serials;
    }
    public int getTime() {
        return time;
    }

}
