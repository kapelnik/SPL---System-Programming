package bgu.spl.mics.application;

import bgu.spl.mics.Event;
import javafx.util.Pair;

public class GadgetAvailableEvent implements Event<Pair<Long,Pair<Boolean,Boolean>>> {
private String gadget;
        public GadgetAvailableEvent(String gadget)
        {
            this.gadget=gadget;
        }

        public String getGadget() {
            return gadget;
        }
}
