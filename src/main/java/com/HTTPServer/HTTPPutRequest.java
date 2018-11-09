package com.HTTPServer;

public class HTTPPutRequest extends HTTPRequest {
    public HTTPPutRequest(String uri){
        super(uri, HTTPVerb.PUT);
    }
}
