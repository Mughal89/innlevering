package com.HTTPServer;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
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

    public HTTPServer(int port){
        this.port = port;
        talks.add(new Talk("Introduction to Java", "An introduction to java."));
        talks.add(new Talk("Introduction to C", "An introduction to C."));
        Talk t = new Talk("Taking the buss", "A talk about why taking the bus sucks.");
        t.id = 4321;
        talks.add(t);
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
        new Thread(() -> serverThread(serverSocket)).start();
    }

    public static void SendResponse(HTTPResponse response, OutputStream output) throws IOException{
        output.write(response.ToBytes());
    }

    public static HTTPResponse HandleRequest(HTTPRequest request, InputStream stream) throws IOException{
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

    public static HTTPResponse HandleRequest(HTTPGetRequest request){

        HTTPResponse response = new HTTPResponse();
        if(request.Uri().equals("/api/talks")){
            response.code = "200 OK";
            response.contentType = "text/csv";
            response.server = "My Web Server";
            response.connection = "close";
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


            int id = Integer.parseInt(idAsString);

            System.out.println("Parsed ID = " + id);

            Talk t = null;
            for(int i=0; i<talks.size(); ++i){
                System.out.println("Iterating talk with id: " + talks.get(i).id);
                if(talks.get(i).id == id){
                    System.out.println("Eureka, found talk.");
                    response.body = Utility.toString(talks.get(i));
                    break;
                }
            }

            response.code = "200 OK";
            response.contentType = "text/plain";
            response.server = "My Web Server";
            response.connection = "close";

            return response;
        }


        return InvalidRequest("Invalid GET");
    }

    public static HTTPResponse HandleRequest(HTTPPostRequest request, InputStream stream) {
        HTTPResponse response = new HTTPResponse();
        response.headers.put("Location", "/api/talks/3");
        response.contentType = "text/plain";
        response.server = "My Web Server";
        response.body = "Creation OK";
        response.connection = "close";
        response.code = "200 OK";



        if(!request.parameters.isEmpty()) {
            System.out.println("Have received POST parameters: ");
            for(Map.Entry<String,String> kv : request.parameters.entrySet()){
                System.out.println("[" + kv.getKey() + "," + kv.getValue() + "]");
            }
        }


        return response;
    }

    public static HTTPResponse HandleRequest(HTTPPutRequest request){
        return InvalidRequest("Invalid Put");
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

    public void serverThread(ServerSocket serverSocket) {
        while(true) {
            try {
                Socket clientSocket = serverSocket.accept();



                InputStream input = clientSocket.getInputStream();
                OutputStream output = clientSocket.getOutputStream();

                HTTPRequest request = ParseRequest(input);
                System.out.println(request);
                HTTPResponse response = HandleRequest(request, input);
                SendResponse(response, output);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                System.out.println("Socket failure: " + e.getMessage());
            }

        }
    }

    public static HTTPRequest ParseRequest(InputStream input) throws java.io.IOException{
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


        String content = null;


        if(headers.containsKey("Expect") && headers.get("Expect").contains("100-continue")){
            if(headers.containsKey("Content-Length")){
                int toRead = Integer.parseInt(headers.get("Content-Length"));
                content = read(input, toRead);
            }
        }

        if(headers.containsKey("Content-Type") && headers.get("Content-Type").contains("www-form-urlencoded")){
            String decoded = URLDecoder.decode(content, "UTF-8");
            String[] paramKeyValues = decoded.split("&");
            for(int i=0; i<paramKeyValues.length; ++i){
                String[] paramKeyValue = paramKeyValues[i].split("=");
                String key = paramKeyValue[0];
                String value = paramKeyValue[1];

                parameters.put(key,value);
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
