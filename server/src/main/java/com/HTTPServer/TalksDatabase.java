package com.HTTPServer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class TalksDatabase {

    private Connection connection = null;
    private PreparedStatement preparedStatement = null;


    public TalksDatabase() {
        //connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/crunchify", "root", "root");

    }

}
