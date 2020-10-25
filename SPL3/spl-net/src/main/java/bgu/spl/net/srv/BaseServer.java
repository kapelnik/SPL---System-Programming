package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncDecImp;
import bgu.spl.net.api.StompMessagingProtocolImp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Supplier;

public abstract class BaseServer<T> implements Server<T> {

    private final int port;
    private final Supplier<StompMessagingProtocolImp> protocolFactory;
    private final Supplier<MessageEncDecImp> encdecFactory;
    private ServerSocket sock;

    public BaseServer(
            int port,
            Supplier<StompMessagingProtocolImp> protocolFactory,
            Supplier<MessageEncDecImp> encdecFactory) {

        this.port = port;
        this.protocolFactory = protocolFactory;
        this.encdecFactory = encdecFactory;
		this.sock = null;
    }

    @Override
    public void serve() {

        try (ServerSocket serverSock = new ServerSocket(port)) {
			System.out.println("Server started");

            this.sock = serverSock; //just to be able to close

            while (!Thread.currentThread().isInterrupted()) {

                Socket clientSock = serverSock.accept();
                StompMessagingProtocolImp protocol = protocolFactory.get();
                BlockingConnectionHandler handler = new BlockingConnectionHandler(
                        clientSock,
                        encdecFactory.get(),
                        protocol);

                // adds the handler to Id-Handler map in ConnectionsImp and do the START
                int connectionId = ConnectionsImp.getInstance().addHandler(handler);
                DataBase.getInstance().getUsers().add(new User(null,null,connectionId,handler,null));
                protocol.start(connectionId,ConnectionsImp.getInstance());

                execute(handler);
            }
        } catch (IOException ex) {
        }

        System.out.println("server closed!!!");
    }

    @Override
    public void close() throws IOException {
		if (sock != null)
			sock.close();
    }

    protected abstract void execute(BlockingConnectionHandler  handler);

}
