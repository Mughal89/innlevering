package com.httpclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

public class HTTPClient {
    private String host;
    private String url;
    private int port;

    public HTTPClient(String host, int port){
        this.host = host;
        this.port = port;
    }

    public HTTPResponse request(HTTPRequest request)  throws IOException {
        try(Socket socket = new Socket(host, port)) {
            OutputStream ostream = socket.getOutputStream();
            InputStream istream = socket.getInputStream();


            ostream.write(request.toBytes());

            HTTPResponse response = parseResponse(istream);
            return response;
        }

    }

    private HTTPResponse parseResponse(InputStream istream)  throws IOException{
        HTTPResponse response = new HTTPResponse();

        response.code = readLine(istream);
        parseHeaders(istream, response);
        if(response.headers.containsKey("Content-Length")){
            int bodyLength = Integer.parseInt(response.headers.get("Content-Length"));
            response.body = parseBody(istream, bodyLength);
        }
        return response;
    }

    private String parseBody(InputStream istream, int bodyLength) throws IOException {
        StringBuilder sb = new StringBuilder();
        int readCount = 0;
        int c;
        while( readCount < bodyLength && (c = istream.read()) != -1) {
            sb.append((char)c);
            readCount++;
        }

        return sb.toString();
    }

    private void parseHeaders(InputStream istream, HTTPResponse response) throws IOException{
        String line;
        while(!(line = readLine(istream)).isEmpty()){
            String[] kv = line.split(":");
            if(kv.length == 2){
                kv[0] = kv[0].trim();
                kv[1] = kv[1].trim();
                response.headers.put(kv[0], kv[1]);
            }
        }
    }

    private String readLine(InputStream stream) throws IOException{
        StringBuilder sb = new StringBuilder();
        int c;
        while((c = stream.read()) != -1){
            if(c == '\r'){
                c = stream.read();
                break;
            }
            sb.append((char)c);
        }

        return sb.toString();
    }
}
