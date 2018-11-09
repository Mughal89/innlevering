package com.HTTPServer;

import java.util.HashMap;
import java.util.Map;

public class HTTPResponse {
    public String code;
    public String body;
    public String connection;
    public String server;
    public String contentType;
    public Map<String, String> headers = new HashMap<>();
    public String Length(){
        return "" + body.length();
    }

    public byte[] ToBytes() {
        String codestr = "HTTP/1.1 " + code + "\r\n";
        String namestr = "X-Server-Name: " +server + "\r\n";
        String connectionstr = "Connection: " + connection + "\r\n";
        String contentTypestr = "Content-Type: " + contentType + "\r\n";
        String bodystr = body;
        String contentLengthstr = "Content-Length: " + body.length() + "\r\n";



        StringBuilder sb = new StringBuilder();
        sb.append(codestr);
        sb.append(namestr);
        sb.append(connectionstr);
        sb.append(contentTypestr);
        sb.append(contentLengthstr);
        for(Map.Entry<String,String> entry : headers.entrySet()){
            String headerName = entry.getKey();
            String headerValue = entry.getValue();
            String header = headerName+ ": " + headerValue + "\r\n";
            sb.append(header);
        }

        sb.append("\r\n");
        sb.append(bodystr);
        return sb.toString().getBytes();
    }
}