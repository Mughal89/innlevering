package com.HTTPServer;

import java.io.IOException;

public class main {
    public static void main(String[] args){
        HTTPServer server = new HTTPServer(41322);
        try {
            System.out.println("Starting server...");
            server.start();
        }
        catch(IOException e){
            System.out.println("Failed to start server, error: " + e.getMessage());
        }

    }
}
