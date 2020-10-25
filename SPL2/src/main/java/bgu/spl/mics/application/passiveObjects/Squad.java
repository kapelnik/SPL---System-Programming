package bgu.spl.mics.application.passiveObjects;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {

	private Map<String, Agent> agents;
	private static Squad instance = new Squad();
		/**
	 * Retrieves the single instance of this class.
	 */
	public static Squad getInstance() {
		//TODO: Implement this
		return instance;
	}

	/**
	 * Initializes the squad. This method adds all the agents to the squad.
	 * <p>
	 * @param agents 	Data structure containing all data necessary for initialization
	 * 						of the squad.
	 */
	public  void  load (Agent[] agents) { // TODO: check if needed to be synchronized
		// TODO
		this.agents= new HashMap<>();// clears the map
		for (Agent a:agents)
		{
			this.agents.put(a.getSerialNumber(),a);
		}
	}

	/**
	 * Releases agents.
	 */
	public void releaseAgents(List<String> serials){
		// TODO
		if (serials!=null) {
			for (String s : serials) {
				synchronized (agents.get(s)) {
					try {
						agents.get(s).release(); // and notify inside
						agents.get(s).notifyAll();

					} catch (Exception ignored) {
					}
				}
			}
		}
		else // notify and realse all agents
		{
			for(int i=0;i<=100;i++) {
				for (Agent a : agents.values()) {
					synchronized (agents.get(a)) {
						try {
							agents.get(a).release();
							agents.get(a).notifyAll();

						} catch (Exception ignored) {
						}
					}
				}
			}
		}
	}

	/**
	 * simulates executing a mission by calling sleep.
	 * @param time   milliseconds to sleep
	 */
	public void sendAgents(List<String> serials, int time){
		// TODO Implement this
		synchronized (this) {
			try {
				this.wait(time);
			} catch (Exception ignored) {
			}//Send the agent to a trip to Paris
		}
		releaseAgents(serials);
	}

	/**
	 * acquires an agent, i.e. holds the agent until the caller is done with it
	 * @param serials   the serial numbers of the agents
	 * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
	 */
	public boolean getAgents(List<String> serials){
		// TODO Implement this
		List<String> serialAcquired= new LinkedList<>();
		for (String s:serials)
		{
			if(agents.containsKey(s)) {
				synchronized (agents.get(s)) {
					while (!agents.get(s).isAvailable()) // until available
					{
						try {
							agents.get(s).wait();
						} catch (Exception exp) {//Thread.currentThread().interrupt();
						}
					}
					serialAcquired.add(s); // add to list
					agents.get(s).acquire();
				}
			}
			else
			{
				releaseAgents(serialAcquired);
				return false;
			}
		}
		return true;

	}

    /**
     * gets the agents names
     * @param serials the serial numbers of the agents
     * @return a list of the names of the agents with the specified serials.
     */
    public List<String> getAgentsNames(List<String> serials){
        // TODO Implement this
		List<String> names= new LinkedList<>();
		for (String s:serials)
		{
			names.add(agents.get(s).getName());
		}
	    return names;
    }

}
