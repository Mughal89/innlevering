package com.HTTPServer;

public class HTTPGetRequest  extends HTTPRequest{
    public HTTPGetRequest(String uri) {
        super(uri, HTTPVerb.GET);
    }
}
