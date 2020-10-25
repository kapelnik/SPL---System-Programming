package bgu.spl.net.frames;

public abstract class Frame {
    String[] headers = null;
    String body = null;

    public Frame(String[] headers, String body) {
        this.headers = headers;
        this.body = body;
    }

    public String[] getHeaders() {
        return headers;
    }

    public void setHeaders(String[] headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    abstract public void process();
}