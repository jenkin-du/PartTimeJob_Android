package com.android.d.parttimejob.Entry.Communication;

public class Datagram {

    private String response;
    private String request;
    private String jsonStream;


    public String getRequest() {
        return request;
    }

    public String getJsonStream() {
        return jsonStream;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public void setJsonStream(String jsonStream) {
        this.jsonStream = jsonStream;
    }

    @Override
    public String toString() {
        return "Datagram [request=" + request + ", jsonStream=" + jsonStream + "]";
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    /**
     *
     */
    public Datagram() {
    }

}
