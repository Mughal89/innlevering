package com.HTTPServer;

import java.util.HashMap;
import java.util.Map;

public class HTTPRequest {
    private String uri;
    private HTTPVerb verb;
    public Map<String, String> headers = new HashMap<>();
    public Map<String, String> parameters = new HashMap<>();

    public HTTPRequest(String uri, HTTPVerb verb){
        this.uri = uri;
        this.verb = verb;
    }

    public String Uri(){
        return uri;
    }

    public HTTPVerb Verb() {
        return verb;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(verb + " " + uri + "\n");
        for(Map.Entry<String,String> kv : headers.entrySet()){
            sb.append(kv.getKey() + ": " + kv.getValue() + "\n");
        }

        return sb.toString();
    }
}
