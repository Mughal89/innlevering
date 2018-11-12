package com.HTTPServer;



import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class main {
    public static void main(String[] args)  throws SQLException, ClassNotFoundException{
        JSONObject dataSource = ParseDataSourceJson("innlevering.properties");

        if(dataSource == null)
            return;

        HTTPServer server = new HTTPServer(dataSource, 41322);
        try {
            System.out.println("Starting server...");
            server.start();
        } catch (IOException e) {
            System.out.println("Failed to start server, error: " + e.getMessage());
        }

    }

    private static JSONObject ParseDataSourceJson(String filename) {
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader("innlevering.properties"));
            JSONObject jsonObject = (JSONObject) obj;
            JSONObject dataSource = (JSONObject)jsonObject.get("dataSource");
            return dataSource;
        }

        catch(IOException e){
            System.out.println("Could not read " + filename + ", error: " + e.getMessage());
            return null;
        }
        catch(ParseException e){
            System.out.println("Failed to parse json file, " + filename + ", error: " + e.getMessage());
            return null;
        }
    }
}
