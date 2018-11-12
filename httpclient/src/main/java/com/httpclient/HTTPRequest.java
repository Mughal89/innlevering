package com.httpclient;

import java.util.HashMap;
import java.util.Map;

public class HTTPRequest {
    public RequestType requestType;
    public Map<String, String> headers = new HashMap<>();
    public String url;
    public String body;


    public byte[] toBytes(){
        StringBuilder sb = new StringBuilder();

        String verb = null;
        switch(requestType){
            case POST:
                verb = "POST";
                break;
            case GET:
                verb = "GET";
                break;
            case PUT:
                verb = "PUT";
                break;
        }

        sb.append(verb + " " + url + " HTTP/1.1\r\n");
        for(Map.Entry<String, String> kv: headers.entrySet()){
            sb.append(kv.getKey());
            sb.append(": ");
            sb.append(kv.getValue());
            sb.append("\r\n");
        }
        sb.append("\r\n");

        sb.append(body + "\r\n");
        sb.append("\r\n");

        /*
        System.out.println("Sending request:");
        System.out.println(sb.toString());
        */

        return sb.toString().getBytes();
    }
}
