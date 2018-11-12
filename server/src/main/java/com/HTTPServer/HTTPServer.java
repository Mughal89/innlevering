package com.HTTPServer;








import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTTPServer {
    private int port;
    private int actualPort;
    private ServerSocket listener;
    private static List<Talk> talks = new ArrayList<>();
    private static ConnectionHandler connection;


    public HTTPServer(JSONObject dataSource, int port) throws SQLException{
        this.port = port;
        this.connection = new ConnectionHandler(dataSource);
    }


    public static boolean isValidRoute(HTTPRequest request){
        return request.Uri().contains("/api/talks");
    }

    public static boolean isValidRequest(HTTPRequest request){
        switch(request.Verb()){
            case GET:
            case PUT:
            case POST:
                return true;
            default:
                return false;
        }
    }



    public void start() throws IOException{
        final ServerSocket serverSocket = new ServerSocket(port);
        this.actualPort = serverSocket.getLocalPort();
        new Thread(() -> {
            try {
                serverThread(serverSocket);
            }
            catch (SQLException e){
                System.out.println("SQLError: " + e.getMessage());
            }

        }).start();
    }

    public static void SendResponse(HTTPResponse response, OutputStream output) throws IOException{
        System.out.println("Sending response: " + response);
        output.write(response.ToBytes());
    }

    public static HTTPResponse HandleRequest(HTTPRequest request, InputStream stream) throws IOException, SQLException{
        if(request == null){
            return InvalidRequest("Failed parsing request");
        }


        if(!isValidRoute(request)){
            return InvalidRequest("Invalid route: " + request.Uri());
        }

        if(!isValidRequest(request)){
            return InvalidRequest("Invalid Request: " + request.Verb());
        }

        if(request instanceof HTTPGetRequest){
            return HandleRequest((HTTPGetRequest) request);
        }

        if(request instanceof  HTTPPostRequest){
            return HandleRequest((HTTPPostRequest) request, stream);
        }

        if(request instanceof  HTTPPutRequest){
            return HandleRequest((HTTPPutRequest) request);

        }

        return InvalidRequest("Unsupported HTTP Operation.");
    }

    public static HTTPResponse HandleRequest(HTTPGetRequest request) throws SQLException{

        HTTPResponse response = new HTTPResponse();
        if(request.Uri().equals("/api/talks/") || request.Uri().equals("/api/talks")){
            response.code = "200 OK";
            response.contentType = "text/csv";
            response.server = "My Web Server";
            response.connection = "close";
            talks = connection.GetTalks();
            response.body = Utility.toString(talks);
            return response;
        }


        Pattern pattern = Pattern.compile("/api/talks/[0-9]+");
        Matcher matcher = pattern.matcher(request.Uri());
        if(matcher.find()){
            int startIndex = request.Uri().lastIndexOf("/") + 1;
            int endIndex = request.Uri().length();
            int length = "/api/talks/".length();
            String idAsString = request.Uri().substring(startIndex, endIndex);


            System.out.println("ID as string: " + idAsString);

            int id = Integer.parseInt(idAsString);

            System.out.println("Parsed ID = " + id);

            Talk t = connection.GetTalk(id);
            if(t!= null){
                System.out.println("Eureka, found talk.");
                response.body = Utility.toString(t);
                response.code = "200 OK";
            }


            else {
                response.code = "404 Not Found";
                response.body = "Could not find talk with id: " + id;
            }


            response.contentType = "text/plain";
            response.server = "My Web Server";
            response.connection = "close";

            return response;
        }


        return InvalidRequest("Invalid GET");
    }

    public static HTTPResponse HandleRequest(HTTPPostRequest request, InputStream stream)  {
        HTTPResponse response = new HTTPResponse();
        response.contentType = "text/plain";
        response.server = "My Web Server";
        response.body = "Creation OK";
        response.connection = "close";
        response.code = "200 OK";



        Talk t = new Talk();

        if(!request.parameters.isEmpty()) {
            System.out.println("Parameters not empty...");
            for(Map.Entry<String,String> kv : request.parameters.entrySet()){
                if(kv.getKey().equals("Title")){
                    t.title = kv.getValue();

                    System.out.println("Parameters not empty... received title");
                }

                else if(kv.getKey().equals("Description")){
                    System.out.println("Parameters not empty... received description");
                    t.description = kv.getValue();
                }

                if(kv.getKey().equals("Topic")){
                    System.out.println("Parameters not empty... received topic");
                    t.topic  = kv.getValue();
                }
            }

            System.out.println("Adding talks to list..");

            talks.add(t);
            try
            {
                int id = connection.createTalk(t);
                response.headers.put("Location", "/api/talks/" + id);
            }
            catch(SQLException e){
                System.out.println("Something wrong: " + e.getMessage());
            }
        }


        return response;
    }

    public static HTTPResponse HandleRequest(HTTPPutRequest request) throws SQLException{
        HTTPResponse response = new HTTPResponse();
        response.contentType = "text/plain";
        response.server = "My Web Server";
        response.body = "Updated talk.";
        response.connection = "close";
        response.code = "200 OK";

        Talk newTalk = new Talk();



        if(request.parameters.containsKey("Id")){


            int id = Integer.parseInt((request.parameters.get("Id")));
            Talk old = connection.GetTalk(id);
            newTalk.id = old.id;

            System.out.println("Requested change to talk with id: " + id);
            System.out.println("Old talk: " + Utility.toString(old));

            if(request.parameters.containsKey("Topic")){
                newTalk.topic = request.parameters.get("Topic");
            }
            else {
                newTalk.topic = old.topic;
            }

            if(request.parameters.containsKey("Description")){
                newTalk.description = request.parameters.get("Description");
            }
            else {
                newTalk.description = old.description;
            }

            if(request.parameters.containsKey("Title")){
                newTalk.title = request.parameters.get("Title");
            }
            else {
                newTalk.title = old.title;
            }

            System.out.println("New talk: " + Utility.toString(newTalk));

            connection.UpdateTalk(newTalk);
        }

        else {
            response.code = "400 Bad Request";
            response.body = "ID missing in update.";
        }

        return response;
    }




    public static HTTPResponse InvalidRequest(String message){
        HTTPResponse response = new HTTPResponse();
        response.code = "400 Bad Request";
        response.body = "Invalid Request: " + message;
        response.connection = "close";
        response.contentType = "text/plain";
        response.server = "My Web Server";
        return response;
    }

    public void serverThread(ServerSocket serverSocket) throws SQLException{
        while(true) {
            try {
                Socket clientSocket = serverSocket.accept();



                InputStream input = clientSocket.getInputStream();
                OutputStream output = clientSocket.getOutputStream();

                HTTPRequest request = ParseRequest(input);
                System.out.println("Request: ");
                System.out.println(request);
                HTTPResponse response = HandleRequest(request, input);
                SendResponse(response, output);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                System.out.println("Socket failure: " + e.getMessage());
            }

        }
    }

    public static HTTPRequest ParseRequest(InputStream input) throws java.io.IOException, SQLException{
        HTTPRequest request = null;

        String line = readNextLine(input);
        String[] firstLine = line.split(" ");
        String verb = firstLine[0];
        String uri = firstLine[1];
        Map<String, String> headers = new HashMap<>();
        Map<String, String> parameters = new HashMap<>();
        while (!line.isEmpty()) {
            line = readNextLine(input);
            String[] headerValue = line.split(" ");
            if(headerValue.length == 2) {
                String header = headerValue[0].substring(0, headerValue[0].length()-1);
                String value = headerValue[1];
                headers.put(header,value);
            }
        }

        /*
        String content = null;
        if(headers.containsKey("Expect") && headers.get("Expect").contains("100-continue")){
            if(headers.containsKey("Content-Length")){
                int toRead = Integer.parseInt(headers.get("Content-Length"));
                content = read(input, toRead);
            }
        }
        */

        if(headers.containsKey("Content-Type") && headers.get("Content-Type").contains("www-form-urlencoded")){
            String body = null;
            if(headers.containsKey("Content-Length")){
                String lengthString = headers.get("Content-Length");
                int length = Integer.parseInt(lengthString);
                body = read(input, length);
                System.out.println("Read body: " + body);
            }

            String decoded = URLDecoder.decode(body, "UTF-8");
            String[] paramKeyValues = decoded.split("&");
            for(int i=0; i<paramKeyValues.length; ++i){
                String[] paramKeyValue = paramKeyValues[i].split("=");
                String key = paramKeyValue[0];
                String value = paramKeyValue[1];

                parameters.put(key,value);
                System.out.println("Read parameters: "+ key + "," + value);
            }

        }



        if(verb.equals("GET")){
            request = new HTTPGetRequest(uri);
        }

        if(verb.equals("PUT")){
            request = new HTTPPutRequest(uri);
        }

        if(verb.equals("POST")){
            request = new HTTPPostRequest(uri);
        }


        if(request != null){
            request.headers = headers;
            request.parameters = parameters;
        }

        return request;
    }

    public static String readNextLine(InputStream input) throws IOException{
        StringBuilder nextLine = new StringBuilder();
        int c;
        char returncarriage = '\r';
        while ((c = input.read()) != -1) {
            if (c == returncarriage) {
                input.read();
                break;
            }

            nextLine.append((char) c);
        }
        return nextLine.toString();
    }

    public static String read(InputStream input, int count) throws IOException {
        StringBuilder sb = new StringBuilder();

        int c;
        int readCount = 0;
        while(readCount < count && (c =input.read()) != -1){
            sb.append((char)c);
            readCount++;
        }

        return sb.toString();
    }

    public int getPort() {
        return actualPort;
    }

}
