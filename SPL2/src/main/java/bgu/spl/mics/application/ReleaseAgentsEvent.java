package bgu.spl.mics.application;

import bgu.spl.mics.Event;

import java.util.List;

public class ReleaseAgentsEvent implements Event<Boolean> {
    private List<String> SeriealstoRelease;
    public ReleaseAgentsEvent(List<String> SeriealstoRelease)
    {
        this.SeriealstoRelease=SeriealstoRelease;
    }
    public List<String> getSeriealstoRelease()
    {
        return SeriealstoRelease;
    }
}
