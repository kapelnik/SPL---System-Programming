package bgu.spl.mics.application;

import bgu.spl.mics.Event;
import javafx.util.Pair;

import java.util.List;

public class AgentsAvailableEvent implements Event<Pair<List<String>, Integer>> { //first   List<string>, second int
    private List<String> serials;
    public AgentsAvailableEvent(List<String> serials)
    {
        this.serials=serials;
    }


    public List<String> getSerials() {
        return serials;
    }
}
