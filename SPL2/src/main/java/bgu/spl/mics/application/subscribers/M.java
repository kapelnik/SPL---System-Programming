package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.MessageBroker;
import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Report;
import bgu.spl.mics.application.passiveObjects.Squad;
import javafx.util.Pair;

import java.util.List;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {
	private long timeTick; // TODO: needs to be update when Broadcast
	private Diary diary;
	private int id;
	private static int totalMs=0;
	public M() {
		super("M" + totalMs);
		++totalMs;
		this.id = totalMs;

	}

	@Override
	protected void initialize() {
		diary = Diary.getInstance();
		this.subscribeBroadcast(TickBroadcast.class,(TickBroadcast b)-> timeTick= b.getTime());

		this.subscribeBroadcast(TerminateBroadcast.class, (TerminateBroadcast b) -> terminate());// TODO Implement this


		this.subscribeEvent(MissionReceivedEvent.class,(MissionReceivedEvent e)->
		{
			Report report = new Report();
			report.setTimeCreated((int)timeTick);
			boolean isCompleted = false;
			boolean isTerminated = false;
			Future<Pair<List<String>, Integer>> agentevent = getSimplePublisher().sendEvent(new AgentsAvailableEvent(e.getInfo().getSerialAgentsNumbers()));
			if((agentevent.get()!=null) && (agentevent.get().getKey()!=null)) { //if moneypenny got agents ready

				Future<Pair<Long,Pair<Boolean,Boolean>>> gadgetevent = getSimplePublisher().sendEvent(new GadgetAvailableEvent(e.getInfo().getGadget()));
				if ((gadgetevent.get()==null)||(gadgetevent.get().getValue().getValue())) { //terminated{
					terminate();
					isTerminated=true;
				}
				else if (gadgetevent.get().getValue().getKey()) {// checks if gadget is available

					timeTick = gadgetevent.get().getKey();
					report.setQTime( gadgetevent.get().getKey().intValue());
					if (e.getInfo().getTimeExpired() >= timeTick) {
						isCompleted = true;
						getSimplePublisher().sendEvent(new SendAgentsEvent(e.getInfo().getSerialAgentsNumbers(), e.getInfo().getDuration())).get(); // send agents, if misson not completed reales agents

					}
				}

			}
			else if(agentevent.get()==null) isTerminated=true;

			if(isCompleted) {
				report.setMoneypenny(agentevent.get().getValue());

				report.setAgentsNames(agentevent.get().getKey());
				report.setAgentsSerialNumbersNumber(e.getInfo().getSerialAgentsNumbers());
				report.setGadgetName(e.getInfo().getGadget());
				report.setM(id);
				report.setMissionName(e.getInfo().getMissionName());

				report.setTimeIssued(e.getInfo().getTimeIssued());
				diary.addReport(report);

			}
			else if(!isTerminated) {
				getSimplePublisher().sendEvent(new ReleaseAgentsEvent(e.getInfo().getSerialAgentsNumbers())).get();

			}
			complete(e,isCompleted);




		});

	}

}
