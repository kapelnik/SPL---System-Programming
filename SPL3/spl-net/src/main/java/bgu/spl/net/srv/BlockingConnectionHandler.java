package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncDecImp;
import bgu.spl.net.api.StompMessagingProtocolImp;
import bgu.spl.net.frames.Frame;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class BlockingConnectionHandler implements Runnable, ConnectionHandler {

    private final StompMessagingProtocolImp protocol;
    private final MessageEncDecImp encdec;
    private final Socket sock;
    private BufferedInputStream in;
    private BufferedOutputStream out;
    private volatile boolean connected = true;

    public BlockingConnectionHandler(Socket sock, MessageEncDecImp reader, StompMessagingProtocolImp protocol) {
        this.sock = sock;
        this.encdec = reader;
        this.protocol = protocol;
    }

    @Override
    public void run() {
        try (Socket sock = this.sock) { //just for automatic closing
            int read;
            in = new BufferedInputStream(sock.getInputStream());
            out = new BufferedOutputStream(sock.getOutputStream());

            while (!protocol.shouldTerminate() && connected && (read = in.read()) >= 0) {
                Frame nextMessage = encdec.decodeNextByte((byte) read);
                if (nextMessage != null) {
                    protocol.process(nextMessage);
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void close() throws IOException {
        connected = false;
        sock.close();
    }

    @Override
    public void send(Frame msg) {
        if (msg != null)
        {
            try {
                out.write(encdec.encode(msg));
                out.flush();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
