package com.httpclient;

import java.util.HashMap;
import java.util.Map;

public class HTTPResponse {
    public Map<String, String> headers = new HashMap<>();
    public String body;
    public String code;
}
