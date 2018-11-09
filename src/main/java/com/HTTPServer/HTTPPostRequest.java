package com.HTTPServer;

public class HTTPPostRequest extends HTTPRequest {
    public HTTPPostRequest(String uri) {
        super(uri, HTTPVerb.POST);
    }
}