package bgu.spl.net.api;

import bgu.spl.net.frames.Frame;
import bgu.spl.net.srv.Connections;
import bgu.spl.net.srv.ConnectionsImp;

public interface StompMessagingProtocol  {
	/**
	 * Used to initiate the current client protocol with it's personal connection ID and the connections implementation
	**/
    void start(int connectionId, ConnectionsImp connections);
    
    void process(Frame frame);
	
	/**
     * @return true if the connection should be terminated
     */
    boolean shouldTerminate();
}
