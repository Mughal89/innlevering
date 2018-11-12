package com.httpclient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Main {

    public static void main(String[] args) throws UnsupportedEncodingException, IOException {
        HTTPClient client = new HTTPClient("127.0.0.1", 41322);
        String command = args[0];

        if(command.equals("add")){
            HTTPQuery query = ParseAddQuery(args);
            HTTPRequest request = CreateAddRequest(query);
            HTTPResponse response = client.request(request);

            if(response.headers.containsKey("Location")){
                System.out.println("Added new talk, located at: " + response.headers.get("Location"));
            }
        }

        else if(command.equals("list")){
            HTTPRequest request = new HTTPRequest();
            request.url = "/api/talks";
            request.requestType = RequestType.GET;
            request.body = "";


            HTTPResponse response = client.request(request);

            System.out.println("Talks:");
            System.out.println(response.body);

        }

        else if(command.equals("show")){
            int id = Integer.parseInt(args[2]);
            HTTPRequest request = new HTTPRequest();
            request.url = "/api/talks/" + id;
            request.requestType = RequestType.GET;
            request.body = "";

            HTTPResponse response = client.request(request);
            System.out.println(response.body);
        }

        else if(command.equals("update")){
            HTTPQuery query = ParseUpdateQuery(args);
            HTTPRequest request = CreateUpdateRequest(query);
            HTTPResponse response = client.request(request);

            System.out.println("Update response: " + response.body);
        }
    }

    private static HTTPRequest CreateUpdateRequest(HTTPQuery query) {
        HTTPRequest request = new HTTPRequest();
        request.requestType = RequestType.PUT;
        request.body = query.toString();
        request.url = "/api/talks/" + query.parameters.get("Id");
        request.headers.put("Content-Type", "application/www-form-urlencoded");
        request.headers.put("Content-Length", String.valueOf(request.body.getBytes().length));
        request.headers.put("Connection", "Keep-Alive");
        request.headers.put("Host", "127.0.0.1");
        request.headers.put("Expect", "100-continue");
        return request;
    }

    private static HTTPQuery ParseUpdateQuery(String[] args) {
        HTTPQuery query = new HTTPQuery();
        String topic = null;
        String title = null;
        String description = null;
        String id = null;
        boolean found = false;

        for(int i=0; i<args.length; ++i){

            if(args[i].equals("-id")){
                id = args[i+1];
            }

            if(args[i].equals("-title")){
                title = args[i+1];
                found = true;
            }

            else if(args[i].equals("-topic")){
                topic = args[i+1];
                found = true;
            }

            else if(args[i].equals("-description")){
                description = args[i+1];
                found = true;
            }
        }

        if(!found){
            throw new RuntimeException("Incorrect usage, at least one field must be updated, eg -topic \"Foo\"");
        }

        if(id == null){
            throw new RuntimeException("Id missing! Usage: -id \"3\"");
        }
        if(title != null){
            query.parameters.put("Title", title);
        }

        if(topic != null){
            query.parameters.put("Topic", topic);
        }

        if(description != null){
            query.parameters.put("Description", description);
        }

        query.parameters.put("Id", id);

        return query;
    }

    private static HTTPRequest CreateAddRequest(HTTPQuery query) {
        HTTPRequest request = new HTTPRequest();


        request.requestType = RequestType.POST;
        request.body = query.toString();
        request.headers.put("Content-Type", "application/www-form-urlencoded");
        request.headers.put("Content-Length", String.valueOf(request.body.getBytes().length));
        request.headers.put("Connection", "Keep-Alive");
        request.headers.put("Host", "127.0.0.1");
        request.headers.put("Expect", "100-continue");
        request.url = "/api/talks";

        return request;
    }

    private static HTTPQuery ParseAddQuery(String[] args) {
        HTTPQuery query = new HTTPQuery();
        query.parameters.put("Title", args[2]);
        query.parameters.put("Description", args[4]);
        query.parameters.put("Topic", args[6]);

        return query;
    }
}
