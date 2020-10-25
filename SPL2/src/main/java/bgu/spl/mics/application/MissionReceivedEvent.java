package bgu.spl.mics.application;
import bgu.spl.mics.Event;
import  bgu.spl.mics.application.passiveObjects.*;

public class MissionReceivedEvent implements Event<Boolean> {
private MissionInfo info;
    public MissionReceivedEvent(MissionInfo info)
    {
        this.info=info;
    }
    public MissionInfo getInfo()
    {
        return info;
    }
}
