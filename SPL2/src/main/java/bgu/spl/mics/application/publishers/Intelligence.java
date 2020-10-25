package bgu.spl.mics.application.publishers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.MissionReceivedEvent;
import bgu.spl.mics.application.TerminateBroadcast;
import bgu.spl.mics.application.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.LinkedList;

/**
 * A Publisher only.
 * Holds a list of Info objects and sends them
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber{

	private LinkedList<MissionInfo> missions;
	private long timeTick;
	private long timeExpired;
	private static int totalMs=0;
	public Intelligence(LinkedList<MissionInfo> missions, long timeExpired) {
		super("intelligence" +totalMs);
		// TODO Implement this
		this.timeExpired = timeExpired;
		this.missions= missions;
		++totalMs;
	}

	@Override
	protected void initialize() {
		// TODO Implement this.
		this.subscribeBroadcast(TickBroadcast.class,(TickBroadcast b)-> {
			timeTick= b.getTime();
			Sendmissions();
		} );

		this.subscribeBroadcast(TerminateBroadcast.class, (TerminateBroadcast b) -> terminate());// TODO Implement this


	}

	private void Sendmissions() {
		for(MissionInfo e : missions) {
			if ((e.getTimeIssued() <= timeTick)&(e.getDuration()+timeTick<=timeExpired)) {
				this.getSimplePublisher().sendEvent(new MissionReceivedEvent(e));
				missions.remove(e);
			}
		}
	}
}
