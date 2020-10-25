package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.GadgetAvailableEvent;
import bgu.spl.mics.application.TerminateBroadcast;
import bgu.spl.mics.application.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;
import javafx.util.Pair;

/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {
	private Inventory inv;
	private Boolean overtime = false;
	private Long timetick;
	public Q() {
		super("Q");
		// TODO Implement this
		inv = Inventory.getInstance();

	}

	@Override
	protected void initialize() {
		this.subscribeBroadcast(TickBroadcast.class, (TickBroadcast b) -> {
			timetick=b.getTime();
		});// TODO Implement this

		this.subscribeBroadcast(TerminateBroadcast.class, (TerminateBroadcast b) -> {
			overtime = true;
			terminate();
		});// TODO Implement this

		this.subscribeEvent(GadgetAvailableEvent.class, (GadgetAvailableEvent e) -> {
			Pair bools = new Pair(inv.getItem(e.getGadget()), overtime);
			Pair result = new Pair(timetick,bools);
			this.complete(e, result); // use lambdas

		});
	}
}
