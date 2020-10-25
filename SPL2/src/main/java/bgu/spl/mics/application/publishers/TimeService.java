package bgu.spl.mics.application.publishers;

import bgu.spl.mics.*;
import bgu.spl.mics.application.TerminateBroadcast;
import bgu.spl.mics.application.TickBroadcast;

/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using
 * This class may not hold references for objects which it is not responsible for.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Subscriber {

	private long now,timeout,statingTime;

	public TimeService(long timeout) {
		super("TimeService");
		// TODO Implement this
		statingTime = System.currentTimeMillis()/100; // everything in Ticks
		this.timeout = timeout;
		now = System.currentTimeMillis()/100;


	}

	@Override
	protected void initialize() {
		// TODO Implement this
		this.terminate();
		while(now-statingTime<timeout) { //CHANGE THIS

			////DO EVERY 100ms:
			if (System.currentTimeMillis()/100 - now > 0) {
				now = System.currentTimeMillis()/100;
				getSimplePublisher().sendBroadcast(new TickBroadcast(now-statingTime));
			}
		}
		getSimplePublisher().sendBroadcast(new TerminateBroadcast());

	}
}
