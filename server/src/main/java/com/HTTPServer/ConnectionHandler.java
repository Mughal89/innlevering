package com.HTTPServer;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConnectionHandler {
    private Connection connection;
    private String tableName;
    public ConnectionHandler(JSONObject dataSource) throws SQLException{

        try  {
            String url = (String)dataSource.get("url");
            String username = (String)dataSource.get("username");
            String password = (String)dataSource.get("password");
            this.tableName = (String)dataSource.get("table");
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to database!");
        } catch (SQLException e) {
            System.out.println("Failed to connect to database.");
            throw e;
        }
    }

    public int createTalk(Talk t) throws SQLException{
        String sql = "insert into " + tableName + " (title, topic, description) "+ "values (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            statement.setString(1, t.title);
            statement.setString(2, t.description);
            statement.setString(3, t.description);
            statement.executeUpdate();
            System.out.println("Talk created!");
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            int id = resultSet.getInt(1);
            return id;
        }
    }


    public void UpdateTalk(Talk newTalk) throws SQLException {
        String sql = "UPDATE "+tableName+" SET title = ?, topic = ?, description = ? WHERE talks.id = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, newTalk.title);
            statement.setString(2, newTalk.topic);
            statement.setString(3, newTalk.description);
            statement.setInt(4, newTalk.id);


            System.out.println("Rows updated: " + statement.executeUpdate() + " for id = " + newTalk.id);
        }
    }

    public ArrayList<Talk> GetTalks() throws SQLException{
        ArrayList<Talk> talks = new ArrayList<>();
        String sql = "select title, topic, description, id\n" + "from " + tableName;
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            try(ResultSet rs = statement.executeQuery()){
                while(rs.next()){
                    String title = rs.getString(1);
                    String topic = rs.getString(2);
                    String description = rs.getString(3);


                    Talk t = new Talk();
                    t.title = title;
                    t.topic = topic;
                    t.description = description;
                    t.id = rs.getInt(4);
                    talks.add(t);
                }
            }
        }
        return talks;
    }

    public Talk GetTalk(int id) throws SQLException{
        String sql = "select * from " + tableName + " T where T.id = ?";
        Talk t = null;
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1,id);
            try(ResultSet rs = statement.executeQuery()){
                while(rs.next()){
                    String title = rs.getString(2);
                    String topic = rs.getString(3);
                    String description = rs.getString(4);
                    t= new Talk();
                    t.title = title;
                    t.topic = topic;
                    t.description = description;
                    t.id = rs.getInt(1);
                    return t;
                }
            }
        }

        return t;
    }


}
