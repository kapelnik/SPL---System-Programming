package bgu.spl.net.impl.stomp;

import bgu.spl.net.api.MessageEncDecImp;
import bgu.spl.net.api.StompMessagingProtocolImp;
import bgu.spl.net.impl.newsfeed.NewsFeed;
import bgu.spl.net.srv.Server;

public class StompServer {

    public static void main(String[] args) {
//        MessageEncDecImp test = new MessageEncDecImp();
//        test.tests();
        if(args[1].equals("tpc")) //
        Server.threadPerClient(
                Integer.parseInt(args[0]), //port
                new StompMessagingProtocolImp(), //protocol factory
                new MessageEncDecImp()//message encoder decoder factory
        ).serve();
        else if (args[1].equals("reactor"))
        Server.reactor(
                Runtime.getRuntime().availableProcessors(),
                Integer.parseInt(args[0]), //port
                new StompMessagingProtocolImp(), //protocol factory
                new MessageEncDecImp()//message encoder decoder factory
        ).serve();
        else
            System.out.println("Wrong args");

    }


}
